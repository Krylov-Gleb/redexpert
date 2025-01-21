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

public class FirstSkipDistinct extends JDialog {

    private final QueryConstructor queryConstructor;
    private final QBPanel queryBuilderPanel;
    private JPanel panelPlacingComponents;
    private JPanel panelButton;
    private JLabel labelFirst;
    private JLabel labelSkip;
    private JTextField textFieldFirst;
    private JTextField textFieldSkip;
    private JCheckBox checkBoxDistinct;
    private JButton buttonAddFirstSkipDistinct;
    private JButton buttonRemoveFirstSkipDistinct;

    public FirstSkipDistinct(QueryConstructor queryConstructor, QBPanel queryBuilderPanel) {
        this.queryConstructor = queryConstructor;
        this.queryBuilderPanel = queryBuilderPanel;
        init();
    }

    private void init() {
        initPanel();
        initLabel();
        initButton();
        initTextField();
        initCheckBox();
        arrangeComponents();
    }

    private void initButton() {
        buttonAddFirstSkipDistinct = WidgetFactory.createButton("buttonAddFirstSkipDistinct", Bundles.get("common.add.button"), event -> {
            eventButtonAddFirstSkipDistinct();
            closeDialog();
        });

        buttonRemoveFirstSkipDistinct = WidgetFactory.createButton("buttonRemoveFirstSkipDistinct", Bundles.get("common.delete.button"), event -> {
            eventButtonRemoveFirstSkipDistinct();
            closeDialog();
        });

        placingButtonsInPanel();
    }

    private void placingButtonsInPanel() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(5, 5, 5, 5).fillHorizontally();
        panelButton.add(buttonAddFirstSkipDistinct, gridBagHelper.setXY(0, 0).setMaxWeightX().get());
        panelButton.add(buttonRemoveFirstSkipDistinct, gridBagHelper.nextCol().setMaxWeightX().get());
    }

    private void initCheckBox() {
        checkBoxDistinct = WidgetFactory.createCheckBox("checkBoxDistinct", "DISTINCT");
        checkBoxDistinct.setToolTipText(Bundles.get("QueryBuilder.FirstSkipDistinct.toolTipTextDistinct"));
        checkUsingDistinct();
    }

    private void checkUsingDistinct() {
        String distinct = queryConstructor.getDistinct();
        checkBoxDistinct.setSelected(!distinct.isEmpty());
    }

    private void initTextField() {
        textFieldFirst = WidgetFactory.createTextField("textFieldFirst");
        textFieldFirst.setText(queryConstructor.getFirst());
        textFieldFirst.setToolTipText(Bundles.get("QueryBuilder.FirstSkipDistinct.enterNumber"));
        textFieldFirst.setMinimumSize(new Dimension(200, 25));
        textFieldFirst.setPreferredSize(new Dimension(200, 25));
        textFieldFirst.setMaximumSize(new Dimension(200, 25));

        textFieldSkip = WidgetFactory.createTextField("textFieldSkip");
        textFieldSkip.setText(queryConstructor.getSkip());
        textFieldSkip.setToolTipText(Bundles.get("QueryBuilder.FirstSkipDistinct.enterNumber"));
        textFieldSkip.setMinimumSize(new Dimension(200, 25));
        textFieldSkip.setPreferredSize(new Dimension(200, 25));
        textFieldSkip.setMaximumSize(new Dimension(200, 25));
    }

    private void initLabel() {
        labelFirst = WidgetFactory.createLabel(Bundles.get("QueryBuilder.FirstSkipDistinct.labelFirst"));
        labelSkip = WidgetFactory.createLabel(Bundles.get("QueryBuilder.FirstSkipDistinct.labelSkip"));
    }

    private void initPanel() {
        panelPlacingComponents = WidgetFactory.createPanel("panelPlacingComponents");
        panelPlacingComponents.setLayout(new GridBagLayout());

        panelButton = WidgetFactory.createPanel("panelButton");
        panelButton.setLayout(new GridBagLayout());
    }

    private void arrangeComponents() {
        arrangeComponentsInPanelForPlacingComponents();
        configurationDialog();
    }

    private void configurationDialog() {
        setLayout(new BorderLayout());
        setTitle(Bundles.get("QueryBuilder.FirstSkipDistinct.title"));
        setResizable(false);
        setIconImage(getAndCreateIconDialog().getImage());
        add(panelPlacingComponents, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(400, 190);
        setVisible(true);
    }

    private void arrangeComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorCenter().setInsets(10, 5, 10, 5).setWidth(1).fillHorizontally();
        panelPlacingComponents.add(labelFirst, gridBagHelper.setXY(0, 0).setMinWeightX().get());
        panelPlacingComponents.add(textFieldFirst, gridBagHelper.nextCol().setMaxWeightX().spanX().get());
        panelPlacingComponents.add(labelSkip, gridBagHelper.previousCol().nextRow().setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(textFieldSkip, gridBagHelper.nextCol().setMaxWeightX().spanX().get());
        panelPlacingComponents.add(checkBoxDistinct, gridBagHelper.previousCol().nextRow().setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(panelButton, gridBagHelper.nextRow().spanX().spanY().setMinWeightX().get());

    }

    private void eventButtonAddFirstSkipDistinct() {
        addFirst();
        addSkip();
        addDistinct();
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    private void eventButtonRemoveFirstSkipDistinct() {
        queryConstructor.setSkip("", "stepBack");
        queryConstructor.setFirst("", "stepBack");
        queryConstructor.setDistinct("", "stepBack");
        textFieldFirst.setText("");
        textFieldSkip.setText("");
        checkBoxDistinct.setSelected(false);
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    private void addDistinct() {
        if (checkBoxDistinct.isSelected()) {
            queryConstructor.setDistinct("DISTINCT", "stepBack");
        } else {
            queryConstructor.setDistinct("", "stepBack");
        }
    }

    private void addSkip() {
        if (!textFieldSkip.getText().isEmpty()) {
            try {
                if (Integer.parseInt(textFieldSkip.getText()) > 0) {
                    queryConstructor.setSkip(textFieldSkip.getText(), "stepBack");

                    textFieldSkip.getText();
                    return;
                }
            } catch (NumberFormatException ignored) {
            }
        } else {
            queryConstructor.setSkip("", "stepBack");
        }

        textFieldSkip.getText();
    }

    private void addFirst() {
        if (!textFieldFirst.getText().isEmpty()) {
            try {
                if (Integer.parseInt(textFieldFirst.getText()) > 0) {
                    queryConstructor.setFirst(textFieldFirst.getText(), "stepBack");
                    textFieldFirst.getText();
                    return;
                }
            } catch (NumberFormatException ignored) {
            }
        } else {
            queryConstructor.setFirst("", "stepBack");
        }

        textFieldFirst.getText();
    }

    private ImageIcon getAndCreateIconDialog() {
        return IconManager.getIcon(BrowserConstants.APPLICATION_IMAGE, "svg", 512, IconManager.IconFolder.BASE);
    }

    private void closeDialog() {
        setVisible(false);
        dispose();
    }

}
