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
 * This class creates a dialog (window) to remove conditions from the query.
 * <p>
 * Этот класс создаёт диалог (окно) для удаления условий из запроса.
 *
 * @author Krylov Gleb
 */
public class RemoveCondition extends JDialog {

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
    private JButton buttonRemoveCondition;
    private JTextField textFieldSearch;
    private JScrollPane scrollPaneForPlacingCheckBoxes;
    private ConnectionsComboBox connections;

    /**
     * A dialog (window) is created to remove conditions from the request.
     * <p>
     * Создаётся диалог (окно) для удаления условий из запроса.
     */
    public RemoveCondition(QueryBuilderPanel queryBuilderPanel, QueryConstructor queryConstructor) {
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
        scrollPaneForPlacingCheckBoxes = new JScrollPane();
        scrollPaneForPlacingCheckBoxes.setPreferredSize(new Dimension(100, 300));
        addCheckBoxesInScrollPane();
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
        buttonRemoveCondition = WidgetFactory.createButton("buttonRemoveCondition", "Удалить", event -> {
            eventButtonRemoveConditions();
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
        setTitle("Удалить условия");
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
        panelForPlacingComponents.add(scrollPaneForPlacingCheckBoxes, gridBagHelper.setXY(0, 2).setMaxWeightX().spanX().get());
        panelForPlacingComponents.add(buttonRemoveCondition, gridBagHelper.setXY(1, 3).setMinWeightX().setWidth(1).get());
    }

    /**
     * A method for placing checkboxes on a ScrollPane.
     * <p>
     * Метод для размещения флажков на ScrollPane.
     */
    private void addCheckBoxesInScrollPane() {
        panelArrangeCheckBoxInScrollPane = new JPanel();
        panelArrangeCheckBoxInScrollPane.setLayout(new BoxLayout(panelArrangeCheckBoxInScrollPane, BoxLayout.Y_AXIS));

        ArrayList<String> historyCondition = queryConstructor.getHistoryCondition();

        for (int i = 0; i < historyCondition.size(); i++) {
            panelArrangeCheckBoxInScrollPane.add(new JCheckBox(historyCondition.get(i)));
        }

        scrollPaneForPlacingCheckBoxes.setViewportView(panelArrangeCheckBoxInScrollPane);
    }

    /**
     * A method for removing conditions from a query.
     * <p>
     * Метод для удаления условий из запроса.
     */
    private void eventButtonRemoveConditions() {
        StringBuilder stringBuilder = new StringBuilder(queryConstructor.getWhere());
        JCheckBox[] checkBoxesInScrollPane = getCheckBoxesFromPanelArrangeCheckBox();

        for (int i = 0; i < checkBoxesInScrollPane.length; i++) {
            if (checkBoxesInScrollPane[i].isSelected()) {
                if (stringBuilder.indexOf(checkBoxesInScrollPane[i].getText()) > 0) {
                    stringBuilder.replace(stringBuilder.indexOf(checkBoxesInScrollPane[i].getText()),
                            stringBuilder.indexOf(checkBoxesInScrollPane[i].getText()) + checkBoxesInScrollPane[i].getText().length(),
                            "");

                    queryConstructor.removeHistoryCondition(checkBoxesInScrollPane[i].getText());
                    eventIfConditionOne(stringBuilder);
                    eventIfConditionEmpty(stringBuilder);
                }
            }
        }

        replaceWhereOrHavingIfNotEmpty(stringBuilder);

        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
        JOptionPane.showMessageDialog(queryBuilderPanel, "Условия удалены", "Условия", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * The method of overwriting Where or Having if they are not empty.
     * <p>
     * Метод перезаписи Where или Having если они не пустые.
     */
    private void replaceWhereOrHavingIfNotEmpty(StringBuilder stringBuilder) {
        if (!queryConstructor.getWhere().isEmpty()) {
            queryConstructor.replaceWhere(stringBuilder.toString());
        }
        if (!queryConstructor.getHaving().isEmpty()) {
            queryConstructor.replaceHaving(stringBuilder.toString());
        }
    }

    /**
     * The (event) method if there are no conditions.
     * <p>
     * Метод (событие) если условий нет.
     */
    private void eventIfConditionEmpty(StringBuilder stringBuilder) {
        if (queryConstructor.getHistoryCondition().isEmpty()) {
            stringBuilder.replace(0, queryConstructor.getWhere().length(), "");
        }
    }

    /**
     * The (event) method if there is only one condition.
     * <p>
     * Метод (событие) если условие только одно.
     */
    private void eventIfConditionOne(StringBuilder stringBuilder) {
        if (queryConstructor.getHistoryCondition().size() == 1) {
            if (stringBuilder.indexOf("OR") > 0) {
                stringBuilder.replace(stringBuilder.indexOf("OR"), stringBuilder.indexOf("OR") + "OR".length(), "");
                queryConstructor.getHistoryCondition().set(0, queryConstructor.getHistoryCondition().get(0).replace("OR", ""));
            }
            if (stringBuilder.indexOf("AND") > 0) {
                stringBuilder.replace(stringBuilder.indexOf("AND"), stringBuilder.indexOf("AND") + "AND".length(), "");
                queryConstructor.getHistoryCondition().set(0, queryConstructor.getHistoryCondition().get(0).replace("AND", ""));
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