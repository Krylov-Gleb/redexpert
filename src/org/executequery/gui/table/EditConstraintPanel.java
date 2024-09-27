package org.executequery.gui.table;

import org.apache.commons.lang.math.NumberUtils;
import org.executequery.Constants;
import org.executequery.databaseobjects.DatabaseColumn;
import org.executequery.databaseobjects.DatabaseTable;
import org.executequery.databaseobjects.DatabaseTableObject;
import org.executequery.databaseobjects.NamedObject;
import org.executequery.databaseobjects.impl.AbstractTableObject;
import org.executequery.databaseobjects.impl.ColumnConstraint;
import org.executequery.gui.ActionContainer;
import org.executequery.gui.WidgetFactory;
import org.executequery.gui.browser.ConnectionsTreePanel;
import org.executequery.gui.databaseobjects.AbstractCreateObjectPanel;
import org.executequery.gui.text.SimpleSqlTextPanel;
import org.executequery.localization.Bundles;
import org.executequery.log.Log;
import org.underworldlabs.swing.ListSelectionPanel;
import org.underworldlabs.swing.Named;
import org.underworldlabs.swing.layouts.GridBagHelper;
import org.underworldlabs.util.MiscUtils;
import org.underworldlabs.util.SQLUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;

import static org.executequery.gui.browser.ColumnConstraint.RULES;

public class EditConstraintPanel extends AbstractCreateObjectPanel {
    public static final String CREATE_TITLE = getCreateTitle(NamedObject.CONSTRAINT);
    public static final String EDIT_TITLE = getEditTitle(NamedObject.CONSTRAINT);

    // --- GUI components ---

    private JComboBox<String> typesCombo;
    private JComboBox<String> updateRulesCombo;
    private JComboBox<String> deleteRulesCombo;
    private JComboBox<String> tablespacesCombo;
    private JComboBox<String> primarySortingCombo;
    private JComboBox<String> foreignSortingCombo;
    private JComboBox<NamedObject> referenceTablesCombo;

    private JTextField tableNameField;
    private JTextField primaryIndexField;
    private JTextField foreignIndexField;
    private SimpleSqlTextPanel checkTextArea;

    private ListSelectionPanel primaryFieldSelectionPanel;
    private ListSelectionPanel constraintFieldSelectionPanel;
    private ListSelectionPanel referenceColumnsSelectionPanel;

    private JPanel checkPanel;
    private JPanel foreignPanel;
    private JPanel primaryPanel;
    private JComponent typePanel;

    // ---

    private boolean isNameAutoGenerated;

    private DatabaseTable table;
    private ColumnConstraint constraint;
    private List<String> tablespacesList;

    public EditConstraintPanel(DatabaseTable table, ActionContainer dialog) {
        super(table.getHost().getDatabaseConnection(), dialog, null, new Object[]{table});
    }

    public EditConstraintPanel(DatabaseTable table, ActionContainer dialog, ColumnConstraint columnConstraint) {
        super(table.getHost().getDatabaseConnection(), dialog, columnConstraint, new Object[]{table});
    }

    public EditConstraintPanel(DatabaseTableObject table, ActionContainer dialog, ColumnConstraint columnConstraint) {
        super(table.getHost().getDatabaseConnection(), dialog, columnConstraint, new Object[]{table});
    }

    public EditConstraintPanel(DatabaseTable table, ActionContainer dialog, int type) {
        super(table.getHost().getDatabaseConnection(), dialog, null, new Object[]{table});
        setConstraintType(type);
    }

    public EditConstraintPanel(DatabaseTableObject table, ActionContainer dialog, int type) {
        super(table.getHost().getDatabaseConnection(), dialog, null, new Object[]{table});
        setConstraintType(type);
    }

    // --- initializing ---

