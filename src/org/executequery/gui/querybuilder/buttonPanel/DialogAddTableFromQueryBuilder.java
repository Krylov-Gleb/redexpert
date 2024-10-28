package org.executequery.gui.querybuilder.buttonPanel;

import org.executequery.databasemediators.DatabaseConnection;
import org.executequery.databaseobjects.impl.DefaultDatabaseHost;
import org.executequery.gui.WidgetFactory;
import org.executequery.gui.browser.ConnectionsTreePanel;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.underworldlabs.swing.ConnectionsComboBox;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDialog for adding tables.
 * <p>
 * JDialog (окно) для добавления таблиц.
 *
 * @author Krylov Gleb
 */
public class DialogAddTableFromQueryBuilder extends JDialog {

    // --- Elements accepted using the constructor ----
    // --- Поля, которые передаются через конструктор. ---

    private QueryBuilderPanel queryBuilderPanelFromConstructor;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelForPlacingComponents;
    private JPanel panelArrangeCheckBoxInScrollPane;
    private JLabel labelConnections;
    private JLabel labelSearch;
    private JButton buttonSearch;
    private JButton buttonCreateDialogAddTable;
    private JTextField textFieldFromSearchTables;
    private JScrollPane scrollPaneFromOutputTables;
    private ConnectionsComboBox connections;

    // --- Other field ---
    // --- Другие поля ---

    private DefaultDatabaseHost defaultDatabaseHost;

    /**
     * Creating a dialog (window) for adding tables.
     * <p>
     * Создаем диалог (окно) для добавления таблиц.
     */
    public DialogAddTableFromQueryBuilder(QueryBuilderPanel queryBuilderPanel) {
        this.queryBuilderPanelFromConstructor = queryBuilderPanel;
        init();
    }

    /**
     * Initializing the fields.
     * <p>
     * Инициализируем поля.
     */
    private void init() {
        panelArrangeCheckBoxInScrollPane = new JPanel();
        initPanels();
        initLabels();
        initButton(panelArrangeCheckBoxInScrollPane);
        initTextField();
        initComboBox();
        initDefaultDataBaseHost();
        initScrollPane(panelArrangeCheckBoxInScrollPane);
        arrangeComponent();
    }

    /**
     * Arrange of components
     * <p>
     * Расположение компонентов
     */
    private void arrangeComponent() {
        arrangeComponentsInPanelFromPlacingComponents();
        configurationDialog();
    }

    private void initDefaultDataBaseHost(){
        defaultDatabaseHost = new DefaultDatabaseHost(connections.getSelectedConnection());
    }

    private void initComboBox(){
        connections = WidgetFactory.createConnectionComboBox("connectionBox", true);
        connections.setMaximumSize(new Dimension(200, 30));
    }

    private void initTextField(){
        textFieldFromSearchTables = WidgetFactory.createTextField("textFieldSearch");
    }

    private void initPanels(){
        panelForPlacingComponents = WidgetFactory.createPanel("Add Table Panel");
        panelForPlacingComponents.setLayout(new GridBagLayout());
        panelForPlacingComponents.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    private void initScrollPane(JPanel panelArrangeCheckBoxInScrollPane){
        scrollPaneFromOutputTables = new JScrollPane();
        addCheckBoxInScrollPane(panelArrangeCheckBoxInScrollPane);
    }

    private void initLabels(){
        labelConnections = WidgetFactory.createLabel("Подключение");
        labelSearch = WidgetFactory.createLabel("Поиск");
    }

    private void initButton(JPanel panelArrangeCheckBoxInScrollPane){
        buttonSearch = WidgetFactory.createButton("buttonSearch", "Поиск");
        buttonCreateDialogAddTable = WidgetFactory.createButton("CreateTable", "Создать", event -> {
            createTable(panelArrangeCheckBoxInScrollPane);
        });
    }

    /**
     * Adding CheckBoxes to the scroll bar.
     * <p>
     * Добавление флажков на полосу прокрутки.
     */
    private void addCheckBoxInScrollPane(JPanel panel){
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        scrollPaneFromOutputTables.setViewportView(panel);
        scrollPaneFromOutputTables.setPreferredSize(new Dimension(100, 300));

        for (int i = 0; i < defaultDatabaseHost.getTableNames().size(); i++) {
            panel.add(new JCheckBox(defaultDatabaseHost.getTableNames().get(i)));
        }

        scrollPaneFromOutputTables.revalidate();
    }

    /**
     * Method for creating a table
     * <p>
     * Метод для создания таблицы
     */
    private void createTable(JPanel panel) {
        ArrayList<JCheckBox> array = new ArrayList<>();

        for (int i = 0; i < panel.getComponents().length; i++) {
            array.add((JCheckBox) panel.getComponents()[i]);
        }

        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).isSelected()) {
                CreatorTableFromQueryBuilder tableQueryBuilder = new CreatorTableFromQueryBuilder(getListNamesColumns(array.get(i).getText()), array.get(i).getText());
                queryBuilderPanelFromConstructor.addTableInInputPanel(tableQueryBuilder.getTable());
                queryBuilderPanelFromConstructor.addTableInListTable(tableQueryBuilder.getJTable());
            }
        }

        setVisible(false);
        dispose();
    }

    /**
     * Setting up our dialog (window).
     * <p>
     * Настройка нашего диалога (окна).
     */
    private void configurationDialog() {
        setLayout(new BorderLayout());
        setTitle("Добавить таблицу");
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setIconImage(new ImageIcon("red_expert.png").getImage());
        setLocationRelativeTo(queryBuilderPanelFromConstructor);
        add(panelForPlacingComponents, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(600, 500);
        setVisible(true);
    }

    /**
     * Configuring and adding components to the component placement panel.
     * <p>
     * Настраиваем и добавляем компоненты в панель размещения компонентов
     */
    private void arrangeComponentsInPanelFromPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(5, 5, 5, 5).fillHorizontally();

        panelForPlacingComponents.add(labelConnections, gridBagHelper.setXY(0, 0).setMinWeightX().get());
        panelForPlacingComponents.add(connections, gridBagHelper.setXY(1, 0).spanX().setMaxWeightX().get());
        panelForPlacingComponents.add(labelSearch, gridBagHelper.setXY(0, 1).setWidth(1).setMinWeightX().get());
        panelForPlacingComponents.add(textFieldFromSearchTables, gridBagHelper.setXY(1, 1).setMaxWeightX().get());
        panelForPlacingComponents.add(buttonSearch, gridBagHelper.setXY(2, 1).setMinWeightX().get());
        panelForPlacingComponents.add(scrollPaneFromOutputTables, gridBagHelper.setXY(0, 2).spanX().setMaxWeightX().get());
        panelForPlacingComponents.add(buttonCreateDialogAddTable, gridBagHelper.setXY(1, 3).setWidth(1).setMinWeightX().get());
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
        return getDefaultDatabaseHost(connections.getSelectedConnection()).getColumnNames(table);
    }

}
