package org.executequery.gui.querybuilder.buttonPanel;

import org.executequery.gui.WidgetFactory;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.underworldlabs.swing.ConnectionsComboBox;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * A class that creates a dialog (window) for deleting tables.
 * <p>
 * Класс создающий диалог (окно) для удаления таблиц.
 */
public class DialogRemoveTableFromQueryBuilder extends JDialog {

    // --- Elements accepted using the constructor ----
    // --- Поля, которые передаются через конструктор. ---

    private QueryBuilderPanel queryBuilderPanelFromConstructor;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelForPlacingComponents;
    private JPanel panelForAddCheckBoxInScrollPane;
    private JLabel labelConnects;
    private JLabel labelSearch;
    private JButton buttonSearch;
    private JButton buttonCreateDialogForRemoveTables;
    private JTextField textFieldFromSearchTables;
    private JScrollPane scrollPaneForPlacingCheckBoxesTables;
    private ConnectionsComboBox connections;

    /**
     * A dialog (window) is created for deleting tables.
     * Calling the method to initialize the fields.
     * <p>
     * Создаётся диалог (окно) для удаления таблиц.
     * Вызываем метод для инициализации полей.
     */
    public DialogRemoveTableFromQueryBuilder(QueryBuilderPanel queryBuilderPanel){
        this.queryBuilderPanelFromConstructor = queryBuilderPanel;
        init();
    }

    /**
     * Initialize the fields.
     * Calling the component location method.
     * <p>
     * Инициализируйте поля.
     * Вызов метода определения местоположения компонента.
     */
    private void init(){
        panelForAddCheckBoxInScrollPane = new JPanel();
        initPanels();
        initComboBoxes();
        initLabels();
        initTextFields();
        initScrollPaneAndAddCheckBoxes();
        initButtons();
        arrangeComponents();
    }

    private void initButtons() {
        buttonSearch = WidgetFactory.createButton("buttonSearch","Начать поиск");

        buttonCreateDialogForRemoveTables = WidgetFactory.createButton("buttonCreate","Удалить",event -> {
            eventButtonFromRemoveTables();
        });
    }

    private void initTextFields() {
        textFieldFromSearchTables = WidgetFactory.createTextField("textFieldSearch");
    }

    private void initLabels() {
        labelConnects = WidgetFactory.createLabel("Подключение:");
        labelSearch = WidgetFactory.createLabel("Поиск:");
    }

    private void initComboBoxes() {
        connections = WidgetFactory.createConnectionComboBox("connectionBox", true);
        connections.setMaximumSize(new Dimension(200, 30));
    }

    private void initPanels() {
        panelForPlacingComponents = WidgetFactory.createPanel("mainPanel");
        panelForPlacingComponents.setLayout(new GridBagLayout());
        panelForPlacingComponents.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    private void initScrollPaneAndAddCheckBoxes() {
        scrollPaneForPlacingCheckBoxesTables = new JScrollPane();
        panelForAddCheckBoxInScrollPane.setLayout(new BoxLayout(panelForAddCheckBoxInScrollPane,BoxLayout.Y_AXIS));

        scrollPaneForPlacingCheckBoxesTables.setViewportView(panelForAddCheckBoxInScrollPane);
        scrollPaneForPlacingCheckBoxesTables.setPreferredSize(new Dimension(100, 300));

        for(int i = 0; i < queryBuilderPanelFromConstructor.getNamesTablesFromOutputPanel().length;i++){
            panelForAddCheckBoxInScrollPane.add(new JCheckBox(queryBuilderPanelFromConstructor.getNamesTablesFromOutputPanel()[i]));
        }
    }

    /**
     * The button event for deleting tables.
     * <p>
     * Событие кнопки для удаления таблиц.
     */
    private void eventButtonFromRemoveTables() {
        ArrayList<JCheckBox> array = new ArrayList<>();

        for (int i = 0; i < panelForAddCheckBoxInScrollPane.getComponents().length; i++) {
            array.add((JCheckBox) panelForAddCheckBoxInScrollPane.getComponents()[i]);
        }

        for(int i = 0; i < array.size(); i++){
            if(array.get(i).isSelected()){
                queryBuilderPanelFromConstructor.removeTableInInputPanel(array.get(i).getText());
                queryBuilderPanelFromConstructor.removeTableInListTable(array.get(i).getText());
            }
        }

        setVisible(false);
        dispose();
    }

    /**
     * A method for placing components.
     * <p>
     * Метод для размещения компонентов.
     */
    private void arrangeComponents(){
        addComponentsInPanelForPlacingComponents();
        configurationDialog();
    }

    /**
     * A method for configuring the dialog (window) created by the class.
     * <p>
     * Метод для настройки диалога (окна) созданного классом.
     */
    private void configurationDialog() {
        setLayout(new BorderLayout());
        setTitle("Удалить таблицу");
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
     * Adding components to the panel to place components.
     * <p>
     * Добавление компонентов в панель для размещения компонентов.
     */
    private void addComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper();
        gridBagHelper.anchorCenter().setInsets(5,5,5,5);

        panelForPlacingComponents.add(labelConnects,gridBagHelper.setXY(0,0).get());
        panelForPlacingComponents.add(connections,gridBagHelper.setXY(1,0).setMaxWeightX().spanX().fillHorizontally().get());
        panelForPlacingComponents.add(labelSearch,gridBagHelper.setXY(0,1).setMinWeightX().setWidth(1).get());
        panelForPlacingComponents.add(textFieldFromSearchTables,gridBagHelper.setXY(1,1).setMaxWeightX().get());
        panelForPlacingComponents.add(buttonSearch,gridBagHelper.setXY(2,1).setMinWeightX().get());
        panelForPlacingComponents.add(scrollPaneForPlacingCheckBoxesTables,gridBagHelper.setXY(0,2).setMaxWeightX().spanX().get());
        panelForPlacingComponents.add(buttonCreateDialogForRemoveTables,gridBagHelper.setXY(1,3).setMinWeightX().setWidth(1).get());
    }

}
