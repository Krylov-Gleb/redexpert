package org.executequery.gui.querybuilder.Dialog.RemoveDialog;

import org.executequery.gui.WidgetFactory;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.executequery.gui.querybuilder.WorkQuryEditor.QueryConstructor;
import org.underworldlabs.swing.ConnectionsComboBox;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This class creates a dialog (window) to remove sorting from the query.
 * <p>
 * Этот класс создаёт диалог (окно) для удаления сортировки из запроса.
 */
public class RemoveOrderBy extends JDialog {

    // --- Elements accepted using the constructor ----
    // --- Поля, которые передаются через конструктор. ---

    private QueryConstructor queryConstructor;
    private QueryBuilderPanel queryBuilderPanel;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelForPlacingComponents;
    private JPanel panelArrangeCheckBoxInScrollPane;
    private JLabel labelConnects;
    private JLabel labelSearch;
    private JButton buttonSearch;
    private JButton buttonRemoveOrderBy;
    private JTextField textFieldSearch;
    private JScrollPane scrollPanePlacingCheckBoxes;
    private ConnectionsComboBox connections;

    public RemoveOrderBy(QueryBuilderPanel queryBuilderPanel, QueryConstructor queryConstructor) {
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
        initLabel();
        initButtons();
        initTextFields();
        initComboBoxes();
        intiScrollPane();
        arrangeComponents();
    }

    /**
     * A method for initializing the ScrollPane and placing checkboxes on it.
     * <p>
     * Метод для инициализации scrollPane и размещения на ней флажков.
     */
    private void intiScrollPane() {
        scrollPanePlacingCheckBoxes = new JScrollPane();
        scrollPanePlacingCheckBoxes.setPreferredSize(new Dimension(100, 300));
        arrangeCheckBoxesInScrollPane();
    }

    /**
     * A method for initializing labels.
     * <p>
     * Метод для инициализации меток.
     */
    private void initLabel() {
        labelConnects = WidgetFactory.createLabel("Подключение:");
        labelSearch = WidgetFactory.createLabel("Поиск:");
    }

