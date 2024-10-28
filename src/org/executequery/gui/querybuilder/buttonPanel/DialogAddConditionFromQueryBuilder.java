package org.executequery.gui.querybuilder.buttonPanel;

import org.executequery.gui.WidgetFactory;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.executequery.gui.querybuilder.WorkQuryEditor.CreateStringQuery;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;

/**
 * This class creates a dialog (window) that adds conditions to the request.
 * <p>
 * Этот класс создаёт диалог (окно) который добавляет в запрос условия.
 *
 * @author Krylov Gleb
 */
public class DialogAddConditionFromQueryBuilder extends JDialog {

    // --- Elements accepted using the constructor. ----
    // --- Поля, которые передаются через конструктор. ---

    private CreateStringQuery createStringQueryConstructor;
    private QueryBuilderPanel queryBuilderPanelConstructor;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelForPlacingComponents;
    private JLabel labelSettingsLeftOperand;
    private JLabel labelSettingsRightOperand;
    private JLabel labelSettingsOperation;
    private JLabel labelSettingJoinsConditions;
    private JLabel labelRemoveCondition;
    private JComboBox<String> comboBoxSettingLeftOperand;
    private JComboBox<String> comboBoxSettingOperation;
    private JComboBox<String> comboBoxSettingJoinsConditions;
    private JComboBox<String> comboBoxRemoveCondition;
    private JButton buttonAddConditionsInQuery;
    private JButton buttonRemoveConditionsInQuery;
    private JTextField rightOperandTextField;

    /**
     * I am creating a dialog (window) to add conditions to the request.
     * I use the method to initialize the fields.
     * <p>
     * Создаю диалог (окно) для добавления условий в запрос.
     * Использую метод для инициализации полей.
     */
    public DialogAddConditionFromQueryBuilder(CreateStringQuery createStringQuery, QueryBuilderPanel queryBuilderPanel){
        this.createStringQueryConstructor = createStringQuery;
        this.queryBuilderPanelConstructor = queryBuilderPanel;
        init();
    }

    /**
     * A method for initializing fields.
     * It calls a method for placing components.
     * <p>
     * Метод для инициализации полей.
     * В нём вызывается метод для размещения компонентов.
     */
    private void init(){
        initPanels();
        initLabels();
        initComboBoxes();
        initTextFields();
        initButtons();
        arrangeComponent();
    }

    private void initButtons() {
        buttonAddConditionsInQuery = WidgetFactory.createButton("Create Condition Panel","Создать",event -> {
            eventButtonAddConditions();
        });

        buttonRemoveConditionsInQuery = WidgetFactory.createButton("Remove Condition Button","Создать",event -> {
            eventButtonRemoveConditions();
        });
    }

    private void initTextFields() {
        rightOperandTextField = WidgetFactory.createTextField("Right Operand TextField");
        rightOperandTextField.setPreferredSize(new Dimension(100, 30));
    }

    private void initComboBoxes() {
        comboBoxSettingLeftOperand = WidgetFactory.createComboBox("Left Operand ComboBox",createStringQueryConstructor.getAttribute().split(","));
        comboBoxSettingLeftOperand.setPreferredSize(new Dimension(100, 30));

        comboBoxSettingOperation = WidgetFactory.createComboBox("Operation ComboBox",new String[]{"=","<>","!=",">","<",">=","<=","BETWEEN","IN","LIKE"});
        comboBoxSettingOperation.setPreferredSize(new Dimension(100, 30));

        comboBoxSettingJoinsConditions = WidgetFactory.createComboBox("If Condition No One ComboBox",new String[]{"AND","OR"});

        comboBoxRemoveCondition = WidgetFactory.createComboBox("Condition Remove",createStringQueryConstructor.getHistoryCondition());
        comboBoxRemoveCondition.setPreferredSize(new Dimension(200,30));


    }

    private void initLabels() {
        labelSettingsLeftOperand = WidgetFactory.createLabel("Укажите левый операнд:");

        labelSettingsRightOperand = WidgetFactory.createLabel("Укажите правый операнд");

        labelSettingsOperation = WidgetFactory.createLabel("Выберите операцию");

        labelSettingJoinsConditions = WidgetFactory.createLabel("Cвязь между условиями:");

        labelRemoveCondition = WidgetFactory.createLabel("Выберите условие для удаления:");
    }

