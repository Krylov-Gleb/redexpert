package org.executequery.gui.querybuilder.Dialog.AddDialog;

import org.executequery.gui.WidgetFactory;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.executequery.gui.querybuilder.WorkQuryEditor.QueryConstructor;
import org.underworldlabs.swing.ConnectionsComboBox;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * A class for creating a dialog (window) to add to the sorting request (OrderBy).
 * <p>
 * Класс создания диалога (окна) для добавления в запрос сортировки (OrderBy).
 *
 * @author Krylov Gleb
 */
public class AddOrderBy extends JDialog{

    // --- Elements accepted using the constructor. ----
    // --- Поля, которые передаются через конструктор. ---

    private QueryConstructor queryConstructor;
    private QueryBuilderPanel queryBuilderPanel;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelForPlacingComponents;
    private JPanel panelArrangeCheckBoxesInScrollPane;
    private JLabel labelConnections;
    private JLabel labelSearch;
    private JLabel labelAddAscAndDesc;
    private JScrollPane scrollPanePlacingCheckBoxesAttributes;
    private JComboBox<String> comboBoxAddAscDesc;
    private JButton buttonAddOrderBy;
    private JButton buttonSearch;
    private ConnectionsComboBox connections;
    private JTextField textFieldSearch;
    private ArrayList<String> queueAttributes;

    /**
     * A dialog (window) is created to add sorting (OrderBy) to the query.
     * <p>
     * Создаётся диалог (окно) для добавления сортировки (OrderBy) в запрос.
     */
    public AddOrderBy(QueryBuilderPanel queryBuilderPanel, QueryConstructor createStringQuery){
        this.queryBuilderPanel = queryBuilderPanel;
        this.queryConstructor = createStringQuery;
        init();
    }

    /**
     * A method for initializing fields.
     * <p>
     * Метод для инициализации полей.
     */
    private void init(){
        initPanel();
        initArrays();
        initLabel();
        initTextField();
        initComboBox();
        initButton();
        initScrollPane();
        arrangeComponents();
    }

    /**
     * The method for initializing JPanel.
     * <p>
     * Метод для инициализации JPanel.
     */
    private void initPanel(){
        panelForPlacingComponents = WidgetFactory.createPanel("panelForPlacingComponents");
        panelForPlacingComponents.setLayout(new GridBagLayout());
        panelForPlacingComponents.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        panelArrangeCheckBoxesInScrollPane = WidgetFactory.createPanel("panelArrangeCheckBoxesInScrollPane");
        panelArrangeCheckBoxesInScrollPane.setLayout(new BoxLayout(panelArrangeCheckBoxesInScrollPane,BoxLayout.Y_AXIS));
    }

    /**
     * A method for initializing labels.
     * <p>
     * Метод для инициализации меток.
     */
    private void initLabel(){
        labelConnections = WidgetFactory.createLabel("Подключение:");
        labelSearch = WidgetFactory.createLabel("Поиск:");
        labelAddAscAndDesc = WidgetFactory.createLabel("Добавить:");
    }

    /**
     * A method for initializing the ScrollPane and placing checkboxes on it.
     * <p>
     * Метод для инициализации scrollPane и размещения на ней флажков.
     */
    private void initScrollPane(){
        scrollPanePlacingCheckBoxesAttributes = new JScrollPane();
        scrollPanePlacingCheckBoxesAttributes.setPreferredSize(new Dimension(200,300));
        arrangeCheckBoxesOnScrollPane();
    }

    /**
     * A method for initializing buttons.
     * <p>
     * Метод для инициализации кнопок.
     */
    private void initButton(){
        buttonSearch = WidgetFactory.createButton("buttonSearch","Поиск",event -> {});
        buttonAddOrderBy = WidgetFactory.createButton("buttonAddOrderBy","Добавить",event -> {
            buttonEventAddOrderByInQuery();
            closeDialog();
        });
    }

    /**
     * A method for initializing a text field.
     * <p>
     * Метод для инициализации текстового поля.
     */
    private void initTextField(){
        textFieldSearch = WidgetFactory.createTextField("textFieldSearch");
    }

    /**
     * A method for initializing arrays.
     * <p>
     * Метод для инициализации массивов.
     */
    private void initArrays(){
        queueAttributes = new ArrayList<>();
    }

    /**
     * A method for initializing drop-down lists (comboBox).
     * <p>
     * Метод для инициализации выпадающих списков (comboBox).
     */
    private void initComboBox(){
        comboBoxAddAscDesc = WidgetFactory.createComboBox("comboBoxAddAscDesc",new String[]{"ASC","DESC"});
        connections = WidgetFactory.createConnectionComboBox("connections", true);
        connections.setMaximumSize(new Dimension(200, 30));
    }

