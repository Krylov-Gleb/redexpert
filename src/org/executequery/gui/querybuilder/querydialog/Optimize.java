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
import java.awt.event.ItemEvent;
import java.util.Objects;

public class Optimize extends JDialog {

    private final QueryConstructor queryConstructor;
    private final QBPanel queryBuilderPanel;
    private JPanel panelPlacingComponents;
    private JPanel panelButton;
    private JLabel labelOptimize;
    private JLabel labelTestOptimize;
    private JComboBox<String> comboBoxFirstAndAll;
    private JTextField textFieldTestOptimize;
    private JButton buttonAddOptimize;
    private JButton buttonRemoveOptimize;

    public Optimize(QueryConstructor queryConstructor, QBPanel queryBuilderPanel) {
        this.queryConstructor = queryConstructor;
        this.queryBuilderPanel = queryBuilderPanel;
        init();
    }

    private void init() {
        initPanel();
        initLabel();
        initComboBox();
        initTextField();
        initButton();
        arrangeComponents();
    }

    private void initPanel() {
        panelPlacingComponents = WidgetFactory.createPanel("panelPlacingComponents");
        panelPlacingComponents.setLayout(new GridBagLayout());

        panelButton = WidgetFactory.createPanel("panelButton");
        panelButton.setLayout(new GridBagLayout());
    }

    private void initLabel() {
        labelOptimize = WidgetFactory.createLabel(Bundles.get("QueryBuilder.Optimize.labelOptimize"));
        labelTestOptimize = WidgetFactory.createLabel(Bundles.get("QueryBuilder.Optimize.labelTestOptimize"));
    }

    private void initComboBox() {
        comboBoxFirstAndAll = WidgetFactory.createComboBox("comboBoxFirstAndAll", new String[]{"ALL", "FIRST"});
        comboBoxFirstAndAll.setToolTipText(Bundles.get("QueryBuilder.Optimize.optimizationMethod"));
        comboBoxFirstAndAll.setMinimumSize(new Dimension(200, 25));
        comboBoxFirstAndAll.setPreferredSize(new Dimension(200, 25));
        comboBoxFirstAndAll.setMaximumSize(new Dimension(200, 25));
        comboBoxFirstAndAll.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                eventChanged();
            }
        });
    }

    private void eventChanged() {
        if (Objects.requireNonNull(comboBoxFirstAndAll.getSelectedItem()).toString().equals("ALL")) {
            textFieldTestOptimize.setText("OPTIMIZE FOR ALL ROWS");
        }
        if (comboBoxFirstAndAll.getSelectedItem().toString().equals("FIRST")) {
            textFieldTestOptimize.setText("OPTIMIZE FOR FIRST ROWS");
        }
    }

    private void initTextField() {
        textFieldTestOptimize = WidgetFactory.createTextField("textFieldTestOptimize", "OPTIMIZE FOR ALL ROWS");
        textFieldTestOptimize.setToolTipText(Bundles.get("QueryBuilder.Optimize.whatItWillLookLike"));
        textFieldTestOptimize.setEditable(false);
        textFieldTestOptimize.setMinimumSize(new Dimension(200, 25));
        textFieldTestOptimize.setPreferredSize(new Dimension(200, 25));
        textFieldTestOptimize.setMaximumSize(new Dimension(200, 25));
    }

    private void initButton() {
        buttonAddOptimize = WidgetFactory.createButton("buttonAddOptimize", Bundles.get("common.add.button"), event -> eventButtonAddOptimize());

        buttonRemoveOptimize = WidgetFactory.createButton("buttonRemoveOptimize", Bundles.get("common.delete.button"), event -> eventButtonRemoveOptimize());

        placingButtonsInPanel();
    }

    private void placingButtonsInPanel() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(5, 5, 5, 5).fillHorizontally();
        panelButton.add(buttonAddOptimize, gridBagHelper.setXY(0, 0).setMaxWeightX().get());
        panelButton.add(buttonRemoveOptimize, gridBagHelper.nextCol().setMaxWeightX().get());
    }

    private void arrangeComponents() {
        arrangeComponentsInPanelForPlacingComponents();
        configurationDialog();
    }

    private void arrangeComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(10, 5, 10, 5).fillHorizontally();
        panelPlacingComponents.add(labelOptimize, gridBagHelper.setXY(0, 0).setMinWeightX().get());
        panelPlacingComponents.add(comboBoxFirstAndAll, gridBagHelper.nextCol().setMaxWeightX().setWidth(2).get());
        panelPlacingComponents.add(labelTestOptimize, gridBagHelper.previousCol().nextRow().setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(textFieldTestOptimize, gridBagHelper.nextCol().setMaxWeightX().setWidth(2).get());
        panelPlacingComponents.add(panelButton, gridBagHelper.previousCol().nextRow().spanX().spanY().setMaxWeightX().get());
    }

    private void configurationDialog() {
        setLayout(new BorderLayout());
        add(panelPlacingComponents, BorderLayout.CENTER);
        setTitle(Bundles.get("QueryBuilder.Optimize.title"));
        setResizable(false);
        setIconImage(getAndCreateIconDialog().getImage());
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(400, 160);
        setVisible(true);
    }

    private void eventButtonAddOptimize() {
        queryConstructor.setOptimization(textFieldTestOptimize.getText(), "stepBack");
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
        closeDialog();
    }

    private void eventButtonRemoveOptimize() {
        queryConstructor.setOptimization("", "stepBack");
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
        closeDialog();
    }

    private ImageIcon getAndCreateIconDialog() {
        return IconManager.getIcon(BrowserConstants.APPLICATION_IMAGE, "svg", 512, IconManager.IconFolder.BASE);
    }

    private void closeDialog() {
        setVisible(false);
        dispose();
    }

}
