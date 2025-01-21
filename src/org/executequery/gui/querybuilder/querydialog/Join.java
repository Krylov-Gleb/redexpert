package org.executequery.gui.querybuilder.querydialog;

import org.executequery.databaseobjects.DatabaseColumn;
import org.executequery.databaseobjects.impl.DefaultDatabaseHost;
import org.executequery.gui.IconManager;
import org.executequery.gui.WidgetFactory;
import org.executequery.gui.browser.BrowserConstants;
import org.executequery.gui.browser.ColumnData;
import org.executequery.gui.querybuilder.QBPanel;
import org.executequery.gui.querybuilder.QBToolBar;
import org.executequery.gui.querybuilder.QueryConstructor;
import org.executequery.localization.Bundles;
import org.underworldlabs.swing.RolloverButton;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Join extends JDialog {

    private final QueryConstructor queryConstructor;
    private final QBPanel queryBuilderPanel;
    private final QBToolBar queryBuilderToolBar;
    private JPanel panelPlacingComponents;
    private JPanel panelPlacingCheckBoxInScrollPane;
    private JPanel panelButton;
    private JScrollPane scrollPaneCheckBoxJoin;
    private JLabel labelLeftTable;
    private JLabel labelRightTable;
    private JLabel labelJoin;
    private JLabel labelValuesUnity;
    private JButton buttonAddJoin;
    private JButton buttonRemoveJoin;
    private JButton buttonClose;
    private RolloverButton buttonShowColumnLeftTable;
    private RolloverButton buttonShowColumnRightTable;
    private JComboBox<String> comboBoxLeftTable;
    private JComboBox<String> comboBoxRightTable;
    private JComboBox<String> comboBoxJoins;
    private JTextField textFieldValuesUnity;

    private DefaultDatabaseHost defaultDatabaseHost;

    public Join(QBPanel queryBuilderPanel, QueryConstructor queryConstructor, QBToolBar queryBuilderToolBar) {
        this.queryBuilderPanel = queryBuilderPanel;
        this.queryConstructor = queryConstructor;
        this.queryBuilderToolBar = queryBuilderToolBar;
        init();
    }

    private void init() {
        initPanels();
        initScrollPane();
        intLabel();
        initDefaultDataBaseHost();
        initComboBox();
        initTextFields();
        initButtons();
        arrangeComponents();
    }

    private void initButtons() {
        buttonAddJoin = WidgetFactory.createButton("buttonAddJoin", Bundles.get("common.add.button"), event -> {
            addJoin();
            arrangeCheckBoxesInScrollPane();
        });

        buttonRemoveJoin = WidgetFactory.createButton("buttonRemoveJoin", Bundles.get("common.delete.button"), event -> {
            removeJoin();
            arrangeCheckBoxesInScrollPane();
        });

        buttonClose = WidgetFactory.createButton("buttonClose", Bundles.get("common.close.button"), event -> closeDialog());

        buttonShowColumnLeftTable = WidgetFactory.createRolloverButton("buttonShowColumnLeftTable",
                Bundles.get("QueryBuilder.Join.showColumnLeftTable"),
                "icon_password_show",
                event -> showColumnTable("left"));

        buttonShowColumnRightTable = WidgetFactory.createRolloverButton("buttonShowColumnRightTable",
                Bundles.get("QueryBuilder.Join.showColumnRightTable"),
                "icon_password_show",
                event -> showColumnTable("right"));

        placingButtonsInPanel();
    }

    private void initDefaultDataBaseHost() {
        defaultDatabaseHost = new DefaultDatabaseHost(queryBuilderToolBar.getConnections().getSelectedConnection());
    }

    private void placingButtonsInPanel() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(5, 5, 5, 5).fillHorizontally();
        panelButton.add(buttonAddJoin, gridBagHelper.setXY(0, 0).setMaxWeightX().get());
        panelButton.add(buttonRemoveJoin, gridBagHelper.nextCol().setMaxWeightX().get());
        panelButton.add(buttonClose, gridBagHelper.nextRow().setMaxWeightX().get());
    }

    private void initComboBox() {
        ArrayList<String> listNameTables = queryBuilderPanel.getListNameTable();
        listNameTables.add(0, "");

        comboBoxLeftTable = WidgetFactory.createComboBox("comboBoxLeftTable", listNameTables);
        comboBoxLeftTable.setToolTipText(Bundles.get("QueryBuilder.Join.toolTipTextInputLeftTable"));
        comboBoxLeftTable.setMinimumSize(new Dimension(200, 30));
        comboBoxLeftTable.setPreferredSize(new Dimension(200, 30));
        comboBoxLeftTable.setMaximumSize(new Dimension(200, 30));
        addActionListenerInComboBox(comboBoxLeftTable);

        comboBoxRightTable = WidgetFactory.createComboBox("comboBoxRightTable", listNameTables);
        comboBoxRightTable.setToolTipText(Bundles.get("QueryBuilder.Join.toolTipTextInputRightTable"));
        comboBoxRightTable.setMinimumSize(new Dimension(200, 30));
        comboBoxRightTable.setPreferredSize(new Dimension(200, 30));
        comboBoxRightTable.setMaximumSize(new Dimension(200, 30));
        addActionListenerInComboBox(comboBoxRightTable);

        comboBoxJoins = WidgetFactory.createComboBox("comboBoxJoins", new String[]{"INNER JOIN", "LEFT JOIN", "RIGHT JOIN", "FULL OUTER JOIN", "CROSS JOIN", "NATURAL JOIN"});
        comboBoxJoins.setMinimumSize(new Dimension(200, 30));
        comboBoxJoins.setPreferredSize(new Dimension(200, 30));
        comboBoxJoins.setMaximumSize(new Dimension(200, 30));
        comboBoxJoins.setToolTipText(Bundles.get("QueryBuilder.Join.toolTipTextSelectJoin"));
    }

    private void intLabel() {
        labelJoin = WidgetFactory.createLabel(Bundles.get("QueryBuilder.Join.labelJoin"));
        labelLeftTable = WidgetFactory.createLabel(Bundles.get("QueryBuilder.Join.labelLeftTable"));
        labelRightTable = WidgetFactory.createLabel(Bundles.get("QueryBuilder.Join.labelRightTable"));
        labelValuesUnity = WidgetFactory.createLabel(Bundles.get("QueryBuilder.Join.labelValuesUnity"));
    }

    private void initPanels() {
        panelPlacingComponents = WidgetFactory.createPanel("panelPlacingComponents");
        panelPlacingComponents.setLayout(new GridBagLayout());

        panelButton = WidgetFactory.createPanel("panelButton");
        panelButton.setLayout(new GridBagLayout());
    }

    private void initTextFields() {
        textFieldValuesUnity = WidgetFactory.createTextField("textFieldValuesUnity");
        textFieldValuesUnity.setToolTipText(Bundles.get("QueryBuilder.Join.toolTipTextEnterValuesUnity"));
        textFieldValuesUnity.setMinimumSize(new Dimension(200, 30));
        textFieldValuesUnity.setPreferredSize(new Dimension(200, 30));
        textFieldValuesUnity.setMaximumSize(new Dimension(200, 30));
    }

    private void initScrollPane() {
        scrollPaneCheckBoxJoin = new JScrollPane();
        scrollPaneCheckBoxJoin.setPreferredSize(new Dimension(100, 200));
        arrangeCheckBoxesInScrollPane();
    }

    private void arrangeComponents() {
        arrangeComponentsInPanelForPlacingComponents();
        configurationDialog();
    }

    private void configurationDialog() {
        setLayout(new BorderLayout());
        setTitle(Bundles.get("QueryBuilder.Join.title"));
        setResizable(false);
        setIconImage(getAndCreateIconDialog().getImage());
        add(panelPlacingComponents, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(600, 500);
        setVisible(true);
    }

    private void arrangeComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().setInsets(10, 5, 10, 5).anchorCenter().fillHorizontally();
        panelPlacingComponents.add(scrollPaneCheckBoxJoin, gridBagHelper.setXY(0, 0).setWidth(4).setMaxWeightX().get());
        panelPlacingComponents.add(labelLeftTable, gridBagHelper.nextRow().setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(comboBoxLeftTable, gridBagHelper.nextCol().setWidth(2).setMaxWeightX().get());
        panelPlacingComponents.add(buttonShowColumnLeftTable, gridBagHelper.nextCol().setWidth(1).setInsets(0, 5, 10, 5).setMinWeightX().get());
        panelPlacingComponents.add(labelJoin, gridBagHelper.previousCol().previousCol().previousCol().nextRow().setWidth(1).setMinWeightX().setInsets(10, 5, 10, 5).get());
        panelPlacingComponents.add(comboBoxJoins, gridBagHelper.nextCol().setWidth(3).setMaxWeightX().get());
        panelPlacingComponents.add(labelRightTable, gridBagHelper.previousCol().nextRow().setWidth(1).setMinWeightX().get());
        panelPlacingComponents.add(comboBoxRightTable, gridBagHelper.nextCol().setWidth(2).setMaxWeightX().get());
        panelPlacingComponents.add(buttonShowColumnRightTable, gridBagHelper.nextCol().setWidth(1).setInsets(0, 5, 10, 5).setMinWeightX().get());
        panelPlacingComponents.add(labelValuesUnity, gridBagHelper.previousCol().previousCol().previousCol().nextRow().setWidth(1).setMinWeightX().setInsets(10, 5, 10, 5).get());
        panelPlacingComponents.add(textFieldValuesUnity, gridBagHelper.nextCol().setWidth(3).setMaxWeightX().get());
        panelPlacingComponents.add(panelButton, gridBagHelper.nextRow().spanX().spanY().setMinWeightX().get());
    }

    private void addJoin() {
        if (!Objects.requireNonNull(comboBoxLeftTable.getSelectedItem()).toString().isEmpty()) {
            if (!Objects.requireNonNull(comboBoxRightTable.getSelectedItem()).toString().isEmpty()) {
                if (!Objects.requireNonNull(comboBoxJoins.getSelectedItem()).toString().isEmpty()) {
                    if (comboBoxJoins.getSelectedItem().toString().equals("CROSS JOIN")) {
                        textFieldValuesUnity.setText("");
                        eventAddJoin(comboBoxLeftTable.getSelectedItem().toString(), comboBoxRightTable.getSelectedItem().toString(), comboBoxJoins.getSelectedItem().toString(), textFieldValuesUnity.getText());
                        queryConstructor.setAttributes(queryBuilderPanel.getListTable());
                        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                        arrangeCheckBoxesInScrollPane();
                    } else {
                        if (!textFieldValuesUnity.getText().isEmpty()) {
                            eventAddJoin(comboBoxLeftTable.getSelectedItem().toString(), comboBoxRightTable.getSelectedItem().toString(), comboBoxJoins.getSelectedItem().toString(), textFieldValuesUnity.getText());
                            queryConstructor.setAttributes(queryBuilderPanel.getListTable());
                            queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                            arrangeCheckBoxesInScrollPane();
                        } else {
                            JOptionPane.showMessageDialog(this, Bundles.get("QueryBuilder.Join.errorNotValuesUnity"), Bundles.get("QueryBuilder.Join.titleErrorNotValuesUnity"), JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, Bundles.get("QueryBuilder.Join.errorNotRightTable"), Bundles.get("QueryBuilder.Join.titleErrorNotRightTable"), JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, Bundles.get("QueryBuilder.Join.errorNotLeftTable"), Bundles.get("QueryBuilder.Join.titleErrorNotLeftTable"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eventAddJoin(String tableNameOne, String tableNameTwo, String joinName, String keyValues) {
        StringBuilder stringBuilderTable = new StringBuilder(queryConstructor.getTable());

        if (stringBuilderTable.indexOf(tableNameOne) == 0) {
            if (!stringBuilderTable.toString().contains(" " + tableNameTwo + " ")) {
                if (!textFieldValuesUnity.getText().isEmpty()) {
                    stringBuilderTable.append(" ").append(joinName.toUpperCase()).append(" ").append(tableNameTwo)
                            .append(" ").append("ON").append(" ").append(keyValues);
                } else {
                    stringBuilderTable.append(" ").append(joinName.toUpperCase()).append(" ").append(tableNameTwo).append(" ");
                }

                queryBuilderPanel.addStepBackActionInHistory("Set Join " + queryConstructor.getTable());
                queryConstructor.setTable(stringBuilderTable.toString(), "stepBack");
            }
            return;
        }

        if (stringBuilderTable.indexOf(tableNameTwo) == 0) {
            if (!stringBuilderTable.toString().contains(" " + tableNameOne + " ")) {
                if (!textFieldValuesUnity.getText().isEmpty()) {
                    stringBuilderTable.append(" ").append(joinName.toUpperCase()).append(" ").append(tableNameOne)
                            .append(" ").append("ON").append(" ").append(keyValues);
                } else {
                    stringBuilderTable.append(" ").append(joinName.toUpperCase()).append(" ").append(tableNameOne).append(" ");
                }

                queryBuilderPanel.addStepBackActionInHistory("Set Join " + queryConstructor.getTable());
                queryConstructor.setTable(stringBuilderTable.toString(), "stepBack");
            }
            return;
        }

        if (stringBuilderTable.toString().contains(" " + tableNameOne + " ")) {
            if (!stringBuilderTable.toString().contains(" " + tableNameTwo + " ")) {
                if (!textFieldValuesUnity.getText().isEmpty()) {
                    stringBuilderTable.append(" ").append(joinName.toUpperCase()).append(" ").append(tableNameTwo)
                            .append(" ").append("ON").append(" ").append(keyValues);
                } else {
                    stringBuilderTable.append(" ").append(joinName.toUpperCase()).append(" ").append(tableNameTwo).append(" ");
                }

                queryBuilderPanel.addStepBackActionInHistory("Set Join " + queryConstructor.getTable());
                queryConstructor.setTable(stringBuilderTable.toString(), "stepBack");
            }
            return;
        }

        if (stringBuilderTable.toString().contains(" " + tableNameTwo + " ")) {
            if (!stringBuilderTable.toString().contains(" " + tableNameOne + " ")) {
                if (!textFieldValuesUnity.getText().isEmpty()) {
                    stringBuilderTable.append(" ").append(joinName.toUpperCase()).append(" ").append(tableNameOne)
                            .append(" ").append("ON").append(" ").append(keyValues);
                } else {
                    stringBuilderTable.append(" ").append(joinName.toUpperCase()).append(" ").append(tableNameOne).append(" ");
                }

                queryBuilderPanel.addStepBackActionInHistory("Set Join " + queryConstructor.getTable());
                queryConstructor.setTable(stringBuilderTable.toString(), "stepBack");
            }
        }
    }

    private void removeJoin() {
        JCheckBox[] checkBoxesInScrollPane = getCheckBoxesFromPanelArrangeCheckBox();

        for (JCheckBox checkBox : checkBoxesInScrollPane) {
            if (checkBox.isSelected()) {
                eventRemoveJoin(checkBox.getText());
                panelPlacingCheckBoxInScrollPane.remove(checkBox);
            }
        }

        queryConstructor.setAttributes(queryBuilderPanel.getListTable());
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    public void eventRemoveJoin(String removeJoinInQuery) {
        StringBuilder stringBuilder = new StringBuilder(queryConstructor.getTable());
        stringBuilder.replace(stringBuilder.indexOf(removeJoinInQuery), stringBuilder.indexOf(removeJoinInQuery) + removeJoinInQuery.length(), "");
        queryBuilderPanel.addStepBackActionInHistory("Set Join " + queryConstructor.getTable());
        queryConstructor.setTable(stringBuilder.toString(), "stepBack");

        ArrayList<String> listNameTable = queryBuilderPanel.getListNameTable();

        String[] splitJoin = removeJoinInQuery.split(" ");
        if (removeJoinInQuery.contains("FULL OUTER JOIN")) {

            for (String s : listNameTable) {
                if (splitJoin[3].equals(s)) {
                    int userDecision = JOptionPane.showConfirmDialog(queryBuilderPanel, Bundles.get("QueryBuilder.Join.connectionMissing") + s + ".\n" + Bundles.get("QueryBuilder.Join.isDeleteTable"), Bundles.get("QueryBuilder.Join.connectionMissingTitle"), JOptionPane.YES_NO_OPTION);
                    if (userDecision == JOptionPane.YES_OPTION) {
                        removeTable(new JCheckBox(s));
                    }
                }
            }

        } else {
            for (String s : listNameTable) {
                if (splitJoin[2].equals(s)) {
                    int userDecision = JOptionPane.showConfirmDialog(queryBuilderPanel, Bundles.get("QueryBuilder.Join.connectionMissing") + s + ".\n" + Bundles.get("QueryBuilder.Join.isDeleteTable"), Bundles.get("QueryBuilder.Join.connectionMissingTitle"), JOptionPane.YES_NO_OPTION);
                    if (userDecision == JOptionPane.YES_OPTION) {
                        removeTable(new JCheckBox(s));
                    }
                }
            }
        }

    }

    private JCheckBox[] getCheckBoxesFromPanelArrangeCheckBox() {
        Component[] component = panelPlacingCheckBoxInScrollPane.getComponents();
        JCheckBox[] checkBoxes = new JCheckBox[panelPlacingCheckBoxInScrollPane.getComponents().length];

        for (int i = 0; i < component.length; i++) {
            checkBoxes[i] = ((JCheckBox) component[i]);
        }

        return checkBoxes;
    }

    private void arrangeCheckBoxesInScrollPane() {
        panelPlacingCheckBoxInScrollPane = WidgetFactory.createPanel("panelPlacingCheckBoxInScrollPane");
        panelPlacingCheckBoxInScrollPane.setLayout(new BoxLayout(panelPlacingCheckBoxInScrollPane, BoxLayout.Y_AXIS));

        String[] historyJoins = queryConstructor.getTable().split("(?=INNER JOIN)|(?=LEFT JOIN)|(?=RIGHT JOIN)" +
                "|(?=FULL OUTER JOIN)|(?=CROSS JOIN)|(?=NATURAL JOIN)");

        for (int i = 1; i < historyJoins.length; i++) {
            JCheckBox checkBox = new JCheckBox(historyJoins[i]);
            if (!Arrays.toString(panelPlacingCheckBoxInScrollPane.getComponents()).contains(checkBox.getText())) {
                checkBox.setToolTipText(Bundles.get("QueryBuilder.Join.toolTipTextCheckBoxConnection"));
                panelPlacingCheckBoxInScrollPane.add(checkBox);
            }
        }

        scrollPaneCheckBoxJoin.setViewportView(panelPlacingCheckBoxInScrollPane);
    }

    private void showColumnTable(String varLeftOrRight) {
        if (varLeftOrRight.equals("left")) {
            if (!Objects.requireNonNull(comboBoxLeftTable.getSelectedItem()).toString().isEmpty()) {
                StringBuilder stringBuilder = new StringBuilder();

                List<DatabaseColumn> databaseColumnTableOne = defaultDatabaseHost.getTableFromName(comboBoxLeftTable.getSelectedItem().toString()).getColumns();

                for (int i = 0; i < databaseColumnTableOne.size(); i++) {
                    ColumnData columnData = new ColumnData(queryBuilderToolBar.getConnections().getSelectedConnection(), databaseColumnTableOne.get(i));
                    if (i != databaseColumnTableOne.size() - 1) {
                        stringBuilder.append(columnData.getColumnName()).append("\n");
                    } else {
                        stringBuilder.append(columnData.getColumnName());
                    }
                }

                JOptionPane.showMessageDialog(queryBuilderPanel, stringBuilder.toString(), Bundles.get("QueryBuilder.Join.columnTableTitle"), JOptionPane.QUESTION_MESSAGE);
            }
        }

        if (varLeftOrRight.equals("right")) {
            if (!Objects.requireNonNull(comboBoxRightTable.getSelectedItem()).toString().isEmpty()) {
                StringBuilder stringBuilder = new StringBuilder();

                List<DatabaseColumn> databaseColumnTableOne = defaultDatabaseHost.getTableFromName(comboBoxRightTable.getSelectedItem().toString()).getColumns();

                for (int i = 0; i < databaseColumnTableOne.size(); i++) {
                    ColumnData columnData = new ColumnData(queryBuilderToolBar.getConnections().getSelectedConnection(), databaseColumnTableOne.get(i));
                    if (i != databaseColumnTableOne.size() - 1) {
                        stringBuilder.append(columnData.getColumnName()).append("\n");
                    } else {
                        stringBuilder.append(columnData.getColumnName());
                    }
                }

                JOptionPane.showMessageDialog(queryBuilderPanel, stringBuilder.toString(), Bundles.get("QueryBuilder.Join.columnTableTitle"), JOptionPane.QUESTION_MESSAGE);
            }
        }
    }

    private void addActionListenerInComboBox(JComboBox<String> comboBox) {
        comboBox.addActionListener(e -> comboBox.setEditable(comboBox.getSelectedIndex() == 0));
    }

    public void removeTable(JCheckBox checkBox) {
        if (queryBuilderPanel.getListNameTable().contains(checkBox.getText())) {
            queryBuilderPanel.removeTableInInputPanel(checkBox.getText());
            queryBuilderPanel.removeTableInListTable(checkBox.getText());
            removeAttributes(checkBox.getText());
            removeMainTable(checkBox.getText());
            removeJoins(checkBox.getText());
            removeLastComma();

            queryConstructor.setAttributes(queryBuilderPanel.getListTable());
            queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());

            checkTableIsEmpty();
        }
    }

    private void checkTableIsEmpty() {
        if (queryBuilderPanel.getPanelGUIComponents().getComponents().length == 0) {
            if (queryBuilderPanel.getListTable().isEmpty()) {
                queryBuilderPanel.setTextInPanelOutputTestingQuery("");
                queryBuilderPanel.getBlocksPanel().removeAll();
            }
        }
    }

    private void removeLastComma() {
        StringBuilder stringBuilderAttributes = new StringBuilder(queryConstructor.getAttribute());

        if (stringBuilderAttributes.toString().length() > 1) {
            if (stringBuilderAttributes.charAt(stringBuilderAttributes.length() - 1) == ',') {
                stringBuilderAttributes.deleteCharAt(stringBuilderAttributes.length() - 1);
            }
        }

        queryConstructor.replaceAttribute(stringBuilderAttributes.toString(), "stepBack");
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    private void removeJoins(String values) {
        StringBuilder stringBuilderJoin = new StringBuilder(queryConstructor.getTable());

        String[] joins = queryConstructor.getTable().split("(?=INNER JOIN)|(?=LEFT JOIN)|(?=RIGHT JOIN)" +
                "|(?=FULL OUTER JOIN)|(?=CROSS JOIN)|(?=NATURAL JOIN)");

        for (int i = 1; i < joins.length; i++) {
            if (joins[i].contains("FULL OUTER JOIN")) {
                if (joins[i].split(" ")[3].equals(values)) {
                    stringBuilderJoin.replace(stringBuilderJoin.indexOf(joins[i]), stringBuilderJoin.indexOf(joins[i]) + joins[i].length(), "");
                } else {
                    if (joins[i].contains(values + ".")) {
                        stringBuilderJoin.replace(stringBuilderJoin.indexOf(joins[i]), stringBuilderJoin.indexOf(joins[i]) + joins[i].length(), "");
                        removeAttributes(joins[i].split(" ")[3]);
                    }
                }
            } else {
                if (joins[i].split(" ")[2].equals(values)) {
                    stringBuilderJoin.replace(stringBuilderJoin.indexOf(joins[i]), stringBuilderJoin.indexOf(joins[i]) + joins[i].length(), "");
                } else {
                    if (joins[i].contains(values + ".")) {
                        stringBuilderJoin.replace(stringBuilderJoin.indexOf(joins[i]), stringBuilderJoin.indexOf(joins[i]) + joins[i].length(), "");
                        removeAttributes(joins[i].split(" ")[2]);
                    }
                }
            }
        }

        queryConstructor.setTable(stringBuilderJoin.toString(), "stepBack");
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    private void removeMainTable(String values) {
        StringBuilder stringBuilderTable = new StringBuilder(queryConstructor.getTable());

        if (stringBuilderTable.indexOf(values) == 0) {
            if (!queryBuilderPanel.getListTable().isEmpty()) {
                stringBuilderTable.replace(stringBuilderTable.indexOf(values), stringBuilderTable.indexOf(values) + values.length(), queryBuilderPanel.getListTable().get(0).getColumnName(0));
            } else {
                queryBuilderPanel.setTextInPanelOutputTestingQuery("");
            }
        }

        queryConstructor.setTable(stringBuilderTable.toString(), "stepBack");
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    private void removeAttributes(String values) {
        StringBuilder stringBuilderAttributes = new StringBuilder(queryConstructor.getAttribute());
        String[] attributes = queryConstructor.getAttribute().split("(?<=,)");

        for (String attribute : attributes) {
            if (attribute.contains(values + ".")) {
                stringBuilderAttributes.replace(stringBuilderAttributes.indexOf(attribute), stringBuilderAttributes.indexOf(attribute) + attribute.length(), "");
            }
        }

        queryConstructor.replaceAttribute(stringBuilderAttributes.toString(), "stepBack");
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    private ImageIcon getAndCreateIconDialog() {
        return IconManager.getIcon(BrowserConstants.APPLICATION_IMAGE, "svg", 512, IconManager.IconFolder.BASE);
    }

    private void closeDialog() {
        setVisible(false);
        dispose();
    }
}
