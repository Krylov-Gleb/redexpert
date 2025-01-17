package org.executequery.gui.querybuilder.QueryDialog;

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
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class creates a dialog (window) that adds a Join to the request.
 * <p>
 * Этот класс создаёт диалог (окно) который добавляет в запрос соединения (Join).
 *
 * @author Krylov Gleb
 */
public class Join extends JDialog {

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

    /**
     * A dialog (window) is created.
     * A method is used to initialize fields.
     * <p>
     * Создаётся диалог (окно).
     * Используется метод для инициализации полей.
     */
    public Join(QBPanel queryBuilderPanel, QueryConstructor queryConstructor, QBToolBar queryBuilderToolBar) {
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
        initPanels();
        initScrollPane();
        intLabel();
        initDefaultDataBaseHost();
        initComboBox();
        initTextFields();
        initButtons();
        arrangeComponents();
    }

    /**
     * A method for initializing buttons.
     * <p>
     * Метод для инициализации кнопок.
     */
    private void initButtons() {
        buttonAddJoin = WidgetFactory.createButton("buttonAddJoin", Bundles.get("common.add.button"), event -> {
            addJoin();
            arrangeCheckBoxesInScrollPane();
        });

        buttonRemoveJoin = WidgetFactory.createButton("buttonRemoveJoin", Bundles.get("common.delete.button"), event -> {
            removeJoin();
            arrangeCheckBoxesInScrollPane();
        });

        buttonClose = WidgetFactory.createButton("buttonClose", Bundles.get("common.close.button"), event -> {
            closeDialog();
        });

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

    /**
     * The method for initializing the database host.
     * <p>
     * Метод для инициализации хоста базы данных.
     */
    private void initDefaultDataBaseHost() {
        defaultDatabaseHost = new DefaultDatabaseHost(queryBuilderToolBar.getConnections().getSelectedConnection());
    }

    /**
     * A method for placing buttons in a panel to place buttons.
     * <p>
     * Метод для размещения кнопок в панели для размещения кнопок.
     */
    private void placingButtonsInPanel() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(5, 5, 5, 5).fillHorizontally();
        panelButton.add(buttonAddJoin, gridBagHelper.setXY(0, 0).setMaxWeightX().get());
        panelButton.add(buttonRemoveJoin, gridBagHelper.nextCol().setMaxWeightX().get());
        panelButton.add(buttonClose, gridBagHelper.nextRow().setMaxWeightX().get());
    }

    /**
     * A method for initializing drop-down lists (comboBox).
     * <p>
     * Метод для инициализации выпадающих списков (comboBox).
     */
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

    /**
     * A method for initializing labels.
     * <p>
     * Метод для инициализации меток.
     */
    private void intLabel() {
        labelJoin = WidgetFactory.createLabel(Bundles.get("QueryBuilder.Join.labelJoin"));
        labelLeftTable = WidgetFactory.createLabel(Bundles.get("QueryBuilder.Join.labelLeftTable"));
        labelRightTable = WidgetFactory.createLabel(Bundles.get("QueryBuilder.Join.labelRightTable"));
        labelValuesUnity = WidgetFactory.createLabel(Bundles.get("QueryBuilder.Join.labelValuesUnity"));
    }

    /**
     * The method for initializing JPanel.
     * <p>
     * Метод для инициализации JPanel.
     */
    private void initPanels() {
        panelPlacingComponents = WidgetFactory.createPanel("panelPlacingComponents");
        panelPlacingComponents.setLayout(new GridBagLayout());

        panelButton = WidgetFactory.createPanel("panelButton");
        panelButton.setLayout(new GridBagLayout());
    }

    /**
     * A method for initializing a text field.
     * <p>
     * Метод для инициализации текстового поля.
     */
    private void initTextFields() {
        textFieldValuesUnity = WidgetFactory.createTextField("textFieldValuesUnity");
        textFieldValuesUnity.setToolTipText(Bundles.get("QueryBuilder.Join.toolTipTextEnterValuesUnity"));
        textFieldValuesUnity.setMinimumSize(new Dimension(200, 30));
        textFieldValuesUnity.setPreferredSize(new Dimension(200, 30));
        textFieldValuesUnity.setMaximumSize(new Dimension(200, 30));
    }

