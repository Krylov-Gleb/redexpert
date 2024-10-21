package org.executequery.gui.querybuilder.toolBar;

import org.executequery.gui.WidgetFactory;
import org.executequery.gui.editor.QueryEditor;
import org.executequery.gui.editor.QueryEditorTextPanel;
import org.executequery.gui.querybuilder.ExecuteQueryEditor;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.executequery.gui.querybuilder.WorkQuryEditor.CreateStringQuery;
import org.executequery.gui.querybuilder.buttonPanel.AddAdditionalSelectSettingsPanel;
import org.executequery.gui.querybuilder.buttonPanel.AddTableQueryBuilder;
import org.executequery.gui.querybuilder.buttonPanel.SettingUpJoinsPanel;
import org.underworldlabs.swing.ConnectionsComboBox;
import org.underworldlabs.swing.RolloverButton;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This class is responsible for creating and managing the toolbar in the main window (QueryBuilderPanel).
 * It performs all the basic functions of QueryBuilder, such as creating tables, joins, and more.
 *
 * @author Krylov Gleb
 */
public class QueryBuilderToolBarPanel extends JToolBar {

    // --- Elements accepted using the constructor ----
    private CreateStringQuery createStringQuery;
    private QueryBuilderPanel queryBuilderPanel;
    private QueryEditorTextPanel queryEditorTextPanel;
    private QueryEditor usingQueryEditor;

    // --- GUI Components ---

    // ComboBoxes
    private ConnectionsComboBox connectionsCombo;

    // Buttons
    private RolloverButton addTable;
    private RolloverButton removeTable;
    private RolloverButton additionalSelectSettings;
    private RolloverButton settingUpJoins;
    private RolloverButton applyQuery;
    private RolloverButton updateQuery;
    private RolloverButton createQuery;
    private RolloverButton clearQuery;

    // Panels
    private AddAdditionalSelectSettingsPanel addAdditionalSelectSettingsPanel;
    private SettingUpJoinsPanel settingUpJoinsPanel;

    // Arrays
    private ArrayList<String> QueryTable;
    private ArrayList<ArrayList<String>> QueryAttributes;

    /**
     * A new toolbar is created and elements are passed to interact with the main panel (QueryBuilderPanel).
     * Graphical components are initialized.
     *
     * @param queryBuilderPanel
     */
    public QueryBuilderToolBarPanel(QueryBuilderPanel queryBuilderPanel, CreateStringQuery createStringQuery) {
        this.queryBuilderPanel = queryBuilderPanel;
        this.queryEditorTextPanel = queryEditorTextPanel;
        this.createStringQuery = createStringQuery;
        init();
    }

