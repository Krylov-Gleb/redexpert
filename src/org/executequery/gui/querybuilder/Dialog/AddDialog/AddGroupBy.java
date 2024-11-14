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
 * A class for creating a dialog (window) for adding groupings to a query.
 * <p>
 * Класс для создания диалога (окна) для добавления группировок в запрос.
 */
public class AddGroupBy extends JDialog {

    // --- Elements accepted using the constructor. ----
    // --- Поля, которые передаются через конструктор. ---

    private QueryConstructor queryConstructor;
    private QueryBuilderPanel queryBuilderPanel;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelForPlacingComponents;
    private JPanel panelArrangeCheckBoxInScrollPane;
    private JLabel labelSearch;
    private JLabel labelConnections;
    private JTextField textFieldSearch;
    private JButton buttonAddGroupByInQuery;
    private JButton buttonSearch;
    private JScrollPane scrollPaneForOutputAttributes;
    private ConnectionsComboBox connections;

    /**
     * Creating a dialog (window) to add groupings to the query.
     * <p>
     * Создаём диалог (окно) для добавления группировок в запрос.
     */
    public AddGroupBy(QueryConstructor createStringQuery, QueryBuilderPanel queryBuilderPanel) {
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
        initComboBox();
        initTextField();
        intiScrollPane();
        initButton();
        arrangeComponents();
    }

    /**
     * A method for placing components and configuring the dialog.
     * <p>
     * Метод для размещения компонентов и настройки диалога.
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
    private void initButton() {
        buttonSearch = WidgetFactory.createButton("buttonSearch", "Начать поиск");
        buttonAddGroupByInQuery = WidgetFactory.createButton("buttonAddGroupByInQuery", "Добавить", event -> {
            eventButtonAddGroupBy();
            closeDialog();
        });
    }

    /**
     * A method for initializing the ScrollPane and placing checkboxes on it.
     * <p>
     * Метод для инициализации scrollPane и размещения на ней флажков.
     */
    private void intiScrollPane() {
        scrollPaneForOutputAttributes = new JScrollPane();
        scrollPaneForOutputAttributes.setPreferredSize(new Dimension(100, 300));
        arrangeCheckBoxesInScrollPane();
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
     * A method for initializing labels.
     * <p>
     * Метод для инициализации меток.
     */
    private void initLabels() {
        labelConnections = WidgetFactory.createLabel("Подключение:");
        labelSearch = WidgetFactory.createLabel("Поиск:");
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
     * A method for adding checkboxes to the scrollbar.
     * <p>
     * Метод для добавления флажков панель прокрутки.
     */
    private void arrangeCheckBoxesInScrollPane() {
        panelArrangeCheckBoxInScrollPane = new JPanel();
        panelArrangeCheckBoxInScrollPane.setLayout(new BoxLayout(panelArrangeCheckBoxInScrollPane, BoxLayout.Y_AXIS));

        if (!queryConstructor.getAttribute().isEmpty()) {
            for (int i = 0; i < queryConstructor.getAttribute().split(",").length; i++) {
                JCheckBox checkBox = new JCheckBox(queryConstructor.getAttribute().split(",")[i]);
                panelArrangeCheckBoxInScrollPane.add(checkBox);
            }
        }

        scrollPaneForOutputAttributes.setViewportView(panelArrangeCheckBoxInScrollPane);
    }

    /**
     * A method for configuring the parameters of a dialog (window).
     * <p>
     * Метод для настройки параметров диалога (окна).
     */
    private void configurationDialog() {
        setLayout(new BorderLayout());
        add(panelForPlacingComponents, BorderLayout.CENTER);
        setTitle("Группировка");
        setIconImage(new ImageIcon("red_expert.png").getImage());
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(600, 500);
        setVisible(true);
    }

    /**
     * A method for placing components on the component placement panel.
     * <p>
     * Метод для размещения компонентов на панели размещения компонентов.
     */
    private void arrangeComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(5, 5, 5, 5).fillHorizontally();
        panelForPlacingComponents.add(labelConnections, gridBagHelper.setXY(0, 0).setMinWeightX().get());
        panelForPlacingComponents.add(connections, gridBagHelper.setXY(1, 0).setMaxWeightX().spanX().get());
        panelForPlacingComponents.add(labelSearch, gridBagHelper.setXY(0, 1).setMinWeightX().setWidth(1).get());
        panelForPlacingComponents.add(textFieldSearch, gridBagHelper.setXY(1, 1).setMaxWeightX().get());
        panelForPlacingComponents.add(buttonSearch, gridBagHelper.setXY(2, 1).setMinWeightX().setWidth(1).get());
        panelForPlacingComponents.add(scrollPaneForOutputAttributes, gridBagHelper.setXY(0, 2).spanX().setMaxWeightX().get());
        panelForPlacingComponents.add(buttonAddGroupByInQuery, gridBagHelper.setXY(1, 3).setMinWeightX().setWidth(1).get());
    }

    /**
     * The event method of the button for adding a grouping to the query.
     * <p>
     * Метод события кнопки добавления группировки в запрос.
     */
    private void eventButtonAddGroupBy() {
        ArrayList<String> NameAttributes = new ArrayList<>();
        JCheckBox[] arrayCheckBox = getCheckBoxesFromPanelArrangeCheckBox();

        for (int i = 0; i < arrayCheckBox.length; i++) {
            if (arrayCheckBox[i].isSelected()) {
                NameAttributes.add(arrayCheckBox[i].getText());
            }
        }

        queryConstructor.addGroupBy(NameAttributes);
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
