package org.executequery.gui.querybuilder.toolBar;

import org.executequery.GUIUtilities;
import org.executequery.gui.WidgetFactory;
import org.executequery.gui.editor.QueryEditor;
import org.executequery.gui.querybuilder.CreateAndUseQueryEditor;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.executequery.gui.querybuilder.WorkQuryEditor.CreateStringQuery;
import org.executequery.gui.querybuilder.buttonPanel.*;
import org.underworldlabs.swing.ConnectionsComboBox;
import org.underworldlabs.swing.RolloverButton;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This class is responsible for creating and managing the toolbar in the main window (QueryBuilderPanel).
 * It performs all the basic functions of QueryBuilder, such as creating tables, joins, and more.
 * <p>
 * Класс для создания панели инструментов в построителе запросов (QueryBuilder).
 *
 * @author Krylov Gleb
 */
public class QueryBuilderToolBar extends JToolBar {

    // --- Elements accepted using the constructor ----
    // --- Поля, которые передаются через конструктор. ---

    private CreateStringQuery queryDesigner;
    private QueryBuilderPanel usingQueryBuilderPanel;
    private QueryEditor usingQueryEditor;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private ConnectionsComboBox connections;

    private RolloverButton buttonAddTableInQuery;
    private RolloverButton buttonRemoveTableInQuery;
    private RolloverButton buttonAddFirstSkipAndDistinct;
    private RolloverButton buttonAddConditionsInQuery;
    private RolloverButton buttonAddGroupByInQuery;
    private RolloverButton buttonFunctionsInQuery;
    private RolloverButton buttonAddJoinsInQuery;
    private RolloverButton buttonFromCreateAndSetTextQueryInQueryEditor;
    private RolloverButton buttonSetValuesAttributesAndTablesInQuery;
    private RolloverButton buttonClearQuery;

    private DialogAddFirstSkipDistinctFromQueryBuilder panelAddSettings;
    private DialogRemoveTableFromQueryBuilder dialogRemoveTableFromQueryBuilder;
    private DialogAddTableFromQueryBuilder dialogAddTableFromQueryBuilder;
    private DialogAddJoinsFromQueryBuilder panelAddJoins;
    private DialogAddConditionFromQueryBuilder panelAddCondition;
    private DialogAddGroupByFromQueryBuilder panelAddGroups;
    private DialogAddFunctionsFromQueryBuilder panelFunction;

    // --- Other field ---
    // --- Другие поля ---

    private ArrayList<String> arrayNamesTables;
    private ArrayList<ArrayList<String>> arrayAttributesTables;

    /**
     * A new toolbar is created and elements are passed to interact with the main panel (QueryBuilderPanel).
     * Graphical components are initialized.
     * <p>
     * Создается новая панель инструментов и передаются элементы для взаимодействия с главной панелью (QueryBuilderPanel).
     * Графические компоненты инициализируются.
     */
    public QueryBuilderToolBar(QueryBuilderPanel queryBuilderPanel, CreateStringQuery createStringQuery) {
        this.usingQueryBuilderPanel = queryBuilderPanel;
        this.queryDesigner = createStringQuery;
        init();
    }