    @Override
    protected void init() {
        isNameAutoGenerated = true;

        tablespacesList = ConnectionsTreePanel.getPanelFromBrowser()
                .getDefaultDatabaseHostFromConnection(connection)
                .getDatabaseObjectsForMetaTag(NamedObject.META_TYPES[NamedObject.TABLESPACE])
                .stream().map(Named::getName).collect(Collectors.toList());

        List<NamedObject> tablesList = ConnectionsTreePanel.getPanelFromBrowser()
                .getDefaultDatabaseHostFromConnection(connection)
                .getDatabaseObjectsForMetaTag(NamedObject.META_TYPES[NamedObject.TABLE]);

        String[] sorting = new String[]{
                Bundles.get("CreateIndexPanel.ascending"),
                Bundles.get("CreateIndexPanel.descending")
        };

        String[] types = new String[]{
                ColumnConstraint.PRIMARY,
                ColumnConstraint.FOREIGN,
                ColumnConstraint.UNIQUE,
                ColumnConstraint.CHECK
        };

        // --- combo boxes ---

        typesCombo = WidgetFactory.createComboBox("typesCombo", types);
        updateRulesCombo = WidgetFactory.createComboBox("updateRulesCombo", RULES);
        deleteRulesCombo = WidgetFactory.createComboBox("deleteRulesCombo", RULES);
        primarySortingCombo = WidgetFactory.createComboBox("primarySortingCombo", sorting);
        foreignSortingCombo = WidgetFactory.createComboBox("foreignSortingCombo", sorting);
        referenceTablesCombo = WidgetFactory.createComboBox("referenceTablesCombo", tablesList);

        tablespacesCombo = WidgetFactory.createComboBox("tablespacesCombo", tablespacesList);
        tablespacesCombo.insertItemAt("PRIMARY", 0);

        // --- selections lists ---

        primaryFieldSelectionPanel = WidgetFactory.createListSelectionPanel(
                "primaryFieldSelectionPanel",
                getColumnNamesFromColumns(table.getColumns()),
                BorderFactory.createTitledBorder(bundleString("OnField"))
        );

        constraintFieldSelectionPanel = WidgetFactory.createListSelectionPanel(
                "constraintFieldSelectionPanel",
                getColumnNamesFromColumns(table.getColumns())
        );

        referenceColumnsSelectionPanel = WidgetFactory.createListSelectionPanel(
                "constraintFieldSelectionPanel",
                getColumnNamesFromColumns(((AbstractTableObject) tablesList.get(0)).getColumns())
        );

        // --- text fields ---

        primaryIndexField = WidgetFactory.createTextField("primaryIndexField");
        foreignIndexField = WidgetFactory.createTextField("foreignIndexField");

        tableNameField = WidgetFactory.createTextField("tableNameField", table.getName());
        tableNameField.setEnabled(false);

        // --- others ---

        checkPanel = WidgetFactory.createPanel("checkPanel");
        foreignPanel = WidgetFactory.createPanel("foreignPanel");
        primaryPanel = WidgetFactory.createPanel("primaryPanel");

        checkTextArea = WidgetFactory.createSimpleSqlTextPanel("checkPanel");
        checkTextArea.setSQLText("CHECK(\n\t/* your code here */\n)");

        // ---

        setBorder(null);

        arrange();
        initListeners();
        loadPanel();
    }

    @Override
    protected void initEdited() {
        isNameAutoGenerated = false;

        nameField.setText(constraint.getName().trim());
        nameField.setEnabled(false);

        tableNameField.setText(constraint.getTableName());
        tableNameField.setEnabled(false);

        typesCombo.setSelectedItem(constraint.getTypeName());
        typesCombo.setEnabled(false);

        if (isPrimaryTypeSelected())
            loadPrimary();
        else if (isForeignTypeSelected())
            loadForeign();
        else if (isCheckTypeSelected())
            loadCheck();
        else if (isUniqueTypeSelected())
            loadUnique();

        SimpleSqlTextPanel ddlPanel = WidgetFactory.createSimpleSqlTextPanel("ddlPanel");
        ddlPanel.setSQLText(generateQuery().replaceAll("\n\tDROP CONSTRAINT .+,", ""));

        tabbedPane.add(ddlPanel, bundleStaticString("createSQL"));
    }

    // --- arranging ---

