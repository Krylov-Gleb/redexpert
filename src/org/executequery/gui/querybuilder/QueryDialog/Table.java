package org.executequery.gui.querybuilder.QueryDialog;

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

/**
 * This class creates a dialog (window) that adds tables to the query.
 * <p>
 * Этот класс создаёт диалог (окно) который добавляет в запрос таблицы.
 *
 * @author Krylov Gleb
 */
public class Table extends JDialog {

    // --- Fields that are passed through the constructor ----
    // --- Поля, которые передаются через конструктор ---

    private QueryConstructor queryConstructor;
    private QBPanel queryBuilderPanel;
    private QBToolBar queryBuilderToolBar;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

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

    /**
     * A dialog (window) is created.
     * A method is used to initialize fields.
     * <p>
     * Создаётся диалог (окно).
     * Используется метод для инициализации полей.
     */
    public Table(QBPanel queryBuilderPanel, QueryConstructor queryConstructor, QBToolBar queryBuilderToolBar) {
        this.queryBuilderPanel = queryBuilderPanel;
        this.queryConstructor = queryConstructor;
        this.queryBuilderToolBar = queryBuilderToolBar;
        init();
    }

    /**
     * A method for initializing fields.
     * <p>
     * Метод для инициализации полей.
     */
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

    /**
     * The method for initializing the database host.
     * <p>
     * Метод для инициализации хоста базы данных.
     */
    private void initDefaultDataBaseHost() {
        defaultDatabaseHost = new DefaultDatabaseHost(queryBuilderToolBar.getConnections().getSelectedConnection());
    }