    /**
     * A method for initializing drop-down lists (comboBox).
     * <p>
     * Метод для инициализации выпадающих списков (comboBox).
     */
    private void initComboBoxes() {
        connections = WidgetFactory.createConnectionComboBox("connections", true);
        connections.setMaximumSize(new Dimension(200, 30));
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
     * A method for initializing a text field.
     * <p>
     * Метод для инициализации текстового поля.
     */
    private void initTextFields() {
        textFieldSearch = WidgetFactory.createTextField("textFieldSearch");
    }

    /**
     * A method for initializing buttons.
     * <p>
     * Метод для инициализации кнопок.
     */
    private void initButtons() {
        buttonSearch = WidgetFactory.createButton("buttonSearch", "Начать поиск");
        buttonRemoveOrderBy = WidgetFactory.createButton("buttonRemoveOrderBy", "Удалить", event -> {
            eventButtonFromRemoveOrderBy();
            closeDialog();
        });
    }

    /**
     * A method for placing components as well as setting up a dialog (window).
     * <p>
     * Метод для размещения компонентов а так же настройки диалога (окна).
     */
    private void arrangeComponents() {
        addComponentsInPanelForPlacingComponents();
        configurationDialog();
    }

    /**
     * A method for configuring the parameters of a dialog (window) created by this class.
     * <p>
     * Метод для настройки параметров диалога (окна) созданного этим классом.
     */
    private void configurationDialog() {
        setLayout(new BorderLayout());
        setTitle("Удалить сортировку");
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
    private void addComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorCenter().setInsets(5, 5, 5, 5);
        panelForPlacingComponents.add(labelConnects, gridBagHelper.setXY(0, 0).get());
        panelForPlacingComponents.add(connections, gridBagHelper.setXY(1, 0).setMaxWeightX().spanX().fillHorizontally().get());
        panelForPlacingComponents.add(labelSearch, gridBagHelper.setXY(0, 1).setMinWeightX().setWidth(1).get());
        panelForPlacingComponents.add(textFieldSearch, gridBagHelper.setXY(1, 1).setMaxWeightX().get());
        panelForPlacingComponents.add(buttonSearch, gridBagHelper.setXY(2, 1).setMinWeightX().get());
        panelForPlacingComponents.add(scrollPanePlacingCheckBoxes, gridBagHelper.setXY(0, 2).setMaxWeightX().spanX().get());
        panelForPlacingComponents.add(buttonRemoveOrderBy, gridBagHelper.setXY(1, 3).setMinWeightX().setWidth(1).get());
    }

    /**
     * A method for placing checkboxes on a ScrollPane.
     * <p>
     * Метод для размещения флажков на ScrollPane.
     */
    private void arrangeCheckBoxesInScrollPane() {
        panelArrangeCheckBoxInScrollPane = WidgetFactory.createPanel("panelArrangeCheckBoxInScrollPane");
        panelArrangeCheckBoxInScrollPane.setLayout(new BoxLayout(panelArrangeCheckBoxInScrollPane, BoxLayout.Y_AXIS));

        ArrayList<String> historyOrderBy = queryConstructor.getHistoryOrderByAttributes();

        for (int i = 0; i < historyOrderBy.size(); i++) {
            panelArrangeCheckBoxInScrollPane.add(new JCheckBox(historyOrderBy.get(i)));
        }

        scrollPanePlacingCheckBoxes.setViewportView(panelArrangeCheckBoxInScrollPane);
    }

    /**
     * A method for adding sorting to a query.
     * <p>
     * Метод для добавления сортировки в запрос.
     */
    private void eventButtonFromRemoveOrderBy() {
        StringBuilder stringBuilder = new StringBuilder(queryConstructor.getOrderBy());
        JCheckBox[] checkBoxesInScrollPane = getCheckBoxesFromPanelArrangeCheckBox();

        for (int i = 0; i < checkBoxesInScrollPane.length; i++) {
            if (checkBoxesInScrollPane[i].isSelected()) {
                if (stringBuilder.indexOf(checkBoxesInScrollPane[i].getText()) > 0) {
                    stringBuilder.replace(stringBuilder.indexOf(checkBoxesInScrollPane[i].getText()),
                            stringBuilder.indexOf(checkBoxesInScrollPane[i].getText()) + checkBoxesInScrollPane[i].getText().length(),
                            "");
                }

                queryConstructor.getHistoryOrderByAttributes().remove(checkBoxesInScrollPane[i].getText());
                eventIfOrderByNotEmpty(stringBuilder);
                eventIfOrderByEmpty(stringBuilder);
            }
        }

        queryConstructor.replaceOrderBy(stringBuilder.toString());
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
        JOptionPane.showMessageDialog(queryBuilderPanel, "Аттрибуты сортировки удалены", "Сортировка", JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Method (event) if OrderBy is empty.
     * <p>
     * Метод (событие) если OrderBy пустой.
     */
    private void eventIfOrderByEmpty(StringBuilder stringBuilder) {
        if (queryConstructor.getHistoryOrderByAttributes().isEmpty()) {
            if (stringBuilder.indexOf("ORDER BY") > 0) {
                stringBuilder.replace(stringBuilder.indexOf("ORDER BY"), stringBuilder.indexOf("ORDER BY") + "ORDER BY".length(), "");
            }

            if (stringBuilder.indexOf("ASC") > 0) {
                stringBuilder.replace(stringBuilder.indexOf("ASC"), stringBuilder.indexOf("ASC") + "ASC".length(), "");
            }

            if (stringBuilder.indexOf("DESC") > 0) {
                stringBuilder.replace(stringBuilder.indexOf("DESC"), stringBuilder.indexOf("DESC") + "DESC".length(), "");
            }
        }
    }

    /**
     * Method (event) if OrderBy is not empty.
     * <p>
     * Метод (событие) если OrderBy не пустой.
     */
    private void eventIfOrderByNotEmpty(StringBuilder stringBuilder) {
        if(!queryConstructor.getHistoryOrderByAttributes().isEmpty()) {
            String lastAttribute = queryConstructor.getHistoryOrderByAttributes().get(queryConstructor.getHistoryOrderByAttributes().size() - 1);

            if (lastAttribute.charAt(lastAttribute.length() - 1) == ',') {
                StringBuilder stringBuilderLastAttribute = new StringBuilder(lastAttribute);
                stringBuilderLastAttribute.deleteCharAt(lastAttribute.length() - 1);
                queryConstructor.getHistoryOrderByAttributes().set(queryConstructor.getHistoryOrderByAttributes().size() - 1, stringBuilderLastAttribute.toString());

                if (stringBuilder.indexOf(",", stringBuilder.indexOf(stringBuilderLastAttribute.toString())) > 0) {
                    stringBuilder.deleteCharAt(stringBuilder.indexOf(",", stringBuilder.indexOf(stringBuilderLastAttribute.toString())));
                }
            }
        }
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
