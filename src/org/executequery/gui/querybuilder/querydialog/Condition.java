package org.executequery.gui.querybuilder.querydialog;

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
import java.util.Objects;

public class Condition extends JDialog {

    private final QueryConstructor queryConstructor;
    private final QBPanel queryBuilderPanel;
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

    public Condition(QueryConstructor queryConstructor, QBPanel queryBuilderPanel) {
        this.queryConstructor = queryConstructor;
        this.queryBuilderPanel = queryBuilderPanel;
        init();
    }

    private void init() {
        initPanel();
        initLabels();
        initTextField();
        initScrollPane();
        initComboBox();
        initButtons();
        arrangeComponent();
    }

    private void initButtons() {
        buttonAddCondition = WidgetFactory.createButton("buttonAddCondition", Bundles.get("common.add.button"), event -> {
            eventButtonAddConditions();
            arrangeCheckBoxesInScrollPane();
        });

        buttonDeleteCondition = WidgetFactory.createButton("buttonDeleteCondition", Bundles.get("common.delete.button"), event -> {
            eventButtonRemoveConditions();
            arrangeCheckBoxesInScrollPane();
        });

        buttonClose = WidgetFactory.createButton("buttonClose", Bundles.get("common.close.button"), event -> closeDialog());

        placingButtonInPanel();
    }

