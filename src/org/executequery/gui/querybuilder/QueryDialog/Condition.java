package org.executequery.gui.querybuilder.QueryDialog;

import org.executequery.gui.IconManager;
import org.executequery.gui.WidgetFactory;
import org.executequery.gui.browser.BrowserConstants;
import org.executequery.gui.querybuilder.QBPanel;
import org.executequery.gui.querybuilder.QueryConstructor;
import org.executequery.localization.Bundles;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This class creates a dialog (window) that adds conditions to the request.
 * <p>
 * Этот класс создаёт диалог (окно) который добавляет в запрос условия.
 *
 * @author Krylov Gleb
 */
public class Condition extends JDialog {

    // --- Fields that are passed through the constructor ----
    // --- Поля, которые передаются через конструктор ---

    private QueryConstructor queryConstructor;
    private QBPanel queryBuilderPanel;

    // --- GUI Components. ---
    // --- Компоненты графического интерфейса. ---

    private JPanel panelPlacingComponents;
    private JPanel panelPlacingCheckBoxInScrollPane;
    private JPanel panelButtons;
    private JScrollPane scrollPaneCheckBoxesCondition;
    private JLabel labelLeftOperand;
    private JLabel labelRightOperand;
    private JLabel labelOperation;
    private JLabel labelConnectionCondition;
    private JComboBox<String> comboBoxLeftOperand;
    private JComboBox<String> comboBoxOperation;
    private JComboBox<String> comboBoxJoinConditions;
    private JTextField textFieldRightOperand;
    private JButton buttonAddCondition;
    private JButton buttonDeleteCondition;
    private JButton buttonClose;

    /**
     * A dialog (window) is created.
     * A method is used to initialize fields.
     * <p>
     * Создаётся диалог (окно).
     * Используется метод для инициализации полей.
     */
    public Condition(QueryConstructor queryConstructor, QBPanel queryBuilderPanel) {
        this.queryConstructor = queryConstructor;
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
        initLabels();
        initTextField();
        initScrollPane();
        initComboBox();
        initButtons();
        arrangeComponent();
    }

    /**
     * A method for initializing buttons.
     * <p>
     * Метод для инициализации кнопок.
     */
    private void initButtons() {
        buttonAddCondition = WidgetFactory.createButton("buttonAddCondition", Bundles.get("common.add.button"), event -> {
            eventButtonAddConditions();
            arrangeCheckBoxesInScrollPane();
        });

        buttonDeleteCondition = WidgetFactory.createButton("buttonDeleteCondition", Bundles.get("common.delete.button"), event -> {
            eventButtonRemoveConditions();
            arrangeCheckBoxesInScrollPane();
        });

        buttonClose = WidgetFactory.createButton("buttonClose", Bundles.get("common.close.button"), event -> {
            closeDialog();
        });

        placingButtonInPanel();
    }