    /**
     * User interface components are initialized.
     */
    private void init() {

        // A button is created for creating tables and its actions are set.
        addTable = WidgetFactory.createRolloverButton("Add Table",
                "Добавить таблицу",
                "icon_table_add",
                event -> {
                    AddTableQueryBuilder addTableFrame = new AddTableQueryBuilder(queryBuilderPanel);
                    addTableFrame.pack();
                    addTableFrame.setLocationRelativeTo(null);
                    addTableFrame.setVisible(true);
                    addTableFrame.setSize(400, 300);
                });

        // A button is created to delete tables and its functionality is set.
        removeTable = WidgetFactory.createRolloverButton("Remove Table",
                "Удалить таблицу",
                "icon_table_drop",
                event -> {
                    queryBuilderPanel.removeTableInInputPanel();
                    queryBuilderPanel.removeListTableInInputPanel();
                });

        // Creating a button to create a window that adds additional parameters to the request. (First,Skip,Distinct).
        additionalSelectSettings = WidgetFactory.createRolloverButton("Select Settings",
                "Дополнения",
                "icon_history",
                event -> {
                    addAdditionalSelectSettingsPanel = new AddAdditionalSelectSettingsPanel(createStringQuery, queryBuilderPanel);
                    addAdditionalSelectSettingsPanel.pack();
                    addAdditionalSelectSettingsPanel.setLocationRelativeTo(null);
                    addAdditionalSelectSettingsPanel.setVisible(true);
                    addAdditionalSelectSettingsPanel.setSize(600, 400);
                });

        // The button for creating a window for adding connections between tables.
        settingUpJoins = WidgetFactory.createRolloverButton("Joins",
                "Соединение (Join)",
                "icon_erd_relation_add",
                event -> {
                    if (!QueryTable.isEmpty()) {
                        if (!QueryAttributes.isEmpty()) {
                            settingUpJoinsPanel = new SettingUpJoinsPanel(this, queryBuilderPanel, createStringQuery);
                            settingUpJoinsPanel.pack();
                            settingUpJoinsPanel.setLocationRelativeTo(null);
                            settingUpJoinsPanel.setVisible(true);
                            settingUpJoinsPanel.setSize(600, 400);
                        }
                    }
                });

        // A button to create a Query Editor in which queries will be used.
        createQuery = WidgetFactory.createRolloverButton("Create Query",
                "Создать редактор запросов",
                "icon_title",
                event -> {
                    ExecuteQueryEditor executeQueryEditor = new ExecuteQueryEditor();
                    usingQueryEditor = executeQueryEditor.getQueryEditor();
                });

        // A button to update the request.(Setting new attributes, tables, etc.)
        updateQuery = WidgetFactory.createRolloverButton("Update Query",
                "Создать и посмотреть",
                "icon_zoom_in",
                event -> {
                    int valuesTrue = 0;

                    ArrayList<String> QueryTable = new ArrayList<>();
                    ArrayList<ArrayList<String>> QueryAttributes = new ArrayList<>();

                    ArrayList<JTable> arrayTableInInputPanel = queryBuilderPanel.getListTableInInputPanel();

                    for (int i = 0; i < arrayTableInInputPanel.size(); i++) {

                        ArrayList<String> Attribute = new ArrayList<>();

                        for (int j = 0; j < arrayTableInInputPanel.get(i).getRowCount(); j++) {
                            if ((boolean) arrayTableInInputPanel.get(i).getValueAt(j, 1)) {
                                Attribute.add(arrayTableInInputPanel.get(i).getColumnName(0) + "." + (String) arrayTableInInputPanel.get(i).getValueAt(j, 0));
                                valuesTrue++;
                            }
                        }

                        if (valuesTrue > 0) {
                            QueryAttributes.add(Attribute);
                            QueryTable.add(arrayTableInInputPanel.get(i).getColumnName(0));
                        }

                        valuesTrue = 0;
                    }

                    this.QueryTable = QueryTable;
                    this.QueryAttributes = QueryAttributes;

                    createStringQuery.addAttributes(QueryAttributes, QueryTable);
                    createStringQuery.addTable(QueryTable.get(0));

                    queryBuilderPanel.setTextInQueryEditorTextPanel(createStringQuery.getQuery());
                });

        // A button to clear the query.
        clearQuery = WidgetFactory.createRolloverButton("Clear Query",
                "Очистить запрос",
                "icon_zoom_out",
                event -> {
                    createStringQuery.clearAttribute();
                    createStringQuery.clearJoin();
                    createStringQuery.clearTable();
                    createStringQuery.clearSkip();
                    createStringQuery.clearFirst();

                    queryBuilderPanel.setTextInQueryEditorTextPanel(createStringQuery.getQuery());
                });

        // A button for transferring a ready-made query to the query constructor being used.
        applyQuery = WidgetFactory.createRolloverButton("Apply Query",
                "Применить запрос",
                "icon_create_script",
                event -> {
                    queryBuilderPanel.ApplyQueryFromQueryEditorTextPanel(usingQueryEditor);
                });

        // A drop-down list to display the connection being used.
        connectionsCombo = WidgetFactory.createConnectionComboBox("connectionsCombo", true);
        connectionsCombo.setMaximumSize(new Dimension(200, 30));
        connectionsCombo.setPreferredSize(new Dimension(connectionsCombo.getWidth(), 30));

        arrangeComponent();
    }

    /**
     * A method for placing components
     */
    private void arrangeComponent() {
        setPreferredSize(new Dimension(getWidth(), 45));
        add(connectionsCombo);
        add(addTable);
        add(removeTable);
        add(additionalSelectSettings);
        add(settingUpJoins);
        add(clearQuery);
        add(updateQuery);
        add(applyQuery);
        add(createQuery);
    }

    /**
     * A method for getting a list of used tables.
     *
     * @return ArrayList<String>
     */
    public ArrayList<String> getQueryTable() {
        return QueryTable;
    }

    /**
     * A method for getting attributes of the tables used (Attributes that have true).
     *
     * @return ArrayList<ArrayList < String>>
     */
    public ArrayList<ArrayList<String>> getQueryAttributes() {
        return QueryAttributes;
    }

}