    private void arrange() {
        GridBagHelper gbh;

        // --- primary panel ---

        gbh = new GridBagHelper().topGap(5).anchorNorthWest().fillHorizontally();
        primaryPanel.add(WidgetFactory.createLabel(bundleString("Index")), gbh.setMinWeightX().get());
        primaryPanel.add(primaryIndexField, gbh.nextCol().leftGap(5).setMaxWeightX().get());
        primaryPanel.add(WidgetFactory.createLabel(bundleString("Sorting")), gbh.nextRowFirstCol().leftGap(0).setMinWeightX().get());
        primaryPanel.add(primarySortingCombo, gbh.nextCol().leftGap(5).setMaxWeightX().get());
        primaryPanel.add(primaryFieldSelectionPanel, gbh.nextRowFirstCol().leftGap(0).fillBoth().spanX().spanY().get());

        // --- foreign panel ---

        JTabbedPane foreignTabbedPane = WidgetFactory.createTabbedPane("foreignTabbedPane");
        foreignTabbedPane.addTab(bundleString("OnField"), constraintFieldSelectionPanel);
        foreignTabbedPane.addTab(bundleString("ReferenceColumn"), referenceColumnsSelectionPanel);

        gbh = new GridBagHelper().topGap(5).anchorNorthWest().fillHorizontally();
        foreignPanel.add(WidgetFactory.createLabel(bundleString("Index")), gbh.setMinWeightX().get());
        foreignPanel.add(foreignIndexField, gbh.nextCol().leftGap(5).setMaxWeightX().get());
        foreignPanel.add(WidgetFactory.createLabel(bundleString("Sorting")), gbh.nextRowFirstCol().leftGap(0).setMinWeightX().get());
        foreignPanel.add(foreignSortingCombo, gbh.nextCol().leftGap(5).setMaxWeightX().get());
        foreignPanel.add(WidgetFactory.createLabel(bundleString("UpdateRule")), gbh.nextRowFirstCol().leftGap(0).setMinWeightX().get());
        foreignPanel.add(updateRulesCombo, gbh.nextCol().leftGap(5).setMaxWeightX().get());
        foreignPanel.add(WidgetFactory.createLabel(bundleString("DeleteRule")), gbh.nextRowFirstCol().leftGap(0).setMinWeightX().get());
        foreignPanel.add(deleteRulesCombo, gbh.nextCol().leftGap(5).setMaxWeightX().get());
        foreignPanel.add(WidgetFactory.createLabel(bundleString("ReferenceTable")), gbh.nextRowFirstCol().leftGap(0).setMinWeightX().get());
        foreignPanel.add(referenceTablesCombo, gbh.nextCol().leftGap(5).setMaxWeightX().get());
        foreignPanel.add(foreignTabbedPane, gbh.nextRowFirstCol().leftGap(0).fillBoth().spanX().spanY().get());

        // --- check panel ---

        checkPanel.add(checkTextArea, new GridBagHelper().spanX().spanY().fillBoth().get());

        // --- central panel ---

        centralPanel.setLayout(new GridBagLayout());

        gbh = new GridBagHelper().setInsets(5, 0, 0, 5).anchorNorthWest().fillHorizontally();
        centralPanel.add(WidgetFactory.createLabel(Bundles.get("common.table")), gbh.setMinWeightX().get());
        centralPanel.add(tableNameField, gbh.nextCol().rightGap(5).setMaxWeightX().get());
        if (tablespacesList != null) {
            centralPanel.add(WidgetFactory.createLabel(Bundles.get("common.tablespace")), gbh.nextRowFirstCol().rightGap(0).setMinWeightX().get());
            centralPanel.add(tablespacesCombo, gbh.nextCol().rightGap(5).setMaxWeightX().get());
        }
        centralPanel.add(WidgetFactory.createLabel(Bundles.get("common.type")), gbh.nextRowFirstCol().rightGap(0).bottomGap(0).setMinWeightX().get());
        centralPanel.add(typesCombo, gbh.nextCol().rightGap(5).setMaxWeightX().get());
    }

