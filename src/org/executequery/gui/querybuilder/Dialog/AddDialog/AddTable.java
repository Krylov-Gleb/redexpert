package org.executequery.gui.querybuilder.Dialog.AddDialog;

import org.executequery.databasemediators.DatabaseConnection;
import org.executequery.databaseobjects.impl.DefaultDatabaseHost;
import org.executequery.gui.WidgetFactory;
import org.executequery.gui.browser.ConnectionsTreePanel;
import org.executequery.gui.querybuilder.CreatorTableFromQueryBuilder;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.executequery.gui.querybuilder.WorkQuryEditor.QueryConstructor;
import org.underworldlabs.swing.ConnectionsComboBox;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * This class creates a dialog (window) for adding tables to a query.
 * <p>
 * Этот класс создаёт диалог (окно) для добавления таблиц в запрос.
 *
 * @author Krylov Gleb
 */
public class AddTable extends JDialog {

    // --- Elements accepted using the constructor ----
    // --- Поля, которые передаются через конструктор. ---

    private QueryConstructor queryConstructor;
    private QueryBuilderPanel queryBuilderPanel;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelForPlacingComponents;
    private JPanel panelArrangeCheckBoxInScrollPane;
    private JLabel labelConnections;
    private JLabel labelSearch;
    private JButton buttonSearch;
    private JButton buttonAddTable;
    private JTextField textFieldSearch;
    private JScrollPane scrollPaneForOutputTables;
    private ConnectionsComboBox connections;

    // --- Other field ---
    // --- Другие поля ---

    private DefaultDatabaseHost defaultDatabaseHost;

    /**
     * Creating a dialog (window) for adding tables.
     * <p>
     * Создаем диалог (окно) для добавления таблиц.
     */
    public AddTable(QueryBuilderPanel queryBuilderPanel, QueryConstructor queryConstructor) {
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
        initPanels();
        initLabels();
        initButton();
        initTextField();
        initComboBox();
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
        defaultDatabaseHost = new DefaultDatabaseHost(connections.getSelectedConnection());
    }

    /**
     * A method for initializing drop-down lists (comboBox).
     * <p>
     * Метод для инициализации выпадающих списков (comboBox).
     */
    private void initComboBox() {
        connections = WidgetFactory.createConnectionComboBox("connections", true);
        connections.setMaximumSize(new Dimension(200, 30));
    }

    /**
     * A method for initializing a text field.
     * <p>
     * Метод для инициализации текстового поля.
     */
    private void initTextField() {
        textFieldSearch = WidgetFactory.createTextField("textFieldSearch");
    }

