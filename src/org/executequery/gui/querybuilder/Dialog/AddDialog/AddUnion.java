package org.executequery.gui.querybuilder.Dialog.AddDialog;

import org.executequery.gui.WidgetFactory;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.executequery.gui.querybuilder.WorkQuryEditor.QueryConstructor;
import org.underworldlabs.swing.ConnectionsComboBox;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This class creates a dialog (window) for adding unions to a query.
 * <p>
 * Это класс создаёт диалог (окно) для добавления союзов (Union) в запрос.
 *
 * @author Krylov Gleb
 */
public class AddUnion extends JDialog {

    // --- Elements accepted using the constructor. ----
    // --- Поля, которые передаются через конструктор. ---

    private QueryConstructor queryConstructor;
    private QueryBuilderPanel queryBuilderPanel;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelForPlacingComponents;
    private JPanel panelArrangeCheckBoxInScrollPane;
    private JLabel labelConnections;
    private JLabel labelSearch;
    private JTextField textFieldSearch;
    private JScrollPane scrollPaneSaveQuery;
    private JButton buttonAddUnion;
    private JButton buttonSearch;
    private ConnectionsComboBox connections;

    /**
     * A dialog (window) is created to add a union to the request.
     * <p>
     * Создаётся диалог (окно) для добавления союза в запрос.
     */
    public AddUnion(QueryConstructor createStringQuery, QueryBuilderPanel queryBuilderPanel) {
        queryConstructor = createStringQuery;
        this.queryBuilderPanel = queryBuilderPanel;
        init();
    }

    /**
     * A method for initializing fields.
     * <p>
     * Метод для инициализации полей.
     */
    private void init() {
        initPanel();
        initLabel();
        initScrollPane();
        initComboBox();
        initTextField();
        initButton();
        arrangeComponents();
    }

    /**
     * The method for initializing JPanel.
     * <p>
     * Метод для инициализации JPanel.
     */
    private void initPanel() {
        panelForPlacingComponents = WidgetFactory.createPanel("panelForPlacingComponents");
        panelForPlacingComponents.setLayout(new GridBagLayout());
        panelForPlacingComponents.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        panelArrangeCheckBoxInScrollPane = new JPanel();
        panelArrangeCheckBoxInScrollPane.setLayout(new BoxLayout(panelArrangeCheckBoxInScrollPane, BoxLayout.Y_AXIS));
    }

    /**
     * A method for initializing labels.
     * <p>
     * Метод для инициализации меток.
     */
    private void initLabel() {
        labelConnections = WidgetFactory.createLabel("Подключение:");
        labelSearch = WidgetFactory.createLabel("Поиск:");
    }

    /**
     * A method for initializing the ScrollPane and placing checkboxes on it.
     * <p>
     * Метод для инициализации scrollPane и размещения на ней флажков.
     */
    private void initScrollPane() {
        scrollPaneSaveQuery = new JScrollPane();
        scrollPaneSaveQuery.setPreferredSize(new Dimension(100, 320));
        scrollPaneSaveQuery.setViewportView(panelArrangeCheckBoxInScrollPane);
        addCheckBoxesInScrollPane();
    }

    /**
     * A method for initializing buttons.
     * <p>
     * Метод для инициализации кнопок.
     */
    private void initButton() {
        buttonAddUnion = WidgetFactory.createButton("buttonAddUnion", "Добавить", event -> {
            eventButtonAddUnion();
            closeDialog();
        });

        buttonSearch = WidgetFactory.createButton("buttonSearch", "Начать поиск");
    }

    /**
     * A method for initializing a text field.
     * <p>
     * Метод для инициализации текстового поля.
     */
    private void initTextField() {
        textFieldSearch = WidgetFactory.createTextField("textFieldSearch");
    }

    /**
     * A method for initializing drop-down lists (comboBox).
     * <p>
     * Метод для инициализации выпадающих списков (comboBox).
     */
    private void initComboBox() {
        connections = WidgetFactory.createConnectionComboBox("connections", true);
        connections.setMaximumSize(new Dimension(200, 30));
    }

    /**
     * A method for adding CheckBoxes to the ScrollPane.
     * <p>
     * Метод для добавления CheckBoxes в ScrollPane.
     */
    private void addCheckBoxesInScrollPane() {
        ArrayList<String> listHistorySelectQuery = queryConstructor.getSaveQueryInHistory();

        for (int i = 0; i < listHistorySelectQuery.size(); i++) {
            JCheckBox checkBox = new JCheckBox(listHistorySelectQuery.get(i));
            panelArrangeCheckBoxInScrollPane.add(checkBox);
        }
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
     * A method for configuring the parameters of a dialog (window) created by this class.
     * <p>
     * Метод для настройки параметров диалога (окна) созданного этим классом.
     */
    private void configurationDialog() {
        setLayout(new BorderLayout());
        add(panelForPlacingComponents, BorderLayout.CENTER);
        setTitle("Добавить союз");
        setIconImage(new ImageIcon("red_expert.png").getImage());
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
    private void arrangeComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().fillHorizontally().setInsets(5, 5, 5, 5);
        panelForPlacingComponents.add(labelConnections, gridBagHelper.setXY(0, 0).setMinWeightX().get());
        panelForPlacingComponents.add(connections, gridBagHelper.setXY(1, 0).setMaxWeightX().setWidth(2).get());
        panelForPlacingComponents.add(labelSearch, gridBagHelper.setXY(0, 1).setMinWeightX().setWidth(1).get());
        panelForPlacingComponents.add(textFieldSearch, gridBagHelper.setXY(1, 1).setMaxWeightX().get());
        panelForPlacingComponents.add(buttonSearch, gridBagHelper.setXY(2, 1).setMinWeightX().get());
        panelForPlacingComponents.add(scrollPaneSaveQuery, gridBagHelper.setXY(0, 3).setMaxWeightX().setWidth(3).get());
        panelForPlacingComponents.add(buttonAddUnion, gridBagHelper.setXY(1, 4).setMinWeightX().setWidth(1).spanY().get());
    }

    /**
     * The method of the button event for adding unions to the request.
     * <p>
     * Метод события кнопки для добавления союзов в запрос.
     */
    private void eventButtonAddUnion(){
        JCheckBox[] checkBoxes = getCheckBoxesFromPanelArrangeCheckBox();

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isSelected()) {
                if (stringBuilder.toString().isEmpty()) {
                    stringBuilder.append(checkBoxes[i].getText().replaceAll(";", ""));
                    queryConstructor.getHistoryUnion().add(checkBoxes[i].getText().replaceAll(";", ""));
                } else {
                    stringBuilder.append("UNION\n").append(checkBoxes[i].getText().replaceAll(";", ""));
                    queryConstructor.getHistoryUnion().add("UNION\n" + checkBoxes[i].getText().replaceAll(";", ""));
                }
            }
        }

        if (stringBuilder.indexOf("UNION") > 0) {
            queryConstructor.addUnion(stringBuilder.toString());
            queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
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
