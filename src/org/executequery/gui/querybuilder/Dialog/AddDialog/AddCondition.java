package org.executequery.gui.querybuilder.Dialog.AddDialog;

import org.executequery.gui.WidgetFactory;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.executequery.gui.querybuilder.WorkQuryEditor.QueryConstructor;
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
public class AddCondition extends JDialog {

    // --- Elements accepted using the constructor. ----
    // --- Поля, которые передаются через конструктор. ---

    private QueryConstructor queryConstructor;
    private QueryBuilderPanel queryBuilderPanel;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelForPlacingComponents;
    private JPanel panelArrangeCheckBoxInScrollPane;
    private JLabel labelLeftOperand;
    private JLabel labelRightOperand;
    private JLabel labelOperand;
    private JLabel labelJoinCondition;
    private JScrollPane scrollPaneForOutputAttributesLeftOperand;
    private JComboBox<String> comboBoxOperating;
    private JComboBox<String> comboBoxJoinConditions;
    private JTextField textFieldRightOperand;
    private JButton buttonAddConditionInQuery;

    /**
     * A dialog (window) is created to add conditions to the request.
     * A method is used to initialize fields.
     * <p>
     * Создаётся диалог (окно) для добавления условий в запрос.
     * Используется метод для инициализации полей.
     */
    public AddCondition(QueryConstructor createStringQuery, QueryBuilderPanel queryBuilderPanel) {
        this.queryConstructor = createStringQuery;
        this.queryBuilderPanel = queryBuilderPanel;
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
        initScrollPane();
        initComboBoxes();
        initTextFields();
        initButtons();
        arrangeComponent();
    }

    /**
     * A method for initializing buttons.
     * <p>
     * Метод для инициализации кнопок.
     */
    private void initButtons() {
        buttonAddConditionInQuery = WidgetFactory.createButton("buttonAddConditionInQuery", "Добавить", event -> {
            eventButtonAddConditions();
            closeDialog();
        });
    }

    /**
     * A method for initializing a text field.
     * <p>
     * Метод для инициализации текстового поля.
     */
    private void initTextFields() {
        textFieldRightOperand = WidgetFactory.createTextField("textFieldRightOperand");
    }

    /**
     * A method for initializing drop-down lists (comboBox).
     * <p>
     * Метод для инициализации выпадающих списков (comboBox).
     */
    private void initComboBoxes() {
        comboBoxOperating = WidgetFactory.createComboBox("comboBoxOperating", new String[]{"", "=", "<>", "!=", ">", ">=", "<", "<=", "BETWEEN", "IN", "LIKE"});
        comboBoxOperating.setEditable(true);

        comboBoxJoinConditions = WidgetFactory.createComboBox("comboBoxJoinConditions", new String[]{"OR", "AND"});
        comboBoxJoinConditions.setEditable(true);
    }

    /**
     * A method for initializing the ScrollPane and placing checkboxes on it.
     * <p>
     * Метод для инициализации scrollPane и размещения на ней флажков.
     */
    private void initScrollPane() {
        scrollPaneForOutputAttributesLeftOperand = new JScrollPane();
        scrollPaneForOutputAttributesLeftOperand.setPreferredSize(new Dimension(100, 275));
        arrangeCheckBoxesInScrollPane();
    }

    /**
     * A method for placing checkboxes on a ScrollPane.
     * <p>
     * Метод для размещения флажков на ScrollPane.
     */
    private void arrangeCheckBoxesInScrollPane() {
        panelArrangeCheckBoxInScrollPane = new JPanel();
        panelArrangeCheckBoxInScrollPane.setLayout(new BoxLayout(panelArrangeCheckBoxInScrollPane, BoxLayout.Y_AXIS));

        String[] arrayAttributes = queryConstructor.getAttribute().split(",");

        if (!queryConstructor.getAttribute().isEmpty()) {
            for (int i = 0; i < arrayAttributes.length; i++) {
                panelArrangeCheckBoxInScrollPane.add(new JCheckBox(arrayAttributes[i]));
            }
        }

        scrollPaneForOutputAttributesLeftOperand.setViewportView(panelArrangeCheckBoxInScrollPane);
    }

    /**
     * A method for initializing labels.
     * <p>
     * Метод для инициализации меток.
     */
    private void initLabels() {
        labelLeftOperand = WidgetFactory.createLabel("Выберите левый операнд");
        labelRightOperand = WidgetFactory.createLabel("Введите правый операнд");
        labelOperand = WidgetFactory.createLabel("Выберите операцию");
        labelJoinCondition = WidgetFactory.createLabel("Выберите соединения для условий");
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
     * A method for placing components as well as setting up a dialog (window).
     * <p>
     * Метод для размещения компонентов а так же настройки диалога (окна).
     */
    private void arrangeComponent() {
        arrangeComponentsInPanelForAddConditionInQuery();
        configurationDialog();
    }

    /**
     * A method for configuring the parameters of a dialog (window) created by this class.
     * <p>
     * Метод для настройки параметров диалога (окна) созданного этим классом.
     */
    private void configurationDialog() {
        setLayout(new BorderLayout());
        add(panelForPlacingComponents, BorderLayout.CENTER);
        setTitle("Добавить условия");
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
    private void arrangeComponentsInPanelForAddConditionInQuery() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(10, 5, 10, 5).fillHorizontally();
        panelForPlacingComponents.add(labelLeftOperand, gridBagHelper.setXY(0, 0).setMaxWeightX().setWidth(2).get());
        panelForPlacingComponents.add(scrollPaneForOutputAttributesLeftOperand, gridBagHelper.setXY(0, 1).setMaxWeightX().spanY().setWidth(2).get());
        panelForPlacingComponents.add(labelOperand, gridBagHelper.setXY(2, 0).setHeight(1).setWidth(1).setMaxWeightX().setHeight(1).setWidth(1).get());
        panelForPlacingComponents.add(comboBoxOperating, gridBagHelper.setXY(2, 1).setMaxWeightX().get());
        panelForPlacingComponents.add(labelRightOperand, gridBagHelper.setXY(2, 2).setMaxWeightX().get());
        panelForPlacingComponents.add(textFieldRightOperand, gridBagHelper.setXY(2, 3).setMaxWeightX().get());
        panelForPlacingComponents.add(labelJoinCondition, gridBagHelper.setXY(2, 4).setMaxWeightX().get());
        panelForPlacingComponents.add(comboBoxJoinConditions, gridBagHelper.setXY(2, 5).setMaxWeightX().get());
        panelForPlacingComponents.add(buttonAddConditionInQuery, gridBagHelper.setXY(2, 6).setMinWeightX().setWidth(1).setInsets(10, 10, 10, 5).get());
    }

    /**
     * The event of the button to remove the condition from the request.
     * <p>
     * Событие кнопки по удалению условия из запроса.
     */
    private void eventButtonAddConditions() {
        JCheckBox[] checkBoxes = getCheckBoxesFromPanelArrangeCheckBox();

        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isSelected()) {
                queryConstructor.addWhere(checkBoxes[i].getText(), comboBoxOperating.getSelectedItem().toString(), textFieldRightOperand.getText(), comboBoxJoinConditions.getSelectedItem().toString());
            }
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
}