    private void placingButtonInPanel() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(5, 5, 5, 5).fillHorizontally();
        panelButtons.add(buttonAddCondition, gridBagHelper.setXY(0, 0).setMaxWeightX().get());
        panelButtons.add(buttonDeleteCondition, gridBagHelper.nextCol().setMaxWeightX().get());
        panelButtons.add(buttonClose, gridBagHelper.nextRow().setMaxWeightX().get());
    }

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

    private void initTextField() {
        textFieldRightOperand = WidgetFactory.createTextField("textFieldRightOperand");
        textFieldRightOperand.setToolTipText(Bundles.get("QueryBuilder.Condition.toolTipTextRightOperand"));
        textFieldRightOperand.setMinimumSize(new Dimension(200, 30));
        textFieldRightOperand.setPreferredSize(new Dimension(200, 30));
        textFieldRightOperand.setMaximumSize(new Dimension(200, 30));
    }

    private void initScrollPane() {
        scrollPaneCheckBoxesCondition = new JScrollPane();
        scrollPaneCheckBoxesCondition.setPreferredSize(new Dimension(100, 200));
        arrangeCheckBoxesInScrollPane();
    }

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

    private void initPanel() {
        panelPlacingComponents = WidgetFactory.createPanel("panelPlacingComponents");
        panelPlacingComponents.setLayout(new GridBagLayout());

        panelButtons = WidgetFactory.createPanel("panelButtons");
        panelButtons.setLayout(new GridBagLayout());
    }

    private void arrangeComponent() {
        arrangeComponentsInPanelForAddConditionInQuery();
        configurationDialog();
    }

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

    private void eventButtonAddConditions() {
        if (!Objects.requireNonNull(comboBoxLeftOperand.getSelectedItem()).toString().isEmpty()) {
            if (!textFieldRightOperand.getText().isEmpty()) {
                if (!Objects.requireNonNull(comboBoxJoinConditions.getSelectedItem()).toString().isEmpty()) {
                    addWhere(comboBoxLeftOperand.getSelectedItem().toString(), Objects.requireNonNull(comboBoxOperation.getSelectedItem()).toString(), textFieldRightOperand.getText(), comboBoxJoinConditions.getSelectedItem().toString());
                }
            }
        }

        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    private void eventButtonRemoveConditions() {
        StringBuilder stringBuilderWhere = new StringBuilder(queryConstructor.getWhere());
        JCheckBox[] checkBoxesInScrollPane = getCheckBoxesFromPanelArrangeCheckBox();

        boolean isSelectAll = true;

        for (JCheckBox jCheckBox : checkBoxesInScrollPane) {
            if (!jCheckBox.isSelected()) {
                isSelectAll = false;
                break;
            }
        }

        if (isSelectAll) {
            queryConstructor.setWhere("", "stepBack");
            queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            return;
        } else {
            for (JCheckBox checkBox : checkBoxesInScrollPane) {
                if (checkBox.isSelected()) {
                    if (stringBuilderWhere.toString().contains(checkBox.getText())) {
                        stringBuilderWhere.replace(stringBuilderWhere.indexOf(checkBox.getText()),
                                stringBuilderWhere.indexOf(checkBox.getText()) + checkBox.getText().length(),
                                "");

                        queryConstructor.setWhere(stringBuilderWhere.toString(), "stepBack");
                        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                    }
                }
            }
        }

        if (stringBuilderWhere.lastIndexOf("OR") == stringBuilderWhere.length() - 3) {
            stringBuilderWhere.replace(stringBuilderWhere.length() - 4, stringBuilderWhere.length(), "");
            queryConstructor.setWhere(stringBuilderWhere.toString(), "stepBack");
            queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
        }

        if (stringBuilderWhere.lastIndexOf("AND") == stringBuilderWhere.length() - 4) {
            stringBuilderWhere.replace(stringBuilderWhere.length() - 5, stringBuilderWhere.length(), "");
            queryConstructor.setWhere(stringBuilderWhere.toString(), "stepBack");
            queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
        }
    }

    private JCheckBox[] getCheckBoxesFromPanelArrangeCheckBox() {
        Component[] component = panelPlacingCheckBoxInScrollPane.getComponents();
        JCheckBox[] checkBoxes = new JCheckBox[panelPlacingCheckBoxInScrollPane.getComponents().length];

        for (int i = 0; i < component.length; i++) {
            checkBoxes[i] = ((JCheckBox) component[i]);
        }

        return checkBoxes;
    }

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
                queryConstructor.setWhere(queryConstructor.getWhere() + " " + join + " " + stringBuilder, "stepBack");
            }

        } else {
            stringBuilder.append("WHERE").append(" ").append(leftOperand).append(" ");
            if (!operation.isEmpty()) {
                stringBuilder.append(operation).append(" ").append(rightOperand);
            } else {
                stringBuilder.append(rightOperand);
            }

            if (!queryConstructor.getWhere().contains(stringBuilder.toString())) {
                queryConstructor.setWhere(queryConstructor.getWhere() + stringBuilder, "stepBack");
            }
        }
    }

    private void arrangeCheckBoxesInScrollPane() {
        panelPlacingCheckBoxInScrollPane = WidgetFactory.createPanel("panelPlacingCheckBoxInScrollPane");
        panelPlacingCheckBoxInScrollPane.setLayout(new BoxLayout(panelPlacingCheckBoxInScrollPane, BoxLayout.Y_AXIS));

        if (!queryConstructor.getWhere().isEmpty()) {
            String[] splitConditionText = queryConstructor.getWhere().split("(?<=OR )|(?<=AND )");

            for (String s : splitConditionText) {
                if (queryConstructor.getWhere().contains(s) & s.contains("WHERE")) {
                    StringBuilder stringBuilder = new StringBuilder(s);
                    stringBuilder.replace(stringBuilder.indexOf("WHERE"), "WHERE ".length(), "");
                    panelPlacingCheckBoxInScrollPane.add(new JCheckBox(stringBuilder.toString()));
                } else {
                    panelPlacingCheckBoxInScrollPane.add(new JCheckBox(s));
                }
            }
        }

        scrollPaneCheckBoxesCondition.setViewportView(panelPlacingCheckBoxInScrollPane);
    }

    private ImageIcon getAndCreateIconDialog() {
        return IconManager.getIcon(BrowserConstants.APPLICATION_IMAGE, "svg", 512, IconManager.IconFolder.BASE);
    }

    private void closeDialog() {
        setVisible(false);
        dispose();
    }
}
