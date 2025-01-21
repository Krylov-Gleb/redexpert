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
import java.util.ArrayList;

public class GroupBy extends JDialog {

    private final QueryConstructor queryConstructor;
    private final QBPanel queryBuilderPanel;
    private JPanel panelPlacingComponents;
    private JPanel panelPlacingCheckBoxInScrollPane;
    private JPanel panelButton;
    private JLabel labelSearch;
    private JTextField textFieldSearch;
    private JScrollPane scrollPaneCheckBoxesAttribute;
    private JButton buttonClose;
    private JButton buttonClear;

    public GroupBy(QueryConstructor queryConstructor, QBPanel queryBuilderPanel) {
        this.queryConstructor = queryConstructor;
        this.queryBuilderPanel = queryBuilderPanel;
        init();
    }

    private void init() {
        initPanel();
        initLabel();
        initButton();
        initTextField();
        intiScrollPane();
        arrangeComponents();
    }

    private void intiScrollPane() {
        scrollPaneCheckBoxesAttribute = new JScrollPane();
        scrollPaneCheckBoxesAttribute.setPreferredSize(new Dimension(100, 300));
        arrangeCheckBoxesInScrollPane();
    }

    private void initButton() {
        buttonClose = WidgetFactory.createButton("buttonClose", Bundles.get("common.close.button"), event -> closeDialog());

        buttonClear = WidgetFactory.createButton("buttonClear", Bundles.get("common.clear.button"), event -> eventClearGroupBy());

        placingButtonsInPanel();
    }

