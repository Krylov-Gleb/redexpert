package org.executequery.gui.querybuilder;

import org.executequery.gui.WidgetFactory;
import org.executequery.gui.querybuilder.querydialog.*;
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
import java.util.ArrayList;

public class QBToolBar extends JToolBar {

    private final Color colorBorderButton = new Color(230, 0, 0);
    private final QueryConstructor queryConstructor;
    private final QBPanel queryBuilderPanel;
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

    public QBToolBar(QBPanel queryBuilderPanel, QueryConstructor queryConstructor) {
        this.queryBuilderPanel = queryBuilderPanel;
        this.queryConstructor = queryConstructor;
        init();
    }

    private void init() {
        initButton();
        initComboBox();
        initJPanel();
        arrangeComponent();
    }

    private void initJPanel() {
        panelPlacingComponents = WidgetFactory.createPanel("panelPlacingComponents");
        panelPlacingComponents.setLayout(new GridBagLayout());
    }

    private void initComboBox() {
        connections = WidgetFactory.createConnectionComboBox("connections", true);
        connections.setMinimumSize(new Dimension(200, 40));
        connections.setPreferredSize(new Dimension(200, 40));
        connections.setMaximumSize(new Dimension(200, 40));
    }

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

    private void arrangeComponent() {
        configurationToolBar();
        addComponentsInTollBar();
    }

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

    private void configurationToolBar() {
        setPreferredSize(new Dimension(getWidth(), 40));
    }

    private void saveQuery() {
        String textCopy = queryBuilderPanel.getTestQuery();
        StringSelection stringSelection = new StringSelection(textCopy);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        JOptionPane.showMessageDialog(queryBuilderPanel, Bundles.get("QueryBuilder.ToolBar.savingRequestClipboard"), Bundles.get("QueryBuilder.ToolBar.savingRequestClipboardTitle"), JOptionPane.QUESTION_MESSAGE);
    }

    private void stepBack() {
        if (!queryBuilderPanel.getHistoryActionStepBack().empty()) {
            StringBuilder backActions = new StringBuilder(queryBuilderPanel.getAndRemoveStepBackActionInHistory());
            String actions = backActions.toString().split(" ")[0];
            String queryElement = backActions.toString().split(" ")[1];
            String pattern = backActions.substring(backActions.indexOf(queryElement) + queryElement.length() + 1);
            methodActionSetStepBack(actions, queryElement, pattern);
            methodIfActionAddStepBack(actions, queryElement, pattern);
            methodIfActionDeleteStepBack(actions, queryElement, pattern);
        }
    }

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

                ArrayList<JTable> tables = queryBuilderPanel.getListTable();
                String attribute = queryConstructor.getAttribute();

                for (JTable jTable : tables) {
                    for (int j = 0; j < jTable.getRowCount(); j++) {
                        if (attribute.contains(jTable.getColumnName(0) + "." + jTable.getValueAt(j, 0))) {
                            queryConstructor.setChangingValueClick(false);
                            jTable.setValueAt(true, j, 1);
                            jTable.revalidate();
                            jTable.repaint();
                        } else {
                            queryConstructor.setChangingValueClick(false);
                            jTable.setValueAt(false, j, 1);
                            jTable.revalidate();
                            jTable.repaint();
                        }
                    }
                }
            }
        }
    }

    private void methodIfActionAddStepBack(String actions, String queryElement, String pattern) {
        if (actions.equals("Add")) {
            if (queryElement.equals("Table")) {
                queryBuilderPanel.addStepUpActionInHistory("Delete Table " + pattern);
                table.addTable(new JCheckBox(pattern), true);
            }
        }
    }

    private void methodIfActionDeleteStepBack(String actions, String queryElement, String pattern) {
        if (actions.equals("Delete")) {
            if (queryElement.equals("Table")) {
                queryBuilderPanel.addStepUpActionInHistory("Add Table " + pattern);
                table.removeTable(new JCheckBox(pattern), true);
            }
        }
    }

    private void stepUp() {
        if (!queryBuilderPanel.getHistoryActionStepUp().isEmpty()) {
            StringBuilder userBak = new StringBuilder(queryBuilderPanel.getAndRemoveStepUpActionInHistory());
            String actions = userBak.toString().split(" ")[0];
            String queryElement = userBak.toString().split(" ")[1];
            String pattern = userBak.substring(userBak.indexOf(queryElement) + queryElement.length() + 1);
            methodActionSetStepUp(actions, queryElement, pattern);
            methodIfActionAddStepUp(actions, queryElement, pattern);
            methodIfActionDeleteStepUp(actions, queryElement, pattern);
        }
    }

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

                ArrayList<JTable> tables = queryBuilderPanel.getListTable();
                String attribute = queryConstructor.getAttribute();

                for (JTable jTable : tables) {
                    for (int j = 0; j < jTable.getRowCount(); j++) {
                        if (attribute.contains(jTable.getColumnName(0) + "." + jTable.getValueAt(j, 0))) {
                            queryConstructor.setChangingValueClick(false);
                            jTable.setValueAt(true, j, 1);
                            jTable.revalidate();
                            jTable.repaint();
                        } else {
                            queryConstructor.setChangingValueClick(false);
                            jTable.setValueAt(false, j, 1);
                            jTable.revalidate();
                            jTable.repaint();
                        }
                    }
                }
            }
        }
    }

    private void methodIfActionAddStepUp(String actions, String queryElement, String pattern) {
        if (actions.equals("Add")) {
            if (queryElement.equals("Table")) {
                queryBuilderPanel.addStepBackActionInHistory("Delete Table " + pattern);
                table.addTable(new JCheckBox(pattern), true);
            }
        }
    }

    private void methodIfActionDeleteStepUp(String actions, String queryElement, String pattern) {
        if (actions.equals("Delete")) {
            if (queryElement.equals("Table")) {
                queryBuilderPanel.addStepBackActionInHistory("Add Table " + pattern);
                table.removeTable(new JCheckBox(pattern), true);
            }
        }
    }

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

    private void removeTableInOutputPanel() {
        queryBuilderPanel.getBlocksPanel().removeAll();
        queryBuilderPanel.getBlocksPanel().revalidate();
        queryBuilderPanel.getBlocksPanel().repaint();
        queryBuilderPanel.getPanelGUIComponents().removeAll();
        queryBuilderPanel.getPanelGUIComponents().revalidate();
        queryBuilderPanel.getPanelGUIComponents().repaint();
        queryBuilderPanel.getListTable().clear();
    }

    public void addJoinsInQuery() {
        new Join(queryBuilderPanel, queryConstructor, this);
    }

    public void addWithInQuery() {
        new With(queryConstructor, queryBuilderPanel);
    }

    private void addFunctionsInQuery() {
        new Functions(queryBuilderPanel, queryConstructor);
    }

    private void addGroupInQuery() {
        new GroupBy(queryConstructor, queryBuilderPanel);
    }

    private void addOrderByInQuery() {
        new OrderBy(queryBuilderPanel, queryConstructor);
    }

    private void addOptimizeInQuery() {
        new Optimize(queryConstructor, queryBuilderPanel);
    }

    private void addConditionsInQuery() {
        new Condition(queryConstructor, queryBuilderPanel);
    }

    private void addUnionInQuery() {
        new Union(queryConstructor, queryBuilderPanel);
    }

    private void addFirsSkipAndDistinctInQuery() {
        new FirstSkipDistinct(queryConstructor, queryBuilderPanel);
    }

    private void addTableInQuery() {
        table = new Table(queryBuilderPanel, queryConstructor, this);
    }

    public ConnectionsComboBox getConnections() {
        return connections;
    }
}