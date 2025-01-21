package org.executequery.gui.querybuilder.querydialog;

import org.executequery.databasemediators.DatabaseConnection;
import org.executequery.databaseobjects.DatabaseColumn;
import org.executequery.databaseobjects.impl.DefaultDatabaseHost;
import org.executequery.gui.IconManager;
import org.executequery.gui.WidgetFactory;
import org.executequery.gui.browser.BrowserConstants;
import org.executequery.gui.browser.ColumnData;
import org.executequery.gui.browser.ConnectionsTreePanel;
import org.executequery.gui.querybuilder.QBCreateTable;
import org.executequery.gui.querybuilder.QBPanel;
import org.executequery.gui.querybuilder.QBToolBar;
import org.executequery.gui.querybuilder.QueryConstructor;
import org.executequery.localization.Bundles;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Table extends JDialog {

    private final QueryConstructor queryConstructor;
    private final QBPanel queryBuilderPanel;
    private final QBToolBar queryBuilderToolBar;
    private JPanel panelPlacingComponents;
    private JPanel panelPlacingCheckBoxInScrollPane;
    private JPanel panelButton;
    private JPanel panelCheckBox;
    private JLabel labelSearch;
    private JTextField textFieldSearch;
    private JScrollPane scrollPaneTable;
    private JCheckBox checkBoxShowSystemTable;
    private JButton buttonClose;

    private DefaultDatabaseHost defaultDatabaseHost;
    private boolean displayEverything;

    public Table(QBPanel queryBuilderPanel, QueryConstructor queryConstructor, QBToolBar queryBuilderToolBar) {
        this.queryBuilderPanel = queryBuilderPanel;
        this.queryConstructor = queryConstructor;
        this.queryBuilderToolBar = queryBuilderToolBar;
        init();
    }

    private void init() {
        initField();
        initPanel();
        initLabel();
        initCheckBox();
        initTextField();
        initButton();
        initDefaultDataBaseHost();
        initScrollPane();
        arrangeComponent();
    }

    private void initDefaultDataBaseHost() {
        defaultDatabaseHost = new DefaultDatabaseHost(queryBuilderToolBar.getConnections().getSelectedConnection());
    }

    private void initCheckBox() {
        checkBoxShowSystemTable = WidgetFactory.createCheckBox("checkBoxShowSystemTable", Bundles.get("QueryBuilder.Table.showSystemTable"));
        checkBoxShowSystemTable.addActionListener(e -> {
            if (checkBoxShowSystemTable.isSelected()) {
                displayEverything = true;
                arrangeCheckBoxesInScrollPane(true);
            } else {
                displayEverything = false;
                arrangeCheckBoxesInScrollPane(false);
            }
        });

        placingCheckBoxInPanel();
    }

    private void initField() {
        displayEverything = false;
    }

    private void initButton() {
        buttonClose = WidgetFactory.createButton("buttonClose", Bundles.get("common.close.button"), event -> closeDialog());

        placingButtonsInPanel();
    }

    private void placingButtonsInPanel() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().fillHorizontally();
        panelButton.add(new JLabel(" "), gridBagHelper.setXY(0, 0).setMaxWeightX().get());
        panelButton.add(buttonClose, gridBagHelper.nextCol().setMaxWeightX().get());
        panelButton.add(new JLabel(" "), gridBagHelper.nextCol().setMaxWeightX().get());
    }

    private void placingCheckBoxInPanel() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().fillHorizontally();
        panelCheckBox.add(new JLabel(" "), gridBagHelper.setXY(0, 0).setMaxWeightX().get());
        panelCheckBox.add(checkBoxShowSystemTable, gridBagHelper.nextCol().setMaxWeightX().get());
        panelCheckBox.add(new JLabel(" "), gridBagHelper.nextCol().setMaxWeightX().get());
    }

    private void initTextField() {
        textFieldSearch = WidgetFactory.createTextField("textFieldSearch");
        textFieldSearch.setToolTipText(Bundles.get("QueryBuilder.Table.toolTipTextSearchTextField"));
        textFieldSearch.setMinimumSize(new Dimension(200, 25));
        textFieldSearch.setPreferredSize(new Dimension(200, 25));
        textFieldSearch.setMaximumSize(new Dimension(200, 25));
        eventAddDocumentListenerInTextFields(textFieldSearch);
    }

    private void initPanel() {
        panelPlacingComponents = WidgetFactory.createPanel("panelPlacingComponents");
        panelPlacingComponents.setLayout(new GridBagLayout());

        panelButton = WidgetFactory.createPanel("panelButton");
        panelButton.setLayout(new GridBagLayout());

        panelCheckBox = WidgetFactory.createPanel("panelCheckBox");
        panelCheckBox.setLayout(new GridBagLayout());
    }

    private void initScrollPane() {
        scrollPaneTable = new JScrollPane();
        scrollPaneTable.setPreferredSize(new Dimension(100, 300));
        arrangeCheckBoxesInScrollPane(displayEverything);
    }

    private void initLabel() {
        labelSearch = WidgetFactory.createLabel(Bundles.get("common.search.button"));
    }

    private void arrangeComponent() {
        arrangeComponentsInPanelFromPlacingComponents();
        configurationDialog();
    }

    private void configurationDialog() {
        setLayout(new BorderLayout());
        setTitle(Bundles.get("QueryBuilder.Table.title"));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                AutoCreateJoin();
            }
        });
        setIconImage(getAndCreateIconDialog().getImage());
        setLocationRelativeTo(queryBuilderPanel);
        setResizable(false);
        add(panelPlacingComponents, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(600, 460);
        setVisible(true);
    }

    private void arrangeComponentsInPanelFromPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(10, 5, 10, 5).fillHorizontally();
        panelPlacingComponents.add(labelSearch, gridBagHelper.setXY(0, 0).setMinWeightX().get());
        panelPlacingComponents.add(textFieldSearch, gridBagHelper.nextCol().setMaxWeightX().get());
        panelPlacingComponents.add(scrollPaneTable, gridBagHelper.previousCol().nextRow().spanX().setMaxWeightX().get());
        panelPlacingComponents.add(checkBoxShowSystemTable, gridBagHelper.nextCol().nextRow().setMaxWeightX().get());
        panelPlacingComponents.add(panelButton, gridBagHelper.previousCol().nextRow().spanX().spanY().setMaxWeightX().get());
    }

    public void addTable(JCheckBox checkBox, boolean externalCall) {
        QBCreateTable tableQueryBuilder = new QBCreateTable(getListNamesColumns(checkBox.getText()), checkBox.getText(), queryBuilderPanel, queryConstructor);
        if (!externalCall) {

            if (!queryBuilderPanel.getListNameTable().contains(tableQueryBuilder.getJTable().getColumnName(0))) {
                queryBuilderPanel.addTableInListTable(tableQueryBuilder.getJTable());
                queryBuilderPanel.addTableInPanelGUIComponents(tableQueryBuilder.getMovePanelTable());
                queryBuilderPanel.addStepBackActionInHistory("Delete Table " + checkBox.getText());
            }

        } else {

            if (!queryBuilderPanel.getListNameTable().contains(tableQueryBuilder.getJTable().getColumnName(0))) {
                queryBuilderPanel.addTableInListTable(tableQueryBuilder.getJTable());
                queryBuilderPanel.addTableInPanelGUIComponents(tableQueryBuilder.getMovePanelTable());
            }

        }
        if (queryBuilderPanel.getListNameTable().size() == 1) {
            queryConstructor.setTable(queryBuilderPanel.getListTable().get(0).getColumnName(0));
        }
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    public void removeTable(JCheckBox checkBox, boolean externalCall) {
        if (!externalCall) {
            if (queryBuilderPanel.getListNameTable().contains(checkBox.getText())) {
                queryBuilderPanel.removeTableInInputPanel(checkBox.getText());
                queryBuilderPanel.removeTableInListTable(checkBox.getText());
                queryBuilderPanel.addStepBackActionInHistory("Add Table " + checkBox.getText());
                removeAttributes(checkBox.getText());
                removeMainTable(checkBox.getText());
                removeJoins(checkBox.getText());
                removeLastComma();

                queryConstructor.setAttributes(queryBuilderPanel.getListTable());
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());

                checkTableIsEmpty();
            }
        } else {
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
    }

    private void checkTableIsEmpty() {
        if (queryBuilderPanel.getPanelGUIComponents().getComponents().length == 0) {
            if (queryBuilderPanel.getListTable().isEmpty()) {
                queryConstructor.clearAttribute();
                queryConstructor.clearFunction();
                queryConstructor.clearWhere();
                queryConstructor.clearHaving();
                queryConstructor.clearTable();
                queryConstructor.clearGroupBy();
                queryConstructor.clearOrderBy();
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

        queryConstructor.replaceAttribute(stringBuilderAttributes.toString());
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

        queryConstructor.setTable(stringBuilderJoin.toString());
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

        queryConstructor.setTable(stringBuilderTable.toString());
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

        queryConstructor.replaceAttribute(stringBuilderAttributes.toString());
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    private void eventAddDocumentListenerInTextFields(JTextField textField) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                textChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                textChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                textChanged();
            }

            private void textChanged() {
                panelPlacingCheckBoxInScrollPane = WidgetFactory.createPanel("panelPlacingCheckBoxInScrollPane");
                panelPlacingCheckBoxInScrollPane.setLayout(new BoxLayout(panelPlacingCheckBoxInScrollPane, BoxLayout.Y_AXIS));

                for (int i = 0; i < defaultDatabaseHost.getTableNames().size(); i++) {
                    if (defaultDatabaseHost.getTableNames().get(i).contains(textField.getText().toUpperCase())) {
                        eventCreateAndAddCheckBox(i, displayEverything);
                    }
                }

                scrollPaneTable.setViewportView(panelPlacingCheckBoxInScrollPane);
                scrollPaneTable.revalidate();
            }
        });
    }

    private void arrangeCheckBoxesInScrollPane(boolean isDisplayEverything) {
        panelPlacingCheckBoxInScrollPane = WidgetFactory.createPanel("panelPlacingCheckBoxInScrollPane");
        panelPlacingCheckBoxInScrollPane.setLayout(new BoxLayout(panelPlacingCheckBoxInScrollPane, BoxLayout.Y_AXIS));

        for (int i = 0; i < defaultDatabaseHost.getTableNames().size(); i++) {
            eventCreateAndAddCheckBox(i, isDisplayEverything);
        }

        scrollPaneTable.setViewportView(panelPlacingCheckBoxInScrollPane);
        scrollPaneTable.revalidate();
    }

    private void eventCreateAndAddCheckBox(int index, boolean isDisplayEverything) {
        if (!isDisplayEverything) {
            if (defaultDatabaseHost.getTableNames().get(index).indexOf("MON$") != 0 & defaultDatabaseHost.getTableNames().get(index).indexOf("RDB$") != 0 & defaultDatabaseHost.getTableNames().get(index).indexOf("SEC$") != 0) {
                JCheckBox checkBox = new JCheckBox(defaultDatabaseHost.getTableNames().get(index));
                checkBox.setToolTipText(Bundles.get("QueryBuilder.Table.toolTipTextCheckBoxTable"));
                checkBox.addItemListener(e -> {
                    if (checkBox.isSelected()) {
                        addTable(checkBox, false);
                    } else {
                        removeTable(checkBox, false);
                    }
                });

                if (queryBuilderPanel.getListNameTable().contains(checkBox.getText())) {
                    checkBox.setSelected(true);
                }

                panelPlacingCheckBoxInScrollPane.add(checkBox);
            }
        } else {
            JCheckBox checkBox = new JCheckBox(defaultDatabaseHost.getTableNames().get(index));
            checkBox.setToolTipText(Bundles.get("QueryBuilder.Table.toolTipTextCheckBoxTable"));
            checkBox.addItemListener(e -> {
                if (checkBox.isSelected()) {
                    addTable(checkBox, false);
                } else {
                    removeTable(checkBox, false);
                }
            });

            if (queryBuilderPanel.getListNameTable().contains(checkBox.getText())) {
                checkBox.setSelected(true);
            }

            panelPlacingCheckBoxInScrollPane.add(checkBox);
        }
    }

    private void addJoin(String tableNameOne, String tableNameTwo, String keyValues) {
        StringBuilder stringBuilderTable = new StringBuilder(queryConstructor.getTable());

        if (stringBuilderTable.indexOf(tableNameOne) == 0) {
            if (!stringBuilderTable.toString().contains(" " + tableNameTwo + " ")) {
                if (!keyValues.isEmpty()) {
                    stringBuilderTable.append(" ").append("INNER JOIN".toUpperCase()).append(" ").append(tableNameTwo)
                            .append(" ").append("ON").append(" ").append(keyValues);
                } else {
                    stringBuilderTable.append(" ").append("INNER JOIN".toUpperCase()).append(" ").append(tableNameTwo).append(" ");
                }

                queryConstructor.setTable(stringBuilderTable.toString(), "stepBack");
            }
            return;
        }

        if (stringBuilderTable.indexOf(tableNameTwo) == 0) {
            if (!stringBuilderTable.toString().contains(" " + tableNameOne + " ")) {
                if (!keyValues.isEmpty()) {
                    stringBuilderTable.append(" ").append("INNER JOIN".toUpperCase()).append(" ").append(tableNameOne)
                            .append(" ").append("ON").append(" ").append(keyValues);
                } else {
                    stringBuilderTable.append(" ").append("INNER JOIN".toUpperCase()).append(" ").append(tableNameOne).append(" ");
                }

                queryConstructor.setTable(stringBuilderTable.toString(), "stepBack");
            }
            return;
        }

        if (stringBuilderTable.toString().contains(" " + tableNameOne + " ")) {
            if (!stringBuilderTable.toString().contains(" " + tableNameTwo + " ")) {
                if (!keyValues.isEmpty()) {
                    stringBuilderTable.append(" ").append("INNER JOIN".toUpperCase()).append(" ").append(tableNameTwo)
                            .append(" ").append("ON").append(" ").append(keyValues);
                } else {
                    stringBuilderTable.append(" ").append("INNER JOIN".toUpperCase()).append(" ").append(tableNameTwo).append(" ");
                }

                queryConstructor.setTable(stringBuilderTable.toString(), "stepBack");
            }
            return;
        }

        if (stringBuilderTable.toString().contains(" " + tableNameTwo + " ")) {
            if (!stringBuilderTable.toString().contains(" " + tableNameOne + " ")) {
                if (!keyValues.isEmpty()) {
                    stringBuilderTable.append(" ").append("INNER JOIN".toUpperCase()).append(" ").append(tableNameOne)
                            .append(" ").append("ON").append(" ").append(keyValues);
                } else {
                    stringBuilderTable.append(" ").append("INNER JOIN".toUpperCase()).append(" ").append(tableNameOne).append(" ");
                }

                queryConstructor.setTable(stringBuilderTable.toString(), "stepBack");
            }
        }
    }

    private static DefaultDatabaseHost getDefaultDatabaseHost(DatabaseConnection connection) {
        return ConnectionsTreePanel.getPanelFromBrowser().getDefaultDatabaseHostFromConnection(connection);
    }

    public List<String> getListNamesColumns(String table) {
        return getDefaultDatabaseHost(queryBuilderToolBar.getConnections().getSelectedConnection()).getColumnNames(table);
    }

    private ImageIcon getAndCreateIconDialog() {
        return IconManager.getIcon(BrowserConstants.APPLICATION_IMAGE, "svg", 512, IconManager.IconFolder.BASE);
    }

    private void closeDialog() {
        AutoCreateJoin();
        setVisible(false);
        dispose();
    }

    private void AutoCreateJoin() {

        ArrayList<JTable> listTables = queryBuilderPanel.getListTable();
        ArrayList<String> listNameTables = queryBuilderPanel.getListNameTable();

        for (int i = 0; i < 3; i++) {
            for (JTable listTable : listTables) {
                if (listTable.getColumnName(0).indexOf("MON$") != 0 & listTable.getColumnName(0).indexOf("RDB$") != 0 & listTable.getColumnName(0).indexOf("SEC$") != 0) {
                    List<DatabaseColumn> databaseColumnTableOne = defaultDatabaseHost.getTableFromName(listTable.getColumnName(0)).getColumns();

                    for (DatabaseColumn databaseColumn : databaseColumnTableOne) {
                        ColumnData columnData = new ColumnData(queryBuilderToolBar.getConnections().getSelectedConnection(), databaseColumn);
                        if (columnData.isForeignKey() || columnData.isPrimaryKey()) {
                            for (int c = 0; c < columnData.getColumnConstraintsArray().length; c++) {
                                if (columnData.getColumnConstraintsArray()[c].getRefTable() != null) {
                                    if (listNameTables.contains(columnData.getColumnConstraintsArray()[c].getRefTable())) {
                                        List<DatabaseColumn> databaseColumn2 = defaultDatabaseHost.getTableFromName(columnData.getColumnConstraintsArray()[c].getRefTable()).getColumns();

                                        for (DatabaseColumn column : databaseColumn2) {
                                            ColumnData columnData2 = new ColumnData(queryBuilderToolBar.getConnections().getSelectedConnection(), column);
                                            if (columnData.isForeignKey() || columnData.isPrimaryKey()) {
                                                addJoin(listTable.getColumnName(0), columnData.getColumnConstraintsArray()[c].getRefTable(), columnData.getTableName() + "." + columnData.getColumnName() + " = " + columnData2.getTableName() + "." + columnData2.getColumnName());
                                                queryConstructor.setAttributes(queryBuilderPanel.getListTable());
                                                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        String tableValues = queryConstructor.getTable();

        if (listNameTables.size() > 1) {
            for (int i = 1; i < listNameTables.size(); i++) {
                if (!tableValues.contains(" " + listNameTables.get(i) + " ")) {
                    int userDecision = JOptionPane.showConfirmDialog(queryBuilderPanel, Bundles.get("QueryBuilder.Join.connectionMissing") + listNameTables.get(i) + ".\n" + Bundles.get("QueryBuilder.Table.isAddTable"), Bundles.get("QueryBuilder.Join.connectionMissingTitle"), JOptionPane.YES_NO_OPTION);
                    if (userDecision == JOptionPane.YES_OPTION) {
                        new Join(queryBuilderPanel, queryConstructor, queryBuilderToolBar);
                        tableValues = queryConstructor.getTable();
                    }
                }
            }

            for (int i = 1; i < listNameTables.size(); i++) {
                if (!tableValues.contains(" " + listNameTables.get(i) + " ")) {
                    int userDecision = JOptionPane.showConfirmDialog(queryBuilderPanel, Bundles.get("QueryBuilder.Join.connectionMissing") + listNameTables.get(i) + ".\n" + Bundles.get("QueryBuilder.Join.isDeleteTable"), Bundles.get("QueryBuilder.Join.connectionMissingTitle"), JOptionPane.YES_NO_OPTION);
                    if (userDecision == JOptionPane.YES_OPTION) {
                        removeTable(new JCheckBox(listNameTables.get(i)), false);
                    }
                }
            }
        }
    }
}
