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

public class With extends JDialog {

    private final QueryConstructor queryConstructor;
    private final QBPanel queryBuilderPanel;
    private JPanel panelPlacingComponents;
    private JPanel panelPlacingCheckBoxInScrollPane;
    private JPanel panelButton;
    private JScrollPane scrollPaneWith;
    private JLabel labelUsingQuery;
    private JLabel labelRecursive;
    private JLabel labelAlias;
    private JTextField textFieldAlias;
    private JTextField textFieldUsingQuery;
    private JComboBox<String> comboBoxRecursive;
    private JButton buttonAddWith;
    private JButton buttonClose;
    private JButton buttonRemoveWith;

    public With(QueryConstructor queryConstructor, QBPanel queryBuilderPanel) {
        this.queryConstructor = queryConstructor;
        this.queryBuilderPanel = queryBuilderPanel;
        init();
    }

    private void init() {
        initPanel();
        initScrollPane();
        initLabel();
        initTextField();
        initButton();
        initComboBox();
        arrangeComponents();
    }

    private void initLabel() {
        labelUsingQuery = WidgetFactory.createLabel(Bundles.get("QueryBuilder.With.labelUsingQuery"));
        labelAlias = WidgetFactory.createLabel(Bundles.get("QueryBuilder.With.labelAlias"));
        labelRecursive = WidgetFactory.createLabel(Bundles.get("QueryBuilder.With.labelRecursive"));
    }

    private void initScrollPane() {
        scrollPaneWith = new JScrollPane();
        scrollPaneWith.setPreferredSize(new Dimension(100, 200));
        arrangeCheckBoxesInScrollPane();
    }

    private void initTextField() {
        textFieldAlias = WidgetFactory.createTextField("textFieldAlias");
        textFieldAlias.setToolTipText(Bundles.get("QueryBuilder.Union.enterAlias"));
        textFieldAlias.setMinimumSize(new Dimension(200, 30));
        textFieldAlias.setPreferredSize(new Dimension(200, 30));
        textFieldAlias.setMaximumSize(new Dimension(200, 30));

        textFieldUsingQuery = WidgetFactory.createTextField("textFieldUsingQuery");
        textFieldUsingQuery.setToolTipText(Bundles.get("QueryBuilder.Union.enterRequest"));
        textFieldUsingQuery.setMinimumSize(new Dimension(200, 30));
        textFieldUsingQuery.setPreferredSize(new Dimension(200, 30));
        textFieldUsingQuery.setMaximumSize(new Dimension(200, 30));
    }

    private void initComboBox() {
        comboBoxRecursive = WidgetFactory.createComboBox("comboBoxRecursive", new String[]{Bundles.get("common.recursive"), Bundles.get("common.not_recursive")});
        comboBoxRecursive.setToolTipText(Bundles.get("QueryBuilder.Union.useRecursion"));
        comboBoxRecursive.setMinimumSize(new Dimension(200, 30));
        comboBoxRecursive.setPreferredSize(new Dimension(200, 30));
        comboBoxRecursive.setMaximumSize(new Dimension(200, 30));
    }

    private void initPanel() {
        panelPlacingComponents = WidgetFactory.createPanel("panelPlacingComponents");
        panelPlacingComponents.setLayout(new GridBagLayout());

        panelButton = WidgetFactory.createPanel("panelButton");
        panelButton.setLayout(new GridBagLayout());
    }

    private void initButton() {
        buttonAddWith = WidgetFactory.createButton("buttonAddWith", Bundles.get("common.add.button"), event -> {
            addWith();
            arrangeCheckBoxesInScrollPane();
        });

        buttonRemoveWith = WidgetFactory.createButton("buttonRemoveWith", Bundles.get("common.delete.button"), event -> {
            removeWith();
            arrangeCheckBoxesInScrollPane();
        });

        buttonClose = WidgetFactory.createButton("buttonClose", Bundles.get("common.close.button"), event -> closeDialog());

        placingButtonsInPanel();
    }