    // --- loaders ---

    private void loadPanel() {

        if (typePanel != null)
            tabbedPane.remove(typePanel);

        boolean tablespacesEnable = isCheckTypeSelected() && tablespacesList != null;
        if (tablespacesEnable != tablespacesCombo.isEnabled())
            tablespacesCombo.setSelectedIndex(0);
        tablespacesCombo.setEnabled(tablespacesEnable);

        if (isPrimaryTypeSelected() || isUniqueTypeSelected())
            typePanel = primaryPanel;
        else if (isForeignTypeSelected())
            typePanel = foreignPanel;
        else if (isCheckTypeSelected())
            typePanel = checkPanel;
        else
            typePanel = WidgetFactory.createPanel("emptyPanel");

        tabbedPane.add(typePanel, 0);
        tabbedPane.setTitleAt(0, bundleString("Constraint"));
        if (isNameAutoGenerated)
            nameField.setText(generateName());

        updateUI();
    }

    private void loadPrimary() {
        try {
            ResultSet rs = sender.getResultSet(getPrimaryQuery()).getResultSet();
            while (rs.next()) {

                primaryFieldSelectionPanel.selectOneStringAction(rs.getString("RDB$FIELD_NAME").trim());
                primaryIndexField.setText(rs.getString("RDB$INDEX_NAME").trim());
                primarySortingCombo.setSelectedIndex(rs.getInt("RDB$INDEX_TYPE"));

                if (tablespacesList != null) {
                    String tablespace = rs.getString("RDB$TABLESPACE_NAME");
                    if (tablespace != null) {
                        tablespacesList.stream()
                                .filter(ts -> Objects.equals(ts.toUpperCase(), tablespace.toUpperCase().trim()))
                                .findFirst().ifPresent(ts -> tablespacesCombo.setSelectedItem(ts));
                    }
                }
            }

        } catch (SQLException e) {
            Log.error(e.getMessage(), e);

        } finally {
            sender.releaseResources();
        }
    }

    private void loadForeign() {

        NamedObject foreignTable = null;
        for (int typeTable = NamedObject.TABLE; typeTable <= NamedObject.VIEW && foreignTable == null; typeTable++) {
            foreignTable = ConnectionsTreePanel.getNamedObjectFromHost(
                    table.getHost().getDatabaseConnection(),
                    typeTable,
                    constraint.getReferencedTable().trim()
            );
        }

        if (foreignTable == null) {
            foreignTable = ConnectionsTreePanel.getNamedObjectFromHost(
                    table.getHost().getDatabaseConnection(),
                    NamedObject.SYSTEM_TABLE,
                    constraint.getReferencedTable().trim()
            );
        }

        referenceTablesCombo.setSelectedItem(foreignTable);

        try {
            ResultSet rs = sender.getResultSet(getForeignQuery()).getResultSet();
            while (rs.next()) {

                foreignIndexField.setText(rs.getString("RDB$INDEX_NAME").trim());
                constraintFieldSelectionPanel.selectOneStringAction(rs.getString("OnField").trim());
                referenceColumnsSelectionPanel.selectOneStringAction(rs.getString("FK_Field").trim());
                foreignSortingCombo.setSelectedIndex(rs.getInt("RDB$INDEX_TYPE"));
                updateRulesCombo.setSelectedItem(constraint.getUpdateRule());
                deleteRulesCombo.setSelectedItem(constraint.getDeleteRule());

                if (tablespacesList != null) {
                    String tablespace = rs.getString("RDB$TABLESPACE_NAME");
                    if (tablespace != null) {
                        tablespacesList.stream()
                                .filter(ts -> Objects.equals(ts.toUpperCase(), tablespace.toUpperCase().trim()))
                                .findFirst().ifPresent(ts -> tablespacesCombo.setSelectedItem(ts));
                    }
                }
            }

        } catch (SQLException e) {
            Log.error(e.getMessage(), e);

        } finally {
            sender.releaseResources();
        }
    }

