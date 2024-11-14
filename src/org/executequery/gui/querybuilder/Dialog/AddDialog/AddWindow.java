package org.executequery.gui.querybuilder.Dialog.AddDialog;

import org.executequery.gui.WidgetFactory;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.executequery.gui.querybuilder.WorkQuryEditor.QueryConstructor;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This class creates a dialog (window) that adds named windows to the query.
 * <p>
 * Этот класс создаёт диалог (окно) который добавляет в запрос именованные окна.
 *
 * @author Krylov Gleb
 */
public class AddWindow extends JDialog {

    // --- Elements accepted using the constructor. ----
    // --- Поля, которые передаются через конструктор. ---

    private QueryConstructor queryConstructor;
    private QueryBuilderPanel queryBuilderPanel;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelForPlacingComponents;
    private JPanel panelArrangeCheckBoxInScrollPane;
    private JLabel labelWindowFunction;
    private JLabel labelAddPartition;
    private JLabel labelAddOrderBy;
    private JLabel labelAddInstructions;
    private JScrollPane scrollPaneForOutputWindowFunctions;
    private JComboBox<String> comboBoxAddPartition;
    private JComboBox<String> comboBoxAddOrderBy;
    private JTextField textFieldAddInstructions;
    private JButton buttonAddWindowInQuery;

    /**
     * A dialog (window) is created to add named windows to the request.
     * <p>
     * Создаётся диалог (окно) для добавления именованных окон в запрос.
     */
    public AddWindow(QueryConstructor createStringQuery, QueryBuilderPanel queryBuilderPanel) {
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
        buttonAddWindowInQuery = WidgetFactory.createButton("buttonAddWindowInQuery", "Добавить", event -> {
            eventButtonAddWindow();
            closeDialog();
        });
    }

    /**
     * A method for initializing a text field.
     * <p>
     * Метод для инициализации текстового поля.
     */
    private void initTextFields() {
        textFieldAddInstructions = WidgetFactory.createTextField("textFieldAddInstructions");
    }

    /**
     * A method for initializing drop-down lists (comboBox).
     * <p>
     * Метод для инициализации выпадающих списков (comboBox).
     */
    private void initComboBoxes() {
        comboBoxAddPartition = WidgetFactory.createComboBox("comboBoxAddPartition", getAttributesUsingTable());
        comboBoxAddPartition.setEditable(true);

        comboBoxAddOrderBy = WidgetFactory.createComboBox("comboBoxAddOrderBy", getAttributesUsingTable());
        comboBoxAddOrderBy.setEditable(true);
    }

    /**
     * A method for initializing the ScrollPane and placing checkboxes on it.
     * <p>
     * Метод для инициализации scrollPane и размещения на ней флажков.
     */
    private void initScrollPane() {
        scrollPaneForOutputWindowFunctions = new JScrollPane();
        scrollPaneForOutputWindowFunctions.setPreferredSize(new Dimension(100, 275));
        arrangeCheckBoxesInScrollPane();
    }

    /**
     * A method for placing checkboxes on a ScrollPane.
     * <p>
     * Метод для размещения флажков на ScrollPane.
     */
    private void arrangeCheckBoxesInScrollPane() {
        panelArrangeCheckBoxInScrollPane = WidgetFactory.createPanel("panelArrangeCheckBoxInScrollPane");
        panelArrangeCheckBoxInScrollPane.setLayout(new BoxLayout(panelArrangeCheckBoxInScrollPane, BoxLayout.Y_AXIS));

        ArrayList<String> historyWindow = queryConstructor.getHistoryWindowAlias();

        for (int i = 0; i < historyWindow.size(); i++) {
            panelArrangeCheckBoxInScrollPane.add(new JCheckBox(historyWindow.get(i)));
        }

        scrollPaneForOutputWindowFunctions.setViewportView(panelArrangeCheckBoxInScrollPane);
    }