    private void placingButtonsInPanel() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(5, 5, 5, 5).fillHorizontally();
        panelButton.add(new Label(" "), gridBagHelper.setXY(0, 0).setMaxWeightX().get());
        panelButton.add(buttonClose, gridBagHelper.nextCol().setMaxWeightX().get());
        panelButton.add(buttonClear, gridBagHelper.nextCol().setMaxWeightX().get());
        panelButton.add(new Label(" "), gridBagHelper.nextCol().setMaxWeightX().get());
    }

    private void eventClearGroupBy() {
        queryConstructor.setGroupBy("", "stepBack");

        for (int i = 0; i < panelPlacingCheckBoxInScrollPane.getComponents().length; i++) {
            JCheckBox checkBox = (JCheckBox) panelPlacingCheckBoxInScrollPane.getComponent(i);
            if (checkBox.isSelected()) {
                checkBox.setSelected(false);
            }
        }

        scrollPaneCheckBoxesAttribute.revalidate();
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    private void initTextField() {
        textFieldSearch = WidgetFactory.createTextField("textFieldSearch");
        textFieldSearch.setToolTipText(Bundles.get("QueryBuilder.GroupBy.searchAttribute"));
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

                ArrayList<String> arrayAttributes = new ArrayList<>();

                for (int i = 0; i < queryBuilderPanel.getListTable().size(); i++) {
                    if (queryConstructor.getTable().contains(queryBuilderPanel.getListTable().get(i).getColumnName(0))) {
                        for (int j = 0; j < queryBuilderPanel.getListTable().get(i).getRowCount(); j++) {
                            arrayAttributes.add(queryBuilderPanel.getListTable().get(i).getColumnName(0) + "." + queryBuilderPanel.getListTable().get(i).getValueAt(j, 0).toString());
                        }
                    }
                }

                if (!queryConstructor.getAttribute().isEmpty()) {
                    StringBuilder stringBuilder = new StringBuilder(queryConstructor.getGroupBy());

                    for (String arrayAttribute : arrayAttributes) {
                        if (arrayAttribute.contains(textFieldSearch.getText().toUpperCase())) {
                            JCheckBox checkBox = new JCheckBox(arrayAttribute);
                            checkBox.setToolTipText(Bundles.get("QueryBuilder.GroupBy.toolTipTextCheckBoxAttribute"));
                            checkBox.addItemListener(e -> {
                                if (checkBox.isSelected()) {
                                    addGroupBy(checkBox);
                                } else {
                                    removeGroupBy(checkBox);
                                }
                            });

                            if (stringBuilder.toString().contains(checkBox.getText())) {
                                checkBox.setSelected(true);
                            }

                            panelPlacingCheckBoxInScrollPane.add(checkBox);
                        }
                    }
                }
                scrollPaneCheckBoxesAttribute.setViewportView(panelPlacingCheckBoxInScrollPane);
                scrollPaneCheckBoxesAttribute.revalidate();
            }
        });
    }

    private void initLabel() {
        labelSearch = WidgetFactory.createLabel(Bundles.get("common.search.button"));
    }

    private void initPanel() {
        panelPlacingComponents = WidgetFactory.createPanel("panelPlacingComponents");
        panelPlacingComponents.setLayout(new GridBagLayout());

        panelButton = WidgetFactory.createPanel("panelButton");
        panelButton.setLayout(new GridBagLayout());
    }

    private void arrangeCheckBoxesInScrollPane() {
        panelPlacingCheckBoxInScrollPane = WidgetFactory.createPanel("panelPlacingCheckBoxInScrollPane");
        panelPlacingCheckBoxInScrollPane.setLayout(new BoxLayout(panelPlacingCheckBoxInScrollPane, BoxLayout.Y_AXIS));

        ArrayList<String> arrayAttributes = new ArrayList<>();

        for (int i = 0; i < queryBuilderPanel.getListTable().size(); i++) {
            if (queryConstructor.getTable().contains(queryBuilderPanel.getListTable().get(i).getColumnName(0))) {
                for (int j = 0; j < queryBuilderPanel.getListTable().get(i).getRowCount(); j++) {
                    arrayAttributes.add(queryBuilderPanel.getListTable().get(i).getColumnName(0) + "." + queryBuilderPanel.getListTable().get(i).getValueAt(j, 0).toString());
                }
            }
        }

        if (!queryConstructor.getAttribute().isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(queryConstructor.getGroupBy());

            for (String arrayAttribute : arrayAttributes) {
                JCheckBox checkBox = new JCheckBox(arrayAttribute);
                checkBox.setToolTipText(Bundles.get("QueryBuilder.GroupBy.toolTipTextCheckBoxAttribute"));
                checkBox.addItemListener(e -> {
                    if (checkBox.isSelected()) {
                        addGroupBy(checkBox);
                    } else {
                        removeGroupBy(checkBox);
                    }
                });

                if (stringBuilder.toString().contains(checkBox.getText())) {
                    checkBox.setSelected(true);
                }

                panelPlacingCheckBoxInScrollPane.add(checkBox);
            }
        }
        scrollPaneCheckBoxesAttribute.setViewportView(panelPlacingCheckBoxInScrollPane);
        scrollPaneCheckBoxesAttribute.revalidate();
    }

    private void arrangeComponents() {
        arrangeComponentsInPanelForPlacingComponents();
        configurationDialog();
    }

    private void configurationDialog() {
        setLayout(new BorderLayout());
        add(panelPlacingComponents, BorderLayout.CENTER);
        setTitle(Bundles.get("QueryBuilder.GroupBy.title"));
        setResizable(false);
        setIconImage(getAndCreateIconDialog().getImage());
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(600, 430);
        setVisible(true);
    }

    private void arrangeComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().setInsets(10, 5, 10, 5).anchorCenter().fillHorizontally();
        panelPlacingComponents.add(labelSearch, gridBagHelper.setXY(0, 0).setMinWeightX().get());
        panelPlacingComponents.add(textFieldSearch, gridBagHelper.nextCol().setMaxWeightX().get());
        panelPlacingComponents.add(scrollPaneCheckBoxesAttribute, gridBagHelper.previousCol().nextRow().spanX().setMaxWeightX().get());
        panelPlacingComponents.add(panelButton, gridBagHelper.nextRow().spanX().spanY().setMaxWeightX().get());
    }

    public void addGroupBy(JCheckBox checkBox) {
        if (!queryConstructor.getGroupBy().contains(checkBox.getText())) {
            StringBuilder stringBuilder = new StringBuilder(queryConstructor.getGroupBy());

            if (stringBuilder.toString().isEmpty()) {
                stringBuilder.append("GROUP BY ").append(checkBox.getText());
            } else {
                stringBuilder.append(",").append(checkBox.getText());
            }

            queryConstructor.setGroupBy(stringBuilder.toString(), "stepBack");
            queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
        }
    }

    public void removeGroupBy(JCheckBox checkBox) {
        if (queryConstructor.getGroupBy().contains(checkBox.getText())) {
            StringBuilder stringBuilder = new StringBuilder(queryConstructor.getGroupBy());

            if (stringBuilder.charAt(stringBuilder.indexOf(checkBox.getText()) - 1) == ',') {
                stringBuilder.replace(stringBuilder.indexOf(checkBox.getText()) - 1, stringBuilder.indexOf(checkBox.getText()) - 1 + checkBox.getText().length() + 1, "");
                queryConstructor.setGroupBy(stringBuilder.toString(), "stepBack");
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                return;
            }

            if (stringBuilder.charAt(stringBuilder.indexOf(checkBox.getText()) - 1) == ' ') {
                if (stringBuilder.toString().split(",").length == 1) {
                    stringBuilder.replace(0, stringBuilder.length(), "");
                    queryConstructor.setGroupBy(stringBuilder.toString(), "stepBack");
                    queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                    return;
                }
                if (stringBuilder.toString().split(",").length > 1) {
                    stringBuilder.replace(stringBuilder.indexOf(checkBox.getText()), stringBuilder.indexOf(checkBox.getText()) + checkBox.getText().length() + 1, "");
                    queryConstructor.setGroupBy(stringBuilder.toString(), "stepBack");
                    queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                }
            }
        }
    }

    private ImageIcon getAndCreateIconDialog() {
        return IconManager.getIcon(BrowserConstants.APPLICATION_IMAGE, "svg", 512, IconManager.IconFolder.BASE);
    }

    private void closeDialog() {
        setVisible(false);
        dispose();
    }

}
