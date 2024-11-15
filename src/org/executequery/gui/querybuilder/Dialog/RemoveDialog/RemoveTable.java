package org.executequery.gui.querybuilder.Dialog.RemoveDialog;

import org.executequery.gui.WidgetFactory;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.executequery.gui.querybuilder.WorkQuryEditor.QueryConstructor;
import org.underworldlabs.swing.ConnectionsComboBox;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;

/**
 * A class that creates a dialog (window) for deleting tables.
 * <p>
 * Класс создающий диалог (окно) для удаления таблиц.
 *
 * @author Krylov Gleb
 */
public class RemoveTable extends JDialog {

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
    private JButton buttonRemoveTables;
    private JTextField textFieldSearch;
    private JScrollPane scrollPanePlacingCheckBoxes;
    private ConnectionsComboBox connections;

    /**
     * A dialog (window) is created to delete tables from the query.
     * <p>
     * Создаётся диалог (окно) для удаления таблиц из запроса.
     */
    public RemoveTable(QueryBuilderPanel queryBuilderPanel, QueryConstructor queryConstructor) {
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
        initComboBoxes();
        initLabels();
        initTextFields();
        initScrollPane();
        initButtons();
        arrangeComponents();
    }

    /**
     * A method for initializing buttons.
     * <p>
     * Метод для инициализации кнопок.
     */
    private void initButtons() {
        buttonSearch = WidgetFactory.createButton("buttonSearch", "Начать поиск");
        buttonRemoveTables = WidgetFactory.createButton("buttonRemoveTables", "Удалить", event -> {
            eventButtonRemoveTables();
            closeDialog();
        });
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
     * A method for initializing labels.
     * <p>
     * Метод для инициализации меток.
     */
    private void initLabels() {
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
     * A method for initializing the ScrollPane and placing checkboxes on it.
     * <p>
     * Метод для инициализации scrollPane и размещения на ней флажков.
     */
    private void initScrollPane() {
        scrollPanePlacingCheckBoxes = new JScrollPane();
        scrollPanePlacingCheckBoxes.setPreferredSize(new Dimension(100, 300));
        addCheckBoxesInScrollPane();
    }

    /**
     * A method for placing checkboxes on a ScrollPane.
     * <p>
     * Метод для размещения флажков на ScrollPane.
     */
    private void addCheckBoxesInScrollPane() {
        panelArrangeCheckBoxInScrollPane = WidgetFactory.createPanel("panelArrangeCheckBoxInScrollPane");
        panelArrangeCheckBoxInScrollPane.setLayout(new BoxLayout(panelArrangeCheckBoxInScrollPane, BoxLayout.Y_AXIS));

        String[] historyNameTable = queryBuilderPanel.getNamesTablesFromOutputPanel();

        for (int i = 0; i < historyNameTable.length; i++) {
            panelArrangeCheckBoxInScrollPane.add(new JCheckBox(historyNameTable[i]));
        }

        scrollPanePlacingCheckBoxes.setViewportView(panelArrangeCheckBoxInScrollPane);
    }

    /**
     * A method for deleting tables.
     * <p>
     * Метод для удаления таблиц.
     */
    private void eventButtonRemoveTables() {
        JCheckBox[] checkBoxes = getCheckBoxesFromPanelArrangeCheckBox();

        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isSelected()) {

                queryBuilderPanel.removeTableInInputPanel(checkBoxes[i].getText());
                queryBuilderPanel.removeTableInListTable(checkBoxes[i].getText());
                queryConstructor.removeJoinNameTableInHistory(checkBoxes[i].getText());

                StringBuilder stringBuilder = new StringBuilder(queryConstructor.getAttribute());

                if (stringBuilder.indexOf(checkBoxes[i].getText()) >= 0) {
                    while (stringBuilder.indexOf(checkBoxes[i].getText()) >= 0) {
                        if (stringBuilder.indexOf(",", stringBuilder.indexOf(checkBoxes[i].getText())) > 0) {
                            stringBuilder.replace(stringBuilder.indexOf(checkBoxes[i].getText()), stringBuilder.indexOf(checkBoxes[i].getText()) + stringBuilder.indexOf(",", stringBuilder.indexOf(checkBoxes[i].getText())) + 1, "");
                        } else {
                            stringBuilder.replace(stringBuilder.indexOf(checkBoxes[i].getText()), stringBuilder.indexOf(checkBoxes[i].getText()) + stringBuilder.length(), "");
                        }
                    }
                    queryConstructor.replaceAttribute(stringBuilder.toString());
                }

            }
        }

        eventIfListTableEmpty();
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    /**
     * Method (event) if there are no tables.
     * <p>
     * Метод (событие) если таблицы отсутствуют.
     */
    private void eventIfListTableEmpty() {
        if (queryBuilderPanel.getListTable().isEmpty()) {
            queryConstructor.addTable("EMPLOYEE");
            queryConstructor.clearAttribute();
        } else {
            queryConstructor.addTable(queryBuilderPanel.getListTable().get(0).getColumnName(0));

            if (!queryConstructor.getHistoryJoinNameTable().contains(queryBuilderPanel.getListTable().get(0).getColumnName(0))) {
                queryConstructor.getHistoryJoinNameTable().add(queryBuilderPanel.getListTable().get(0).getColumnName(0));
            }
        }
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
     * A method for configuring the dialog (window) created by the class.
     * <p>
     * Метод для настройки диалога (окна) созданного классом.
     */
    private void configurationDialog() {
        setLayout(new BorderLayout());
        setTitle("Удалить таблицы");
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
     * Adding components to the panel to place components.
     * <p>
     * Добавление компонентов в панель для размещения компонентов.
     */
    private void addComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorCenter().setInsets(5, 5, 5, 5);
        panelForPlacingComponents.add(labelConnects, gridBagHelper.setXY(0, 0).get());
        panelForPlacingComponents.add(connections, gridBagHelper.setXY(1, 0).setMaxWeightX().spanX().fillHorizontally().get());
        panelForPlacingComponents.add(labelSearch, gridBagHelper.setXY(0, 1).setMinWeightX().setWidth(1).get());
        panelForPlacingComponents.add(textFieldSearch, gridBagHelper.setXY(1, 1).setMaxWeightX().get());
        panelForPlacingComponents.add(buttonSearch, gridBagHelper.setXY(2, 1).setMinWeightX().get());
        panelForPlacingComponents.add(scrollPanePlacingCheckBoxes, gridBagHelper.setXY(0, 2).setMaxWeightX().spanX().get());
        panelForPlacingComponents.add(buttonRemoveTables, gridBagHelper.setXY(1, 3).setMinWeightX().setWidth(1).get());
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