    /**
     * User interface components are initialized.
     * Placing the components.
     * <p>
     * Компоненты пользовательского интерфейса инициализированы.
     * Размещаем компоненты.
     */
    private void init() {

        // A button is created for creating tables and its actions are set.
        // Создается кнопка для создания таблиц и настраиваются ее действия.
        buttonAddTableInQuery = WidgetFactory.createRolloverButton("Add Table",
                "Добавить таблицу",
                "icon_table_add",
                event -> {
                    addTableInQuery();
                });

        // A button is created to delete tables and its functionality is set.
        // Создана кнопка для удаления таблиц и настроен ее функционал.
        buttonRemoveTableInQuery = WidgetFactory.createRolloverButton("Remove Table",
                "Удалить таблицу",
                "icon_table_drop",
                event -> {
                    removeTableInQuery();
                });

        // Creating a button that adds additional parameters to the query. (First, skip, distinguish).
        // Создание кнопки которая добавляет дополнительные параметры к запросу. (First,Skip,Distinct).
        buttonAddFirstSkipAndDistinct = WidgetFactory.createRolloverButton("Select Settings",
                "Дополнения",
                "icon_history",
                event -> {
                    addFirsSkipAndDistinctInQuery();
                });

        // A button is created to add conditions to the query.
        // Создаётся кнопка для добавления условий в запрос.
        buttonAddConditionsInQuery = WidgetFactory.createRolloverButton("Condition",
                "Условия",
                "icon_comment",
                event -> {
                    addConditionsInQuery();
                });

        // A button for adding groupings to a query.
        // Кнопка для добавления группировок в запрос
        buttonAddGroupByInQuery = WidgetFactory.createRolloverButton("Group By",
                "Группировка",
                "icon_background",
                event -> {
                    addGroupInQuery();
                });

        // A button for adding functions to the query.
        // Кнопка для добавления функций в запрос.
        buttonFunctionsInQuery = WidgetFactory.createRolloverButton("Functions",
                "Функции",
                "icon_style_font",
                event -> {
                    addFunctionsInQuery();
                });

        // A button to add connections to the request.
        // Кнопка для добавления соединений в запрос.
        buttonAddJoinsInQuery = WidgetFactory.createRolloverButton("Joins",
                "Соединение (Join)",
                "icon_erd_relation_add",
                event -> {
                    addJoinsInQuery();
                });

        // A button to change the values of tables and their attributes in the query.
        // Кнопка для смены значений таблиц и их атрибутов в запросе.
        buttonSetValuesAttributesAndTablesInQuery = WidgetFactory.createRolloverButton("Update Query",
                "Создать и посмотреть",
                "icon_zoom_in",
                event -> {
                    setValuesAttributesAndTablesInQuery();
                });

        // The button to delete the request.
        // Кнопка для удаления запроса.
        buttonClearQuery = WidgetFactory.createRolloverButton("Clear Query",
                "Очистить запрос",
                "icon_zoom_out",
                event -> {
                    clearQuery();
                });

        // A button for creating and changing the query text in the query editor.
        // Кнопка для создания и смены текста запроса в редакторе запросов.
        buttonFromCreateAndSetTextQueryInQueryEditor = WidgetFactory.createRolloverButton("Apply Query",
                "Применить запрос",
                "icon_create_script",
                event -> {
                    CreateAndSetTextQueryInQueryEditor();
                });

        // Creating a drop-down list of connections.
        // Создание выпадающего списка подключений.
        connections = WidgetFactory.createConnectionComboBox("connectionsCombo", true);
        connections.setMaximumSize(new Dimension(200, 30));
        connections.setPreferredSize(new Dimension(connections.getWidth(), 30));

        // Placement of components
        // Размещение компонентов
        arrangeComponent();
    }

    /**
     * A method for placing components.
     * <p>
     * Размещение компонентов.
     */
    private void arrangeComponent() {
        setPreferredSize(new Dimension(getWidth(), 45));
        add(connections);
        add(buttonAddTableInQuery);
        add(buttonRemoveTableInQuery);
        add(buttonAddFirstSkipAndDistinct);
        add(buttonAddConditionsInQuery);
        add(buttonAddGroupByInQuery);
        add(buttonFunctionsInQuery);
        add(buttonAddJoinsInQuery);
        add(buttonClearQuery);
        add(buttonSetValuesAttributesAndTablesInQuery);
        add(buttonFromCreateAndSetTextQueryInQueryEditor);
    }

    /**
     * Creating and installing the query text in the query editor.
     * <p>
     * Создание и установка текста запроса в редактор запросов.
     */
    private void CreateAndSetTextQueryInQueryEditor() {
        if (usingQueryEditor == null) {
            createNewQueryEditor();
        }
        if (!GUIUtilities.isPanelOpen(usingQueryEditor.getDisplayName())) {
            createNewQueryEditor();
        }
        if (GUIUtilities.isPanelOpen(usingQueryEditor.getDisplayName())) {
            addQueryInQueryEditor();
        }
    }

    /**
     * Method for adding a query to the query editor.
     * <p>
     * Метод для добавления запроса в редактор запросов.
     */
    private void addQueryInQueryEditor() {
        usingQueryBuilderPanel.rescheduleQueryFromPanelOutputTestingQueryInEditorTextQueryEditor(usingQueryEditor);
    }