    /**
     * The method of initializing the checkbox.
     * <p>
     * Метод инициализации флажка.
     */
    private void initCheckBox() {
        checkBoxShowSystemTable = WidgetFactory.createCheckBox("checkBoxShowSystemTable", Bundles.get("QueryBuilder.Table.showSystemTable"));
        checkBoxShowSystemTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkBoxShowSystemTable.isSelected()) {
                    displayEverything = true;
                    arrangeCheckBoxesInScrollPane(displayEverything);
                } else {
                    displayEverything = false;
                    arrangeCheckBoxesInScrollPane(displayEverything);
                }
            }
        });

        placingCheckBoxInPanel();
    }

    /**
     * Method for initializing fields.
     * <p>
     * Метод для инициализации полей.
     */
    private void initField() {
        displayEverything = false;
    }

    /**
     * A method for initializing buttons.
     * <p>
     * Метод для инициализации кнопок.
     */
    private void initButton() {
        buttonClose = WidgetFactory.createButton("buttonClose", Bundles.get("common.close.button"), event -> {
            closeDialog();
        });

        placingButtonsInPanel();
    }

    /**
     * A method for placing buttons in a panel to place buttons.
     * <p>
     * Метод для размещения кнопок в панели для размещения кнопок.
     */
    private void placingButtonsInPanel() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().fillHorizontally();
        panelButton.add(new JLabel(" "), gridBagHelper.setXY(0, 0).setMaxWeightX().get());
        panelButton.add(buttonClose, gridBagHelper.nextCol().setMaxWeightX().get());
        panelButton.add(new JLabel(" "), gridBagHelper.nextCol().setMaxWeightX().get());
    }

    /**
     * A method for placing checkboxes in a panel for placing checkboxes.
     * <p>
     * Метод для размещения флажков в панели для размещения флажков.
     */
    private void placingCheckBoxInPanel() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().fillHorizontally();
        panelCheckBox.add(new JLabel(" "), gridBagHelper.setXY(0, 0).setMaxWeightX().get());
        panelCheckBox.add(checkBoxShowSystemTable, gridBagHelper.nextCol().setMaxWeightX().get());
        panelCheckBox.add(new JLabel(" "), gridBagHelper.nextCol().setMaxWeightX().get());
    }

    /**
     * A method for initializing a text field.
     * <p>
     * Метод для инициализации текстового поля.
     */
    private void initTextField() {
        textFieldSearch = WidgetFactory.createTextField("textFieldSearch");
        textFieldSearch.setToolTipText(Bundles.get("QueryBuilder.Table.toolTipTextSearchTextField"));
        textFieldSearch.setMinimumSize(new Dimension(200, 25));
        textFieldSearch.setPreferredSize(new Dimension(200, 25));
        textFieldSearch.setMaximumSize(new Dimension(200, 25));
        eventAddDocumentListenerInTextFields(textFieldSearch);
    }

    /**
     * The method for initializing JPanel.
     * <p>
     * Метод для инициализации JPanel.
     */
    private void initPanel() {
        panelPlacingComponents = WidgetFactory.createPanel("panelPlacingComponents");
        panelPlacingComponents.setLayout(new GridBagLayout());

        panelButton = WidgetFactory.createPanel("panelButton");
        panelButton.setLayout(new GridBagLayout());

        panelCheckBox = WidgetFactory.createPanel("panelCheckBox");
        panelCheckBox.setLayout(new GridBagLayout());
    }

    /**
     * A method for initializing the ScrollPane and placing checkboxes on it.
     * <p>
     * Метод для инициализации scrollPane и размещения на ней флажков.
     */
    private void initScrollPane() {
        scrollPaneTable = new JScrollPane();
        scrollPaneTable.setPreferredSize(new Dimension(100, 300));
        arrangeCheckBoxesInScrollPane(displayEverything);
    }

    /**
     * A method for initializing labels.
     * <p>
     * Метод для инициализации меток.
     */
    private void initLabel() {
        labelSearch = WidgetFactory.createLabel(Bundles.get("common.search.button"));
    }

    /**
     * A method for placing components as well as setting up a dialog (window).
     * <p>
     * Метод для размещения компонентов а так же настройки диалога (окна).
     */
    private void arrangeComponent() {
        arrangeComponentsInPanelFromPlacingComponents();
        configurationDialog();
    }

    /**
     * A method for configuring the parameters of a dialog (window) created by this class.
     * <p>
     * Метод для настройки параметров диалога (окна) созданного этим классом.
     */
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

    /**
     * A method for placing components in a panel for placing components.
     * <p>
     * Метод для размещения компонентов в панели для размещения компонентов.
     */
    private void arrangeComponentsInPanelFromPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(10, 5, 10, 5).fillHorizontally();
        panelPlacingComponents.add(labelSearch, gridBagHelper.setXY(0, 0).setMinWeightX().get());
        panelPlacingComponents.add(textFieldSearch, gridBagHelper.nextCol().setMaxWeightX().get());
        panelPlacingComponents.add(scrollPaneTable, gridBagHelper.previousCol().nextRow().spanX().setMaxWeightX().get());
        panelPlacingComponents.add(checkBoxShowSystemTable, gridBagHelper.nextCol().nextRow().setMaxWeightX().get());
        panelPlacingComponents.add(panelButton, gridBagHelper.previousCol().nextRow().spanX().spanY().setMaxWeightX().get());
    }

    /**
     * A method that implements the functionality of adding tables to a query.
     * <p>
     * Метод реализующий функционал добавления таблиц в запрос.
     */
    public void addTable(JCheckBox checkBox) {
        QBCreateTable tableQueryBuilder = new QBCreateTable(getListNamesColumns(checkBox.getText()), checkBox.getText(), queryBuilderPanel, queryConstructor);

        if (!queryBuilderPanel.getListNameTable().contains(tableQueryBuilder.getJTable().getColumnName(0))) {
            queryBuilderPanel.addTableInListTable(tableQueryBuilder.getJTable());
            queryBuilderPanel.addTableInPanelGUIComponents(tableQueryBuilder.getMovePanelTable());
        }

        if (queryBuilderPanel.getListNameTable().size() == 1) {
            queryConstructor.setTable(queryBuilderPanel.getListTable().get(0).getColumnName(0), "stepBack");
        }

        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    /**
     * A method that implements the functionality of deleting tables from a query.
     * <p>
     * Метод реализующий функционал удаления таблиц из запроса.
     */
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

    /**
     * Checking for tables in the output panel.
     * <p>
     * Проверка на наличие таблиц на панели вывода.
     */
    private void checkTableIsEmpty() {
        if (queryBuilderPanel.getPanelGUIComponents().getComponents().length == 0) {
            if (queryBuilderPanel.getListTable().isEmpty()) {
                queryBuilderPanel.setTextInPanelOutputTestingQuery("");
                queryBuilderPanel.getBlocksPanel().removeAll();
                clearIsNotTable();
            }
        }
    }

    /**
     * A method for removing the last comma from a query.
     * <p>
     * Метод для удаления последней запятой из запроса.
     */
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

    /**
     * A method for removing a connection (join) from a request.
     * <p>
     * Метод для удаления соединения (join) из запроса.
     */
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

    /**
     * A method for deleting a table from a query.
     * <p>
     * Метод для удаления таблицы из запроса.
     */
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

    /**
     * A method for removing attributes from a query.
     * <p>
     * Метод для удаления атрибутов из запроса.
     */
    private void removeAttributes(String values) {
        StringBuilder stringBuilderAttributes = new StringBuilder(queryConstructor.getAttribute());
        String[] attributes = queryConstructor.getAttribute().split("(?<=,)");

        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].contains(values + ".")) {
                stringBuilderAttributes.replace(stringBuilderAttributes.indexOf(attributes[i]), stringBuilderAttributes.indexOf(attributes[i]) + attributes[i].length(), "");
            }
        }

        queryConstructor.replaceAttribute(stringBuilderAttributes.toString(), "stepBack");
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    /**
     * Search implementation.
     * <p>
     * Реализация поиска.
     */
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

    /**
     * A method for placing checkboxes on a ScrollPane.
     * <p>
     * Метод для размещения флажков на ScrollPane.
     */
    private void arrangeCheckBoxesInScrollPane(boolean isDisplayEverything) {
        panelPlacingCheckBoxInScrollPane = WidgetFactory.createPanel("panelPlacingCheckBoxInScrollPane");
        panelPlacingCheckBoxInScrollPane.setLayout(new BoxLayout(panelPlacingCheckBoxInScrollPane, BoxLayout.Y_AXIS));

        for (int i = 0; i < defaultDatabaseHost.getTableNames().size(); i++) {
            eventCreateAndAddCheckBox(i, isDisplayEverything);
        }

        scrollPaneTable.setViewportView(panelPlacingCheckBoxInScrollPane);
        scrollPaneTable.revalidate();
    }

    /**
     * Method for creating and deleting a CheckBox.
     * <p>
     * Метод для создания и удаления CheckBox.
     */
    private void eventCreateAndAddCheckBox(int index, boolean isDisplayEverything) {
        if (!isDisplayEverything) {
            if (defaultDatabaseHost.getTableNames().get(index).indexOf("MON$") != 0 & defaultDatabaseHost.getTableNames().get(index).indexOf("RDB$") != 0 & defaultDatabaseHost.getTableNames().get(index).indexOf("SEC$") != 0) {
                JCheckBox checkBox = new JCheckBox(defaultDatabaseHost.getTableNames().get(index));
                checkBox.setToolTipText(Bundles.get("QueryBuilder.Table.toolTipTextCheckBoxTable"));
                checkBox.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (checkBox.isSelected()) {
                            addTable(checkBox);
                        } else {
                            removeTable(checkBox);
                        }
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
            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (checkBox.isSelected()) {
                        addTable(checkBox);
                    } else {
                        removeTable(checkBox);
                    }
                }
            });

            if (queryBuilderPanel.getListNameTable().contains(checkBox.getText())) {
                checkBox.setSelected(true);
            }

            panelPlacingCheckBoxInScrollPane.add(checkBox);
        }
    }

    /**
     * The method for adding connections (Join).
     * <p>
     * Метод для добавления соединений (Join).
     */
    private void addJoin(String tableNameOne, String tableNameTwo, String joinName, String keyValues) {
        StringBuilder stringBuilderTable = new StringBuilder(queryConstructor.getTable());

        if (stringBuilderTable.indexOf(tableNameOne) == 0) {
            if (!stringBuilderTable.toString().contains(" " + tableNameTwo + " ")) {
                if (!keyValues.isEmpty()) {
                    stringBuilderTable.append(" ").append(joinName.toUpperCase()).append(" ").append(tableNameTwo)
                            .append(" ").append("ON").append(" ").append(keyValues);
                } else {
                    stringBuilderTable.append(" ").append(joinName.toUpperCase()).append(" ").append(tableNameTwo).append(" ");
                }

                queryBuilderPanel.addStepBackActionInHistory("Set Join " + queryConstructor.getTable());
                queryConstructor.setTable(stringBuilderTable.toString(), "stepBack");
                return;
            } else {
                return;
            }
        }

        if (stringBuilderTable.indexOf(tableNameTwo) == 0) {
            if (!stringBuilderTable.toString().contains(" " + tableNameOne + " ")) {
                if (!keyValues.isEmpty()) {
                    stringBuilderTable.append(" ").append(joinName.toUpperCase()).append(" ").append(tableNameOne)
                            .append(" ").append("ON").append(" ").append(keyValues);
                } else {
                    stringBuilderTable.append(" ").append(joinName.toUpperCase()).append(" ").append(tableNameOne).append(" ");
                }

                queryBuilderPanel.addStepBackActionInHistory("Set Join " + queryConstructor.getTable());
                queryConstructor.setTable(stringBuilderTable.toString(), "stepBack");
                return;
            } else {
                return;
            }
        }

        if (stringBuilderTable.toString().contains(" " + tableNameOne + " ")) {
            if (!stringBuilderTable.toString().contains(" " + tableNameTwo + " ")) {
                if (!keyValues.isEmpty()) {
                    stringBuilderTable.append(" ").append(joinName.toUpperCase()).append(" ").append(tableNameTwo)
                            .append(" ").append("ON").append(" ").append(keyValues);
                } else {
                    stringBuilderTable.append(" ").append(joinName.toUpperCase()).append(" ").append(tableNameTwo).append(" ");
                }

                queryBuilderPanel.addStepBackActionInHistory("Set Join " + queryConstructor.getTable());
                queryConstructor.setTable(stringBuilderTable.toString(), "stepBack");
                return;
            } else {
                return;
            }
        }

        if (stringBuilderTable.toString().contains(" " + tableNameTwo + " ")) {
            if (!stringBuilderTable.toString().contains(" " + tableNameOne + " ")) {
                if (!keyValues.isEmpty()) {
                    stringBuilderTable.append(" ").append(joinName.toUpperCase()).append(" ").append(tableNameOne)
                            .append(" ").append("ON").append(" ").append(keyValues);
                } else {
                    stringBuilderTable.append(" ").append(joinName.toUpperCase()).append(" ").append(tableNameOne).append(" ");
                }

                queryBuilderPanel.addStepBackActionInHistory("Set Join " + queryConstructor.getTable());
                queryConstructor.setTable(stringBuilderTable.toString(), "stepBack");
                return;
            } else {
                return;
            }
        }
    }

    /**
     * The method for getting the database host.
     * <p>
     * Метод для получения хоста базы данных.
     */
    private static DefaultDatabaseHost getDefaultDatabaseHost(DatabaseConnection connection) {
        return ConnectionsTreePanel.getPanelFromBrowser().getDefaultDatabaseHostFromConnection(connection);
    }


    /**
     * We get a list of table column from names.
     * <p>
     * Получаем лист имён колонок из таблиц.
     */
    public List<String> getListNamesColumns(String table) {
        return getDefaultDatabaseHost(queryBuilderToolBar.getConnections().getSelectedConnection()).getColumnNames(table);
    }

    private void clearIsNotTable() {
        queryConstructor.clearAttribute();
        queryConstructor.clearFunction();
        queryConstructor.clearWhere();
        queryConstructor.clearHaving();
        queryConstructor.clearTable();
        queryConstructor.clearGroupBy();
        queryConstructor.clearOrderBy();
    }

    /**
     * A method for creating and receiving a dialog icon.
     * <p>
     * Метод для создания и получения иконки диалога.
     */
    private ImageIcon getAndCreateIconDialog() {
        return IconManager.getIcon(BrowserConstants.APPLICATION_IMAGE, "svg", 512, IconManager.IconFolder.BASE);
    }

    /**
     * The method for closing the dialog (window).
     * <p>
     * Метод для закрытия диалога (окна).
     */
    private void closeDialog() {
        AutoCreateJoin();
        setVisible(false);
        dispose();
    }

    /**
     * A method for automatically adding connections.
     * <p>
     * Метод для автоматического добавления соединений.
     */
    private void AutoCreateJoin() {

        ArrayList<JTable> listTables = queryBuilderPanel.getListTable();
        ArrayList<String> listNameTables = queryBuilderPanel.getListNameTable();

        for (int i = 0; i < 3; i++) {
            for (int a = 0; a < listTables.size(); a++) {
                if (listTables.get(a).getColumnName(0).indexOf("MON$") != 0 & listTables.get(a).getColumnName(0).indexOf("RDB$") != 0 & listTables.get(a).getColumnName(0).indexOf("SEC$") != 0) {
                    List<DatabaseColumn> databaseColumnTableOne = defaultDatabaseHost.getTableFromName(listTables.get(a).getColumnName(0)).getColumns();

                    for (int b = 0; b < databaseColumnTableOne.size(); b++) {
                        ColumnData columnData = new ColumnData(queryBuilderToolBar.getConnections().getSelectedConnection(), databaseColumnTableOne.get(b));
                        if (columnData.isForeignKey() || columnData.isPrimaryKey()) {
                            for (int c = 0; c < columnData.getColumnConstraintsArray().length; c++) {
                                if (columnData.getColumnConstraintsArray()[c].getRefTable() != null) {
                                    if (listNameTables.contains(columnData.getColumnConstraintsArray()[c].getRefTable())) {
                                        List<DatabaseColumn> databaseColumn2 = defaultDatabaseHost.getTableFromName(columnData.getColumnConstraintsArray()[c].getRefTable()).getColumns();

                                        for (int d = 0; d < databaseColumn2.size(); d++) {
                                            ColumnData columnData2 = new ColumnData(queryBuilderToolBar.getConnections().getSelectedConnection(), databaseColumn2.get(d));
                                            if (columnData.isForeignKey() || columnData.isPrimaryKey()) {
                                                addJoin(listTables.get(a).getColumnName(0), columnData.getColumnConstraintsArray()[c].getRefTable(), "INNER JOIN", columnData.getTableName() + "." + columnData.getColumnName() + " = " + columnData2.getTableName() + "." + columnData2.getColumnName());
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
                        removeTable(new JCheckBox(listNameTables.get(i)));
                    }
                }
            }
        }
    }
}
