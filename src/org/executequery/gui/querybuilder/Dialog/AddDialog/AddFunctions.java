package org.executequery.gui.querybuilder.Dialog.AddDialog;

import org.executequery.gui.WidgetFactory;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.executequery.gui.querybuilder.WorkQuryEditor.QueryConstructor;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * A class for creating a dialog (window) that adds functions to a request.
 * <p>
 * Класс для создания диалога (окна) который добавляет функции в запрос.
 *
 * @author Krylov Gleb
 */
public class AddFunctions extends JDialog {

    // --- Elements accepted using the constructor. ----
    // --- Поля, которые передаются через конструктор. ---

    private QueryConstructor queryConstructor;
    private QueryBuilderPanel queryBuilderPanel;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelForPlacingComponents;
    private JPanel panelArrangeCheckBoxesInScrollPane;
    private JLabel labelAttributes;
    private JLabel labelFunctions;
    private JLabel labelPartitionWindowFunctions;
    private JLabel labelOrderByWindowFunctions;
    private JLabel labelInstructionsWindowFunctions;
    private JLabel labelAddAliasWindowFunctions;
    private JLabel labelAddAliasWindow;
    private JScrollPane scrollPaneAttributes;
    private JTextField textFieldFunction;
    private JTextField textFieldAlias;
    private JTextField textFieldWindowAlias;
    private JTextField textFieldInstructionsWindowsFunctions;
    private JCheckBox checkBoxIsCreateNewFunction;
    private JCheckBox checkBoxIsUsingThisFunctions;
    private JCheckBox checkBoxIsUsingWindowFunctions;
    private JComboBox<String> comboBoxPartitionsWindowFunctions;
    private JComboBox<String> comboBoxOrderByWindowFunctions;
    private JButton buttonAddFunctionsInQuery;

