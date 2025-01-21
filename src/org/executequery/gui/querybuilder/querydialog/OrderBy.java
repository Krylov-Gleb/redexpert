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
import java.util.Objects;

public class OrderBy extends JDialog {

    private final QueryConstructor queryConstructor;
    private final QBPanel queryBuilderPanel;
    private JPanel panelPlacingComponents;
    private JPanel panelPlacingCheckBoxesInScrollPane;
    private JPanel panelButton;
    private JScrollPane scrollPaneAttributesOrderBy;
    private JLabel labelSearch;
    private JLabel labelAscAndDesc;
    private JComboBox<String> comboBoxAscDesc;
    private JTextField textFieldSearch;
    private JButton buttonClose;
    private JButton buttonClear;

    public OrderBy(QBPanel queryBuilderPanel, QueryConstructor createStringQuery) {
        this.queryBuilderPanel = queryBuilderPanel;
        this.queryConstructor = createStringQuery;
        init();
    }

    private void init() {
        initPanel();
        initLabel();
        initButton();
        initTextField();
        initComboBox();
        initScrollPane();
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
        labelAscAndDesc = WidgetFactory.createLabel(Bundles.get("QueryBuilder.OrderBy.labelAddAscAndDesc"));
    }

    private void initScrollPane() {
        scrollPaneAttributesOrderBy = new JScrollPane();
        scrollPaneAttributesOrderBy.setPreferredSize(new Dimension(200, 300));
        arrangeCheckBoxesOnScrollPane();
    }

    private void initButton() {
        buttonClose = WidgetFactory.createButton("buttonClose", Bundles.get("common.close.button"), event -> closeDialog());

        buttonClear = WidgetFactory.createButton("buttonClear", Bundles.get("common.clear.button"), event -> eventClearOrderBy());

        placingButtonsInPanel();
    }

    private void eventClearOrderBy() {
        queryBuilderPanel.addStepBackActionInHistory("Set OrderBy " + queryConstructor.getOrderBy());
        queryConstructor.setOrderBy("", "stepBack");

        for (int i = 0; i < panelPlacingCheckBoxesInScrollPane.getComponents().length; i++) {
            JCheckBox checkBox = (JCheckBox) panelPlacingCheckBoxesInScrollPane.getComponent(i);
            if (checkBox.isSelected()) {
                checkBox.setSelected(false);
            }
        }

        scrollPaneAttributesOrderBy.revalidate();
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    private void placingButtonsInPanel() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(10, 5, 10, 5).fillHorizontally();
        panelButton.add(new Label(" "), gridBagHelper.setXY(0, 0).setMaxWeightX().get());
        panelButton.add(buttonClose, gridBagHelper.nextCol().setMaxWeightX().get());
        panelButton.add(buttonClear, gridBagHelper.nextCol().setMaxWeightX().get());
        panelButton.add(new Label(" "), gridBagHelper.nextCol().setMaxWeightX().get());
    }

    private void initTextField() {
        textFieldSearch = WidgetFactory.createTextField("textFieldSearch");
        textFieldSearch.setToolTipText(Bundles.get("QueryBuilder.OrderBy.searchAttribute"));
        textFieldSearch.setMinimumSize(new Dimension(200, 25));
        textFieldSearch.setPreferredSize(new Dimension(200, 25));
        textFieldSearch.setMaximumSize(new Dimension(200, 25));
        eventAddDocumentListenerInTextFields();
    }