    /**
     * A method for initializing the ScrollPane and placing checkboxes on it.
     * <p>
     * Метод для инициализации scrollPane и размещения на ней флажков.
     */
    private void initScrollPane() {
        scrollPaneCheckBoxJoin = new JScrollPane();
        scrollPaneCheckBoxJoin.setPreferredSize(new Dimension(100, 200));
        arrangeCheckBoxesInScrollPane();
    }

    /**
     * A method for placing components as well as setting up a dialog (window).
     * <p>
     * Метод для размещения компонентов а так же настройки диалога (окна).
     */
    private void arrangeComponents() {
        arrangeComponentsInPanelForPlacingComponents();
        configurationDialog();
    }

    /**
     * Configuring the parameters of the dialog (window) created by this class.
     * <p>
     * Настройка параметров диалога (окна) созданного этим классом.
     */
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

    /**
     * The method of placing components on the panel for placing components.
     * <p>
     * Метод размещения компонентов на панели для размещения компонентов.
     */
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

    /**
     * A method that implements the functionality of adding connections (Join) to a query.
     * <p>
     * Метод реализующий функционал добавления соединений (Join) в запрос.
     */
    private void addJoin() {
        if (!comboBoxLeftTable.getSelectedItem().toString().isEmpty()) {
            if (!comboBoxRightTable.getSelectedItem().toString().isEmpty()) {
                if (!comboBoxJoins.getSelectedItem().toString().isEmpty()) {
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

    /**
     * The method for adding connections (Join).
     * <p>
     * Метод для добавления соединений (Join).
     */
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
                return;
            } else {
                return;
            }
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
                return;
            } else {
                return;
            }
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
                return;
            } else {
                return;
            }
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
                return;
            } else {
                return;
            }
        }
    }

    /**
     * A method that implements the functionality of removing connections (Join) from a query.
     * <p>
     * Метод реализующий функционал удаления соединений (Join) из запроса.
     */
    private void removeJoin() {
        JCheckBox[] checkBoxesInScrollPane = getCheckBoxesFromPanelArrangeCheckBox();

        for (int i = 0; i < checkBoxesInScrollPane.length; i++) {
            if (checkBoxesInScrollPane[i].isSelected()) {
                eventRemoveJoin(checkBoxesInScrollPane[i].getText());
                panelPlacingCheckBoxInScrollPane.remove(checkBoxesInScrollPane[i]);
            }
        }

        queryConstructor.setAttributes(queryBuilderPanel.getListTable());
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    /**
     * The method for deleting a connection (Join).
     * <p>
     * Метод для удаления соединения (Join).
     */
    public void eventRemoveJoin(String removeJoinInQuery) {
        StringBuilder stringBuilder = new StringBuilder(queryConstructor.getTable());
        stringBuilder.replace(stringBuilder.indexOf(removeJoinInQuery), stringBuilder.indexOf(removeJoinInQuery) + removeJoinInQuery.length(), "");
        queryBuilderPanel.addStepBackActionInHistory("Set Join " + queryConstructor.getTable());
        queryConstructor.setTable(stringBuilder.toString(), "stepBack");

        ArrayList<String> listNameTable = queryBuilderPanel.getListNameTable();

        if (removeJoinInQuery.contains("FULL OUTER JOIN")) {
            String[] splitJoin = removeJoinInQuery.split(" ");

            for (int i = 0; i < listNameTable.size(); i++) {
                if (splitJoin[3].equals(listNameTable.get(i))) {
                    int userDecision = JOptionPane.showConfirmDialog(queryBuilderPanel, Bundles.get("QueryBuilder.Join.connectionMissing") + listNameTable.get(i) + ".\n" + Bundles.get("QueryBuilder.Join.isDeleteTable"), Bundles.get("QueryBuilder.Join.connectionMissingTitle"), JOptionPane.YES_NO_OPTION);
                    if (userDecision == JOptionPane.YES_OPTION) {
                        removeTable(new JCheckBox(listNameTable.get(i)));
                    }
                }
            }

        } else {
            String[] splitJoin = removeJoinInQuery.split(" ");
            for (int i = 0; i < listNameTable.size(); i++) {
                if (splitJoin[2].equals(listNameTable.get(i))) {
                    int userDecision = JOptionPane.showConfirmDialog(queryBuilderPanel, Bundles.get("QueryBuilder.Join.connectionMissing") + listNameTable.get(i) + ".\n" + Bundles.get("QueryBuilder.Join.isDeleteTable"), Bundles.get("QueryBuilder.Join.connectionMissingTitle"), JOptionPane.YES_NO_OPTION);
                    if (userDecision == JOptionPane.YES_OPTION) {
                        removeTable(new JCheckBox(listNameTable.get(i)));
                    }
                }
            }
        }

    }

    /**
     * A method for getting an array of checkboxes from the panel on which they are placed.
     * <p>
     * Метод для получения массива флажков из панели на которой они размещены.
     */
    private JCheckBox[] getCheckBoxesFromPanelArrangeCheckBox() {
        Component[] component = panelPlacingCheckBoxInScrollPane.getComponents();
        JCheckBox[] checkBoxes = new JCheckBox[panelPlacingCheckBoxInScrollPane.getComponents().length];

        for (int i = 0; i < component.length; i++) {
            checkBoxes[i] = ((JCheckBox) component[i]);
        }

        return checkBoxes;
    }

    /**
     * A method for placing checkboxes on a ScrollPane.
     * <p>
     * Метод для размещения флажков на ScrollPane.
     */
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

    /**
     * A method for displaying columns of a user-selected table on the screen.
     * <p>
     * Метод для вывода колонок выбранной пользователем таблицы  на экран.
     */
    private void showColumnTable(String varLeftOrRight) {
        if (varLeftOrRight.equals("left")) {
            if (!comboBoxLeftTable.getSelectedItem().toString().isEmpty()) {
                StringBuilder stringBuilder = new StringBuilder();

                List<DatabaseColumn> databaseColumnTableOne = defaultDatabaseHost.getTableFromName(comboBoxLeftTable.getSelectedItem().toString()).getColumns();

                for (int i = 0; i < databaseColumnTableOne.size(); i++) {
                    if (i != databaseColumnTableOne.size() - 1) {
                        ColumnData columnData = new ColumnData(queryBuilderToolBar.getConnections().getSelectedConnection(), databaseColumnTableOne.get(i));
                        stringBuilder.append(columnData.getColumnName()).append("\n");
                    } else {
                        ColumnData columnData = new ColumnData(queryBuilderToolBar.getConnections().getSelectedConnection(), databaseColumnTableOne.get(i));
                        stringBuilder.append(columnData.getColumnName());
                    }
                }

                JOptionPane.showMessageDialog(queryBuilderPanel, stringBuilder.toString(), Bundles.get("QueryBuilder.Join.columnTableTitle"), JOptionPane.QUESTION_MESSAGE);
            }
        }

        if (varLeftOrRight.equals("right")) {
            if (!comboBoxRightTable.getSelectedItem().toString().isEmpty()) {
                StringBuilder stringBuilder = new StringBuilder();

                List<DatabaseColumn> databaseColumnTableOne = defaultDatabaseHost.getTableFromName(comboBoxRightTable.getSelectedItem().toString()).getColumns();

                for (int i = 0; i < databaseColumnTableOne.size(); i++) {
                    if (i != databaseColumnTableOne.size() - 1) {
                        ColumnData columnData = new ColumnData(queryBuilderToolBar.getConnections().getSelectedConnection(), databaseColumnTableOne.get(i));
                        stringBuilder.append(columnData.getColumnName()).append("\n");
                    } else {
                        ColumnData columnData = new ColumnData(queryBuilderToolBar.getConnections().getSelectedConnection(), databaseColumnTableOne.get(i));
                        stringBuilder.append(columnData.getColumnName());
                    }
                }

                JOptionPane.showMessageDialog(queryBuilderPanel, stringBuilder.toString(), Bundles.get("QueryBuilder.Join.columnTableTitle"), JOptionPane.QUESTION_MESSAGE);
            }
        }
    }

    /**
     * A method for changing the state of a drop-down list item from non editable to editable.
     * <p>
     * Метод для изменения состояния элемента выпадающего списка с не редактируемого на редактируемый.
     */
    private void addActionListenerInComboBox(JComboBox<String> comboBox) {
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboBox.getSelectedIndex() == 0) {
                    comboBox.setEditable(true);
                } else {
                    comboBox.setEditable(false);
                }
            }
        });
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
        setVisible(false);
        dispose();
    }
}
