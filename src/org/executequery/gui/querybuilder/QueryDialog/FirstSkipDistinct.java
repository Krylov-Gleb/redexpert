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

/**
 * This class creates a dialog (window) that adds additions to the request (first skip distinct).
 * <p>
 * Этот класс создаёт диалог (окно) который добавляет в запрос дополнения (first skip distinct).
 *
 * @author Krylov Gleb
 */
public class FirstSkipDistinct extends JDialog {

    // --- Fields that are passed through the constructor ----
    // --- Поля, которые передаются через конструктор ---

    private QueryConstructor queryConstructor;
    private QBPanel queryBuilderPanel;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelPlacingComponents;
    private JPanel panelButton;
    private JLabel labelFirst;
    private JLabel labelSkip;
    private JTextField textFieldFirst;
    private JTextField textFieldSkip;
    private JCheckBox checkBoxDistinct;
    private JButton buttonAddFirstSkipDistinct;
    private JButton buttonRemoveFirstSkipDistinct;

    /**
     * A dialog (window) is created.
     * A method is used to initialize fields.
     * <p>
     * Создаётся диалог (окно).
     * Используется метод для инициализации полей.
     */
    public FirstSkipDistinct(QueryConstructor queryConstructor, QBPanel queryBuilderPanel) {
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
        initLabel();
        initButton();
        initTextField();
        initCheckBox();
        arrangeComponents();
    }

    /**
     * A method for initializing buttons.
     * <p>
     * Метод для инициализации кнопок.
     */
    private void initButton() {
        buttonAddFirstSkipDistinct = WidgetFactory.createButton("buttonAddFirstSkipDistinct",Bundles.get("common.add.button"),event -> {
            eventButtonAddFirstSkipDistinct();
        });

        buttonRemoveFirstSkipDistinct = WidgetFactory.createButton("buttonRemoveFirstSkipDistinct",Bundles.get("common.delete.button"),event -> {
            eventButtonRemoveFirstSkipDistinct();
        });

        placingButtonsInPanel();
    }