    /**
     * Creating a new Query Editor.
     * <p>
     * Создание нового редактора запросов.
     */
    private void createNewQueryEditor() {
        CreateAndUseQueryEditor executeQueryEditor = new CreateAndUseQueryEditor();
        usingQueryEditor = executeQueryEditor.getQueryEditor();
    }

    /**
     * Method for deleting a query.
     * <p>
     * Метод для удаления запроса.
     */
    private void clearQuery() {
        queryDesigner.clearAll();
        addQueryInQueryEditorTextPanel();
    }

    /**
     * A method for adding a test query to a text panel in the Query Builder Panel
     * <p>
     * Метод для добавления тестового запроса в текстовую панель в Query Builder Panel
     */
    private void addQueryInQueryEditorTextPanel() {
        usingQueryBuilderPanel.setTextInPanelOutputTestingQuery(queryDesigner.getQuery());
    }

    /**
     * A method for changing the values of attributes and tables in a query.
     * <p>
     * Метод для смены значений атрибутов и таблиц в запросе.
     */
    private void setValuesAttributesAndTablesInQuery() {
        int valuesTrue = 0;

        ArrayList<String> QueryTable = new ArrayList<>();
        ArrayList<ArrayList<String>> QueryAttributes = new ArrayList<>();

        ArrayList<JTable> arrayTableInInputPanel = usingQueryBuilderPanel.getListTable();

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

        this.arrayNamesTables = QueryTable;
        this.arrayAttributesTables = QueryAttributes;

        queryDesigner.addAttributes(QueryAttributes, QueryTable);
        queryDesigner.addTable(QueryTable.get(0));

        addQueryInQueryEditorTextPanel();
    }

    /**
     * A method for adding join to a query.
     * <p>
     * Метод для добавления соединений в запрос.
     */
    private void addJoinsInQuery() {
        panelAddJoins = new DialogAddJoinsFromQueryBuilder(this, usingQueryBuilderPanel, queryDesigner);
    }

    /**
     * A method for adding functions to a query.
     * <p>
     * Метод для добавления функций в запрос.
     */
    private void addFunctionsInQuery() {
        panelFunction = new DialogAddFunctionsFromQueryBuilder(usingQueryBuilderPanel, queryDesigner);
    }

    /**
     * A method for adding groupings to a query.
     * <p>
     * Метод для добавления группировок в запрос.
     */
    private void addGroupInQuery() {
        panelAddGroups = new DialogAddGroupByFromQueryBuilder(queryDesigner, usingQueryBuilderPanel);
    }

    /**
     * A method for adding conditions to a query.
     * <p>
     * Метод для добавления условий в запрос.
     */
    private void addConditionsInQuery() {
        panelAddCondition = new DialogAddConditionFromQueryBuilder(queryDesigner, usingQueryBuilderPanel);
    }

    /**
     * Add First Skip and Distinct to the request
     * <p>
     * Добавить First Skip и Distinct в запрос.
     */
    private void addFirsSkipAndDistinctInQuery() {
        panelAddSettings = new DialogAddFirstSkipDistinctFromQueryBuilder(queryDesigner, usingQueryBuilderPanel);
    }

    /**
     * A method for deleting a table from a query.
     * <p>
     * Метод для удаления таблицы из запроса.
     */
    private void removeTableInQuery() {
        dialogRemoveTableFromQueryBuilder = new DialogRemoveTableFromQueryBuilder(usingQueryBuilderPanel);
    }

    /**
     * A method for adding a table to a query.
     * <p>
     * Метод для добавления таблицы в запрос.
     */
    private void addTableInQuery() {
        dialogAddTableFromQueryBuilder = new DialogAddTableFromQueryBuilder(usingQueryBuilderPanel);
    }

    /**
     * A method for getting a list of used tables.
     * <p>
     * Метод получения списка используемых таблиц.
     */
    public ArrayList<String> getArrayNamesTablesFromQueryBuilderToolBar() {
        return arrayNamesTables;
    }

    /**
     * A method for getting attributes of the tables used (Attributes that have true).
     * <p>
     * Метод получения атрибутов используемых таблиц (атрибутов, имеющих значение true).
     */
    public ArrayList<ArrayList<String>> getArrayAttributesFromQueryBuilderToolBar() {
        return arrayAttributesTables;
    }

}