    /**
     * The method for initializing JPanel.
     * <p>
     * Метод для инициализации JPanel.
     */
    private void initPanels() {
        panelForPlacingComponents = WidgetFactory.createPanel("panelForPlacingComponents");
        panelForPlacingComponents.setLayout(new GridBagLayout());
        panelForPlacingComponents.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    /**
     * A method for initializing the ScrollPane and placing checkboxes on it.
     * <p>
     * Метод для инициализации scrollPane и размещения на ней флажков.
     */
    private void initScrollPane() {
        scrollPaneForOutputTables = new JScrollPane();
        scrollPaneForOutputTables.setPreferredSize(new Dimension(100, 300));
        addCheckBoxInScrollPane();
    }

    /**
     * A method for initializing labels.
     * <p>
     * Метод для инициализации меток.
     */
    private void initLabels() {
        labelConnections = WidgetFactory.createLabel("Подключение");
        labelSearch = WidgetFactory.createLabel("Поиск");
    }

    /**
     * A method for initializing buttons.
     * <p>
     * Метод для инициализации кнопок.
     */
    private void initButton() {
        buttonSearch = WidgetFactory.createButton("buttonSearch", "Поиск");
        buttonAddTable = WidgetFactory.createButton("buttonAddTable", "Создать", event -> {
            eventButtonAddTable();
            closeDialog();
        });
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
     * A method for placing checkboxes on a ScrollPane.
     * <p>
     * Метод для размещения флажков на ScrollPane.
     */
    private void addCheckBoxInScrollPane() {
        panelArrangeCheckBoxInScrollPane = new JPanel();
        panelArrangeCheckBoxInScrollPane.setLayout(new BoxLayout(panelArrangeCheckBoxInScrollPane, BoxLayout.Y_AXIS));

        scrollPaneForOutputTables.setViewportView(panelArrangeCheckBoxInScrollPane);

        for (int i = 0; i < defaultDatabaseHost.getTableNames().size(); i++) {
            panelArrangeCheckBoxInScrollPane.add(new JCheckBox(defaultDatabaseHost.getTableNames().get(i)));
        }

        scrollPaneForOutputTables.revalidate();
    }

    /**
     * A method for adding tables.
     * <p>
     * Метод для добавления таблиц.
     */
    private void eventButtonAddTable() {
        JCheckBox[] jCheckBoxes = getCheckBoxesFromPanelArrangeCheckBox();

        for (int i = 0; i < jCheckBoxes.length; i++) {
            if (jCheckBoxes[i].isSelected()) {
                CreatorTableFromQueryBuilder tableQueryBuilder = new CreatorTableFromQueryBuilder(getListNamesColumns(jCheckBoxes[i].getText()), jCheckBoxes[i].getText(), queryBuilderPanel, queryConstructor);
                if (!queryBuilderPanel.getListNameTable().contains(tableQueryBuilder.getJTable().getColumnName(0))) {
                    queryBuilderPanel.addTableInListTable(tableQueryBuilder.getJTable());
                    queryBuilderPanel.addTableInInputPanel(tableQueryBuilder.getTable());
                }

            }
        }

        queryConstructor.addTable(queryBuilderPanel.getListTable().get(0).getColumnName(0));

        if (queryConstructor.getHistoryJoinNameTable().isEmpty()) {
            queryConstructor.getHistoryJoinNameTable().add(queryBuilderPanel.getListTable().get(0).getColumnName(0));
        }

        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    /**
     * A method for getting an array of checkboxes from the panel on which they are placed.
     * <p>
     * Метод для получения массива флажков из панели на которой они размещены.
     */
    private JCheckBox[] getCheckBoxesFromPanelArrangeCheckBox() {
        Component[] component = panelArrangeCheckBoxInScrollPane.getComponents();
        JCheckBox[] checkBoxes = new JCheckBox[panelArrangeCheckBoxInScrollPane.getComponents().length];

        for (int i = 0; i < component.length; i++) {
            checkBoxes[i] = ((JCheckBox) component[i]);
        }

        return checkBoxes;
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

    /**
     * A method for configuring the parameters of a dialog (window) created by this class.
     * <p>
     * Метод для настройки параметров диалога (окна) созданного этим классом.
     */
    private void configurationDialog() {
        setLayout(new BorderLayout());
        setTitle("Добавить таблицу");
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setIconImage(new ImageIcon("red_expert.png").getImage());
        setLocationRelativeTo(queryBuilderPanel);
        add(panelForPlacingComponents, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(600, 500);
        setVisible(true);
    }

    /**
     * A method for placing components in a panel for placing components.
     * <p>
     * Метод для размещения компонентов в панели для размещения компонентов.
     */
    private void arrangeComponentsInPanelFromPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(5, 5, 5, 5).fillHorizontally();
        panelForPlacingComponents.add(labelConnections, gridBagHelper.setXY(0, 0).setMinWeightX().get());
        panelForPlacingComponents.add(connections, gridBagHelper.setXY(1, 0).spanX().setMaxWeightX().get());
        panelForPlacingComponents.add(labelSearch, gridBagHelper.setXY(0, 1).setWidth(1).setMinWeightX().get());
        panelForPlacingComponents.add(textFieldSearch, gridBagHelper.setXY(1, 1).setMaxWeightX().get());
        panelForPlacingComponents.add(buttonSearch, gridBagHelper.setXY(2, 1).setMinWeightX().get());
        panelForPlacingComponents.add(scrollPaneForOutputTables, gridBagHelper.setXY(0, 2).spanX().setMaxWeightX().get());
        panelForPlacingComponents.add(buttonAddTable, gridBagHelper.setXY(1, 3).setWidth(1).setMinWeightX().get());
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
