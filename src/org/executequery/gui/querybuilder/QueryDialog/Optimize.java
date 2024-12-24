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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * This class creates a dialog (window) that adds optimization to the query.
 * <p>
 * Этот класс создаёт диалог (окно) который добавляет в запрос оптимизацию.
 *
 * @author Krylov Gleb
 */
public class Optimize extends JDialog {

    // --- Fields that are passed through the constructor ----
    // --- Поля, которые передаются через конструктор ---

    private QueryConstructor queryConstructor;
    private QBPanel queryBuilderPanel;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelPlacingComponents;
    private JPanel panelButton;
    private JLabel labelOptimize;
    private JLabel labelTestOptimize;
    private JComboBox<String> comboBoxFirstAndAll;
    private JTextField textFieldTestOptimize;
    private JButton buttonAddOptimize;
    private JButton buttonRemoveOptimize;

    /**
     * A dialog (window) is created.
     * A method is used to initialize fields.
     * <p>
     * Создаётся диалог (окно).
     * Используется метод для инициализации полей.
     */
    public Optimize(QueryConstructor queryConstructor, QBPanel queryBuilderPanel) {
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
        initComboBox();
        initTextField();
        initButton();
        arrangeComponents();
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
     * A method for initializing labels.
     * <p>
     * Метод для инициализации меток.
     */
    private void initLabel() {
        labelOptimize = WidgetFactory.createLabel(Bundles.get("QueryBuilder.Optimize.labelOptimize"));
        labelTestOptimize = WidgetFactory.createLabel(Bundles.get("QueryBuilder.Optimize.labelTestOptimize"));
    }

    /**
     * A method for initializing drop-down lists (comboBox).
     * <p>
     * Метод для инициализации выпадающих списков (comboBox).
     */
    private void initComboBox() {
        comboBoxFirstAndAll = WidgetFactory.createComboBox("comboBoxFirstAndAll", new String[]{"ALL", "FIRST"});
        comboBoxFirstAndAll.setToolTipText(Bundles.get("QueryBuilder.Optimize.optimizationMethod"));
        comboBoxFirstAndAll.setMinimumSize(new Dimension(200, 25));
        comboBoxFirstAndAll.setPreferredSize(new Dimension(200, 25));
        comboBoxFirstAndAll.setMaximumSize(new Dimension(200, 25));
        comboBoxFirstAndAll.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    eventChanged();
                }
            }
        });
    }

    /**
     * The event that will be triggered when the state of the drop-down list (ComboBox) changes.
     * <p>
     * Событие которое будет срабатывать при изменении состояния выпадающего (ComboBox) списка.
     */
    private void eventChanged() {
        if (comboBoxFirstAndAll.getSelectedItem().toString().equals("ALL")) {
            textFieldTestOptimize.setText("OPTIMIZE FOR ALL ROWS");
        }
        if (comboBoxFirstAndAll.getSelectedItem().toString().equals("FIRST")) {
            textFieldTestOptimize.setText("OPTIMIZE FOR FIRST ROWS");
        }
    }

    /**
     * A method for initializing a text field.
     * <p>
     * Метод для инициализации текстового поля.
     */
    private void initTextField() {
        textFieldTestOptimize = WidgetFactory.createTextField("textFieldTestOptimize", "OPTIMIZE FOR ALL ROWS");
        textFieldTestOptimize.setToolTipText(Bundles.get("QueryBuilder.Optimize.whatItWillLookLike"));
        textFieldTestOptimize.setEditable(false);
        textFieldTestOptimize.setMinimumSize(new Dimension(200, 25));
        textFieldTestOptimize.setPreferredSize(new Dimension(200, 25));
        textFieldTestOptimize.setMaximumSize(new Dimension(200, 25));
    }

    /**
     * A method for initializing buttons.
     * <p>
     * Метод для инициализации кнопок.
     */
    private void initButton() {
        buttonAddOptimize = WidgetFactory.createButton("buttonAddOptimize", Bundles.get("common.add.button"), event -> {
            eventButtonAddOptimize();
        });

        buttonRemoveOptimize = WidgetFactory.createButton("buttonRemoveOptimize", Bundles.get("common.delete.button"), event -> {
            eventButtonRemoveOptimize();
        });

        placingButtonsInPanel();
    }

    /**
     * A method for placing buttons in a panel to place buttons.
     * <p>
     * Метод для размещения кнопок в панели для размещения кнопок.
     */
    private void placingButtonsInPanel() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(5, 5, 5, 5).fillHorizontally();
        panelButton.add(buttonAddOptimize, gridBagHelper.setXY(0, 0).setMaxWeightX().get());
        panelButton.add(buttonRemoveOptimize, gridBagHelper.nextCol().setMaxWeightX().get());
    }

    /**
     * A method for placing components as well as setting up a dialog (window).
     * <p>
     * Метод для размещения компонентов а так же настройки диалога (окна).
     */
    private void arrangeComponents() {
        arrangeComponentsInPanelForPlacingComponents();
        configurationDialog();
    }

    /**
     * A method for placing components in a panel for placing components.
     * <p>
     * Метод для размещения компонентов в панели для размещения компонентов.
     */
    private void arrangeComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(10, 5, 10, 5).fillHorizontally();
        panelPlacingComponents.add(labelOptimize, gridBagHelper.setXY(0, 0).setMinWeightX().get());
        panelPlacingComponents.add(comboBoxFirstAndAll, gridBagHelper.nextCol().setMaxWeightX().setWidth(2).get());
        panelPlacingComponents.add(labelTestOptimize, gridBagHelper.previousCol().nextRow().setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(textFieldTestOptimize, gridBagHelper.nextCol().setMaxWeightX().setWidth(2).get());
        panelPlacingComponents.add(panelButton, gridBagHelper.previousCol().nextRow().spanX().spanY().setMaxWeightX().get());
    }

    /**
     * A method for configuring the parameters of a dialog (window) created by this class.
     * <p>
     * Метод для настройки параметров диалога (окна) созданного этим классом.
     */
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

    /**
     * A method that implements the functionality of adding optimization to a query.
     * <p>
     * Метод реализующий функционал добавления оптимизации в запрос.
     */
    private void eventButtonAddOptimize() {
        queryConstructor.setOptimization(textFieldTestOptimize.getText());
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
        closeDialog();
    }

    /**
     * A method that implements the functionality of removing optimization from a query.
     * <p>
     * Метод реализующий функционал удаления оптимизации из запроса.
     */
    private void eventButtonRemoveOptimize() {
        queryConstructor.setOptimization("");
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
        closeDialog();
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