    private void placingButtonsInPanel() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(5, 5, 5, 5).fillHorizontally();
        panelButton.add(buttonAddWith, gridBagHelper.setXY(0, 0).setMaxWeightX().get());
        panelButton.add(buttonRemoveWith, gridBagHelper.nextCol().setMaxWeightX().get());
        panelButton.add(buttonClose, gridBagHelper.nextRow().setMaxWeightX().get());
    }

    private void arrangeComponents() {
        arrangeComponentsInPanelForPlacingComponents();
        configurationDialog();
    }

    private void configurationDialog() {
        setLayout(new BorderLayout());
        add(panelPlacingComponents, BorderLayout.CENTER);
        setTitle(Bundles.get("QueryBuilder.With.title"));
        setResizable(false);
        setIconImage(getAndCreateIconDialog().getImage());
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(600, 460);
        setVisible(true);
    }

    private void arrangeComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().setInsets(10, 5, 10, 5).anchorCenter().setMaxWeightX().fillHorizontally();
        panelPlacingComponents.add(scrollPaneWith, gridBagHelper.setXY(0, 0).setWidth(3).setMaxWeightX().get());
        panelPlacingComponents.add(labelUsingQuery, gridBagHelper.nextRow().setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(textFieldUsingQuery, gridBagHelper.nextCol().setWidth(2).setMaxWeightX().get());
        panelPlacingComponents.add(labelAlias, gridBagHelper.previousCol().nextRow().setWidth(1).setMinWeightX().get());
        panelPlacingComponents.add(textFieldAlias, gridBagHelper.nextCol().setWidth(2).setMaxWeightX().get());
        panelPlacingComponents.add(labelRecursive, gridBagHelper.previousCol().nextRow().setWidth(1).setMinWeightX().get());
        panelPlacingComponents.add(comboBoxRecursive, gridBagHelper.nextCol().setWidth(2).setMaxWeightX().get());
        panelPlacingComponents.add(panelButton, gridBagHelper.nextRow().spanX().spanY().setMinWeightX().get());
    }

    private void addWith() {
        StringBuilder stringBuilder = new StringBuilder(queryConstructor.getWith());
        StringBuilder stringBuilderUseQuery = new StringBuilder(textFieldUsingQuery.getText());

        if (!stringBuilder.toString().contains(" " + textFieldAlias.getText() + " ")) {
            if (!stringBuilder.toString().contains("\n" + textFieldAlias.getText() + " ")) {
                if (!textFieldUsingQuery.toString().isEmpty()) {
                    if (!textFieldAlias.getText().isEmpty()) {
                        if (stringBuilder.toString().isEmpty()) {
                            stringBuilder.append("WITH");

                            if (comboBoxRecursive.getSelectedIndex() == 0) {
                                stringBuilder.append(" ").append("RECURSIVE");
                            }

                            stringBuilder.append(" ").append(textFieldAlias.getText()).append(" AS (");
                            stringBuilder.append(stringBuilderUseQuery.toString().replace(";", ""));

                            stringBuilder.append(")");

                        } else {
                            if (!stringBuilder.toString().contains(stringBuilderUseQuery.toString())) {
                                if (comboBoxRecursive.getSelectedIndex() == 0) {
                                    if (stringBuilder.indexOf("RECURSIVE") < 0) {
                                        stringBuilder.replace(stringBuilder.indexOf("WITH") + "WITH".length(), stringBuilder.indexOf("WITH") + "WITH".length() + 1, " RECURSIVE ");
                                    }
                                } else {
                                    if (stringBuilder.indexOf("RECURSIVE") > 0) {
                                        stringBuilder.replace(stringBuilder.indexOf("RECURSIVE"), stringBuilder.indexOf("RECURSIVE") + "RECURSIVE ".length(), "");
                                    }
                                }

                                stringBuilder.append(",");

                                stringBuilder.append(textFieldAlias.getText()).append(" AS (");
                                stringBuilder.append(stringBuilderUseQuery.toString().replace(";", ""));

                                stringBuilder.append(")");
                            }
                        }

                        queryBuilderPanel.addStepBackActionInHistory("Set With " + queryConstructor.getWith());
                        queryConstructor.setWith(stringBuilder.toString(), "stepBack");
                        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                    }
                }
            }
        }
    }

    private void removeWith() {
        StringBuilder stringBuilder = new StringBuilder(queryConstructor.getWith());
        JCheckBox[] checkBoxesInScrollPane = getCheckBoxesFromPanelArrangeCheckBox();

        for (JCheckBox checkBox : checkBoxesInScrollPane) {
            if (checkBox.isSelected()) {
                if (checkBoxesInScrollPane.length == 1) {
                    stringBuilder.replace(0, stringBuilder.length(), "");

                    queryBuilderPanel.addStepBackActionInHistory("Set With " + queryConstructor.getWith());
                    queryConstructor.setWith("", "stepBack");
                    queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                    return;
                } else {
                    if (stringBuilder.indexOf(checkBox.getText()) >= 0) {
                        stringBuilder.replace(stringBuilder.indexOf(checkBox.getText()),
                                stringBuilder.indexOf(checkBox.getText()) + checkBox.getText().length(),
                                "");
                    }
                }
            }
        }

        if (stringBuilder.lastIndexOf(",") == stringBuilder.length() - 1) {
            stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "");
        }

        if (stringBuilder.toString().length() == "WITH RECURSIVE ".length() || stringBuilder.toString().length() == "WITH ".length()) {
            stringBuilder.replace(0, stringBuilder.length(), "");
        }

        queryBuilderPanel.addStepBackActionInHistory("Set With " + queryConstructor.getWith());
        queryConstructor.setWith(stringBuilder.toString(), "stepBack");
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    private JCheckBox[] getCheckBoxesFromPanelArrangeCheckBox() {
        Component[] component = panelPlacingCheckBoxInScrollPane.getComponents();
        JCheckBox[] checkBoxes = new JCheckBox[panelPlacingCheckBoxInScrollPane.getComponents().length];

        for (int i = 0; i < component.length; i++) {
            checkBoxes[i] = ((JCheckBox) component[i]);
        }

        return checkBoxes;
    }

    private void arrangeCheckBoxesInScrollPane() {
        panelPlacingCheckBoxInScrollPane = WidgetFactory.createPanel("panelPlacingCheckBoxInScrollPane");
        panelPlacingCheckBoxInScrollPane.setLayout(new BoxLayout(panelPlacingCheckBoxInScrollPane, BoxLayout.Y_AXIS));

        if (!queryConstructor.getWith().isEmpty()) {
            String[] splitWithText = queryConstructor.getWith().split("(?<=\\),)");

            for (int i = 0; i < splitWithText.length; i++) {
                if (i == 0) {
                    StringBuilder stringBuilder = new StringBuilder(splitWithText[i]);

                    if (stringBuilder.indexOf("WITH RECURSIVE") >= 0) {
                        stringBuilder.replace(stringBuilder.indexOf("WITH RECURSIVE"), stringBuilder.indexOf("WITH RECURSIVE") + "WITH RECURSIVE ".length(), "");
                    } else {
                        stringBuilder.replace(stringBuilder.indexOf("WITH"), stringBuilder.indexOf("WITH") + "WITH ".length(), "");
                    }

                    splitWithText[i] = stringBuilder.toString();
                }
                panelPlacingCheckBoxInScrollPane.add(new JCheckBox(splitWithText[i]));
            }
        }

        scrollPaneWith.setViewportView(panelPlacingCheckBoxInScrollPane);
    }

    private ImageIcon getAndCreateIconDialog() {
        return IconManager.getIcon(BrowserConstants.APPLICATION_IMAGE, "svg", 512, IconManager.IconFolder.BASE);
    }

    private void closeDialog() {
        setVisible(false);
        dispose();
    }
}