    /**
     * A method for placing buttons in a panel to place buttons.
     * <p>
     * Метод для размещения кнопок в панели для размещения кнопок.
     */
    private void placingButtonInPanel() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(5, 5, 5, 5).fillHorizontally();
        panelButtons.add(buttonAddCondition, gridBagHelper.setXY(0, 0).setMaxWeightX().get());
        panelButtons.add(buttonDeleteCondition, gridBagHelper.nextCol().setMaxWeightX().get());
        panelButtons.add(buttonClose, gridBagHelper.nextRow().setMaxWeightX().get());
    }

    /**
     * A method for initializing drop-down lists (comboBox).
     * <p>
     * Метод для инициализации выпадающих списков (comboBox).
     */
    private void initComboBox() {
        comboBoxLeftOperand = WidgetFactory.createComboBox("comboBoxLeftOperand", arrangeComponentsInLeftComboBox());
        comboBoxLeftOperand.setEditable(true);
        comboBoxLeftOperand.setMinimumSize(new Dimension(200, 30));
        comboBoxLeftOperand.setPreferredSize(new Dimension(200, 30));
        comboBoxLeftOperand.setMaximumSize(new Dimension(200, 30));
        comboBoxLeftOperand.setToolTipText(Bundles.get("QueryBuilder.Condition.toolTipTextLeftOperand"));

        comboBoxOperation = WidgetFactory.createComboBox("comboBoxOperation", new String[]{"", "=", "<>", "!=", ">", ">=", "<", "<=", "BETWEEN", "IN", "LIKE"});

        comboBoxOperation.setEditable(true);
        comboBoxOperation.setMinimumSize(new Dimension(200, 30));
        comboBoxOperation.setPreferredSize(new Dimension(200, 30));
        comboBoxOperation.setMaximumSize(new Dimension(200, 30));
        comboBoxOperation.setToolTipText(Bundles.get("QueryBuilder.Condition.toolTipTextOperation"));

        comboBoxJoinConditions = WidgetFactory.createComboBox("comboBoxJoinConditions", new String[]{"OR", "AND"});
        comboBoxJoinConditions.setEditable(false);
        comboBoxJoinConditions.setMinimumSize(new Dimension(200, 30));
        comboBoxJoinConditions.setPreferredSize(new Dimension(200, 30));
        comboBoxJoinConditions.setMaximumSize(new Dimension(200, 30));
        comboBoxJoinConditions.setToolTipText(Bundles.get("QueryBuilder.Condition.toolTipTextConnection"));
    }

    /**
     * A method for adding components to the left ComboBox.
     * <p>
     * Метод для добавления компонентов в левый ComboBox.
     */
    private ArrayList<String> arrangeComponentsInLeftComboBox() {
        ArrayList<String> attributes = new ArrayList<>();
        attributes.add("");

        for (int i = 0; i < queryBuilderPanel.getListTable().size(); i++) {
            if (queryConstructor.getTable().contains(queryBuilderPanel.getListTable().get(i).getColumnName(0))) {
                for (int j = 0; j < queryBuilderPanel.getListTable().get(i).getRowCount(); j++) {
                    attributes.add(queryBuilderPanel.getListTable().get(i).getColumnName(0) + "." + queryBuilderPanel.getListTable().get(i).getValueAt(j, 0).toString());
                }
            }
        }

        return attributes;
    }

    /**
     * A method for initializing text fields.
     * <p>
     * Метод для инициализации текстовых полей.
     */
    private void initTextField() {
        textFieldRightOperand = WidgetFactory.createTextField("textFieldRightOperand");
        textFieldRightOperand.setToolTipText(Bundles.get("QueryBuilder.Condition.toolTipTextRightOperand"));
        textFieldRightOperand.setMinimumSize(new Dimension(200, 30));
        textFieldRightOperand.setPreferredSize(new Dimension(200, 30));
        textFieldRightOperand.setMaximumSize(new Dimension(200, 30));
    }

    /**
     * A method for initializing the ScrollPane and placing checkboxes on it.
     * <p>
     * Метод для инициализации панели прокрутки (scrollPane) и размещения на ней флажков.
     */
    private void initScrollPane() {
        scrollPaneCheckBoxesCondition = new JScrollPane();
        scrollPaneCheckBoxesCondition.setPreferredSize(new Dimension(100, 200));
        arrangeCheckBoxesInScrollPane();
    }

    /**
     * A method for initializing labels.
     * <p>
     * Метод для инициализации меток.
     */
    private void initLabels() {
        labelLeftOperand = WidgetFactory.createLabel(Bundles.get("QueryBuilder.Condition.labelLeftOperand"));
        labelLeftOperand.setPreferredSize(new Dimension(100, 30));
        labelRightOperand = WidgetFactory.createLabel(Bundles.get("QueryBuilder.Condition.labelRightOperand"));
        labelRightOperand.setPreferredSize(new Dimension(100, 30));
        labelOperation = WidgetFactory.createLabel(Bundles.get("QueryBuilder.Condition.labelOperation"));
        labelOperation.setPreferredSize(new Dimension(100, 30));
        labelConnectionCondition = WidgetFactory.createLabel(Bundles.get("QueryBuilder.Condition.labelConnectionCondition"));
        labelConnectionCondition.setPreferredSize(new Dimension(100, 30));
    }

    /**
     * The method for initializing JPanel.
     * <p>
     * Метод для инициализации JPanel.
     */
    private void initPanel() {
        panelPlacingComponents = WidgetFactory.createPanel("panelPlacingComponents");
        panelPlacingComponents.setLayout(new GridBagLayout());

        panelButtons = WidgetFactory.createPanel("panelButtons");
        panelButtons.setLayout(new GridBagLayout());
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
        add(panelPlacingComponents, BorderLayout.CENTER);
        setTitle(Bundles.get("QueryBuilder.Condition.title"));
        setResizable(false);
        setIconImage(getAndCreateIconDialog().getImage());
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
    private void arrangeComponentsInPanelForAddConditionInQuery() {
        GridBagHelper gridBagHelper = new GridBagHelper().setInsets(10, 5, 10, 5).anchorCenter().setMaxWeightX().fillHorizontally();
        panelPlacingComponents.add(scrollPaneCheckBoxesCondition, gridBagHelper.setXY(0, 0).setWidth(3).setMaxWeightX().get());
        panelPlacingComponents.add(labelLeftOperand, gridBagHelper.nextRow().setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(comboBoxLeftOperand, gridBagHelper.nextCol().setWidth(2).setMaxWeightX().get());
        panelPlacingComponents.add(labelOperation, gridBagHelper.previousCol().nextRow().setWidth(1).setMinWeightX().get());
        panelPlacingComponents.add(comboBoxOperation, gridBagHelper.nextCol().setWidth(2).setMaxWeightX().get());
        panelPlacingComponents.add(labelRightOperand, gridBagHelper.previousCol().nextRow().setWidth(1).setMinWeightX().get());
        panelPlacingComponents.add(textFieldRightOperand, gridBagHelper.nextCol().setWidth(2).setMaxWeightX().get());
        panelPlacingComponents.add(labelConnectionCondition, gridBagHelper.previousCol().nextRow().setWidth(1).setMinWeightX().get());
        panelPlacingComponents.add(comboBoxJoinConditions, gridBagHelper.nextCol().setMinWeightX().get());
        panelPlacingComponents.add(panelButtons, gridBagHelper.nextRow().spanY().spanX().setMaxWeightX().get());
    }

    /**
     * The event of the button to remove the condition from the request.
     * <p>
     * Событие кнопки по удалению условия из запроса.
     */
    private void eventButtonAddConditions() {
        if (!comboBoxLeftOperand.getSelectedItem().toString().isEmpty()) {
            if (!textFieldRightOperand.getText().isEmpty()) {
                if (!comboBoxJoinConditions.getSelectedItem().toString().isEmpty()) {
                    addWhere(comboBoxLeftOperand.getSelectedItem().toString(), comboBoxOperation.getSelectedItem().toString(), textFieldRightOperand.getText(), comboBoxJoinConditions.getSelectedItem().toString());
                }
            }
        }

        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    /**
     * A method for removing conditions from a query.
     * <p>
     * Метод для удаления условий из запроса.
     */
    private void eventButtonRemoveConditions() {
        StringBuilder stringBuilderWhere = new StringBuilder(queryConstructor.getWhere());
        JCheckBox[] checkBoxesInScrollPane = getCheckBoxesFromPanelArrangeCheckBox();

        for (int i = 0; i < checkBoxesInScrollPane.length; i++) {
            if (checkBoxesInScrollPane[i].isSelected()) {
                if (checkBoxesInScrollPane.length == 1) {
                    stringBuilderWhere.replace(0, stringBuilderWhere.length(), "");
                    queryConstructor.setWhere(stringBuilderWhere.toString());
                    queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                    return;
                } else {
                    if (stringBuilderWhere.toString().contains(checkBoxesInScrollPane[i].getText())) {
                        stringBuilderWhere.replace(stringBuilderWhere.indexOf(checkBoxesInScrollPane[i].getText()),
                                stringBuilderWhere.indexOf(checkBoxesInScrollPane[i].getText()) + checkBoxesInScrollPane[i].getText().length(),
                                "");

                        queryConstructor.setWhere(stringBuilderWhere.toString());
                        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                    }
                }
            }
        }

        if(stringBuilderWhere.lastIndexOf("OR") == stringBuilderWhere.length()-3){
            stringBuilderWhere.replace(stringBuilderWhere.length()-4,stringBuilderWhere.length(),"");
            queryConstructor.setWhere(stringBuilderWhere.toString());
            queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
        }

        if(stringBuilderWhere.lastIndexOf("AND") == stringBuilderWhere.length()-4){
            stringBuilderWhere.replace(stringBuilderWhere.length()-5,stringBuilderWhere.length(),"");
            queryConstructor.setWhere(stringBuilderWhere.toString());
            queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
        }
    }

    /**
     * A method for getting an array of checkboxes from the panel on which they are placed.
     * <p>
     * Метод для получения массива флажков из панели на которой они размещены.
     */
    private JCheckBox[] getCheckBoxesFromPanelArrangeCheckBox() {
        Component[] component = panelPlacingCheckBoxInScrollPane.getComponents();
        JCheckBox[] checkBoxes = new JCheckBox[panelPlacingCheckBoxInScrollPane.getComponents().length];

        for (int i = 0; i < component.length; i++) {
            checkBoxes[i] = ((JCheckBox) component[i]);
        }

        return checkBoxes;
    }

    /**
     * A method for adding a condition.
     * <p>
     * Метод для добавления условия.
     */
    public void addWhere(String leftOperand, String operation, String rightOperand, String join) {
        StringBuilder stringBuilder = new StringBuilder();

        if (!queryConstructor.getWhere().isEmpty()) {
            stringBuilder.append(leftOperand).append(" ");
            if (!operation.isEmpty()) {
                stringBuilder.append(operation).append(" ").append(rightOperand);
            } else {
                stringBuilder.append(rightOperand);
            }

            if (!queryConstructor.getWhere().contains(stringBuilder.toString())) {
                queryConstructor.setWhere(queryConstructor.getWhere() + " " + join + " " + stringBuilder.toString());
            }

            return;

        } else {
            stringBuilder.append("WHERE").append(" ").append(leftOperand).append(" ");
            if (!operation.isEmpty()) {
                stringBuilder.append(operation).append(" ").append(rightOperand);
            } else {
                stringBuilder.append(rightOperand);
            }

            if (!queryConstructor.getWhere().contains(stringBuilder.toString())) {
                queryConstructor.setWhere(queryConstructor.getWhere() + stringBuilder.toString());
            }

            return;
        }
    }

    /**
     * A method for placing checkboxes on a ScrollPane.
     * <p>
     * Метод для размещения флажков на ScrollPane.
     */
    private void arrangeCheckBoxesInScrollPane() {
        panelPlacingCheckBoxInScrollPane = WidgetFactory.createPanel("panelPlacingCheckBoxInScrollPane");
        panelPlacingCheckBoxInScrollPane.setLayout(new BoxLayout(panelPlacingCheckBoxInScrollPane, BoxLayout.Y_AXIS));

        if (!queryConstructor.getWhere().isEmpty()) {
            String[] splitConditionText = queryConstructor.getWhere().split("(?<=OR )|(?<=AND )");

            for (int i = 0; i < splitConditionText.length; i++) {
                if (queryConstructor.getWhere().contains(splitConditionText[i]) & splitConditionText[i].contains("WHERE")) {
                    StringBuilder stringBuilder = new StringBuilder(splitConditionText[i]);
                    stringBuilder.replace(stringBuilder.indexOf("WHERE"), "WHERE ".length(), "");
                    panelPlacingCheckBoxInScrollPane.add(new JCheckBox(stringBuilder.toString()));
                } else {
                    panelPlacingCheckBoxInScrollPane.add(new JCheckBox(splitConditionText[i]));
                }
            }
        }

        scrollPaneCheckBoxesCondition.setViewportView(panelPlacingCheckBoxInScrollPane);
    }

    /**
     * A method for creating and receiving a dialog icon.
     * <p>
     * Метод для создания и получения иконки диалога.
     */
    private ImageIcon getAndCreateIconDialog() {
        return IconManager.getIcon(BrowserConstants.APPLICATION_IMAGE, "svg", 512, IconManager.IconFolder.BASE);
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