    private void loadUnique() {
        try {
            ResultSet rs = sender.getResultSet(getPrimaryQuery()).getResultSet();
            while (rs.next()) {

                for (int i = 0; i < primaryFieldSelectionPanel.getAvailableValues().size(); i++) {
                    if (primaryFieldSelectionPanel.getAvailableValues().get(i).toString().trim().contentEquals(rs.getString("RDB$FIELD_NAME").trim())) {
                        primaryFieldSelectionPanel.selectOneAction(i);
                        break;
                    }
                }

                primaryIndexField.setText(rs.getString("RDB$INDEX_NAME").trim());
                primarySortingCombo.setSelectedIndex(rs.getInt("RDB$INDEX_TYPE"));

                if (tablespacesList != null) {
                    String tablespace = rs.getString("RDB$TABLESPACE_NAME");
                    if (tablespace != null) {
                        tablespacesList.stream()
                                .filter(ts -> Objects.equals(ts.toUpperCase(), tablespace.toUpperCase().trim()))
                                .findFirst().ifPresent(ts -> tablespacesCombo.setSelectedItem(ts));
                    }
                }
            }

        } catch (SQLException e) {
            Log.error(e.getMessage(), e);

        } finally {
            sender.releaseResources();
        }
    }

    private void loadCheck() {
        checkTextArea.setSQLText(constraint.getCheck().toUpperCase());
    }

    private void setConstraintType(int type) {
        switch (type) {
            case NamedObject.PRIMARY_KEY:
                typesCombo.setSelectedItem(ColumnConstraint.PRIMARY);
                break;
            case NamedObject.FOREIGN_KEY:
                typesCombo.setSelectedItem(ColumnConstraint.FOREIGN);
                break;
            case NamedObject.UNIQUE_KEY:
                typesCombo.setSelectedItem(ColumnConstraint.UNIQUE);
                break;
            case NamedObject.CHECK_KEY:
                typesCombo.setSelectedItem(ColumnConstraint.CHECK);
                break;
        }

        typesCombo.setEnabled(false);
    }

