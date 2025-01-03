package org.executequery.gui.querybuilder;

import org.executequery.gui.WidgetFactory;
import org.executequery.gui.querybuilder.QueryDialog.*;
import org.executequery.localization.Bundles;
import org.underworldlabs.swing.ConnectionsComboBox;
import org.underworldlabs.swing.RolloverButton;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * This class creates a toolbar for the query constructor (QueryBuilder).
 * <p>
 * Этот класс создаёт панель инструментов для конструктора запросов (QueryBuilder).
 *
 * @author Krylov Gleb
 */
public class QBToolBar extends JToolBar {

    private final Color colorBorderButton = new Color(230, 0, 0);

    // --- Fields that are passed through the constructor ----
    // --- Поля, которые передаются через конструктор ---

    private final QueryConstructor queryConstructor;
    private final QBPanel queryBuilderPanel;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private ConnectionsComboBox connections;
    private JPanel panelPlacingComponents;
    private RolloverButton buttonTable;
    private RolloverButton buttonFirstSkipDistinct;
    private RolloverButton buttonConditions;
    private RolloverButton buttonGroupBy;
    private RolloverButton buttonOrderBy;
    private RolloverButton buttonOptimize;
    private RolloverButton buttonUnion;
    private RolloverButton buttonFunctions;
    private RolloverButton buttonJoin;
    private RolloverButton buttonWith;
    private RolloverButton buttonSaveQuery;
    private RolloverButton buttonClearQuery;

    /**
     * A toolbar is being created.
     * A method is used to initialize fields.
     * <p>
     * Создаётся панель инструментов.
     * Используется метод для инициализации полей.
     */
    public QBToolBar(QBPanel queryBuilderPanel, QueryConstructor queryConstructor) {
        this.queryBuilderPanel = queryBuilderPanel;
        this.queryConstructor = queryConstructor;
        init();
    }

    /**
     * A method for initializing fields.
     * <p>
     * Метод для инициализации полей.
     */
    private void init() {
        initButton();
        initComboBox();
        initJPanel();
        arrangeComponent();
    }

    /**
     * The method for initializing JPanel.
     * <p>
     * Метод для инициализации JPanel.
     */
    private void initJPanel() {
        panelPlacingComponents = WidgetFactory.createPanel("panelPlacingComponents");
        panelPlacingComponents.setLayout(new GridBagLayout());
    }

    /**
     * A method for initializing drop-down lists (comboBox).
     * <p>
     * Метод для инициализации выпадающих списков (comboBox).
     */
    private void initComboBox() {
        connections = WidgetFactory.createConnectionComboBox("connections", true);
        connections.setMinimumSize(new Dimension(200, 40));
        connections.setPreferredSize(new Dimension(200, 40));
        connections.setMaximumSize(new Dimension(200, 40));
    }

