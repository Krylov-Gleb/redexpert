package org.executequery.gui.querybuilder.buttonPanel;

import org.executequery.gui.WidgetFactory;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.executequery.gui.querybuilder.WorkQuryEditor.CreateStringQuery;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;

/**
 * A class that creates a dialog (window) for adding first,skip,distinct.
 * <p>
 * Класс создающий диалог (окно) для добавления first,skip,distinct.
 *
 * @author Krylov Gleb
 */
public class DialogAddFirstSkipDistinctFromQueryBuilder extends JDialog {

    // --- Elements accepted using the constructor. ----
    // --- Поля, которые передаются через конструктор. ---

    private CreateStringQuery createStringQueryConstructor;
    private QueryBuilderPanel queryBuilderPanelConstructor;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelForPlacingComponents;
    private JLabel labelSettingFirst;
    private JLabel labelSettingSkip;
    private JTextField textFieldFromSettingFirst;
    private JTextField textFieldFromSettingSkip;
    private JCheckBox checkBoxSettingDistinct;
    private JButton buttonAddFirstSkipDistinctInQuery;

    /**
     * A dialog (window) is created to add first,skip,distinct to the request.
     * The initialization method is used.
     * <p>
     * Создаётся диалог (окно) для добавления first,skip,distinct в запрос.
     * Используется метод для инициализации.
     */
    public DialogAddFirstSkipDistinctFromQueryBuilder(CreateStringQuery createStringQuery, QueryBuilderPanel queryBuilderPanel) {
        this.createStringQueryConstructor = createStringQuery;
        this.queryBuilderPanelConstructor = queryBuilderPanel;
        init();
    }

    /**
     * A method for initializing fields.
     * A method is used to place the components.
     * <p>
     * Метод для  инициализации полей.
     * Используется метод для размещения компонентов.
     */
    private void init() {
        initPanels();
        initLabels();
        initTextField();
        initCheckBoxes();
        initButtons();
        arrangeComponents();
    }

    private void initButtons() {
        buttonAddFirstSkipDistinctInQuery = WidgetFactory.createButton("Create Select Setting Panel", "Создать", event -> {
            eventButtonAddFirstSkipDistinct();
        });
    }

    private void initCheckBoxes() {
        checkBoxSettingDistinct = WidgetFactory.createCheckBox("distinctCheckBox","DISTINCT");
    }

    private void initTextField() {
        textFieldFromSettingFirst = WidgetFactory.createTextField("Input First Select");
        textFieldFromSettingSkip = WidgetFactory.createTextField("Input Skip Select");
    }

    private void initLabels() {
        labelSettingFirst = WidgetFactory.createLabel("Укажите число (FIRST)");
        labelSettingSkip = WidgetFactory.createLabel("Укажите число (SKIP)");
    }

    private void initPanels() {
        panelForPlacingComponents = WidgetFactory.createPanel("Additional Select Settings Panel");
        panelForPlacingComponents.setLayout(new GridBagLayout());
        panelForPlacingComponents.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    /**
     * A method for placing components.
     * <p>
     * Метод для размещения компонентов.
     */
    private void arrangeComponents() {
        arrangeComponentsInPanelForPlacingComponents();
        configurationDialog();
    }

    /**
     * Configuring the dialog (window) created by this class.
     * <p>
     * Настройка диалога (окна) созданного этим классом.
     */
    private void configurationDialog() {
        setLayout(new BorderLayout());
        setTitle("Дополнительные настройки");
        setIconImage(new ImageIcon("red_expert.png").getImage());
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(panelForPlacingComponents, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(400, 200);
        setVisible(true);
    }

    /**
     * Placing components in a panel to place components.
     * <p>
     * Размещение компонентов в панели для размещения компонентов.
     */
    private void arrangeComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorCenter().setInsets(5,5,5,5).fillHorizontally();

        panelForPlacingComponents.add(labelSettingFirst, gridBagHelper.setXY(0,0).setMinWeightX().get());
        panelForPlacingComponents.add(textFieldFromSettingFirst, gridBagHelper.setXY(1,0).setMaxWeightX().setWidth(2).get());
        panelForPlacingComponents.add(labelSettingSkip, gridBagHelper.setXY(0,1).setMinWeightX().setWidth(1).get());
        panelForPlacingComponents.add(textFieldFromSettingSkip, gridBagHelper.setXY(1,1).setMaxWeightX().setWidth(2).get());
        panelForPlacingComponents.add(checkBoxSettingDistinct,gridBagHelper.setXY(1,2).setMinWeightX().setWidth(1).get());
        panelForPlacingComponents.add(buttonAddFirstSkipDistinctInQuery, gridBagHelper.setXY(1,3).setMinWeightX().get());
    }

    /**
     * A button event that implements adding first skip distinct to the request.
     * <p>
     * Событие кнопки реализующее добавления first skip distinct в запрос.
     */
    private void eventButtonAddFirstSkipDistinct() {
        addFirst();
        addSkip();
        addDistinct();
        setTextInQueryBuilderEditorText();
        closeDialog();
    }

    /**
     * A method for changing the text on the test query display screen in the query designer.
     * <p>
     * Метод для смены текста на экране отображения тестовых запросов в конструкторе запросов.
     */
    private void setTextInQueryBuilderEditorText() {
        queryBuilderPanelConstructor.setTextInPanelOutputTestingQuery(createStringQueryConstructor.getQuery());
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

    /**
     * A method for adding Distinct to a query.
     * <p>
     * Метод для добавления Distinct в запрос.
     */
    private void addDistinct() {
        if(checkBoxSettingDistinct.isSelected()){
            createStringQueryConstructor.setDistinct("DISTINCT");
        }
        else{
            createStringQueryConstructor.setDistinct("ALL");
        }
    }

    /**
     * The method for adding skip to the request.
     * <p>
     * Метод для добавления skip в запрос.
     */
    private void addSkip(){
        String skip = "";

        if (!textFieldFromSettingSkip.getText().isEmpty()) {
            try {
                Integer.parseInt(textFieldFromSettingSkip.getText());
                skip = textFieldFromSettingSkip.getText();
            } catch (NumberFormatException numberFormatException) {

            }

        } else {
            skip = "";
        }

        createStringQueryConstructor.addSkip(skip);
    }

    /**
     * The method for adding first to the request.
     * <p>
     * Метод для добавления first в запрос.
     */
    private void addFirst(){
        String first = "";

        if (!textFieldFromSettingFirst.getText().isEmpty()) {
            try {
                Integer.parseInt(textFieldFromSettingFirst.getText());
                first = textFieldFromSettingFirst.getText();
            } catch (NumberFormatException numberFormatException) {

            }

        } else {
            first = "";
        }

        createStringQueryConstructor.addFirst(first);
    }


}