    private void initListeners() {

        typesCombo.addItemListener(this::typesComboTriggered);
        referenceTablesCombo.addItemListener(this::referenceTablesComboTriggered);

        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                isNameAutoGenerated = false;
            }
        });
    }

    // --- handlers ---

    private void typesComboTriggered(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED)
            loadPanel();
    }

    private void referenceTablesComboTriggered(ItemEvent e) {
        if (e.getStateChange() != ItemEvent.SELECTED)
            return;

        AbstractTableObject tableObject = ((AbstractTableObject) referenceTablesCombo.getSelectedItem());
        List<String> values = tableObject != null ?
                getColumnNamesFromColumns(tableObject.getColumns()) :
                new ArrayList<>();

        referenceColumnsSelectionPanel.createAvailableList(values);
    }

    // --- helpers ---

    private String getPrimaryQuery() {
        return "SELECT\n" +
                "I.RDB$FIELD_NAME,\n" +
                "RC.RDB$INDEX_NAME,\n" +
                "IDX.RDB$INDEX_TYPE" +
                (tablespacesList != null ? ",\nIDX.RDB$TABLESPACE_NAME\n" : "\n") +
                "FROM RDB$RELATION_CONSTRAINTS RC,\n" +
                "RDB$INDEX_SEGMENTS I,\n" +
                "RDB$INDICES IDX\n" +
                "WHERE (RC.RDB$CONSTRAINT_NAME = '" + nameField.getText() + "')\n" +
                "AND (I.RDB$INDEX_NAME = RC.RDB$INDEX_NAME)\n" +
                "AND (IDX.RDB$INDEX_NAME = RC.RDB$INDEX_NAME)\n" +
                "AND (RC.RDB$RELATION_NAME = '" + table.getName() + "')\n" +
                "ORDER BY RC.RDB$RELATION_NAME, I.RDB$FIELD_POSITION";
    }

    private String getForeignQuery() {
        return "SELECT\n" +
                "A.RDB$RELATION_NAME,\n" +
                "A.RDB$CONSTRAINT_NAME,\n" +
                "A.RDB$CONSTRAINT_TYPE,\n" +
                "B.RDB$CONST_NAME_UQ,\n" +
                "B.RDB$UPDATE_RULE,\n" +
                "B.RDB$DELETE_RULE,\n" +
                "C.RDB$RELATION_NAME AS FK_Table,\n" +
                "A.RDB$INDEX_NAME,\n" +
                "D.RDB$FIELD_NAME AS FK_Field,\n" +
                "E.RDB$FIELD_NAME AS OnField,\n" +
                "I.RDB$INDEX_TYPE" +
                (tablespacesList != null ? ",\nI.RDB$TABLESPACE_NAME\n" : "\n") +
                "FROM RDB$REF_CONSTRAINTS B,\n" +
                "RDB$RELATION_CONSTRAINTS A,\n" +
                "RDB$RELATION_CONSTRAINTS C,\n" +
                "RDB$INDEX_SEGMENTS D,\n" +
                "RDB$INDEX_SEGMENTS E,\n" +
                "RDB$INDICES I\n" +
                "WHERE (A.RDB$CONSTRAINT_TYPE = 'FOREIGN KEY')\n" +
                "AND (A.RDB$CONSTRAINT_NAME = '" + constraint.getName() + "')\n" +
                "AND (A.RDB$CONSTRAINT_NAME = B.RDB$CONSTRAINT_NAME)\n" +
                "AND (B.RDB$CONST_NAME_UQ = C.RDB$CONSTRAINT_NAME)\n" +
                "AND (C.RDB$INDEX_NAME = D.RDB$INDEX_NAME)\n" +
                "AND (A.RDB$INDEX_NAME = E.RDB$INDEX_NAME)\n" +
                "AND (A.RDB$INDEX_NAME = I.RDB$INDEX_NAME)\n" +
                "AND (A.RDB$RELATION_NAME = '" + table.getName() + "')\n" +
                "ORDER BY A.RDB$RELATION_NAME, A.RDB$CONSTRAINT_NAME, D.RDB$FIELD_POSITION, E.RDB$FIELD_POSITION";
    }

    private String generateName() {
        String name = Constants.EMPTY;

        if (isPrimaryTypeSelected())
            name = "PK";
        else if (isForeignTypeSelected())
            name = "FK";
        else if (isCheckTypeSelected())
            name = "CHECK";
        else if (isUniqueTypeSelected())
            name = "UQ";

        name += "_" + table.getName().trim() + "_";

        String number = "0";
        try {
            String query = "SELECT RDB$CONSTRAINT_NAME AS NAME\n" +
                    "FROM RDB$RELATION_CONSTRAINTS\n" +
                    "WHERE RDB$CONSTRAINT_NAME STARTING WITH '" + name + "'\n" +
                    "ORDER BY 1";

            ResultSet rs = sender.getResultSet(query).getResultSet();
            while (rs.next()) {
                String tempNumber = rs.getString("NAME").trim().replace(name, "").trim();
                number = NumberUtils.isNumber(tempNumber) ? tempNumber : number;
            }

            number = String.valueOf(Integer.parseInt(number) + 1);

        } catch (SQLException e) {
            Log.error(e.getMessage(), e);

        } finally {
            sender.releaseResources();
        }

        return name + number;
    }

    private String getColumnsFromVector(Vector<?> vector) {
        return vector.stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.joining(", "));
    }

    private List<String> getColumnNamesFromColumns(List<DatabaseColumn> columns) {
        return columns.stream().map(Named::getName).collect(Collectors.toList());
    }

    private String getFormattedTableName() {
        return MiscUtils.getFormattedObject(table.getName().trim(), getDatabaseConnection());
    }

    private boolean isPrimaryTypeSelected() {
        return Objects.equals(typesCombo.getSelectedItem(), ColumnConstraint.PRIMARY);
    }

    private boolean isForeignTypeSelected() {
        return Objects.equals(typesCombo.getSelectedItem(), ColumnConstraint.FOREIGN);
    }

    private boolean isCheckTypeSelected() {
        return Objects.equals(typesCombo.getSelectedItem(), ColumnConstraint.CHECK);
    }

    private boolean isUniqueTypeSelected() {
        return Objects.equals(typesCombo.getSelectedItem(), ColumnConstraint.UNIQUE);
    }

    // --- AbstractCreateObjectPanel impl ---

    @Override
    protected String generateQuery() {

        org.executequery.gui.browser.ColumnConstraint columnConstraint = new org.executequery.gui.browser.ColumnConstraint();
        columnConstraint.setName(nameField.getText());
        if (tablespacesCombo.getSelectedItem() != null)
            columnConstraint.setTablespace((String) tablespacesCombo.getSelectedItem());

        if (isPrimaryTypeSelected() || isUniqueTypeSelected()) {

            if (isPrimaryTypeSelected())
                columnConstraint.setType(NamedObject.PRIMARY_KEY);
            else
                columnConstraint.setType(NamedObject.UNIQUE_KEY);

            columnConstraint.setColumn(getColumnsFromVector(primaryFieldSelectionPanel.getSelectedValues()));
            columnConstraint.setCountCols(primaryFieldSelectionPanel.getSelectedValues().size());

            if (!primaryIndexField.getText().isEmpty()) {
                String sorting = primarySortingCombo.getSelectedIndex() == 0 ? "ASCENDING" : "DESCENDING";
                columnConstraint.setSorting(sorting);
            }

        } else if (isForeignTypeSelected()) {

            columnConstraint.setType(NamedObject.FOREIGN_KEY);
            columnConstraint.setColumn(getColumnsFromVector(constraintFieldSelectionPanel.getSelectedValues()));
            columnConstraint.setCountCols(constraintFieldSelectionPanel.getSelectedValues().size());

            String refTable = referenceTablesCombo.getSelectedItem() instanceof AbstractTableObject ?
                    ((AbstractTableObject) referenceTablesCombo.getSelectedItem()).getName() :
                    (String) referenceTablesCombo.getSelectedItem();

            columnConstraint.setRefTable(refTable);
            columnConstraint.setRefColumn(getColumnsFromVector(referenceColumnsSelectionPanel.getSelectedValues()));

            if (!foreignIndexField.getText().isEmpty()) {
                String sorting = primarySortingCombo.getSelectedIndex() == 0 ? "ASCENDING" : "DESCENDING";
                columnConstraint.setSorting(sorting);
            }

            columnConstraint.setUpdateRule((String) updateRulesCombo.getSelectedItem());
            columnConstraint.setDeleteRule((String) deleteRulesCombo.getSelectedItem());

        } else if (isCheckTypeSelected()) {
            columnConstraint.setType(NamedObject.CHECK_KEY);
            columnConstraint.setCheck(checkTextArea.getSQLText());
        }

        String constraintDefinition = SQLUtils.generateDefinitionColumnConstraint(
                columnConstraint,
                false,
                true,
                getDatabaseConnection(),
                true
        );

        StringBuilder sb = new StringBuilder();
        sb.append("ALTER TABLE ").append(getFormattedTableName());
        if (editing)
            sb.append("\n\tDROP CONSTRAINT ").append(getFormattedName()).append(",");
        sb.append("\n\tADD ").append(constraintDefinition.replaceFirst(",", "").trim());
        sb.append("\n^");

        return sb.toString();
    }

    @Override
    public void createObject() {
        displayExecuteQueryDialog(generateQuery(), "^");
    }

    @Override
    public String getTypeObject() {
        return NamedObject.META_TYPES[NamedObject.CONSTRAINT];
    }

    @Override
    public void setDatabaseObject(Object databaseObject) {
        constraint = (ColumnConstraint) databaseObject;
    }

    @Override
    public void setParameters(Object[] params) {
        table = (DatabaseTable) params[0];
    }

    @Override
    public String getCreateTitle() {
        return CREATE_TITLE;
    }

    @Override
    public String getEditTitle() {
        return EDIT_TITLE;
    }

    @Override
    protected void reset() {
    }

}