    private void initPanels() {
        panelForPlacingComponents = WidgetFactory.createPanel("Condition Panel");
        panelForPlacingComponents.setLayout(new GridBagLayout());
        panelForPlacingComponents.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    /**
     * A method for placing components.
     * <p>
     * Метод для размещения компонентов.
     */
    private void arrangeComponent(){
        arrangeComponentsInPanelFroPlacingComponents();
        configurationDialog();
    }

    /**
     * A method for configuring the parameters of a dialog (window) created by this class.
     * <p>
     * Метод для настройки параметров диалога (окна) созданного этим классом.
     */
    private void configurationDialog() {
        setLayout(new BorderLayout());
        add(panelForPlacingComponents,BorderLayout.CENTER);
        setTitle("Условия");
        setIconImage(new ImageIcon("red_expert.png").getImage());
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(600, 400);
        setVisible(true);
    }

    /**
     * A method for placing components in a panel for placing components.
     * <p>
     * Метод для размещения компонентов в панели для размещения компонентов.
     */
    private void arrangeComponentsInPanelFroPlacingComponents() {
        panelForPlacingComponents.add(labelSettingsLeftOperand,new GridBagHelper().setX(0).setY(0).anchorCenter().setInsets(5,5,5,5).get());
        panelForPlacingComponents.add(comboBoxSettingLeftOperand,new GridBagHelper().setX(0).setY(10).anchorCenter().setInsets(5,5,5,5).get());
        panelForPlacingComponents.add(labelSettingsRightOperand,new GridBagHelper().setX(20).setY(0).anchorCenter().setInsets(5,5,5,5).get());
        panelForPlacingComponents.add(rightOperandTextField,new GridBagHelper().setX(20).setY(10).anchorCenter().setInsets(5,5,5,5).get());
        panelForPlacingComponents.add(labelSettingsOperation,new GridBagHelper().setX(10).setY(0).anchorCenter().setInsets(5,5,5,5).get());
        panelForPlacingComponents.add(comboBoxSettingOperation,new GridBagHelper().setX(10).setY(10).anchorCenter().setInsets(5,5,5,5).get());
        panelForPlacingComponents.add(labelSettingJoinsConditions,new GridBagHelper().setX(10).setY(30).anchorCenter().setInsets(5,10,5,5).get());
        panelForPlacingComponents.add(comboBoxSettingJoinsConditions,new GridBagHelper().setX(10).setY(40).anchorCenter().setInsets(5,5,5,5).get());
        panelForPlacingComponents.add(buttonAddConditionsInQuery,new GridBagHelper().setX(10).setY(50).anchorCenter().setInsets(5,5,5,5).get());
        panelForPlacingComponents.add(labelRemoveCondition,new GridBagHelper().setX(10).setY(60).anchorCenter().setInsets(5,5,5,5).get());
        panelForPlacingComponents.add(comboBoxRemoveCondition,new GridBagHelper().setX(10).setY(70).anchorCenter().setInsets(5,5,5,5).get());
        panelForPlacingComponents.add(buttonRemoveConditionsInQuery,new GridBagHelper().setX(10).setY(80).anchorCenter().setInsets(5,5,5,5).get());
    }

    /**
     * The event of the button for adding conditions to the request.
     * <p>
     * Событие кнопки по добавлению условий в запрос.
     */
    private void eventButtonRemoveConditions() {
        if(!comboBoxRemoveCondition.getSelectedItem().toString().isEmpty()){
            createStringQueryConstructor.removeWhere(comboBoxRemoveCondition.getSelectedItem().toString());
            queryBuilderPanelConstructor.setTextInPanelOutputTestingQuery(createStringQueryConstructor.getQuery());
        }
    }

    /**
     * The event of the button to remove the condition from the request.
     * <p>
     * Событие кнопки по удалению условия из запроса.
     */
    private void eventButtonAddConditions() {
        if(!rightOperandTextField.getText().isEmpty()) {
            createStringQueryConstructor.addWhere(comboBoxSettingLeftOperand.getSelectedItem().toString(), comboBoxSettingOperation.getSelectedItem().toString(), rightOperandTextField.getText(), comboBoxSettingJoinsConditions.getSelectedItem().toString());
            queryBuilderPanelConstructor.setTextInPanelOutputTestingQuery(createStringQueryConstructor.getQuery());
        }
        else{
            JOptionPane.showMessageDialog(new JFrame(),"Неполное условие.\nДобавьте пожалуйста текст в правый операнд!","Неполное условие",JOptionPane.ERROR_MESSAGE);
        }
    }



}