    /**
     * Creating a dialog (window) to add functions to the request.
     * <p>
     * Создаём диалог (окно) для добавления функций в запрос.
     */
    public AddFunctions(QueryBuilderPanel queryBuilderPanel, QueryConstructor createStringQuery) {
        this.queryBuilderPanel = queryBuilderPanel;
        this.queryConstructor = createStringQuery;
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
        initTextFields();
        initCheckBox();
        initComboBox();
        initButtons();
        arrangeComponents();
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
     * A method for initializing buttons.
     * <p>
     * Метод для инициализации кнопок.
     */
    private void initButtons() {
        buttonAddFunctionsInQuery = WidgetFactory.createButton("buttonAddFunctionsInQuery", "Применить", event -> {
            eventButtonFunctionsInQuery();
            closeDialog();
        });
    }

    /**
     * A method for initializing drop-down lists (comboBox).
     * <p>
     * Метод для инициализации выпадающих списков (comboBox).
     */
    private void initCheckBox() {
        checkBoxIsCreateNewFunction = WidgetFactory.createCheckBox("checkBoxIsCreateNewFunction", "Создать новый атрибут");
        checkBoxIsUsingThisFunctions = WidgetFactory.createCheckBox("checkBoxIsUsingThisFunctions", "Использовать этот");

        checkBoxIsUsingWindowFunctions = WidgetFactory.createCheckBox("checkBoxUsingWindowFunctions", "Использовать оконную функцию");
        checkBoxIsUsingWindowFunctions.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (checkBoxIsUsingWindowFunctions.isSelected()) {
                    eventCheckBoxesIsSelected();
                }
                if (!checkBoxIsUsingWindowFunctions.isSelected()) {
                    eventCheckBoxesIsNotSelected();
                }
            }
        });
    }

    /**
     * The CheckBox event if from is highlighted.
     * <p>
     * Событие CheckBox если от выделен.
     */
    private void eventCheckBoxesIsSelected() {
        comboBoxPartitionsWindowFunctions.setEnabled(true);
        comboBoxOrderByWindowFunctions.setEnabled(true);
        textFieldWindowAlias.setEnabled(true);
        textFieldInstructionsWindowsFunctions.setEnabled(true);

    }

    /**
     * The CheckBoxes event if from is not highlighted.
     * <p>
     * Событие CheckBoxes если от не выделен.
     */
    private void eventCheckBoxesIsNotSelected() {
        comboBoxPartitionsWindowFunctions.setSelectedIndex(0);
        comboBoxOrderByWindowFunctions.setSelectedIndex(0);
        textFieldInstructionsWindowsFunctions.setText("");
        textFieldWindowAlias.setText("");
        comboBoxPartitionsWindowFunctions.setEnabled(false);
        comboBoxOrderByWindowFunctions.setEnabled(false);
        textFieldWindowAlias.setEnabled(false);
        textFieldInstructionsWindowsFunctions.setEnabled(false);

    }

    /**
     * A method for initializing the ScrollPane and placing checkboxes on it.
     * <p>
     * Метод для инициализации scrollPane и размещения на ней флажков.
     */
    private void initScrollPane() {
        scrollPaneAttributes = new JScrollPane();
        scrollPaneAttributes.setPreferredSize(new Dimension(200, 557));
        arrangeCheckBoxOnScrollPane();
    }

    /**
     * A method for initializing a text field.
     * <p>
     * Метод для инициализации текстового поля.
     */
    private void initTextFields() {
        textFieldFunction = WidgetFactory.createTextField("textFieldFunction");
        textFieldFunction.setPreferredSize(new Dimension(200, 30));

        textFieldAlias = WidgetFactory.createTextField("textFieldAlias");
        textFieldAlias.setPreferredSize(new Dimension(200, 30));
        textFieldAlias.setEnabled(true);

        textFieldWindowAlias = WidgetFactory.createTextField("textFieldWindowAlias");
        textFieldWindowAlias.setPreferredSize(new Dimension(200, 30));
        textFieldWindowAlias.setEnabled(false);

        textFieldInstructionsWindowsFunctions = WidgetFactory.createTextField("textFieldInstructionsWindowsFunctions");
        textFieldInstructionsWindowsFunctions.setPreferredSize(new Dimension(200, 30));
        textFieldInstructionsWindowsFunctions.setEnabled(false);
    }

    /**
     * A method for initializing labels.
     * <p>
     * Метод для инициализации меток.
     */
    private void initLabels() {
        labelAttributes = WidgetFactory.createLabel("Выберите используемый атрибут");
        labelFunctions = WidgetFactory.createLabel("Напишите название функции");
        labelPartitionWindowFunctions = WidgetFactory.createLabel("Добавить партицию в оконную функцию");
        labelOrderByWindowFunctions = WidgetFactory.createLabel("Добавить сортировку в оконную функцию");
        labelInstructionsWindowFunctions = WidgetFactory.createLabel("Добавить инструкцию в оконную функцию");
        labelAddAliasWindow = WidgetFactory.createLabel("Добавить псевдоним именованного окна");
        labelAddAliasWindowFunctions = WidgetFactory.createLabel("Добавить псевдоним");
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
     * A method for initializing drop-down lists (comboBox).
     * <p>
     * Метод для инициализации выпадающих списков (comboBox).
     */
    private void initComboBox() {
        comboBoxPartitionsWindowFunctions = WidgetFactory.createComboBox("comboBoxPartitionsWindowFunctions", getAttributesUsingTable());
        comboBoxPartitionsWindowFunctions.setEnabled(false);

        comboBoxOrderByWindowFunctions = WidgetFactory.createComboBox("comboBoxOrderByWindowFunctions", getAttributesUsingTable());
        comboBoxOrderByWindowFunctions.setEnabled(false);
    }

    /**
     * A method for configuring the parameters of a dialog (window) created by this class.
     * <p>
     * Метод для настройки параметров диалога (окна) созданного этим классом.
     */
    private void configurationDialog() {
        setLayout(new BorderLayout());
        add(panelForPlacingComponents, BorderLayout.CENTER);
        setTitle("Добавить функции");
        setIconImage(new ImageIcon("red_expert.png").getImage());
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(850, 680);
        setVisible(true);
    }

    /**
     * A method for placing components in a panel for placing components.
     * <p>
     * Метод для размещения компонентов в панели для размещения компонентов.
     */
    private void arrangeComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().setInsets(5, 5, 5, 5).anchorNorth().fillHorizontally();
        panelForPlacingComponents.add(labelAttributes, gridBagHelper.setXY(0, 0).setMaxWeightX().get());
        panelForPlacingComponents.add(scrollPaneAttributes, gridBagHelper.setXY(0, 1).setMaxWeightX().setHeight(15).get());
        panelForPlacingComponents.add(labelFunctions, gridBagHelper.setXY(1, 0).setMaxWeightX().setHeight(1).get());
        panelForPlacingComponents.add(textFieldFunction, gridBagHelper.setXY(1, 1).get());
        panelForPlacingComponents.add(checkBoxIsCreateNewFunction, gridBagHelper.setXY(1, 2).get());
        panelForPlacingComponents.add(checkBoxIsUsingThisFunctions, gridBagHelper.setXY(1, 3).get());
        panelForPlacingComponents.add(checkBoxIsUsingWindowFunctions, gridBagHelper.setXY(1, 4).get());
        panelForPlacingComponents.add(labelPartitionWindowFunctions, gridBagHelper.setXY(1, 5).get());
        panelForPlacingComponents.add(comboBoxPartitionsWindowFunctions, gridBagHelper.setXY(1, 6).get());
        panelForPlacingComponents.add(labelOrderByWindowFunctions, gridBagHelper.setXY(1, 7).get());
        panelForPlacingComponents.add(comboBoxOrderByWindowFunctions, gridBagHelper.setXY(1, 8).get());
        panelForPlacingComponents.add(labelInstructionsWindowFunctions, gridBagHelper.setXY(1, 9).get());
        panelForPlacingComponents.add(textFieldInstructionsWindowsFunctions, gridBagHelper.setXY(1, 10).get());
        panelForPlacingComponents.add(labelAddAliasWindow, gridBagHelper.setXY(1, 11).get());
        panelForPlacingComponents.add(textFieldWindowAlias, gridBagHelper.setXY(1, 12).get());
        panelForPlacingComponents.add(labelAddAliasWindowFunctions, gridBagHelper.setXY(1, 13).get());
        panelForPlacingComponents.add(textFieldAlias, gridBagHelper.setXY(1, 14).get());
        panelForPlacingComponents.add(buttonAddFunctionsInQuery, gridBagHelper.setXY(1, 15).setInsets(5, 15, 5, 5).spanY().get());
    }

    /**
     * The button event method for adding functions to a request.
     * <p>
     * Метод события кнопки для добавления функций в запрос.
     */
    private void eventButtonFunctionsInQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        JCheckBox[] checkBoxes = getCheckBoxesListFromScrollPane();

        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isSelected()) {
                if (!textFieldFunction.getText().isEmpty()) {
                    if (checkBoxIsCreateNewFunction.isSelected() & !checkBoxIsUsingThisFunctions.isSelected()) {
                        eventIfSelectedCreateNewFunction(checkBoxes[i]);
                    }
                    if (checkBoxIsUsingThisFunctions.isSelected() & !checkBoxIsCreateNewFunction.isSelected()) {
                        eventIfSelectedUsingThisFunctions(checkBoxes[i]);
                    }
                }
            }
            stringBuilder.replace(0, stringBuilder.length(), "");
        }
    }

    /**
     * Event if the user has chosen to use existing attributes to create functions.
     * <p>
     * Событие если пользователь выбрал использовать существующие атрибуты для создания функций.
     */
    private void eventIfSelectedUsingThisFunctions(JCheckBox checkBoxes) {
        StringBuilder stringBuilder = new StringBuilder();

        if (checkBoxIsUsingWindowFunctions.isSelected()) {
            stringBuilder.append(textFieldFunction.getText()).append("(").append(checkBoxes.getText()).append(") ").append("OVER").append(" ");
            eventIfUsingWindowFunctions(stringBuilder);
        } else {
            stringBuilder.append(textFieldFunction.getText()).append("(").append(checkBoxes.getText()).append(")");
            eventIfNotUsingWindowFunctions(stringBuilder);
        }
        StringBuilder stringBuilder1 = new StringBuilder(queryConstructor.getAttribute());
        stringBuilder1.replace(stringBuilder1.indexOf(checkBoxes.getText()), stringBuilder1.indexOf(checkBoxes.getText()) + checkBoxes.getText().length(), stringBuilder.toString());
        replaceAttributeAndSetTextTestingQuery(stringBuilder1);
    }

    /**
     * Event if the user has chosen to create new attributes to create functions.
     * <p>
     * Событие если пользователь выбрал создать новые атрибуты для создания функций.
     */
    private void eventIfSelectedCreateNewFunction(JCheckBox checkBoxes) {

        StringBuilder stringBuilder = new StringBuilder();

        if (checkBoxIsUsingWindowFunctions.isSelected()) {
            stringBuilder.append(",").append(textFieldFunction.getText()).append("(").append(checkBoxes.getText()).append(") ").append("OVER").append(" ");
            eventIfUsingWindowFunctions(stringBuilder);
        } else {
            stringBuilder.append(",").append(textFieldFunction.getText()).append("(").append(checkBoxes.getText()).append(")");
            eventIfNotUsingWindowFunctions(stringBuilder);
        }
        StringBuilder stringBuilder1 = new StringBuilder(queryConstructor.getAttribute());
        stringBuilder1.insert(stringBuilder1.indexOf(checkBoxes.getText()) + checkBoxes.getText().length(), stringBuilder.toString());
        replaceAttributeAndSetTextTestingQuery(stringBuilder1);
    }

    /**
     * A method for overwriting attribute values in a query and using the query in a field for test outputs.
     * <p>
     * Метод для перезаписи значений атрибутов в запросе и использование запроса в поле для тестовых выводов.
     */
    private void replaceAttributeAndSetTextTestingQuery(StringBuilder stringBuilder) {
        queryConstructor.replaceAttribute(stringBuilder.toString());
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    /**
     * Actions if the user will use the window function.
     * <p>
     * Действия если пользователь будет использовать оконную функцию.
     */
    private void eventIfUsingWindowFunctions(StringBuilder stringBuilder) {
        if (textFieldWindowAlias.getText().isEmpty()) {

            stringBuilder.append("(");

            if (!comboBoxPartitionsWindowFunctions.getSelectedItem().toString().isEmpty()) {
                stringBuilder.append("PARTITION BY ").append(comboBoxPartitionsWindowFunctions.getSelectedItem().toString()).append(" ");
            }
            if (!comboBoxOrderByWindowFunctions.getSelectedItem().toString().isEmpty()) {
                stringBuilder.append("ORDER BY ").append(comboBoxOrderByWindowFunctions.getSelectedItem().toString()).append(" ");
            }
            if (!textFieldInstructionsWindowsFunctions.getText().isEmpty()) {
                stringBuilder.append(textFieldInstructionsWindowsFunctions.getText());
            }

            stringBuilder.append(")");
        }

        if (!textFieldWindowAlias.getText().isEmpty()) {
            stringBuilder.append(textFieldWindowAlias.getText());
            if (!queryConstructor.getHistoryWindowAlias().contains(textFieldWindowAlias.getText())) {
                queryConstructor.addHistoryWindowAlias(textFieldWindowAlias.getText());
            }
        }

        if (!textFieldAlias.getText().isEmpty()) {
            stringBuilder.append(" AS \"").append(textFieldAlias.getText()).append("\"");
        }

        queryConstructor.addHistoryFunction(stringBuilder.toString());
    }

    /**
     * Actions if the user will not use the window function.
     * <p>
     * Действия если пользователь не будет использовать оконную функцию.
     */
    private void eventIfNotUsingWindowFunctions(StringBuilder stringBuilder) {
        if (!textFieldAlias.getText().isEmpty()) {
            stringBuilder.append(" AS \"").append(textFieldAlias.getText()).append("\"");
        }

        queryConstructor.addHistoryFunction(stringBuilder.toString());
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
     * A method for placing checkboxes on a ScrollPane.
     * <p>
     * Метод для размещения флажков на ScrollPane.
     */
    private void arrangeCheckBoxOnScrollPane() {
        panelArrangeCheckBoxesInScrollPane = WidgetFactory.createPanel("panelArrangeCheckBoxesInScrollPane");
        panelArrangeCheckBoxesInScrollPane.setLayout(new BoxLayout(panelArrangeCheckBoxesInScrollPane, BoxLayout.Y_AXIS));

        String[] listAttributes = queryConstructor.getListAttributes();

        for (int i = 0; i < listAttributes.length; i++) {
            panelArrangeCheckBoxesInScrollPane.add(new JCheckBox(listAttributes[i]));
        }

        scrollPaneAttributes.setViewportView(panelArrangeCheckBoxesInScrollPane);
    }

    /**
     * A method for getting an array of checkboxes from the panel on which they are placed.
     * <p>
     * Метод для получения массива флажков из панели на которой они размещены.
     */
    private JCheckBox[] getCheckBoxesListFromScrollPane() {
        Component[] components = panelArrangeCheckBoxesInScrollPane.getComponents();
        JCheckBox[] checkBoxes = new JCheckBox[components.length];

        for (int i = 0; i < components.length; i++) {
            checkBoxes[i] = (JCheckBox) components[i];
        }

        return checkBoxes;
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
