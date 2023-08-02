/*
 * DefaultDatabaseMetaTag.java
 *
 * Copyright (C) 2002-2017 Takis Diakoumis
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.executequery.databaseobjects.impl;

import biz.redsoft.IFBDatabaseConnection;
import org.executequery.databasemediators.spi.DefaultStatementExecutor;
import org.executequery.databaseobjects.*;
import org.executequery.datasource.PooledConnection;
import org.executequery.datasource.PooledResultSet;
import org.executequery.gui.browser.ComparerDBPanel;
import org.executequery.gui.browser.tree.TreePanel;
import org.executequery.localization.Bundles;
import org.executequery.log.Log;
import org.underworldlabs.jdbc.DataSourceException;
import org.underworldlabs.swing.util.InterruptibleThread;
import org.underworldlabs.util.DynamicLibraryLoader;
import org.underworldlabs.util.MiscUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.executequery.gui.browser.tree.TreePanel.DEFAULT;

/**
 * Default meta tag object implementation.
 *
 * @author Takis Diakoumis
 */
public class DefaultDatabaseMetaTag extends AbstractNamedObject
        implements DatabaseMetaTag {

    DatabaseObject dependedObject;

    int typeTree;

    /**
     * the catalog object for this meta tag
     */
    private DatabaseCatalog catalog;

    /**
     * the schema object for this meta tag
     */
    private DatabaseSchema schema;

    /**
     * the host object for this meta tag
     */
    private final DatabaseHost host;

    /**
     * the meta data key name of this object
     */
    private final String metaDataKey;

    /**
     * the child objects of this meta type
     */
    private List<NamedObject> children;

    /**
     * Creates a new instance of DefaultDatabaseMetaTag
     */


    public DefaultDatabaseMetaTag(DatabaseHost host,
                                  DatabaseCatalog catalog,
                                  DatabaseSchema schema,
                                  String metaDataKey, int typeTree) {
        this.typeTree = typeTree;
        this.host = host;
        setCatalog(catalog);
        setSchema(schema);
        this.metaDataKey = metaDataKey;
    }

    public DefaultDatabaseMetaTag(DatabaseHost host,
                                  DatabaseCatalog catalog,
                                  DatabaseSchema schema,
                                  String metaDataKey) {
        this(host, catalog, schema, metaDataKey, TreePanel.DEFAULT);
    }

    public DefaultDatabaseMetaTag(DatabaseHost host,
                                  DatabaseCatalog catalog,
                                  DatabaseSchema schema,
                                  String metaDataKey,
                                  int typeTree,
                                  DatabaseObject dependedObject) {
        this(host, catalog, schema, metaDataKey, typeTree);
        this.dependedObject = dependedObject;
    }


    /**
     * Returns the db object with the specified name or null if
     * it does not exist.
     *
     * @param name the name of the object
     * @return the NamedObject or null if not found
     */
    public NamedObject getNamedObject(String name) throws DataSourceException {

        List<NamedObject> objects = getObjects();
        if (objects != null) {

            name = name.toUpperCase();

            for (NamedObject object : objects) {

                if (name.equals(object.getName().toUpperCase())) {

                    return object;
                }

            }

        }

        return null;
    }

    /**
     * Retrieves child objects classified as this tag type.
     * These may be database tables, functions, procedures, sequences, views, etc.
     *
     * @return this meta tag's child database objects.
     */
    public List<NamedObject> getObjects() throws DataSourceException {

        if (!isMarkedForReload() && children != null) {

            return children;
        }

        int type = getSubType();
        if (type >= SYSTEM_DOMAIN)
            setSystemFlag(true);
        if (type == DATABASE_TRIGGER
                || type == DDL_TRIGGER
                || type == SYSTEM_DOMAIN
                || type == SYSTEM_FUNCTION
                || type == SYSTEM_INDEX
                || type == SYSTEM_TABLE
                || type == SYSTEM_VIEW
                || type == SYSTEM_TRIGGER
                || type == SYSTEM_ROLE
                || type == GLOBAL_TEMPORARY
                || type == SYSTEM_PACKAGE
                || type == ROLE
                || type == TABLESPACE
                || type == JOB
        )
            if (typeTree == TreePanel.DEPENDENT || typeTree == TreePanel.DEPENDED_ON) {
                return new ArrayList<NamedObject>();
            }
        if (type != SYSTEM_FUNCTION) {


            children = loadObjects(type);

        } else {

            // system functions break down further

            children = getSystemFunctionTypes();
        }

        // loop through and add this object as the parent object
        addAsParentToObjects(children);
        setMarkedForReload(false);

        return children;
    }

    @Override
    public void loadFullInfoForObjects() {

        getHost().setPauseLoadingTreeForSearch(true);
        List<NamedObject> objects = getObjects();
        boolean first = true;

        DefaultStatementExecutor querySender = new DefaultStatementExecutor(getHost().getDatabaseConnection());

        if (objects.size() == 0)
            return;

        String query = ((AbstractDatabaseObject) objects.get(0)).queryForInfoAllObjects();
        try {

            InterruptibleThread thread = null;
            if (Thread.currentThread() instanceof InterruptibleThread)
                thread = (InterruptibleThread) Thread.currentThread();

            ResultSet rs = querySender.getResultSet(query).getResultSet();
            int i = 0;

            ComparerDBPanel comparerDBPanel = getComparerDBPanel(
                    thread, "LoadFullInfoForObjects", objects.size());

            while (rs != null && rs.next()) {

                if (thread != null && thread.isCanceled()) {
                    querySender.releaseResources();
                    return;
                }

                while (!objects.get(i).getName().contentEquals(MiscUtils.trimEnd(rs.getString(1)))) {
                    i++;
                    if (i >= objects.size())
                        throw new DataSourceException("Error load info for" + metaDataKey);
                    first = true;
                }

                if (first) {
                    ((AbstractDatabaseObject) objects.get(i)).prepareLoadingInfo();
                    if (comparerDBPanel != null)
                        comparerDBPanel.incrementProgressBarValue();
                }

                ((AbstractDatabaseObject) objects.get(i)).setInfoFromSingleRowResultSet(rs, first);
                first = false;
            }

            for (NamedObject namedObject : objects) {
                ((AbstractDatabaseObject) namedObject).finishLoadingInfo();
                ((AbstractDatabaseObject) namedObject).setMarkedForReload(false);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {
            if (querySender != null)
                querySender.releaseResources();
            getHost().setPauseLoadingTreeForSearch(false);
        }

    }

    @Override
    public void loadColumnsForAllTables() {

        List<NamedObject> objects = getObjects();
        boolean first = true;

        DefaultStatementExecutor querySender = new DefaultStatementExecutor(getHost().getDatabaseConnection());

        if (objects.size() == 0)
            return;

        String query = ((AbstractDatabaseObject) objects.get(0)).getBuilderLoadColsForAllTables().getSQLQuery();
        try {

            InterruptibleThread thread = null;
            if (Thread.currentThread() instanceof InterruptibleThread)
                thread = (InterruptibleThread) Thread.currentThread();

            ResultSet rs = querySender.getResultSet(query).getResultSet();
            int i = 0;

            ComparerDBPanel comparerDBPanel = getComparerDBPanel(
                    thread, "LoadColumnsForAllTables", objects.size());

            AbstractDatabaseObject previousObject = null;
            while (rs != null && rs.next()) {

                if (thread != null && thread.isCanceled()) {
                    querySender.releaseResources();
                    return;
                }

                while (getHost().isPauseLoadingTreeForSearch() && Thread.currentThread().getName().contentEquals("loadingTreeForSearch")) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                while (!objects.get(i).getName().contentEquals(MiscUtils.trimEnd(rs.getString(1)))) {
                    i++;
                    if (i >= objects.size())
                        throw new DataSourceException("Error load columns for " + metaDataKey);
                    first = true;
                }

                if (((AbstractDatabaseObject) objects.get(i)).isMarkedForReloadCols()) {
                    if (first) {
                        ((AbstractDatabaseObject) objects.get(i)).prepareLoadColumns();
                        if (previousObject != null) {
                            previousObject.finishLoadColumns();
                            previousObject.setMarkedForReloadCols(false);
                        }
                        if (comparerDBPanel != null)
                            comparerDBPanel.incrementProgressBarValue();
                    }
                    ((AbstractDatabaseObject) objects.get(i)).addColumnFromResultSet(rs);
                    first = false;
                    previousObject = (AbstractDatabaseObject) objects.get(i);
                }
            }

            if (previousObject != null) {
                previousObject.finishLoadColumns();
                previousObject.setMarkedForReloadCols(false);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {
            if (querySender != null)
                querySender.releaseResources();
            getHost().setPauseLoadingTreeForSearch(false);
        }

    }

    private ComparerDBPanel getComparerDBPanel(InterruptibleThread thread, String labelKey, int objectsSize) {

        ComparerDBPanel comparerDBPanel = null;
        if (thread != null) {

            Object threadUserObject = thread.getUserObject();
            if (threadUserObject instanceof ComparerDBPanel) {
                comparerDBPanel = ((ComparerDBPanel) threadUserObject);
                comparerDBPanel.recreateProgressBar(labelKey, NamedObject.META_TYPES_FOR_BUNDLE[getSubType()], objectsSize);
            }
        }

        return comparerDBPanel;
    }

    private void addAsParentToObjects(List<NamedObject> children) {

        if (children != null) {

            for (NamedObject i : children) {

                i.setParent(this);
            }

        }

    }

    private AbstractDatabaseObject getTable(ResultSet rs,String metaDataKey,int type)throws SQLException {


                String tableName = rs.getString(1);
        DefaultDatabaseObject object = new DefaultDatabaseObject(this, metaDataKey);
                object.setName(tableName);
                if (typeTree == DEFAULT) {
                } else {
                    object.setTypeTree(typeTree);
                    object.setDependObject(dependedObject);

                }
                if (metaDataKey.contains("SYSTEM"))
                    object.setSystemFlag(true);
                switch (type)
                {
                    case TABLE: return new DefaultDatabaseTable(object);
                    case VIEW: return new DefaultDatabaseView(object);
                    case GLOBAL_TEMPORARY: return new DefaultTemporaryDatabaseTable(object);
                    default:return object;
                }


    }




    private ResultSet getObjectsResultSet(int type) throws SQLException {
        switch (type) {
            case DOMAIN: return getDomainsResultSet();
            case PROCEDURE: return getProceduresResultSet();
            case FUNCTION: return getFunctionsResultSet();
            case PACKAGE: return getPackagesResultSet();
            case TRIGGER: return getTriggersResultSet();
            case DDL_TRIGGER:
                return getDDLTriggerResultSet();
            case DATABASE_TRIGGER:
                return getDatabaseTriggerResultSet();
            case SEQUENCE:
                return getSequencesResultSet();
            case EXCEPTION:
                return getExceptionResultSet();
            case UDF:
                return getUDFResultSet();
            case USER:
                return getUsersResultSet();
            case ROLE:
                return getRolesResultSet();
            case INDEX:
                return getIndicesResultSet();
            case TABLESPACE:
                return getTablespacesResultSet();
            case JOB:
                return getJobsResultSet();
            case COLLATION:
                return getCollationsResultSet();
            case SYSTEM_DOMAIN:
                return getSystemDomainResultSet();
            case SYSTEM_TRIGGER:
                return getSystemTriggerResultSet();
            case SYSTEM_SEQUENCE:
                return getSystemSequencesResultSet();
            case SYSTEM_ROLE:
                return getSystemRolesResultSet();
            case SYSTEM_INDEX:
                return getSystemIndexResultSet();
            case SYSTEM_PACKAGE:
                return getSystemPackagesResultSet();
            default:
                ResultSet rs = getTablesResultSet(getMetaDataKey(), false);
                if (rs == null)
                    rs = getTablesResultSet(getMetaDataKey(), true);
                return rs;
        }
    }

    private List<NamedObject> loadObjects(int type) throws DataSourceException {

        ResultSet rs = null;
        try {

            List<NamedObject> list = new ArrayList<NamedObject>();
            rs = getObjectsResultSet(type);
            if (rs != null) {

                while (rs.next()) {
                    if (!getHost().getDatabaseConnection().isConnected())
                        return new ArrayList<>();
                    AbstractDatabaseObject namedObject = null;
                    switch (type) {
                        case DOMAIN:
                        case SYSTEM_DOMAIN:
                            namedObject = getDomain(rs);
                            break;
                        case TABLE:
                        case GLOBAL_TEMPORARY:
                        case VIEW:
                        case SYSTEM_TABLE:
                        case SYSTEM_VIEW:
                            namedObject = getTable(rs, getMetaDataKey(), type);
                            break;
                        case PROCEDURE:
                            namedObject = getProcedure(rs);
                            break;
                        case FUNCTION: namedObject = getFunction(rs);
                            break;
                        case PACKAGE:
                        case SYSTEM_PACKAGE:
                            namedObject = getPackage(rs);
                            break;
                        case TRIGGER:
                        case DDL_TRIGGER:
                        case DATABASE_TRIGGER:
                        case SYSTEM_TRIGGER:
                            namedObject = getTrigger(rs);
                            break;
                        case SEQUENCE:
                        case SYSTEM_SEQUENCE:
                            namedObject = getSequence(rs);
                            break;
                        case EXCEPTION:
                            namedObject = getException(rs);
                            break;
                        case UDF:
                            namedObject = getUDF(rs);
                            break;
                        case USER:
                            namedObject = getUser(rs);
                            break;
                        case ROLE:
                        case SYSTEM_ROLE:
                            namedObject = getRole(rs);
                            break;
                        case INDEX:
                        case SYSTEM_INDEX:
                            namedObject = getIndex(rs);
                            break;
                        case TABLESPACE:
                            namedObject = getTablespace(rs);
                            break;
                        case JOB:
                            namedObject = getJob(rs);
                            break;
                        case COLLATION:
                            namedObject = getCollation(rs);
                            break;
                    }
                    if (namedObject != null) {
                        if (type >= SYSTEM_DOMAIN)
                            namedObject.setSystemFlag(true);
                        namedObject.setHost(getHost());
                        list.add(namedObject);
                    }
                }

            } else {

            }
            return list;

        } catch (SQLException e) {

            logThrowable(e);
            return new ArrayList<NamedObject>(0);

        } finally {

            try {
                releaseResources(rs, getHost().getDatabaseMetaData().getConnection());
            } catch (SQLException throwables) {
                releaseResources(rs, null);
            }
        }
    }

    /**
     * Loads the database functions.
     */
    private AbstractDatabaseObject getFunction(ResultSet rs) throws SQLException {
        if (typeTree == TreePanel.DEFAULT) {
            DefaultDatabaseFunction function = new DefaultDatabaseFunction(this, rs.getString(3));
            function.setRemarks(rs.getString(4));
            return function;
        } else {
            return new DefaultDatabaseFunction(this, rs.getString(1));
        }
    }

    /**
     * Loads the database procedures.
     */
    private AbstractDatabaseObject getProcedure(ResultSet rs) throws SQLException {
            if (((PooledResultSet) rs).getResultSet().unwrap(ResultSet.class).getClass().getName().contains("FBResultSet")) {
                    return new DefaultDatabaseProcedure(this, rs.getString(1));
            } else {
                    DefaultDatabaseProcedure procedure = new DefaultDatabaseProcedure(this, rs.getString(3));
                    procedure.setRemarks(rs.getString(7));
                    return procedure;
                }
    }

    /**
     * Loads the database indices.
     */
    private AbstractDatabaseObject getIndex(ResultSet rs) throws SQLException {


        DefaultDatabaseIndex index = new DefaultDatabaseIndex(this, MiscUtils.trimEnd(rs.getString(1)));
        index.setHost(this.getHost());
        index.setActive(rs.getInt(2) != 1);
        return index;
    }

    public DefaultDatabaseIndex getIndexFromName(String name) throws DataSourceException {

        ResultSet rs = null;
        DefaultDatabaseIndex index=null;
        try {

            rs = getIndexFromNameResultSet(name);
            while (rs.next()) {

                index = new DefaultDatabaseIndex(this, MiscUtils.trimEnd(rs.getString(1)));
                index.setTableName(rs.getString(2));
                index.setIndexType(rs.getInt(4));
                index.setActive(rs.getInt(6) != 1);
                index.setUnique(rs.getInt(5) == 1);
                index.setRemarks(rs.getString(7));
                index.setConstraint_type(rs.getString(8));
                index.setHost(this.getHost());
            }

            return index;

        } catch (SQLException e) {

            logThrowable(e);
            return null;

        } finally {

            try {
                releaseResources(rs, getHost().getDatabaseMetaData().getConnection());
            } catch (SQLException throwables) {
                releaseResources(rs, null);
            }
        }
    }

    /**
     * Loads the database triggers.
     */
    private AbstractDatabaseObject getTrigger(ResultSet rs) throws SQLException {
        DefaultDatabaseTrigger trigger = new DefaultDatabaseTrigger(this,
                MiscUtils.trimEnd(rs.getString(1)));
                if (typeTree == TreePanel.DEFAULT)
                    trigger.setTriggerActive(rs.getInt(2) != 1);
                else trigger.getObjectInfo();
                return trigger;

    }

    /**
     * Loads the database triggers.
     */
    private AbstractDatabaseObject getSequence(ResultSet rs) throws SQLException {

        return new DefaultDatabaseSequence(this, rs.getString(1));
    }
    private AbstractDatabaseObject getDomain(ResultSet rs) throws SQLException {
        return new DefaultDatabaseDomain(this, rs.getString(1));

    }


    private AbstractDatabaseObject getUser(ResultSet rs) throws SQLException {

        return new DefaultDatabaseUser(this, rs.getObject(1).toString());

    }

    private AbstractDatabaseObject getTablespace(ResultSet rs) throws SQLException {

        return new DefaultDatabaseTablespace(this, rs.getObject(1).toString());

    }

    private AbstractDatabaseObject getJob(ResultSet rs) throws SQLException {

        return new DefaultDatabaseJob(this, rs.getObject(1).toString());

    }

    private AbstractDatabaseObject getCollation(ResultSet rs) throws SQLException {

        return new DefaultDatabaseCollation(this, rs.getObject(1).toString());

    }

    private AbstractDatabaseObject getRole(ResultSet rs) throws SQLException {
        return new DefaultDatabaseRole(this, rs.getObject(1).toString());
    }

    /**
     * Loads the database triggers.
     */
    private AbstractDatabaseObject getException(ResultSet rs) throws SQLException {

        DefaultDatabaseException exception = new DefaultDatabaseException(this, rs.getString(1));
        exception.setRemarks(rs.getString(2));
        return exception;
    }


    /**
     * Loads the database UDFs.
     */
    private AbstractDatabaseObject getUDF(ResultSet rs) throws SQLException {

                DefaultDatabaseUDF udf = new DefaultDatabaseUDF(this,
                        MiscUtils.trimEnd(rs.getString(1)),
                        this.getHost());
                udf.setRemarks(rs.getString(2));
                String moduleName = rs.getString(3);
                if (!MiscUtils.isNull(moduleName))
                    udf.setModuleName(moduleName.trim());
                String entryPoint = rs.getString(4);
                if (!MiscUtils.isNull(entryPoint))
                    udf.setEntryPoint(entryPoint.trim());
                udf.setReturnArg(rs.getInt(5));
        udf.setRemarks(rs.getString("description"));
                return udf;
    }





    private AbstractDatabaseObject getPackage(ResultSet rs) throws SQLException {
        return new DefaultDatabasePackage(this, MiscUtils.trimEnd(rs.getString(1)));
    }

    private ResultSet getResultSetFromQuery(String query) throws SQLException {
        DefaultStatementExecutor querySender = new DefaultStatementExecutor(getHost().getDatabaseConnection());
        return querySender.getResultSet(query).getResultSet();
    }

    private ResultSet getProceduresResultSet() throws SQLException {

        String catalogName = catalogNameForQuery();
        String schemaName = schemaNameForQuery();

        DatabaseMetaData dmd = getHost().getDatabaseMetaData();
        Connection realConnection = ((PooledConnection) dmd.getConnection()).getRealConnection();
        if (realConnection.unwrap(Connection.class).getClass().getName().contains("FBConnection")) { // Red Database or FB
            Connection fbConn = realConnection.unwrap(Connection.class);
            IFBDatabaseConnection db = null;
            try {
                db = (IFBDatabaseConnection) DynamicLibraryLoader.loadingObjectFromClassLoader(getHost().getDatabaseConnection().getDriverMajorVersion(), fbConn, "FBDatabaseConnectionImpl");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            db.setConnection(fbConn);
            String condition = "";
            if (db.getMajorVersion() > 2)
                condition = "where RDB$PACKAGE_NAME is null\n";
            String sql = "select rdb$procedure_name as procedure_name\n" +
                    "from rdb$procedures \n" +
                    condition +
                    "order by procedure_name";
            if (typeTree == TreePanel.DEPENDED_ON)
                sql = getDependOnQuery(5);
            else if (typeTree == TreePanel.DEPENDENT)
                sql = getDependentQuery(5);
            ResultSet rs = getResultSetFromQuery(sql);
            return rs;
        } else { // Another database
            return dmd.getProcedures(catalogName, schemaName, null);
        }
    }

    private ResultSet getIndicesResultSet() throws SQLException {
        String query = "select " +
                "I.RDB$INDEX_NAME,\n" +
                "I.RDB$INDEX_INACTIVE\n" +
                "FROM RDB$INDICES AS I \n" +
                "where I.RDB$SYSTEM_FLAG = 0 \n" +
                "ORDER BY I.RDB$INDEX_NAME";
        if (typeTree == TreePanel.DEPENDED_ON)
            query = getDependOnQuery(10);
        else if (typeTree == TreePanel.DEPENDENT)
            query = getDependentQuery(10);
        else if (typeTree == TreePanel.TABLESPACE)
            query = ((DefaultDatabaseTablespace) dependedObject).getIndexesQuery();
        return getResultSetFromQuery(query);
    }

    private ResultSet getIndexFromNameResultSet(String name) throws SQLException {
        String query = "select " +
                "I.RDB$INDEX_NAME, " +
                "I.RDB$RELATION_NAME, " +
                "I.RDB$SYSTEM_FLAG," +
                "I.RDB$INDEX_TYPE," +
                "I.RDB$UNIQUE_FLAG," +
                "I.RDB$INDEX_INACTIVE," +
                "I.RDB$DESCRIPTION," +
                "C.RDB$CONSTRAINT_TYPE\n" +
                "FROM RDB$INDICES AS I LEFT JOIN rdb$relation_constraints as c on i.rdb$index_name=c.rdb$index_name\n" +
                "where I.RDB$SYSTEM_FLAG = 0 \n" +
                "AND I.RDB$INDEX_NAME=?";
        DefaultStatementExecutor querySender = new DefaultStatementExecutor(getHost().getDatabaseConnection());
        PreparedStatement st = querySender.getPreparedStatement(query);
        st.setString(1, name);
        ResultSet resultSet = querySender.getResultSet(-1, st).getResultSet();
        return resultSet;
    }

    private ResultSet getTriggersResultSet() throws SQLException {
        String query = "select t.rdb$trigger_name,\n" +
                "t.rdb$trigger_inactive\n" +
                "from rdb$triggers t\n" +
                "where t.rdb$system_flag = 0\n" +
                "and t.rdb$trigger_type <= 114 \n" +
                "order by t.rdb$trigger_name";
        if (typeTree == TreePanel.DEPENDED_ON)
            query = getDependOnQuery(2);
        else if (typeTree == TreePanel.DEPENDENT)
            query = getDependentQuery(2);
        return getResultSetFromQuery(query);
    }


    private ResultSet getSequencesResultSet() throws SQLException {
        String query = "select rdb$generator_name from rdb$generators where ((RDB$SYSTEM_FLAG is NULL) or (RDB$SYSTEM_FLAG = 0))\n" +
                "     order by  rdb$generator_name";
        if (typeTree == TreePanel.DEPENDED_ON)
            query = getDependOnQuery(14);
        else if (typeTree == TreePanel.DEPENDENT)
            query = getDependentQuery(14);
        return getResultSetFromQuery(query);
    }

    private ResultSet getSystemSequencesResultSet() throws SQLException {
        String query = "select rdb$generator_name from rdb$generators where ((RDB$SYSTEM_FLAG is not NULL) and (RDB$SYSTEM_FLAG != 0))\n" +
                "     order by  rdb$generator_name";
        if (typeTree == TreePanel.DEPENDED_ON)
            query = getDependOnQuery(14);
        else if (typeTree == TreePanel.DEPENDENT)
            query = getDependentQuery(14);
        return getResultSetFromQuery(query);
    }

    private ResultSet getDomainsResultSet() throws SQLException {
        String query = "select " +
                "RDB$FIELD_NAME " +
                "from RDB$FIELDS\n" +
                "where (not (RDB$FIELD_NAME starting with 'RDB$')) and (RDB$SYSTEM_FLAG=0 or RDB$SYSTEM_FLAG IS NULL)\n" +
                "order by RDB$FIELD_NAME";
        if (typeTree == TreePanel.DEPENDED_ON)
            query = getDependOnQuery(9);
        else if (typeTree == TreePanel.DEPENDENT)
            query = getDependentQuery(9);
        return getResultSetFromQuery(query);
    }

    private ResultSet getSystemDomainResultSet() throws SQLException {
        String query = "select " +
                "RDB$FIELD_NAME " +
                "from RDB$FIELDS\n" +
                "where (RDB$FIELD_NAME starting with 'RDB$') or (RDB$SYSTEM_FLAG<>0 and RDB$SYSTEM_FLAG IS not NULL)\n" +
                "order by RDB$FIELD_NAME";
        return getResultSetFromQuery(query);
    }

    private ResultSet getSystemRolesResultSet() throws SQLException {
        String query = "SELECT RDB$ROLE_NAME FROM RDB$ROLES WHERE RDB$SYSTEM_FLAG!=0 AND RDB$SYSTEM_FLAG IS NOT NULL ORDER BY 1";
        return getResultSetFromQuery(query);
    }

    private ResultSet getSystemPackagesResultSet() throws SQLException {
        String query = "SELECT RDB$PACKAGE_NAME FROM RDB$PACKAGES WHERE RDB$SYSTEM_FLAG!=0 AND RDB$SYSTEM_FLAG IS NOT NULL ORDER BY 1";
        return getResultSetFromQuery(query);
    }

    private ResultSet getUsersResultSet() throws SQLException {
        String query = "SELECT SEC$USER_NAME FROM SEC$USERS ORDER BY 1";
        if (typeTree == TreePanel.DEPENDED_ON)
            query = getDependOnQuery(8);
        else if (typeTree == TreePanel.DEPENDENT)
            query = getDependentQuery(8);
        return getResultSetFromQuery(query);
    }

    private ResultSet getRolesResultSet() throws SQLException {
        String query = "SELECT RDB$ROLE_NAME FROM RDB$ROLES WHERE RDB$SYSTEM_FLAG=0 OR RDB$SYSTEM_FLAG IS NULL ORDER BY 1";
        return getResultSetFromQuery(query);
    }

    private ResultSet getTablespacesResultSet() throws SQLException {
        String query = "SELECT RDB$TABLESPACE_NAME FROM RDB$TABLESPACES ORDER BY 1";
        return getResultSetFromQuery(query);
    }

    private ResultSet getJobsResultSet() throws SQLException {
        String query = "SELECT RDB$JOB_NAME FROM RDB$JOBS ORDER BY 1";
        return getResultSetFromQuery(query);
    }

    private ResultSet getCollationsResultSet() throws SQLException {
        String query = "SELECT RDB$COLLATION_NAME FROM RDB$COLLATIONS WHERE RDB$SYSTEM_FLAG=0 OR RDB$SYSTEM_FLAG IS NULL ORDER BY 1";
        return getResultSetFromQuery(query);
    }

    private ResultSet getExceptionResultSet() throws SQLException {
        String query = "select RDB$EXCEPTION_NAME, " +
                "RDB$DESCRIPTION\n" +
                "from RDB$EXCEPTIONS\n" +
                "order by RDB$EXCEPTION_NAME";
        if (typeTree == TreePanel.DEPENDED_ON)
            query = getDependOnQuery(7);
        else if (typeTree == TreePanel.DEPENDENT)
            query = getDependentQuery(7);
        return getResultSetFromQuery(query);
    }

    private ResultSet getUDFResultSet() throws SQLException {

        ResultSet resultSet = null;
        if (getHost().getDatabaseMetaData().getDatabaseMajorVersion() == 2) {
            String query = "select RDB$FUNCTION_NAME,\n" +
                    "RDB$DESCRIPTION,\n" +
                    "RDB$MODULE_NAME,\n" +
                    "RDB$ENTRYPOINT,\n" +
                    "RDB$RETURN_ARGUMENT,\n" +
                    "RDB$DESCRIPTION as description\n" +
                    "from RDB$FUNCTIONS\n" +
                    "where RDB$SYSTEM_FLAG =0 or RDB$SYSTEM_FLAG is null\n" +
                    "order by RDB$FUNCTION_NAME";
            if (typeTree == TreePanel.DEPENDED_ON)
                query = getDependOnQuery(15);
            else if (typeTree == TreePanel.DEPENDENT)
                query = getDependentQuery(15);
            resultSet = getResultSetFromQuery(query);
        } else {
            String query;
            query = "select RDB$FUNCTION_NAME,\n" +
                    "RDB$DESCRIPTION,\n" +
                    "RDB$MODULE_NAME,\n" +
                    "RDB$ENTRYPOINT,\n" +
                    "RDB$RETURN_ARGUMENT,\n" +
                    "RDB$DESCRIPTION as description\n" +
                    "from RDB$FUNCTIONS\n" +
                    "where RDB$LEGACY_FLAG = 1 and (RDB$MODULE_NAME is not NULL) and (RDB$SYSTEM_FLAG =0 or RDB$SYSTEM_FLAG is null)\n" +
                    "order by RDB$FUNCTION_NAME";
            if (typeTree == TreePanel.DEPENDED_ON)
                query = getDependOnQuery(15);
            else if (typeTree == TreePanel.DEPENDENT)
                query = getDependentQuery(15);
            resultSet = getResultSetFromQuery(query);
        }

        return resultSet;
    }

    private ResultSet getSystemIndexResultSet() throws SQLException {
        String query = "select " +
                "I.RDB$INDEX_NAME,\n" +
                "I.RDB$INDEX_INACTIVE\n" +
                "FROM RDB$INDICES AS I LEFT JOIN rdb$relation_constraints as c on i.rdb$index_name=c.rdb$index_name\n" +
                "where I.RDB$SYSTEM_FLAG = 1 \n" +
                "ORDER BY I.RDB$INDEX_NAME";

        return getResultSetFromQuery(query);
    }

    private ResultSet getSystemTriggerResultSet() throws SQLException {
        String query = "select t.rdb$trigger_name,\n" +
                "t.rdb$trigger_inactive\n" +
                "from rdb$triggers t\n" +
                "where t.rdb$system_flag <> 0" +
                "order by t.rdb$trigger_name";
        return getResultSetFromQuery(query);
    }

    private ResultSet getDDLTriggerResultSet() throws SQLException {
        String query = "select t.rdb$trigger_name,\n" +
                "t.rdb$trigger_inactive\n" +
                "from rdb$triggers t\n" +
                "where t.rdb$system_flag = 0" +
                "and bin_and(t.rdb$trigger_type," + DefaultDatabaseTrigger.RDB_TRIGGER_TYPE_MASK + ")=" + DefaultDatabaseTrigger.TRIGGER_TYPE_DDL + " \n" +
                "order by t.rdb$trigger_name";
        return getResultSetFromQuery(query);
    }

    private ResultSet getDatabaseTriggerResultSet() throws SQLException {
        String query = "select t.rdb$trigger_name,\n" +
                "t.rdb$trigger_inactive\n" +
                "from rdb$triggers t\n" +
                "where t.rdb$system_flag = 0" +
                "and bin_and(t.rdb$trigger_type," + DefaultDatabaseTrigger.RDB_TRIGGER_TYPE_MASK + ")=" + DefaultDatabaseTrigger.TRIGGER_TYPE_DB + " \n" +
                "order by t.rdb$trigger_name";
        return getResultSetFromQuery(query);
    }

    private ResultSet getPackagesResultSet() throws SQLException {
        String query = "select p.rdb$package_name \n" +
                "from rdb$packages p\n" +
                "WHERE RDB$SYSTEM_FLAG=0 OR RDB$SYSTEM_FLAG IS NULL ORDER BY 1";
        if (typeTree == TreePanel.DEPENDED_ON)
            query = getDependOnQuery(19);
        else if (typeTree == TreePanel.DEPENDENT)
            query = getDependentQuery(19);
        return getResultSetFromQuery(query);
    }

    private ResultSet getTablesResultSet(String metaDataKey, boolean repeat) throws SQLException {
        ResultSet resultSet = null;
        if (metaDataKey.equals(NamedObject.META_TYPES[TABLE])) {
            String rel_type = " and (rdb$relation_type=0 or rdb$relation_type=2 or rdb$relation_type is NULL)";
            if (getHost().getDatabaseMetaData().getDatabaseMajorVersion() < 2 || repeat)
                rel_type = "";
            String query = "select rdb$relation_name \n" +
                    "from rdb$relations\n" +
                    "where rdb$view_blr is null \n" +
                    "and (rdb$system_flag is null or rdb$system_flag = 0)" +
                    rel_type +
                    "\norder by rdb$relation_name";
            if (typeTree == TreePanel.DEPENDED_ON)
                query = getDependOnQuery(0);
            else if (typeTree == TreePanel.DEPENDENT)
                query = getDependentQuery(0);
            else if (typeTree == TreePanel.TABLESPACE)
                query = ((DefaultDatabaseTablespace) dependedObject).getTablesQuery();
            resultSet = getResultSetFromQuery(query);
        } else if (metaDataKey.equals(NamedObject.META_TYPES[SYSTEM_TABLE])) {
            String query = "select rdb$relation_name \n" +
                    "from rdb$relations\n" +
                    "where rdb$view_blr is null \n" +
                    "and (rdb$system_flag is not null and rdb$system_flag = 1) \n" +
                    "order by rdb$relation_name";
            resultSet = getResultSetFromQuery(query);
        } else if (metaDataKey.equals(NamedObject.META_TYPES[VIEW])) {
            String query = "select rdb$relation_name \n" +
                    "from rdb$relations\n" +
                    "where rdb$view_blr is not null \n" +
                    "and (rdb$system_flag is null or rdb$system_flag = 0) \n" +
                    "order by rdb$relation_name";
            if (typeTree == TreePanel.DEPENDED_ON)
                query = getDependOnQuery(1);
            else if (typeTree == TreePanel.DEPENDENT)
                query = getDependentQuery(1);
            resultSet = getResultSetFromQuery(query);
        } else if (metaDataKey.equals(NamedObject.META_TYPES[SYSTEM_VIEW])) {
            String query = "select rdb$relation_name \n" +
                    "from rdb$relations\n" +
                    "where rdb$view_blr is not null \n" +
                    "and (rdb$system_flag is not null and rdb$system_flag = 1) \n" +
                    "order by rdb$relation_name";
            resultSet = getResultSetFromQuery(query);
        } else if (metaDataKey.equals(NamedObject.META_TYPES[GLOBAL_TEMPORARY])) {
            String query = "select r.rdb$relation_name \n" +
                    "from rdb$relations r\n" +
                    "join rdb$types t on r.rdb$relation_type = t.rdb$type \n" +
                    "where\n" +
                    "(t.rdb$field_name = 'RDB$RELATION_TYPE') \n" +
                    "and (t.rdb$type = 4 or t.rdb$type = 5) \n" +
                    "order by r.rdb$relation_name";
            resultSet = getResultSetFromQuery(query);
        }

        return resultSet;
    }

    private String schemaNameForQuery() {

        return getHost().getSchemaNameForQueries(getSchemaName());
    }

    private String catalogNameForQuery() {

        return getHost().getCatalogNameForQueries(getCatalogName());
    }

    private ResultSet getFunctionsResultSet() throws SQLException {

        try {
            String catalogName = catalogNameForQuery();
            String schemaName = schemaNameForQuery();

            DatabaseMetaData dmd = getHost().getDatabaseMetaData();
            String query = "select 0,\n" +
                    "0,\n" +
                    "rdb$function_name as function_name,\n" +
                    "rdb$description as remarks\n" +
                    "from rdb$functions\n" +
                    "where (RDB$MODULE_NAME is NULL) and (RDB$PACKAGE_NAME is NULL)\n" +
                    "order by function_name ";

            Connection realConnection = ((PooledConnection) dmd.getConnection()).getRealConnection();
            if (realConnection.unwrap(Connection.class).getClass().getName().contains("FBConnection")) {
                if (typeTree == TreePanel.DEPENDED_ON)
                    query = getDependOnQuery(15);
                else if (typeTree == TreePanel.DEPENDENT)
                    query = getDependentQuery(15);
                ResultSet rs = getResultSetFromQuery(query);
                return rs;
            } else {
                return dmd.getFunctions(catalogName, schemaName, null);
            }

        } catch (Throwable e) {

            // possible SQLFeatureNotSupportedException

            Log.warning("Error retrieving database functions - " + e.getMessage());
            Log.warning("Reverting to old function retrieval implementation");

            return getFunctionResultSetOldImpl();
        }
    }

    private ResultSet getFunctionResultSetOldImpl() throws SQLException {

        DatabaseMetaData dmd = getHost().getDatabaseMetaData();
        return dmd.getProcedures(getCatalogName(), getSchemaName(), null);
    }

    /**
     * Loads the system function types.
     */
    private List<NamedObject> getSystemFunctionTypes() {

        List<NamedObject> objects = new ArrayList<NamedObject>(3);

        objects.add(new DefaultSystemFunctionMetaTag(
                this, SYSTEM_STRING_FUNCTIONS, "String Functions"));

        objects.add(new DefaultSystemFunctionMetaTag(
                this, SYSTEM_NUMERIC_FUNCTIONS, "Numeric Functions"));

        objects.add(new DefaultSystemFunctionMetaTag(
                this, SYSTEM_DATE_TIME_FUNCTIONS, "Date/Time Functions"));

        return objects;
    }

    /**
     * Returns the sub-type indicator of this meta tag - the type this
     * meta tag ultimately represents.
     *
     * @return the sub-type, or -1 if not found/available
     */
    public int getSubType() {

        String key = getMetaDataKey();
        for (int i = 0; i < META_TYPES.length; i++) {

            if (META_TYPES[i].equals(key)) {

                return i;
            }

        }

        return -1;
    }

    /**
     * Returns the parent host object.
     *
     * @return the parent object
     */
    public DatabaseHost getHost() {
        return host;
    }

    /**
     * Returns the name of this object.
     *
     * @return the object name
     */
    public String getName() {
        return Bundles.get(NamedObject.class, NamedObject.META_TYPES_FOR_BUNDLE[getSubType()]);
    }

    /**
     * Override to do nothing - name is the meta data key value.
     */
    public void setName(String name) {
    }

    /**
     * Returns the catalog name or null if there is
     * no catalog attached.
     */
    private String getCatalogName() {

        DatabaseCatalog _catalog = getCatalog();
        if (_catalog != null) {

            return _catalog.getName();
        }

        return null;
    }

    /**
     * Returns the parent catalog object.
     *
     * @return the parent catalog object
     */
    public DatabaseCatalog getCatalog() {
        return catalog;
    }

    /**
     * Returns the schema name or null if there is
     * no schema attached.
     */
    private String getSchemaName() {

        DatabaseSchema _schema = getSchema();
        if (_schema != null) {

            return _schema.getName();
        }

        return null;
    }

    /**
     * Returns the parent schema object.
     *
     * @return the parent schema object
     */
    public DatabaseSchema getSchema() {
        return schema;
    }

    /**
     * Returns the parent named object of this object.
     *
     * @return the parent object - catalog or schema
     */
    public NamedObject getParent() {
        return getSchema() == null ? getCatalog() : getSchema();
    }

    /**
     * Returns the database object type.
     *
     * @return the object type
     */
    public int getType() {
        return META_TAG;
    }

    /**
     * Returns the meta data key name of this object.
     *
     * @return the meta data key name.
     */
    public String getMetaDataKey() {
        return metaDataKey;
    }

    /**
     * Does nothing.
     */
    public int drop() throws DataSourceException {
        return 0;
    }

    @Override
    public boolean allowsChildren() {
        return true;
    }

    public void setCatalog(DatabaseCatalog catalog) {
        this.catalog = catalog;
    }

    public void setSchema(DatabaseSchema schema) {
        this.schema = schema;
    }

    public int getTypeTree() {
        return typeTree;
    }

    public void setTypeTree(int typeTree) {
        this.typeTree = typeTree;
    }

    private List<Integer> getTypeDependFromDatabaseObject(DatabaseObject databaseObject) {
        ArrayList<Integer> list = new ArrayList<>();
        if (databaseObject instanceof DefaultDatabaseTable)
            list.add(0);
        if (databaseObject instanceof DefaultDatabaseView)
            list.add(1);
        if (databaseObject instanceof DefaultDatabaseTrigger)
            list.add(2);
        if (databaseObject instanceof DefaultDatabaseProcedure)
            list.add(5);
        if (databaseObject instanceof DefaultDatabaseIndex)
            list.add(6);
        if (databaseObject instanceof DefaultDatabaseException)
            list.add(7);
        if (databaseObject instanceof DefaultDatabaseDomain)
            list.add(9);
        if (databaseObject instanceof DefaultDatabaseIndex)
            list.add(10);
        if (databaseObject instanceof DefaultDatabaseSequence)
            list.add(14);
        if (databaseObject instanceof DefaultDatabaseFunction)
            list.add(15);
        if (databaseObject instanceof DefaultDatabasePackage) {
            list.add(18);
            list.add(19);
        }
        return list;
    }

    private String getDependOnQuery(int typeObject) {
        String query = null;
        int version = ((AbstractDatabaseObject) dependedObject).getDatabaseMajorVersion();
        List<Integer> list = getTypeDependFromDatabaseObject(dependedObject);
        String domainsQuery = "select distinct rdb$field_source, cast(null as varchar(64)), cast(9 as integer)\n" +
                "from rdb$relation_fields\n" +
                "where (rdb$relation_name = '" + dependedObject.getName() + "') and (rdb$field_source not starting with 'RDB$')\n" +
                "union all\n";
        String tableQuery = "select distinct\n" +
                "C.RDB$RELATION_NAME as FK_Table,\n" +
                "null, cast(0 as integer)\n" +
                "from RDB$REF_CONSTRAINTS B, RDB$RELATION_CONSTRAINTS A, RDB$RELATION_CONSTRAINTS C,\n" +
                "RDB$INDEX_SEGMENTS D, RDB$INDEX_SEGMENTS E, RDB$INDICES I\n" +
                "where (A.RDB$CONSTRAINT_TYPE = 'FOREIGN KEY') and\n" +
                "(A.RDB$CONSTRAINT_NAME = B.RDB$CONSTRAINT_NAME) and\n" +
                "(B.RDB$CONST_NAME_UQ=C.RDB$CONSTRAINT_NAME) and (C.RDB$INDEX_NAME=D.RDB$INDEX_NAME) and\n" +
                "(A.RDB$INDEX_NAME=E.RDB$INDEX_NAME) and\n" +
                "(A.RDB$INDEX_NAME=I.RDB$INDEX_NAME)\n" +
                "and (A.RDB$RELATION_NAME = '" + dependedObject.getName() + "')\n" +
                "union all\n";
        String condition = "";
        if (version > 2)
            condition = "and (T2.RDB$PACKAGE_NAME IS NULL)\n";
        String packageQuery = "select distinct T2.RDB$PACKAGE_NAME, cast(T2.RDB$FIELD_NAME as varchar(64)), CAST(19 AS INTEGER)\n" +
                "from RDB$DEPENDENCIES T2 where (T2.RDB$DEPENDENT_NAME = 'COUNTRY')\n" +
                "and (T2.RDB$DEPENDENT_TYPE = 0)\n" +
                condition +
                "union all\n";
        condition = "";
        if (version > 2)
            condition = "and (T1.RDB$PACKAGE_NAME IS NULL)\n";
        String comparing = "";
        for (int i = 0; i < list.size(); i++) {
            String union = "or";
            if (i == 0)
                union = "and (";
            comparing += union + " (t1.RDB$DEPENDENT_TYPE = " + list.get(i) + ")\n";
        }
        comparing += ")";
        query = "select distinct t1.RDB$DEPENDED_ON_NAME, null, CAST(T1.RDB$DEPENDED_ON_TYPE AS INTEGER)\n" +
                "from RDB$DEPENDENCIES t1 where (t1.RDB$DEPENDENT_NAME = '" + dependedObject.getName() + "')\n" +
                comparing +
                condition +
                "and (T1.RDB$DEPENDED_ON_TYPE=" + typeObject + ")\n" +
                "union all\n" +
                "select distinct d.rdb$depended_on_name, null, CAST(D.RDB$DEPENDED_ON_TYPE AS INTEGER)\n" +
                "from rdb$dependencies d, rdb$relation_fields f\n" +
                "where (d.rdb$dependent_type = 3) and\n" +
                "(d.rdb$dependent_name = f.rdb$field_source)\n" +
                "and (f.rdb$relation_name = '" + dependedObject.getName() + "')\n" +
                "and (D.RDB$DEPENDED_ON_TYPE='" + typeObject + "')\n" +
                "order by 1,2";
        if (typeObject == 9)
            query = domainsQuery + query;
        if (typeObject == 18 || typeObject == 19)
            query = packageQuery + query;
        if (typeObject == 0)
            query = tableQuery + query;
        return query;
    }

    private String getDependentQuery(int typeObject) {
        String query = null;
        int version = ((AbstractDatabaseObject) dependedObject).getDatabaseMajorVersion();
        List<Integer> list = getTypeDependFromDatabaseObject(dependedObject);
        String tableQuery = "union all\n" +
                "select distinct f2.rdb$relation_name\n" +
                "from rdb$dependencies d2, rdb$relation_fields f2\n" +
                "left join rdb$relations r2 on ((f2.rdb$relation_name = r2.rdb$relation_name) and (not (r2.Rdb$View_Blr is null)))\n" +
                "where (d2.rdb$dependent_type = 3) and\n" +
                "(d2.rdb$dependent_name = f2.rdb$field_source)\n" +
                "and (d2.rdb$depended_on_name = '" + dependedObject.getName() + "')\n" +
                "union all\n" +
                "select distinct A.RDB$RELATION_NAME\n" +
                "from RDB$REF_CONSTRAINTS B, RDB$RELATION_CONSTRAINTS A, RDB$RELATION_CONSTRAINTS C,\n" +
                "RDB$INDEX_SEGMENTS D, RDB$INDEX_SEGMENTS E\n" +
                "where (A.RDB$CONSTRAINT_TYPE = 'FOREIGN KEY') and\n" +
                "(A.RDB$CONSTRAINT_NAME = B.RDB$CONSTRAINT_NAME) and\n" +
                "(B.RDB$CONST_NAME_UQ=C.RDB$CONSTRAINT_NAME) and (C.RDB$INDEX_NAME=D.RDB$INDEX_NAME) and\n" +
                "(A.RDB$INDEX_NAME=E.RDB$INDEX_NAME)\n" +
                "and (C.RDB$RELATION_NAME = '" + dependedObject.getName() + "')\n";
        String comparing = "";
        for (int i = 0; i < list.size(); i++) {
            String union = "or";
            if (i == 0)
                union = "and (";
            comparing += union + " (d1.RDB$DEPENDED_ON_TYPE = " + list.get(i) + ")\n";
        }
        comparing += ")";
        query = "select distinct D1.RDB$DEPENDENT_NAME\n" +
                "from RDB$DEPENDENCIES D1\n" +
                "left join rdb$relations r1 on ((D1.RDB$DEPENDENT_NAME = r1.rdb$relation_name) and (not (r1.Rdb$View_Blr is null)))\n" +
                "where (D1.RDB$DEPENDENT_TYPE = " + typeObject + ")\n" +
                "and (D1.RDB$DEPENDENT_TYPE <> 3)\n" +
                "and (D1.RDB$DEPENDED_ON_NAME = '" + dependedObject.getName() + "')\n" +
                comparing;
        if (list.contains(9)) {
            tableQuery = "union all\n" +
                    "SELECT distinct F.RDB$RELATION_NAME\n" +
                    "FROM RDB$RELATION_FIELDS F, RDB$RELATIONS R\n" +
                    "WHERE (R.RDB$VIEW_BLR IS NULL) AND (F.RDB$RELATION_NAME = R.RDB$RELATION_NAME) AND\n" +
                    "(F.RDB$FIELD_SOURCE = '" + dependedObject.getName() + "')";
            if (typeObject == 1)
                query += "\nUNION ALL\n" +
                        "SELECT distinct F1.RDB$RELATION_NAME\n" +
                        "FROM RDB$RELATION_FIELDS F1, RDB$RELATIONS R1\n" +
                        " WHERE (NOT (R1.RDB$VIEW_BLR IS NULL)) " +
                        "AND (F1.RDB$RELATION_NAME = R1.RDB$RELATION_NAME) " +
                        "AND(F1.RDB$FIELD_SOURCE = '" + dependedObject.getName() + "')\n" +
                        "UNION ALL SELECT RF.RDB$RELATION_NAME\n" +
                        "FROM RDB$DEPENDENCIES D1 LEFT JOIN RDB$RELATION_FIELDS RF ON (RF.RDB$FIELD_SOURCE = D1.RDB$DEPENDENT_NAME) " +
                        "\nWHERE (D1.RDB$DEPENDED_ON_NAME =  '" + dependedObject.getName() + "')  " +
                        "AND (D1.RDB$DEPENDENT_TYPE = 3) AND(RF.RDB$VIEW_CONTEXT IS NOT NULL)";
            if (typeObject == 5)
                query += "UNION ALL SELECT P.RDB$PROCEDURE_NAME FROM RDB$PROCEDURE_PARAMETERS P WHERE (P.RDB$FIELD_SOURCE = '" + dependedObject.getName() + "')";
        }
        if (typeObject == 0) {
            query = query + tableQuery;
        }
        return query;
    }
}