    /**
     * A method for placing buttons in a panel to place buttons.
     * <p>
     * Метод для размещения кнопок в панели для размещения кнопок.
     */
    private void placingButtonsInPanel() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(5,5,5,5).fillHorizontally();
        panelButton.add(buttonAddFirstSkipDistinct,gridBagHelper.setXY(0,0).setMaxWeightX().get());
        panelButton.add(buttonRemoveFirstSkipDistinct,gridBagHelper.nextCol().setMaxWeightX().get());
    }

    /**
     * Method for initializing checkboxes (JCheckBox).
     * <p>
     * Метод для инициализации флажков (JCheckBox)
     */
    private void initCheckBox() {
        checkBoxDistinct = WidgetFactory.createCheckBox("checkBoxDistinct", "DISTINCT");
        checkBoxDistinct.setToolTipText(Bundles.get("QueryBuilder.FirstSkipDistinct.toolTipTextDistinct"));
        checkUsingDistinct();
    }

    /**
     * A method to check whether Distinct is used.
     * <p>
     * Метод для проверки того, используется ли Distinct.
     */
    private void checkUsingDistinct() {
        String distinct = queryConstructor.getDistinct();
        if(distinct.isEmpty()){
            checkBoxDistinct.setSelected(false);
        }
        else{
            checkBoxDistinct.setSelected(true);
        }
    }

    /**
     * A method for initializing a text field.
     * <p>
     * Метод для инициализации текстового поля.
     */
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

    /**
     * A method for initializing labels.
     * <p>
     * Метод для инициализации меток.
     */
    private void initLabel() {
        labelFirst = WidgetFactory.createLabel(Bundles.get("QueryBuilder.FirstSkipDistinct.labelFirst"));
        labelSkip = WidgetFactory.createLabel(Bundles.get("QueryBuilder.FirstSkipDistinct.labelSkip"));
    }

    /**
     * The method for initializing JPanel.
     * <p>
     * Метод для инициализации JPanel.
     */
    private void initPanel() {
        panelPlacingComponents = WidgetFactory.createPanel("panelPlacingComponents");
        panelPlacingComponents.setLayout(new GridBagLayout());

        panelButton = WidgetFactory.createPanel("panelButton");
        panelButton.setLayout(new GridBagLayout());
    }

    /**
     * A method for placing components and configuring a dialog (window).
     * <p>
     * Метод для размещения компонентов и настройки диалога (окна).
     */
    private void arrangeComponents() {
        arrangeComponentsInPanelForPlacingComponents();
        configurationDialog();
    }

    /**
     * Configuring the dialog (window) created by this class.
     * <p>
     * Настройка диалога (окна) созданного этим классом.
     */
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

    /**
     * Placing components in a panel to place components.
     * <p>
     * Размещение компонентов в панели для размещения компонентов.
     */
    private void arrangeComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorCenter().setInsets(10, 5, 10, 5).setWidth(1).fillHorizontally();
        panelPlacingComponents.add(labelFirst, gridBagHelper.setXY(0, 0).setMinWeightX().get());
        panelPlacingComponents.add(textFieldFirst, gridBagHelper.nextCol().setMaxWeightX().spanX().get());
        panelPlacingComponents.add(labelSkip, gridBagHelper.previousCol().nextRow().setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(textFieldSkip, gridBagHelper.nextCol().setMaxWeightX().spanX().get());
        panelPlacingComponents.add(checkBoxDistinct, gridBagHelper.previousCol().nextRow().setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(panelButton, gridBagHelper.nextRow().spanX().spanY().setMinWeightX().get());

    }

    /**
     * A method that implements the functionality of adding add-ons (first,skip,distinct) to a query.
     * <p>
     * Метод реализующий функционал добавления дополнений (first,skip,distinct) в запрос.
     */
    private void eventButtonAddFirstSkipDistinct() {
        addFirst();
        addSkip();
        addDistinct();
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
        closeDialog();
    }

    /**
     * A method that implements the functionality of removing add-ons (first,skip,distinct) from a query.
     * <p>
     * Метод реализующий функционал удаления дополнений (first,skip,distinct) из запроса.
     */
    private void eventButtonRemoveFirstSkipDistinct(){
        queryConstructor.setSkip("");
        queryConstructor.setFirst("");
        queryConstructor.setDistinct("");
        textFieldFirst.setText("");
        textFieldSkip.setText("");
        checkBoxDistinct.setSelected(false);
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    /**
     * A method for adding Distinct to a query.
     * <p>
     * Метод для добавления Distinct в запрос.
     */
    private String addDistinct() {
        if (checkBoxDistinct.isSelected()) {
            queryConstructor.setDistinct("DISTINCT");
            return "DISTINCT";
        } else {
            queryConstructor.setDistinct("");
            return "";
        }
    }

    /**
     * The method for adding skip to the request.
     * <p>
     * Метод для добавления skip в запрос.
     */
    private String addSkip() {
        if (!textFieldSkip.getText().isEmpty()) {
            try {
                if (Integer.parseInt(textFieldSkip.getText()) > 0) {
                    queryConstructor.setSkip(textFieldSkip.getText());

                    return textFieldSkip.getText();
                }
            } catch (NumberFormatException ignored) {
            }
        } else {
            queryConstructor.setSkip("");
        }

        return textFieldSkip.getText();
    }

    /**
     * The method for adding first to the request.
     * <p>
     * Метод для добавления first в запрос.
     */
    private String addFirst() {
        if (!textFieldFirst.getText().isEmpty()) {
            try {
                if (Integer.parseInt(textFieldFirst.getText()) > 0) {
                    queryConstructor.setFirst(textFieldFirst.getText());

                    return textFieldFirst.getText();
                }
            } catch (NumberFormatException ignored) {
            }
        } else {
            queryConstructor.setFirst("");
            queryConstructor.setFirst("");
        }

        return textFieldFirst.getText();
    }

    /**
     * A method for creating and receiving a dialog icon.
     * <p>
     * Метод для создания и получения иконки диалога.
     */
    private ImageIcon getAndCreateIconDialog(){
        return IconManager.getIcon(BrowserConstants.APPLICATION_IMAGE,"svg",512, IconManager.IconFolder.BASE);
    }

    /**
     * A method for closing a dialog (window) created by this class.
     * <p>
     * Метод для закрытия диалога (окна) созданного этим классом.
     */
    private void closeDialog() {
        setVisible(false);
        dispose();
    }

}