    /**
     * A method for initializing buttons.
     * <p>
     * Метод для инициализации кнопок.
     */
    private void initButton() {
        buttonTable = WidgetFactory.createRolloverButton("buttonTable",
                Bundles.get("common.tables"),
                "icon_db_table",
                event -> addTableInQuery());

        buttonTable.setBorder(new CompoundBorder(BorderFactory.createLineBorder(colorBorderButton, 1, true), BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        buttonFirstSkipDistinct = WidgetFactory.createRolloverButton("buttonFirstSkipDistinct",
                Bundles.get("common.additions"),
                "icon_limit_row_count",
                event -> addFirsSkipAndDistinctInQuery());

        buttonFirstSkipDistinct.setBorder(new CompoundBorder(BorderFactory.createLineBorder(colorBorderButton, 1, true), BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        buttonConditions = WidgetFactory.createRolloverButton("buttonConditions",
                Bundles.get("common.conditions"),
                "icon_filter",
                event -> addConditionsInQuery());

        buttonConditions.setBorder(new CompoundBorder(BorderFactory.createLineBorder(colorBorderButton, 1, true), BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        buttonGroupBy = WidgetFactory.createRolloverButton("buttonGroupBy",
                Bundles.get("common.grouping"),
                "icon_background",
                event -> addGroupInQuery());

        buttonGroupBy.setBorder(new CompoundBorder(BorderFactory.createLineBorder(colorBorderButton, 1, true), BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        buttonOrderBy = WidgetFactory.createRolloverButton("buttonOrderBy",
                Bundles.get("common.sorting"),
                "icon_refresh_connection",
                event -> addOrderByInQuery());

        buttonOrderBy.setBorder(new CompoundBorder(BorderFactory.createLineBorder(colorBorderButton, 1, true), BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        buttonOptimize = WidgetFactory.createRolloverButton("buttonOptimize",
                Bundles.get("common.optimization"),
                "icon_db_statistic",
                event -> addOptimizeInQuery());

        buttonOptimize.setBorder(new CompoundBorder(BorderFactory.createLineBorder(colorBorderButton, 1, true), BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        buttonUnion = WidgetFactory.createRolloverButton("buttonUnion",
                Bundles.get("common.union"),
                "icon_erd_relation_add",
                event -> addUnionInQuery());

        buttonUnion.setBorder(new CompoundBorder(BorderFactory.createLineBorder(colorBorderButton, 1, true), BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        buttonFunctions = WidgetFactory.createRolloverButton("buttonFunctions",
                Bundles.get("common.functions"),
                "icon_style_font",
                event -> addFunctionsInQuery());

        buttonFunctions.setBorder(new CompoundBorder(BorderFactory.createLineBorder(colorBorderButton, 1, true), BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        buttonJoin = WidgetFactory.createRolloverButton("buttonJoin",
                Bundles.get("common.join"),
                "icon_web",
                event -> addJoinsInQuery());

        buttonJoin.setBorder(new CompoundBorder(BorderFactory.createLineBorder(colorBorderButton, 1, true), BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        buttonWith = WidgetFactory.createRolloverButton("buttonWith",
                Bundles.get("common.with"),
                "icon_create_db",
                event -> addWithInQuery());

        buttonWith.setBorder(new CompoundBorder(BorderFactory.createLineBorder(colorBorderButton, 1, true), BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        buttonClearQuery = WidgetFactory.createRolloverButton("buttonClearQuery",
                Bundles.get("common.clearAll"),
                "icon_zoom_out",
                event -> clearQuery());

        buttonClearQuery.setBorder(new CompoundBorder(BorderFactory.createLineBorder(colorBorderButton, 1, true), BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        buttonSaveQuery = WidgetFactory.createRolloverButton("buttonSaveQuery",
                Bundles.get("common.saveQuery"),
                "icon_create_script",
                event -> saveQuery());

        buttonSaveQuery.setBorder(new CompoundBorder(BorderFactory.createLineBorder(colorBorderButton, 1, true), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
    }

    /**
     * A method for placing components as well as configuring the toolbar.
     * <p>
     * Метод для размещения компонентов а так же настройки панели инструментов.
     */
    private void arrangeComponent() {
        configurationToolBar();
        addComponentsInTollBar();
    }

    /**
     * A method for placing components in a panel for placing components.
     * <p>
     * Метод для размещения компонентов в панели для размещения компонентов.
     */
    private void addComponentsInTollBar() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().fillHorizontally().setInsets(5, 5, 5, 5);
        panelPlacingComponents.add(connections, gridBagHelper.setXY(0, 0).setMinWeightX().spanY().get());
        panelPlacingComponents.add(buttonTable, gridBagHelper.setXY(2, 0).setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(buttonJoin, gridBagHelper.setXY(3, 0).setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(buttonFunctions, gridBagHelper.setXY(4, 0).setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(buttonConditions, gridBagHelper.setXY(5, 0).setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(buttonFirstSkipDistinct, gridBagHelper.setXY(6, 0).setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(buttonGroupBy, gridBagHelper.setXY(7, 0).setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(buttonOrderBy, gridBagHelper.setXY(8, 0).setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(buttonOptimize, gridBagHelper.setXY(9, 0).setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(buttonUnion, gridBagHelper.setXY(10, 0).setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(buttonWith, gridBagHelper.setXY(11, 0).setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(buttonClearQuery, gridBagHelper.setXY(12, 0).setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(buttonSaveQuery, gridBagHelper.setXY(13, 0).setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(new JLabel(" "), gridBagHelper.setXY(14, 0).spanX().get());
        add(panelPlacingComponents);
    }

    /**
     * A method for configuring the toolbar.
     * <p>
     * Метод для настройки панели инструментов.
     */
    private void configurationToolBar() {
        setPreferredSize(new Dimension(getWidth(), 40));
    }

    /**
     * The method for using the request.
     * <p>
     * Метод для использования запроса.
     */
    private void saveQuery() {
        String textCopy = queryBuilderPanel.getTestQuery();
        StringSelection stringSelection = new StringSelection(textCopy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        JOptionPane.showMessageDialog(queryBuilderPanel, Bundles.get("QueryBuilder.ToolBar.savingRequestClipboard"), Bundles.get("QueryBuilder.ToolBar.savingRequestClipboardTitle"), JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * The method for clearing the request.
     * <p>
     * Метод для очистки запроса.
     */
    private void clearQuery() {
        queryConstructor.clearAll();
        removeTableInOutputPanel();
        queryBuilderPanel.setTextInPanelOutputTestingQuery("");
    }

    /**
     * A method for deleting a table from the output panel.
     * <p>
     * Метод для удаления таблицы с панели вывода.
     */
    private void removeTableInOutputPanel() {
        queryBuilderPanel.getBlocksPanel().removeAll();
        queryBuilderPanel.getBlocksPanel().revalidate();
        queryBuilderPanel.getBlocksPanel().repaint();
        queryBuilderPanel.getPanelGUIComponents().removeAll();
        queryBuilderPanel.getPanelGUIComponents().revalidate();
        queryBuilderPanel.getPanelGUIComponents().repaint();
        queryBuilderPanel.getListTable().clear();
    }

    /**
     * A method for adding join to a query.
     * <p>
     * Метод для добавления соединений в запрос.
     */
    public void addJoinsInQuery() {
        new Join(queryBuilderPanel, queryConstructor);
    }

    /**
     * A method for adding queries (With) to a query.
     * <p>
     * Метод для добавления запросов (With) в запрос.
     */
    public void addWithInQuery() {
        new With(queryConstructor, queryBuilderPanel);
    }

    /**
     * A method for adding functions to a query.
     * <p>
     * Метод для добавления функций в запрос.
     */
    private void addFunctionsInQuery() {
        new Functions(queryBuilderPanel, queryConstructor);
    }

    /**
     * A method for adding groupings to a query.
     * <p>
     * Метод для добавления группировок в запрос.
     */
    private void addGroupInQuery() {
        new GroupBy(queryConstructor, queryBuilderPanel);
    }

    /**
     * A method for adding sorting to a query.
     * <p>
     * Метод для добавления сортировки в запрос.
     */
    private void addOrderByInQuery() {
        new OrderBy(queryBuilderPanel, queryConstructor);
    }

    /**
     * A method for adding optimization to a query.
     * <p>
     * Метод для добавления оптимизации в запрос.
     */
    private void addOptimizeInQuery() {
        new Optimize(queryConstructor, queryBuilderPanel);
    }

    /**
     * A method for adding conditions to a query.
     * <p>
     * Метод для добавления условий в запрос.
     */
    private void addConditionsInQuery() {
        new Condition(queryConstructor, queryBuilderPanel);
    }

    /**
     * A method for adding unions to a query.
     * <p>
     * Метод для добавления объединений (Union) в запрос.
     */
    private void addUnionInQuery() {
        new Union(queryConstructor, queryBuilderPanel);
    }

    /**
     * Add First Skip and Distinct to the request
     * <p>
     * Добавить First Skip и Distinct в запрос.
     */
    private void addFirsSkipAndDistinctInQuery() {
        new FirstSkipDistinct(queryConstructor, queryBuilderPanel);
    }

    /**
     * A method for adding a table to a query.
     * <p>
     * Метод для добавления таблицы в запрос.
     */
    private void addTableInQuery() {
        new Table(queryBuilderPanel, queryConstructor, this);
    }

    /**
     * Method for getting a drop-down list with connections
     * <p>
     * Метод для получения выпадающего списка с подключениями
     */
    public ConnectionsComboBox getConnections() {
        return connections;
    }
}