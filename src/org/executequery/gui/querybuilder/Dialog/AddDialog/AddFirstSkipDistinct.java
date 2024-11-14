package org.executequery.gui.querybuilder.Dialog.AddDialog;

import org.executequery.gui.WidgetFactory;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.executequery.gui.querybuilder.WorkQuryEditor.QueryConstructor;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;

/**
 * A class that creates a dialog (window) for adding first,skip,distinct.
 * <p>
 * Класс создающий диалог (окно) для добавления first,skip,distinct.
 *
 * @author Krylov Gleb
 */
public class AddFirstSkipDistinct extends JDialog {

    // --- Elements accepted using the constructor. ----
    // --- Поля, которые передаются через конструктор. ---

    private QueryConstructor queryConstructor;
    private QueryBuilderPanel queryBuilderPanel;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelForPlacingComponents;
    private JLabel labelFirst;
    private JLabel labelSkip;
    private JTextField textFieldSettingFirst;
    private JTextField textFieldSettingSkip;
    private JCheckBox checkBoxSettingDistinct;
    private JButton buttonAddFirstSkipDistinctInQuery;

    /**
     * A dialog (window) is created to add first,skip,distinct to the request.
     * The initialization method is used.
     * <p>
     * Создаётся диалог (окно) для добавления first,skip,distinct в запрос.
     * Используется метод для инициализации.
     */
    public AddFirstSkipDistinct(QueryConstructor createStringQuery, QueryBuilderPanel queryBuilderPanel) {
        this.queryConstructor = createStringQuery;
        this.queryBuilderPanel = queryBuilderPanel;
        init();
    }

    /**
     * A method for initializing fields.
     * A method is used to place the components.
     * <p>
     * Метод для  инициализации полей.
     * Используется метод для размещения компонентов.
     */
    private void init() {
        initPanels();
        initLabels();
        initTextField();
        initCheckBoxes();
        initButtons();
        arrangeComponents();
    }

    /**
     * A method for initializing buttons.
     * <p>
     * Метод для инициализации кнопок.
     */
    private void initButtons() {
        buttonAddFirstSkipDistinctInQuery = WidgetFactory.createButton("buttonAddFirstSkipDistinctInQuery", "Создать", event -> {
            eventButtonAddFirstSkipDistinct();
        });
    }

    /**
     * The method for initializing CheckBoxes.
     * <p>
     * Метод для инициализации CheckBoxes.
     */
    private void initCheckBoxes() {
        checkBoxSettingDistinct = WidgetFactory.createCheckBox("checkBoxSettingDistinct", "DISTINCT");
    }

    /**
     * A method for initializing a text field.
     * <p>
     * Метод для инициализации текстового поля.
     */
    private void initTextField() {
        textFieldSettingFirst = WidgetFactory.createTextField("textFieldSettingFirst");
        textFieldSettingSkip = WidgetFactory.createTextField("textFieldSettingSkip");
    }

    /**
     * A method for initializing labels.
     * <p>
     * Метод для инициализации меток.
     */
    private void initLabels() {
        labelFirst = WidgetFactory.createLabel("Укажите число (FIRST)");
        labelSkip = WidgetFactory.createLabel("Укажите число (SKIP)");
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
        setTitle("First Skip Distinct");
        setIconImage(new ImageIcon("red_expert.png").getImage());
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(panelForPlacingComponents, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(400, 200);
        setVisible(true);
    }

    /**
     * Placing components in a panel to place components.
     * <p>
     * Размещение компонентов в панели для размещения компонентов.
     */
    private void arrangeComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorCenter().setInsets(5, 5, 5, 5).fillHorizontally();
        panelForPlacingComponents.add(labelFirst, gridBagHelper.setXY(0, 0).setMinWeightX().get());
        panelForPlacingComponents.add(textFieldSettingFirst, gridBagHelper.setXY(1, 0).setMaxWeightX().setWidth(2).get());
        panelForPlacingComponents.add(labelSkip, gridBagHelper.setXY(0, 1).setMinWeightX().setWidth(1).get());
        panelForPlacingComponents.add(textFieldSettingSkip, gridBagHelper.setXY(1, 1).setMaxWeightX().setWidth(2).get());
        panelForPlacingComponents.add(checkBoxSettingDistinct, gridBagHelper.setXY(1, 2).setMinWeightX().setWidth(1).get());
        panelForPlacingComponents.add(buttonAddFirstSkipDistinctInQuery, gridBagHelper.setXY(1, 3).setMinWeightX().get());
    }

    /**
     * A button event that implements adding first skip distinct to the request.
     * <p>
     * Событие кнопки реализующее добавления first skip distinct в запрос.
     */
    private void eventButtonAddFirstSkipDistinct() {
        addFirst();
        addSkip();
        addDistinct();
        setTextInQueryBuilderEditorText();
        closeDialog();
    }

    /**
     * A method for changing the text on the test query display screen in the query designer.
     * <p>
     * Метод для смены текста на экране отображения тестовых запросов в конструкторе запросов.
     */
    private void setTextInQueryBuilderEditorText() {
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
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

    /**
     * A method for adding Distinct to a query.
     * <p>
     * Метод для добавления Distinct в запрос.
     */
    private void addDistinct() {
        if (checkBoxSettingDistinct.isSelected()) {
            queryConstructor.setDistinct("DISTINCT");
        } else {
            queryConstructor.setDistinct("ALL");
        }
    }

    /**
     * The method for adding skip to the request.
     * <p>
     * Метод для добавления skip в запрос.
     */
    private void addSkip() {
        if (!textFieldSettingSkip.getText().isEmpty()) {
            try {
                if (Integer.parseInt(textFieldSettingSkip.getText()) > 0) {
                    queryConstructor.addSkip(textFieldSettingSkip.getText());
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }

    /**
     * The method for adding first to the request.
     * <p>
     * Метод для добавления first в запрос.
     */
    private void addFirst() {
        if (!textFieldSettingFirst.getText().isEmpty()) {
            try {
                if (Integer.parseInt(textFieldSettingFirst.getText()) > 0) {
                    queryConstructor.addFirst(textFieldSettingFirst.getText());
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }


}
