package org.executequery.gui.querybuilder.buttonPanel;

import org.executequery.gui.WidgetFactory;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.executequery.gui.querybuilder.WorkQuryEditor.CreateStringQuery;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;

/**
 * A class for creating a dialog (window) that adds functions to a request.
 * <p>
 * Класс для создания диалога (окна) который добавляет функции в запрос.
 */
public class DialogAddFunctionsFromQueryBuilder extends JDialog {

    // --- Elements accepted using the constructor. ----
    // --- Поля, которые передаются через конструктор. ---

    private QueryBuilderPanel queryBuilderPanelConstructor;
    private CreateStringQuery createStringQueryConstructor;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelForPlacingComponents;
    private JLabel labelAttributes;
    private JLabel labelFunctions;
    private JComboBox<String> comboBoxAttributes;
    private JTextField textFieldFunction;
    private JCheckBox checkBoxCreateNewFunction;
    private JCheckBox checkBoxUsingThisFunctions;
    private JButton buttonAddFunctionsInQuery;

    /**
     * Creating a dialog (window) to add functions to the request.
     * <p>
     * Создаём диалог (окно) для добавления функций в запрос.
     */
    public DialogAddFunctionsFromQueryBuilder(QueryBuilderPanel queryBuilderPanel, CreateStringQuery createStringQuery){
        this.queryBuilderPanelConstructor = queryBuilderPanel;
        this.createStringQueryConstructor = createStringQuery;
        init();
    }

    /**
     * A method for initializing fields.
     * <p>
     * Метод для инициализации полей.
     */
    private void init(){
        initPanels();
        initLabels();
        initComboBox();
        initTextFields();
        initCheckBox();
        initButtons();
        arrangeComponents();
    }

    /**
     * A method for placing components.
     * <p>
     * Метод для размещения компонентов.
     */
    private void arrangeComponents(){
        arrangeComponentsInPanelForPlacingComponents();
        configurationDialog();
    }

    private void initButtons() {
        buttonAddFunctionsInQuery = WidgetFactory.createButton("Create Function Button","Применить",event -> {
            addFunctionsInQuery();
        });
    }

    private void initCheckBox() {
        checkBoxCreateNewFunction = WidgetFactory.createCheckBox("Create New Functions","Создать новый атрибут");
        checkBoxUsingThisFunctions = WidgetFactory.createCheckBox("Using This Functions","Использовать этот");
    }

    private void initComboBox() {
        comboBoxAttributes = WidgetFactory.createComboBox("Using Attributes ComboBox",createStringQueryConstructor.getAttribute().split(","));
        comboBoxAttributes.setPreferredSize(new Dimension(200,30));
    }

    private void initTextFields() {
        textFieldFunction = WidgetFactory.createTextField("Using Functions");
        textFieldFunction.setPreferredSize(new Dimension(200,30));
    }

    private void initLabels() {
        labelAttributes = WidgetFactory.createLabel("Выберите используемый атрибут");
        labelFunctions = WidgetFactory.createLabel("Напишите название функции");
    }

    private void initPanels() {
        panelForPlacingComponents = WidgetFactory.createPanel("Functions Panel");
    }

    /**
     * A method for configuring the parameters of a dialog (window) created by this class.
     * <p>
     * Метод для настройки параметров диалога (окна) созданного этим классом.
     */
    private void configurationDialog() {
        setLayout(new BorderLayout());
        add(panelForPlacingComponents, BorderLayout.CENTER);
        setTitle("Установите функции");
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
    private void arrangeComponentsInPanelForPlacingComponents() {
        panelForPlacingComponents.setLayout(new GridBagLayout());
        panelForPlacingComponents.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panelForPlacingComponents.add(labelAttributes,new GridBagHelper().setX(0).setY(0).anchorCenter().setInsets(5,5,5,5).get());
        panelForPlacingComponents.add(comboBoxAttributes,new GridBagHelper().setX(0).setY(10).anchorCenter().setInsets(5,5,5,5).get());
        panelForPlacingComponents.add(labelFunctions,new GridBagHelper().setX(0).setY(20).anchorCenter().setInsets(5,5,5,5).get());
        panelForPlacingComponents.add(textFieldFunction,new GridBagHelper().setX(0).setY(30).anchorCenter().setInsets(5,5,5,5).get());
        panelForPlacingComponents.add(checkBoxCreateNewFunction,new GridBagHelper().setX(0).setY(40).anchorCenter().setInsets(5,5,5,5).get());
        panelForPlacingComponents.add(checkBoxUsingThisFunctions,new GridBagHelper().setX(0).setY(50).anchorCenter().setInsets(5,5,5,5).get());
        panelForPlacingComponents.add(buttonAddFunctionsInQuery,new GridBagHelper().setX(0).setY(60).anchorCenter().setInsets(0,5,0,5).get());
    }

    /**
     * The button event method for adding functions to a request.
     * <p>
     * Метод события кнопки для добавления функций в запрос.
     */
    private void addFunctionsInQuery() {
        StringBuilder stringBuilder = new StringBuilder("");

        if(!textFieldFunction.getText().isEmpty()){
            if(checkBoxCreateNewFunction.isSelected() & !checkBoxUsingThisFunctions.isSelected()){
                stringBuilder.append(",").append(textFieldFunction.getText()).append("(").append(comboBoxAttributes.getSelectedItem().toString()).append(")");
                StringBuilder stringBuilder1 = new StringBuilder(createStringQueryConstructor.getAttribute());
                stringBuilder1.insert(stringBuilder1.indexOf(comboBoxAttributes.getSelectedItem().toString())+comboBoxAttributes.getSelectedItem().toString().length(),stringBuilder.toString());
                createStringQueryConstructor.replaceAttribute(stringBuilder1.toString());
                queryBuilderPanelConstructor.setTextInPanelOutputTestingQuery(createStringQueryConstructor.getQuery());
            }
            if(checkBoxUsingThisFunctions.isSelected() & !checkBoxCreateNewFunction.isSelected()){
                stringBuilder.append(textFieldFunction.getText()).append("(").append(comboBoxAttributes.getSelectedItem().toString()).append(")");
                StringBuilder stringBuilder1 = new StringBuilder(createStringQueryConstructor.getAttribute());
                stringBuilder1.replace(stringBuilder1.indexOf(comboBoxAttributes.getSelectedItem().toString()),stringBuilder1.indexOf(comboBoxAttributes.getSelectedItem().toString()) + comboBoxAttributes.getSelectedItem().toString().length(),stringBuilder.toString());
                createStringQueryConstructor.replaceAttribute(stringBuilder1.toString());
                queryBuilderPanelConstructor.setTextInPanelOutputTestingQuery(createStringQueryConstructor.getQuery());
            }
        }

        closeDialog();
    }

    /**
     * A method for closing a dialog (window) created by this class.
     * <p>
     * Метод для закрытия диалога (окна) созданного этим классом.
     */
    private void closeDialog() {
        setVisible(false);
        dispose();
    }



}
