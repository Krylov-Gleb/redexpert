package org.executequery.gui.querybuilder.Dialog.AddDialog;

import org.executequery.gui.WidgetFactory;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.executequery.gui.querybuilder.WorkQuryEditor.QueryConstructor;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * This class creates a dialog for adding optimization to a query.
 * <p>
 * Этот класс создаёт диалог для добавления оптимизации в запрос.
 *
 * @author Krylov Gleb
 */
public class AddOptimize extends JDialog {

    // --- Elements accepted using the constructor. ----
    // --- Поля, которые передаются через конструктор. ---

    private QueryConstructor queryConstructor;
    private QueryBuilderPanel queryBuilderPanel;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelForPlacingComponents;
    private JLabel labelOptimize;
    private JLabel labelTestOptimize;
    private JComboBox<String> comboBoxOptimizeFirstAndAll;
    private JTextField textFieldTestOptimize;
    private JButton buttonAddOptimize;

    /**
     * Creating a dialog (window) to add optimization to the query.
     * <p>
     * Создаём диалог (окно) для добавления оптимизации в запрос.
     */
    public AddOptimize(QueryConstructor queryConstructor, QueryBuilderPanel queryBuilderPanel){
        this.queryConstructor = queryConstructor;
        this.queryBuilderPanel = queryBuilderPanel;
        init();
    }

    /**
     * A method for initializing fields.
     * <p>
     * Метод для инициализации полей.
     */
    private void init(){
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
    private void initPanel(){
        panelForPlacingComponents = WidgetFactory.createPanel("panelForPlacingComponents");
        panelForPlacingComponents.setLayout(new GridBagLayout());
        panelForPlacingComponents.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    /**
     * A method for initializing labels.
     * <p>
     * Метод для инициализации меток.
     */
    private void initLabel(){
        labelOptimize = WidgetFactory.createLabel("Выберите оптимизацию:");
        labelTestOptimize = WidgetFactory.createLabel("Как будет выглядеть:");
    }

    /**
     * A method for initializing drop-down lists (comboBox).
     * <p>
     * Метод для инициализации выпадающих списков (comboBox).
     */
    private void initComboBox(){
        comboBoxOptimizeFirstAndAll = WidgetFactory.createComboBox("comboBoxOptimizeFirstAndAll",new String[]{"ALL","FIRST"});
        comboBoxOptimizeFirstAndAll.addItemListener(new ItemListener() {
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
    private void eventChanged(){
        if(comboBoxOptimizeFirstAndAll.getSelectedItem().toString().equals("ALL")) {
            textFieldTestOptimize.setText("OPTIMIZE FOR ALL ROWS");
        }
        if(comboBoxOptimizeFirstAndAll.getSelectedItem().toString().equals("FIRST")) {
            textFieldTestOptimize.setText("OPTIMIZE FOR FIRST ROWS");
        }
    }

    /**
     * A method for initializing a text field.
     * <p>
     * Метод для инициализации текстового поля.
     */
    private void initTextField(){
        textFieldTestOptimize = WidgetFactory.createTextField("textFieldTestOptimize","OPTIMIZE FOR ALL ROWS");
        textFieldTestOptimize.setEditable(false);
    }

    /**
     * A method for initializing buttons.
     * <p>
     * Метод для инициализации кнопок.
     */
    private void initButton(){
        buttonAddOptimize = WidgetFactory.createButton("buttonAddOptimize","Добавить",event -> {
            eventButtonAddOptimize();
        });
    }

    /**
     * A method for placing components as well as setting up a dialog (window).
     * <p>
     * Метод для размещения компонентов а так же настройки диалога (окна).
     */
    private void arrangeComponents(){
        arrangeComponentsInPanelForPlacingComponents();
        configurationDialog();
    }

    /**
     * A method for placing components in a panel for placing components.
     * <p>
     * Метод для размещения компонентов в панели для размещения компонентов.
     */
    private void arrangeComponentsInPanelForPlacingComponents(){
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(5,5,5,5).fillHorizontally();
        panelForPlacingComponents.add(labelOptimize,gridBagHelper.setXY(0,0).setMinWeightX().get());
        panelForPlacingComponents.add(comboBoxOptimizeFirstAndAll,gridBagHelper.setXY(1,0).setMaxWeightX().setWidth(2).get());
        panelForPlacingComponents.add(labelTestOptimize,gridBagHelper.setXY(0,1).setMinWeightX().setWidth(1).get());
        panelForPlacingComponents.add(textFieldTestOptimize,gridBagHelper.setXY(1,1).setMaxWeightX().setWidth(2).get());
        panelForPlacingComponents.add(buttonAddOptimize,gridBagHelper.setXY(2,2).setMinWeightX().setWidth(1).spanY().get());
    }

    /**
     * A method for configuring the parameters of a dialog (window) created by this class.
     * <p>
     * Метод для настройки параметров диалога (окна) созданного этим классом.
     */
    private void configurationDialog(){
        setLayout(new BorderLayout());
        add(panelForPlacingComponents, BorderLayout.CENTER);
        setTitle("Добавление оптимизации");
        setIconImage(new ImageIcon("red_expert.png").getImage());
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(400, 180);
        setVisible(true);
    }


    private void eventButtonAddOptimize(){
        queryConstructor.addOptimization(textFieldTestOptimize.getText());
        JOptionPane.showMessageDialog(queryBuilderPanel,"Оптимизация добавлена в запрос.","Оптимизация",JOptionPane.QUESTION_MESSAGE);
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
        closeDialog();
    }

    /**
     * The method for closing the dialog (window).
     * <p>
     * Метод для закрытия диалога (окна).
     */
    private void closeDialog(){
        setVisible(false);
        dispose();
    }

}