    /**
     * A method for placing components as well as setting up a dialog (window).
     * <p>
     * Метод для размещения компонентов а так же настройки диалога (окна).
     */
    private void arrangeComponents(){
        arrangeComponentsInPanelForPlacingComponents();
        configurationDialog();
    }

    /**
     * A method for placing CheckBoxes on a ScrollPane.
     * <p>
     * Метод для размещения CheckBoxes на ScrollPane.
     */
    private void arrangeCheckBoxesOnScrollPane(){
        ArrayList<JTable> listTables = queryBuilderPanel.getListTable();

        for(int i = 0; i < listTables.size(); i++){
            if(queryConstructor.getValueJoin().contains(listTables.get(i).getColumnName(0)) || queryConstructor.getValuesTable().contains(listTables.get(i).getColumnName(0))){
                for(int j = 0; j < listTables.get(i).getRowCount(); j++){
                    JCheckBox checkBox = new JCheckBox((String)listTables.get(i).getValueAt(j,0));
                    String nameTable = listTables.get(i).getColumnName(0);
                    addItemListenerInCheckBox(checkBox, nameTable);
                    panelArrangeCheckBoxesInScrollPane.add(checkBox);
                }
            }
        }
        scrollPanePlacingCheckBoxesAttributes.setViewportView(panelArrangeCheckBoxesInScrollPane);
    }

    /**
     * Method for setting the value change event in JCheckBox.
     * <p>
     * Метод для установки события смены значения в JCheckBox.
     */
    private void addItemListenerInCheckBox(JCheckBox checkBox,String nameTable){
        checkBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(checkBox.isSelected()){
                    queueAttributes.add(nameTable + "." + checkBox.getText());
                }
                if(!checkBox.isSelected()){
                    queueAttributes.remove(nameTable + "." + checkBox.getText());
                }
            }
        });
    }

    /**
     * The method of the button event for adding sorting to the query.
     * <p>
     * Метод события кнопки для добавления сортировки в запрос.
     */
    private void buttonEventAddOrderByInQuery(){
        queryConstructor.addOrderBy(queueAttributes,comboBoxAddAscDesc.getSelectedItem().toString());
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    /**
     * A method for placing components in a panel for placing components.
     * <p>
     * Метод для размещения компонентов в панели для размещения компонентов.
     */
    private void arrangeComponentsInPanelForPlacingComponents(){
        GridBagHelper gridBagHelper = new GridBagHelper().anchorCenter().fillHorizontally().setInsets(5,5,5,5);
        panelForPlacingComponents.add(labelConnections,gridBagHelper.setXY(0,0).setMinWeightX().get());
        panelForPlacingComponents.add(connections,gridBagHelper.setXY(1,0).setMaxWeightX().spanX().get());
        panelForPlacingComponents.add(labelSearch,gridBagHelper.setXY(0,1).setMinWeightX().setWidth(1).get());
        panelForPlacingComponents.add(textFieldSearch,gridBagHelper.setXY(1,1).setMaxWeightX().get());
        panelForPlacingComponents.add(buttonSearch,gridBagHelper.setXY(2,1).setMinWeightX().get());
        panelForPlacingComponents.add(scrollPanePlacingCheckBoxesAttributes,gridBagHelper.setXY(0,2).setWidth(3).setMaxWeightX().get());
        panelForPlacingComponents.add(labelAddAscAndDesc,gridBagHelper.setXY(0,3).setMinWeightX().setWidth(1).get());
        panelForPlacingComponents.add(comboBoxAddAscDesc,gridBagHelper.setXY(1,3).setMaxWeightX().spanX().get());
        panelForPlacingComponents.add(buttonAddOrderBy,gridBagHelper.setXY(1,4).setMinWeightX().setWidth(1).spanY().get());
    }

    /**
     * A method for configuring the parameters of a dialog (window) created by this class.
     * <p>
     * Метод для настройки параметров диалога (окна) созданного этим классом.
     */
    private void configurationDialog(){
        setLayout(new BorderLayout());
        add(panelForPlacingComponents, BorderLayout.CENTER);
        setTitle("Добавить сортировку");
        setIconImage(new ImageIcon("red_expert.png").getImage());
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(700, 530);
        setVisible(true);
    }

    /**
     * The method for closing the dialog (window).
     * <p>
     * Метод для закрытия диалога (окна).
     */
    private void closeDialog(){
        setVisible(false);
        dispose();
    }

}
