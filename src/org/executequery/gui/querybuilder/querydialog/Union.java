package org.executequery.gui.querybuilder.querydialog;

import org.executequery.gui.IconManager;
import org.executequery.gui.WidgetFactory;
import org.executequery.gui.browser.BrowserConstants;
import org.executequery.gui.querybuilder.QBPanel;
import org.executequery.gui.querybuilder.QueryConstructor;
import org.executequery.localization.Bundles;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class Union extends JDialog {

    private final QueryConstructor queryConstructor;
    private final QBPanel queryBuilderPanel;
    private JPanel panelPlacingComponents;
    private JPanel panelPlacingCheckBoxInScrollPane;
    private JPanel panelButton;
    private JLabel labelSearch;
    private JTextField textFieldSearch;
    private JScrollPane scrollPaneUnions;
    private JButton buttonAddUnion;
    private JButton buttonRemoveUnion;
    private JButton buttonClose;

    public Union(QueryConstructor queryConstructor, QBPanel queryBuilderPanel) {
        this.queryConstructor = queryConstructor;
        this.queryBuilderPanel = queryBuilderPanel;
        init();
    }

    private void init() {
        initPanel();
        initLabel();
        initButton();
        initScrollPane();
        initTextField();
        arrangeComponents();
    }

    private void initPanel() {
        panelPlacingComponents = WidgetFactory.createPanel("panelPlacingComponents");
        panelPlacingComponents.setLayout(new GridBagLayout());

        panelButton = WidgetFactory.createPanel("panelButton");
        panelButton.setLayout(new GridBagLayout());
    }

    private void initLabel() {
        labelSearch = WidgetFactory.createLabel(Bundles.get("common.search.button"));
    }

    private void initScrollPane() {
        scrollPaneUnions = new JScrollPane();
        scrollPaneUnions.setPreferredSize(new Dimension(100, 320));
        scrollPaneUnions.setViewportView(panelPlacingCheckBoxInScrollPane);
        arrangeCheckBoxesInScrollPane();
    }

    private void initButton() {
        buttonAddUnion = WidgetFactory.createButton("buttonAddUnion", Bundles.get("common.add.button"), event -> {
            eventAddUnion();
            arrangeCheckBoxesInScrollPane();
        });

        buttonRemoveUnion = WidgetFactory.createButton("buttonRemoveUnion", Bundles.get("common.delete.button"), event -> {
            eventRemoveUnion();
            arrangeCheckBoxesInScrollPane();
        });

        buttonClose = WidgetFactory.createButton("buttonClose", Bundles.get("common.close.button"), event -> closeDialog());

        placingButtonsInPanel();
    }

    private void placingButtonsInPanel() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(10, 5, 10, 5).fillHorizontally();
        panelButton.add(buttonAddUnion, gridBagHelper.setXY(0, 0).setMaxWeightX().get());
        panelButton.add(buttonRemoveUnion, gridBagHelper.nextCol().setMaxWeightX().get());
        panelButton.add(buttonClose, gridBagHelper.nextRow().setMaxWeightX().get());
    }

    private void initTextField() {
        textFieldSearch = WidgetFactory.createTextField("textFieldSearch");
        textFieldSearch.setToolTipText(Bundles.get("QueryBuilder.Union.EnterTheUnion"));
        textFieldSearch.setMinimumSize(new Dimension(200, 25));
        textFieldSearch.setPreferredSize(new Dimension(200, 25));
        textFieldSearch.setMaximumSize(new Dimension(200, 25));
        textFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                textChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                textChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                textChanged();
            }

            private void textChanged() {
                panelPlacingCheckBoxInScrollPane = WidgetFactory.createPanel("panelPlacingCheckBoxInScrollPane");
                panelPlacingCheckBoxInScrollPane.setLayout(new BoxLayout(panelPlacingCheckBoxInScrollPane, BoxLayout.Y_AXIS));

                if (!queryConstructor.getUnion().isEmpty()) {
                    String[] splitUnionText = queryConstructor.getUnion().split("(?<=UNION)");

                    for (int i = 0; i < splitUnionText.length - 1; i++) {
                        if (splitUnionText[i].contains(textFieldSearch.getText().toUpperCase())) {
                            JCheckBox checkBox = new JCheckBox(splitUnionText[i]);
                            checkBox.setToolTipText(Bundles.get("QueryBuilder.Union.toolTipTextCheckBoxUnion"));
                            panelPlacingCheckBoxInScrollPane.add(checkBox);
                        }
                    }
                }
                scrollPaneUnions.setViewportView(panelPlacingCheckBoxInScrollPane);
            }
        });
    }

    private void arrangeComponents() {
        arrangeComponentsInPanelForPlacingComponents();
        configurationDialog();
    }

    private void configurationDialog() {
        setLayout(new BorderLayout());
        add(panelPlacingComponents, BorderLayout.CENTER);
        setTitle(Bundles.get("QueryBuilder.Union.title"));
        setResizable(false);
        setIconImage(getAndCreateIconDialog().getImage());
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(600, 485);
        setVisible(true);
    }

    private void arrangeComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().setInsets(10, 5, 10, 5).anchorCenter().fillHorizontally();
        panelPlacingComponents.add(labelSearch, gridBagHelper.setXY(0, 0).setMinWeightX().get());
        panelPlacingComponents.add(textFieldSearch, gridBagHelper.nextCol().setMaxWeightX().get());
        panelPlacingComponents.add(scrollPaneUnions, gridBagHelper.previousCol().nextRow().spanX().setMaxWeightX().get());
        panelPlacingComponents.add(panelButton, gridBagHelper.nextCol().nextRow().spanX().spanY().setMaxWeightX().get());
    }

    private void eventAddUnion() {
        StringBuilder stringBuilderUnionValue = new StringBuilder(queryConstructor.getUnion());
        StringBuilder stringBuilderTestQueryValue = new StringBuilder(queryBuilderPanel.getTestQuery());

        if (stringBuilderTestQueryValue.indexOf(stringBuilderUnionValue.toString()) >= 0) {
            stringBuilderTestQueryValue.replace(stringBuilderTestQueryValue.indexOf(stringBuilderUnionValue.toString()), stringBuilderTestQueryValue.indexOf(stringBuilderUnionValue.toString()) + stringBuilderUnionValue.toString().length(), "");
        }

        stringBuilderTestQueryValue.replace(stringBuilderTestQueryValue.length() - 1, stringBuilderTestQueryValue.length(), "");
        deleteOptimization(stringBuilderTestQueryValue);
        deleteOrderBy(stringBuilderTestQueryValue);
        deleteWith(stringBuilderTestQueryValue);
        stringBuilderUnionValue.append(stringBuilderTestQueryValue).append("UNION").append("\n");

        queryConstructor.setUnion(stringBuilderUnionValue.toString(), "stepBack");
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    private void deleteOptimization(StringBuilder stringBuilderTestQueryValue) {
        if (stringBuilderTestQueryValue.toString().contains("OPTIMIZE FOR ALL ROWS")) {
            stringBuilderTestQueryValue.replace(stringBuilderTestQueryValue.indexOf("OPTIMIZE FOR ALL ROWS") - 1, stringBuilderTestQueryValue.indexOf("OPTIMIZE FOR ALL ROWS") + "OPTIMIZE FOR ALL ROWS".length() + 1, "");
        }

        if (stringBuilderTestQueryValue.toString().contains("OPTIMIZE FOR FIRST ROWS")) {
            stringBuilderTestQueryValue.replace(stringBuilderTestQueryValue.indexOf("OPTIMIZE FOR FIRST ROWS") - 1, stringBuilderTestQueryValue.indexOf("OPTIMIZE FOR FIRST ROWS") + "OPTIMIZE FOR FIRST ROWS".length() + 1, "");
        }
    }

    private void deleteOrderBy(StringBuilder stringBuilderTestQueryValue) {
        if (stringBuilderTestQueryValue.toString().contains("ORDER BY")) {
            String orderBy = queryConstructor.getOrderBy();
            stringBuilderTestQueryValue.replace(stringBuilderTestQueryValue.indexOf("ORDER BY"), stringBuilderTestQueryValue.indexOf("ORDER BY") + orderBy.length() + 4, "");
        }
    }

    private void deleteWith(StringBuilder stringBuilderTestQueryValue) {
        if (stringBuilderTestQueryValue.toString().contains("WITH RECURSIVE")) {
            String with = queryConstructor.getWith();
            stringBuilderTestQueryValue.replace(stringBuilderTestQueryValue.indexOf("WITH RECURSIVE"), stringBuilderTestQueryValue.indexOf("WITH RECURSIVE") + with.length() + 2, "");
        }
        if (stringBuilderTestQueryValue.toString().contains("WITH")) {
            String with = queryConstructor.getWith();
            stringBuilderTestQueryValue.replace(stringBuilderTestQueryValue.indexOf("WITH"), stringBuilderTestQueryValue.indexOf("WITH") + with.length() + 4, "");
        }
    }

    private void eventRemoveUnion() {
        StringBuilder stringBuilderUnionValue = new StringBuilder(queryConstructor.getUnion());
        JCheckBox[] checkBoxesFromScrollPane = getCheckBoxesFromPanelArrangeCheckBox();

        for (JCheckBox checkBox : checkBoxesFromScrollPane) {
            if (checkBox.isSelected()) {
                if (checkBoxesFromScrollPane.length == 1) {
                    stringBuilderUnionValue.replace(0, stringBuilderUnionValue.length(), "");
                    queryConstructor.setUnion("", "stepBack");
                    queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                    return;
                } else {
                    if (stringBuilderUnionValue.indexOf(checkBox.getText()) >= 0) {
                        stringBuilderUnionValue.replace(stringBuilderUnionValue.indexOf(checkBox.getText()),
                                stringBuilderUnionValue.indexOf(checkBox.getText()) + checkBox.getText().length(),
                                "");
                    }
                }
            }
        }

        if (stringBuilderUnionValue.indexOf("\n") == 0) {
            stringBuilderUnionValue.replace(0, 1, "");
        }

        queryConstructor.setUnion(stringBuilderUnionValue.toString(), "stepBack");
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    private JCheckBox[] getCheckBoxesFromPanelArrangeCheckBox() {
        Component[] components = panelPlacingCheckBoxInScrollPane.getComponents();
        JCheckBox[] checkBoxes = new JCheckBox[panelPlacingCheckBoxInScrollPane.getComponents().length];

        for (int i = 0; i < components.length; i++) {
            checkBoxes[i] = ((JCheckBox) components[i]);
        }

        return checkBoxes;
    }

    private void arrangeCheckBoxesInScrollPane() {
        panelPlacingCheckBoxInScrollPane = WidgetFactory.createPanel("panelPlacingCheckBoxInScrollPane");
        panelPlacingCheckBoxInScrollPane.setLayout(new BoxLayout(panelPlacingCheckBoxInScrollPane, BoxLayout.Y_AXIS));

        if (!queryConstructor.getUnion().isEmpty()) {
            String[] splitUnionText = queryConstructor.getUnion().split("(?<=UNION)");

            for (int i = 0; i < splitUnionText.length - 1; i++) {
                JCheckBox checkBox = new JCheckBox(splitUnionText[i]);
                checkBox.setToolTipText(Bundles.get("QueryBuilder.Union.toolTipTextCheckBoxUnion"));
                panelPlacingCheckBoxInScrollPane.add(checkBox);
            }
        }

        scrollPaneUnions.setViewportView(panelPlacingCheckBoxInScrollPane);
    }

    private ImageIcon getAndCreateIconDialog() {
        return IconManager.getIcon(BrowserConstants.APPLICATION_IMAGE, "svg", 512, IconManager.IconFolder.BASE);
    }

    private void closeDialog() {
        setVisible(false);
        dispose();
    }
}
