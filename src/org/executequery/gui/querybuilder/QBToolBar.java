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
import java.io.*;

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
    private QBPanel queryBuilderPanel;

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
    private RolloverButton buttonStepBack;
    private RolloverButton buttonStepUp;
    private RolloverButton buttonClearQuery;
    private RolloverButton buttonSaveQueryBuilder;

    private Table table;
    private OrderBy orderBy;
    private Condition condition;
    private Functions functions;
    private GroupBy groupBy;
    private Join join;
    private Union union;

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

        buttonStepBack = WidgetFactory.createRolloverButton("buttonStepBack",
                Bundles.get("QueryBuilder.ToolBar.stepBack"),
                "icon_move_previous",
                event -> stepBack());

        buttonStepBack.setBorder(new CompoundBorder(BorderFactory.createLineBorder(colorBorderButton, 1, true), BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        buttonStepUp = WidgetFactory.createRolloverButton("buttonStepUp",
                Bundles.get("QueryBuilder.ToolBar.stepUp"),
                "icon_move_next",
                event -> stepUp());

        buttonStepUp.setBorder(new CompoundBorder(BorderFactory.createLineBorder(colorBorderButton, 1, true), BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        buttonSaveQuery = WidgetFactory.createRolloverButton("buttonSaveQuery",
                Bundles.get("common.saveQuery"),
                "icon_create_script",
                event -> saveQuery());

        buttonSaveQuery.setBorder(new CompoundBorder(BorderFactory.createLineBorder(colorBorderButton, 1, true), BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        buttonSaveQueryBuilder = WidgetFactory.createRolloverButton("buttonSaveQueryBuilder",
                Bundles.get("QueryBuilder.ToolBar.saveQueryBuilder"),
                "icon_query_editor",
                event -> saveQueryBuilder());

        buttonSaveQueryBuilder.setBorder(new CompoundBorder(BorderFactory.createLineBorder(colorBorderButton, 1, true), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
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
        panelPlacingComponents.add(buttonStepBack, gridBagHelper.setXY(12, 0).setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(buttonStepUp, gridBagHelper.setXY(13, 0).setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(buttonClearQuery, gridBagHelper.setXY(14, 0).setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(buttonSaveQuery, gridBagHelper.setXY(15, 0).setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(buttonSaveQueryBuilder, gridBagHelper.setXY(16, 0).setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(new JLabel(" "), gridBagHelper.setXY(17, 0).spanX().get());
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
     * The method that implements the functionality is a step back.
     * <p>
     * Метод реализующий функционал шаг назад.
     */
    private void stepBack() {
        if (!queryBuilderPanel.getHistoryActionStepBack().empty()) {
            StringBuilder backActions = new StringBuilder(queryBuilderPanel.getAndRemoveStepBackActionInHistory());
            String actions = backActions.toString().split(" ")[0];
            String queryElement = backActions.toString().split(" ")[1];
            String pattern = backActions.substring(backActions.indexOf(queryElement) + queryElement.length() + 1);
            methodActionSetStepBack(actions, queryElement, pattern);
        }
    }

    /**
     * A method that works with a Set entry on the stack (step back).
     * <p>
     * Метод работающий с записью Set в стеке (шаг назад).
     */
    private void methodActionSetStepBack(String actions, String queryElement, String pattern) {
        if (actions.equals("Set")) {
            if (queryElement.equals("First")) {
                queryConstructor.setFirst(pattern, "stepUp");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
            if (queryElement.equals("Skip")) {
                queryConstructor.setSkip(pattern, "stepUp");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
            if (queryElement.equals("Distinct")) {
                queryConstructor.setDistinct(pattern, "stepUp");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
            if (queryElement.equals("Where")) {
                queryConstructor.setWhere(pattern, "stepUp");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
            if (queryElement.equals("Optimize")) {
                queryConstructor.setOptimization(pattern, "stepUp");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
            if (queryElement.equals("OrderBy")) {
                queryConstructor.setOrderBy(pattern, "stepUp");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
            if (queryElement.equals("Function")) {
                queryConstructor.replaceFunctions(pattern, "stepUp");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
            if (queryElement.equals("GroupBy")) {
                queryConstructor.setGroupBy(pattern, "stepUp");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
            if (queryElement.equals("Table")) {
                queryConstructor.setTable(pattern, "stepUp");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
            if (queryElement.equals("Union")) {
                queryConstructor.setUnion(pattern, "stepUp");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
            if (queryElement.equals("With")) {
                queryConstructor.setWith(pattern, "stepUp");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
            if (queryElement.equals("Attribute")) {
                queryConstructor.replaceAttribute(pattern, "stepUp");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
        }
    }


    /**
     * Taking a step back. Making a record for a step forward.
     * <p>
     * Делая шаг назад. Делаем запись для шага вперёд.
     */
    private void addNextFirstSkipDistinctInHistory() {
        StringBuilder stringBuilder = new StringBuilder("Set FirstSkipDistinct ");

        if (queryConstructor.getFirst().isEmpty()) {
            stringBuilder.append("empty");
        } else {
            stringBuilder.append(queryConstructor.getFirst());
        }

        stringBuilder.append(" ");

        if (queryConstructor.getSkip().isEmpty()) {
            stringBuilder.append("empty");
        } else {
            stringBuilder.append(queryConstructor.getSkip());
        }

        stringBuilder.append(" ");

        if (queryConstructor.getDistinct().isEmpty()) {
            stringBuilder.append("empty");
        } else {
            stringBuilder.append(queryConstructor.getDistinct());
        }

        queryBuilderPanel.addStepUpActionInHistory(stringBuilder.toString());
    }

    /**
     * The method that implements the functionality is a step forward.
     * <p>
     * Метод реализующий функционал шаг вперёд.
     */
    private void stepUp() {
        if (!queryBuilderPanel.getHistoryActionStepUp().isEmpty()) {
            StringBuilder userBak = new StringBuilder(queryBuilderPanel.getAndRemoveStepUpActionInHistory());
            String actions = userBak.toString().split(" ")[0];
            String queryElement = userBak.toString().split(" ")[1];
            String pattern = userBak.substring(userBak.indexOf(queryElement) + queryElement.length() + 1);
            methodActionSetStepUp(actions, queryElement, pattern);
        }
    }

    /**
     * A method that works with a Set entry on the stack (step up).
     * <p>
     * Метод работающий с записью Set в стеке (шаг вперёд).
     */
    private void methodActionSetStepUp(String actions, String queryElement, String pattern) {
        if (actions.equals("Set")) {
            if (queryElement.equals("First")) {
                queryConstructor.setFirst(pattern, "stepBack");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
            if (queryElement.equals("Skip")) {
                queryConstructor.setSkip(pattern, "stepBack");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
            if (queryElement.equals("Distinct")) {
                queryConstructor.setDistinct(pattern, "stepBack");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
            if (queryElement.equals("Where")) {
                queryConstructor.setWhere(pattern, "stepBack");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
            if (queryElement.equals("Optimize")) {
                queryConstructor.setOptimization(pattern, "stepBack");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
            if (queryElement.equals("OrderBy")) {
                queryConstructor.setOrderBy(pattern, "stepBack");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
            if (queryElement.equals("Function")) {
                queryConstructor.replaceFunctions(pattern, "stepBack");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
            if (queryElement.equals("GroupBy")) {
                queryConstructor.setGroupBy(pattern, "stepBack");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
            if (queryElement.equals("Table")) {
                queryConstructor.setTable(pattern, "stepBack");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
            if (queryElement.equals("Union")) {
                queryConstructor.setUnion(pattern, "stepBack");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
            if (queryElement.equals("With")) {
                queryConstructor.setWith(pattern, "stepBack");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
            if (queryElement.equals("Attribute")) {
                queryConstructor.replaceAttribute(pattern, "stepBack");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
        }
    }

    /**
     * A method for saving the query builder.
     * <p>
     * Метод для сохранения построителя запросов.
     */
    private void saveQueryBuilder() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(Bundles.get("QueryBuilder.ToolBar.saveQueryBuilder"));
        int saveCondition = fileChooser.showSaveDialog(null);
        if (saveCondition == JFileChooser.APPROVE_OPTION) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(fileChooser.getSelectedFile() + ".txt");
                byte[] bytesString = queryBuilderPanel.getTestQuery().getBytes();
                fileOutputStream.write(bytesString);
                fileOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The method for clearing the request.
     * <p>
     * Метод для очистки запроса.
     */
    public void clearQuery() {
        int userDecision = JOptionPane.showConfirmDialog(queryBuilderPanel, Bundles.get("QueryBuilder.ToolBar.warningCompleteCleaning"), Bundles.get("common.warning"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (userDecision == JOptionPane.YES_OPTION) {
            queryConstructor.clearAll();
            removeTableInOutputPanel();
            queryBuilderPanel.getHistoryActionStepBack().clear();
            queryBuilderPanel.getHistoryActionStepUp().clear();
            queryBuilderPanel.setTextInPanelOutputTestingQuery("");
        }
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
        join = new Join(queryBuilderPanel, queryConstructor, this);
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
        functions = new Functions(queryBuilderPanel, queryConstructor);
    }

    /**
     * A method for adding groupings to a query.
     * <p>
     * Метод для добавления группировок в запрос.
     */
    private void addGroupInQuery() {
        groupBy = new GroupBy(queryConstructor, queryBuilderPanel);
    }

    /**
     * A method for adding sorting to a query.
     * <p>
     * Метод для добавления сортировки в запрос.
     */
    private void addOrderByInQuery() {
        orderBy = new OrderBy(queryBuilderPanel, queryConstructor);
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
        condition = new Condition(queryConstructor, queryBuilderPanel);
    }

    /**
     * A method for adding unions to a query.
     * <p>
     * Метод для добавления объединений (Union) в запрос.
     */
    private void addUnionInQuery() {
        union = new Union(queryConstructor, queryBuilderPanel);
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
        table = new Table(queryBuilderPanel, queryConstructor, this);
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