    /**
     * A method for initializing labels.
     * <p>
     * Метод для инициализации меток.
     */
    private void initLabels() {
        labelWindowFunction = WidgetFactory.createLabel("Выберите оконную функцию для добавления");
        labelAddPartition = WidgetFactory.createLabel("Добавить партицию");
        labelAddOrderBy = WidgetFactory.createLabel("Добавить сортировку");
        labelAddInstructions = WidgetFactory.createLabel("Добавить модификации");
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
        setTitle("Добавить именованное окно");
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
        panelForPlacingComponents.add(labelWindowFunction, gridBagHelper.setXY(0, 0).setMaxWeightX().setWidth(2).get());
        panelForPlacingComponents.add(scrollPaneForOutputWindowFunctions, gridBagHelper.setXY(0, 1).setMaxWeightX().spanY().setWidth(2).get());
        panelForPlacingComponents.add(labelAddPartition, gridBagHelper.setXY(2, 0).setHeight(1).setWidth(1).setMaxWeightX().setHeight(1).setWidth(1).get());
        panelForPlacingComponents.add(comboBoxAddPartition, gridBagHelper.setXY(2, 1).setMaxWeightX().get());
        panelForPlacingComponents.add(labelAddOrderBy, gridBagHelper.setXY(2, 2).setMaxWeightX().get());
        panelForPlacingComponents.add(comboBoxAddOrderBy, gridBagHelper.setXY(2, 3).setMaxWeightX().get());
        panelForPlacingComponents.add(labelAddInstructions, gridBagHelper.setXY(2, 4).setMaxWeightX().get());
        panelForPlacingComponents.add(textFieldAddInstructions, gridBagHelper.setXY(2, 5).setMaxWeightX().get());
        panelForPlacingComponents.add(buttonAddWindowInQuery, gridBagHelper.setXY(2, 6).setMaxWeightX().setWidth(1).setInsets(10, 10, 10, 5).get());
    }

    /**
     * The button event for adding a named window to the request.
     * <p>
     * Событие кнопки для добавления именованного окна в запрос.
     */
    private void eventButtonAddWindow() {
        StringBuilder stringBuilder = new StringBuilder();
        JCheckBox[] checkBoxesInScrollPane = getCheckBoxesFromPanelArrangeCheckBox();

        for (int i = 0; i < checkBoxesInScrollPane.length; i++) {
            if (checkBoxesInScrollPane[i].isSelected()) {
                if (stringBuilder.toString().isEmpty()) {
                    eventIfFirstCheckBox(stringBuilder, checkBoxesInScrollPane[i]);
                }
                else{
                    eventIfNotFirstCheckBox(stringBuilder, checkBoxesInScrollPane[i]);
                }
            }
        }

        queryConstructor.replaceWindow(stringBuilder.toString());
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
        JOptionPane.showMessageDialog(queryBuilderPanel, "Именованные окна добавлены", "Именованные окна", JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * A method for adding a named window if it is not the first flag (CheckBox).
     * <p>
     * Метод для добавления именованного окна если это не первый флаг (CheckBox).
     */
    private void eventIfNotFirstCheckBox(StringBuilder stringBuilder, JCheckBox checkBoxesInScrollPane) {
        stringBuilder.append(",").append(checkBoxesInScrollPane.getText()).append(" AS ").append("(");
        if(!comboBoxAddPartition.getSelectedItem().toString().isEmpty()){
            stringBuilder.append("PARTITION BY ").append(comboBoxAddPartition.getSelectedItem().toString()).append(" ");
        }
        if(!comboBoxAddOrderBy.getSelectedItem().toString().isEmpty()){
            stringBuilder.append("ORDER BY ").append(comboBoxAddOrderBy.getSelectedItem().toString()).append(" ");
        }
        if(!textFieldAddInstructions.getText().isEmpty()){
            stringBuilder.append(textFieldAddInstructions.getText()).append(" ");
        }
        stringBuilder.append(")");
    }

    /**
     * A method for adding a named window if this is the first flag (CheckBox).
     * <p>
     * Метод для добавления именованного окна если это первый флаг (CheckBox).
     */
    private void eventIfFirstCheckBox(StringBuilder stringBuilder, JCheckBox checkBoxesInScrollPane) {
        stringBuilder.append("WINDOW ").append(checkBoxesInScrollPane.getText()).append(" AS ").append("(");
        if(!comboBoxAddPartition.getSelectedItem().toString().isEmpty()){
            stringBuilder.append("PARTITION BY ").append(comboBoxAddPartition.getSelectedItem().toString()).append(" ");
        }
        if(!comboBoxAddOrderBy.getSelectedItem().toString().isEmpty()){
            stringBuilder.append("ORDER BY ").append(comboBoxAddOrderBy.getSelectedItem().toString()).append(" ");
        }
        if(!textFieldAddInstructions.getText().isEmpty()){
            stringBuilder.append(textFieldAddInstructions.getText()).append(" ");
        }
        stringBuilder.append(")");
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
     * A method for getting attributes of the tables used.
     * <p>
     * Метод для получения атрибутов используемых таблиц.
     */
    private ArrayList<String> getAttributesUsingTable() {
        ArrayList<String> arrayAttributes = new ArrayList<>();
        ArrayList<JTable> tableList = queryBuilderPanel.getListTable();

        arrayAttributes.add("");

        for (int i = 0; i < tableList.size(); i++) {
            for (int j = 0; j < tableList.get(i).getRowCount(); j++) {
                arrayAttributes.add(tableList.get(i).getColumnName(0) + "." + tableList.get(i).getValueAt(j, 0));
            }
        }

        return arrayAttributes;
    }

}