    private void eventAddDocumentListenerInTextFields() {
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
                panelPlacingCheckBoxesInScrollPane = WidgetFactory.createPanel("panelPlacingCheckBoxesInScrollPane");
                panelPlacingCheckBoxesInScrollPane.setLayout(new BoxLayout(panelPlacingCheckBoxesInScrollPane, BoxLayout.Y_AXIS));

                ArrayList<String> arrayAttributes = new ArrayList<>();

                for (int i = 0; i < queryBuilderPanel.getListTable().size(); i++) {
                    if (queryConstructor.getTable().contains(queryBuilderPanel.getListTable().get(i).getColumnName(0))) {
                        for (int j = 0; j < queryBuilderPanel.getListTable().get(i).getRowCount(); j++) {
                            arrayAttributes.add(queryBuilderPanel.getListTable().get(i).getColumnName(0) + "." + queryBuilderPanel.getListTable().get(i).getValueAt(j, 0).toString());
                        }
                    }
                }

                if (!queryConstructor.getAttribute().isEmpty()) {
                    StringBuilder stringBuilder = new StringBuilder(queryConstructor.getOrderBy());

                    for (String arrayAttribute : arrayAttributes) {
                        if (arrayAttribute.contains(textFieldSearch.getText().toUpperCase())) {
                            JCheckBox checkBox = new JCheckBox(arrayAttribute);
                            checkBox.setToolTipText(Bundles.get("QueryBuilder.OrderBy.toolTipTextCheckBoxAttribute"));
                            checkBox.addItemListener(e -> {
                                if (checkBox.isSelected()) {
                                    addOrderBy(checkBox);
                                } else {
                                    removeOrderBy(checkBox);
                                }
                            });

                            if (stringBuilder.toString().contains(checkBox.getText())) {
                                checkBox.setSelected(true);
                            }

                            panelPlacingCheckBoxesInScrollPane.add(checkBox);
                        }
                    }
                }

                scrollPaneAttributesOrderBy.setViewportView(panelPlacingCheckBoxesInScrollPane);
                scrollPaneAttributesOrderBy.revalidate();
            }
        });
    }

    private void initComboBox() {
        comboBoxAscDesc = WidgetFactory.createComboBox("comboBoxAscDesc", new String[]{"ASC", "DESC"});
        comboBoxAscDesc.setToolTipText(Bundles.get("QueryBuilder.OrderBy.sortingOrder"));
    }

    private void arrangeComponents() {
        arrangeComponentsInPanelForPlacingComponents();
        configurationDialog();
    }

    private void arrangeCheckBoxesOnScrollPane() {
        panelPlacingCheckBoxesInScrollPane = WidgetFactory.createPanel("panelPlacingCheckBoxesInScrollPane");
        panelPlacingCheckBoxesInScrollPane.setLayout(new BoxLayout(panelPlacingCheckBoxesInScrollPane, BoxLayout.Y_AXIS));

        ArrayList<String> arrayAttributes = new ArrayList<>();

        for (int i = 0; i < queryBuilderPanel.getListTable().size(); i++) {
            if (queryConstructor.getTable().contains(queryBuilderPanel.getListTable().get(i).getColumnName(0))) {
                for (int j = 0; j < queryBuilderPanel.getListTable().get(i).getRowCount(); j++) {
                    arrayAttributes.add(queryBuilderPanel.getListTable().get(i).getColumnName(0) + "." + queryBuilderPanel.getListTable().get(i).getValueAt(j, 0).toString());
                }
            }
        }

        if (!queryConstructor.getAttribute().isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(queryConstructor.getOrderBy());

            for (String arrayAttribute : arrayAttributes) {
                JCheckBox checkBox = new JCheckBox(arrayAttribute);
                checkBox.setToolTipText(Bundles.get("QueryBuilder.OrderBy.toolTipTextCheckBoxAttribute"));
                checkBox.addItemListener(e -> {
                    if (checkBox.isSelected()) {
                        addOrderBy(checkBox);
                    } else {
                        removeOrderBy(checkBox);
                    }
                });

                if (stringBuilder.toString().contains(checkBox.getText())) {
                    checkBox.setSelected(true);
                }

                panelPlacingCheckBoxesInScrollPane.add(checkBox);
            }
        }

        scrollPaneAttributesOrderBy.setViewportView(panelPlacingCheckBoxesInScrollPane);
        scrollPaneAttributesOrderBy.revalidate();
    }

    private void arrangeComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(10, 5, 10, 5).fillHorizontally();
        panelPlacingComponents.add(labelSearch, gridBagHelper.setXY(0, 0).setMinWeightX().get());
        panelPlacingComponents.add(textFieldSearch, gridBagHelper.nextCol().setMaxWeightX().get());
        panelPlacingComponents.add(scrollPaneAttributesOrderBy, gridBagHelper.previousCol().nextRow().spanX().setMaxWeightX().get());
        panelPlacingComponents.add(labelAscAndDesc, gridBagHelper.nextRow().spanX().setMinWeightX().get());
        panelPlacingComponents.add(comboBoxAscDesc, gridBagHelper.nextRow().spanX().setMinWeightX().get());
        panelPlacingComponents.add(panelButton, gridBagHelper.nextRow().spanX().spanY().setMaxWeightX().get());
    }

    private void configurationDialog() {
        setLayout(new BorderLayout());
        add(panelPlacingComponents, BorderLayout.CENTER);
        setTitle(Bundles.get("QueryBuilder.OrderBy.title"));
        setResizable(false);
        setIconImage(getAndCreateIconDialog().getImage());
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(600, 505);
        setVisible(true);
    }

    public void addOrderBy(JCheckBox checkBox) {
        if (!queryConstructor.getOrderBy().contains(checkBox.getText())) {
            StringBuilder stringBuilder = new StringBuilder(queryConstructor.getOrderBy());

            if (stringBuilder.toString().isEmpty()) {
                stringBuilder.append("ORDER BY ").append(checkBox.getText()).append(" ").append(Objects.requireNonNull(comboBoxAscDesc.getSelectedItem())).append(" ");
            } else {
                stringBuilder.append(",").append(checkBox.getText()).append(" ").append(Objects.requireNonNull(comboBoxAscDesc.getSelectedItem())).append(" ");
            }

            queryConstructor.setOrderBy(stringBuilder.toString(), "stepBack");
            queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
        }
    }

    public void removeOrderBy(JCheckBox checkBox) {
        if (queryConstructor.getOrderBy().contains(checkBox.getText())) {
            StringBuilder stringBuilder = new StringBuilder(queryConstructor.getOrderBy());
            String[] orderByElements = stringBuilder.substring(stringBuilder.indexOf("ORDER BY") + "ORDER BY".length() + 1).split(",");

            for (String orderByElement : orderByElements) {
                if (orderByElements.length == 1) {
                    stringBuilder.replace(0, stringBuilder.length(), "");

                    queryConstructor.setOrderBy(stringBuilder.toString(), "stepBack");
                    queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                    return;
                }
                if (orderByElement.contains(checkBox.getText())) {
                    stringBuilder.replace(stringBuilder.indexOf(orderByElement), stringBuilder.indexOf(orderByElement) + orderByElement.length() + 1, "");
                }
            }

            if (stringBuilder.lastIndexOf(",") == stringBuilder.length() - 1) {
                stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "");
            }

            queryConstructor.setOrderBy(stringBuilder.toString(), "stepBack");
            queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
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
