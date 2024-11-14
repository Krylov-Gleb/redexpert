package org.executequery.gui.querybuilder.Dialog.AddDialog;

import org.executequery.gui.WidgetFactory;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.executequery.gui.querybuilder.WorkQuryEditor.QueryConstructor;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * A class that creates a dialog (window) for adding connections to a query.
 * <p>
 * Класс создающий диалог (окно) для добавления соединений (join) в запрос.
 *
 * @author Krylov Gleb
 */
public class AddJoins extends JDialog {

    // --- Elements accepted using the constructor ----
    // --- Поля, которые передаются через конструктор. ---

    private QueryConstructor queryConstructor;
    private QueryBuilderPanel queryBuilderPanel;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelForPlacingComponents;
    private JComboBox<String> leftComboBoxTables;
    private JComboBox<String> leftComboBoxAttributesTables;
    private JComboBox<String> rightComboBoxTables;
    private JComboBox<String> rightComboBoxAttributeTables;
    private JComboBox<String> comboBoxJoins;
    private JButton buttonAddJoinInQuery;

    private ArrayList<JTable> arrayTable;
    private final String[] arrayJoin = new String[]{"Inner Join", "Left Join", "Right Join", "Full Outer Join", "Natural Join", "Cross Join", "Join не выбран"};

    /**
     * Creating a dialog (window) that will add connections to the request.
     * <p>
     * Создаём диалог (окно) который будет добавлять соединения (join)  в запрос.
     */
    public AddJoins(QueryBuilderPanel queryBuilderPanel, QueryConstructor createStringQuery) {
        this.queryBuilderPanel = queryBuilderPanel;
        this.queryConstructor = createStringQuery;
        init();
    }

    /**
     * Method for initializing fields.
     * <p>
     * Метод для инициализации полей.
     */
    private void init() {
        initPanels();
        initArrays();
        initComboBoxes();
        initButtons();
        arrangeComponents();
    }

    /**
     * A method for initializing buttons.
     * <p>
     * Метод для инициализации кнопок.
     */
    private void initButtons() {
        buttonAddJoinInQuery = WidgetFactory.createButton("buttonAddJoinInQuery", "Создать", event -> {
            buttonEventAddJoin();
        });
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
     * A method for initializing arrays.
     * <p>
     * Метод для инициализации массивов.
     */
    private void initArrays() {
        arrayTable = queryBuilderPanel.getListTable();
    }

    /**
     * A method for initializing drop-down lists (comboBox).
     * <p>
     * Метод для инициализации выпадающих списков (comboBox).
     */
    private void initComboBoxes() {
        leftComboBoxAttributesTables = WidgetFactory.createComboBox("leftComboBoxAttributesTables");
        leftComboBoxAttributesTables.setPreferredSize(new Dimension(150, 30));

        rightComboBoxAttributeTables = WidgetFactory.createComboBox("rightComboBoxAttributeTables");
        rightComboBoxAttributeTables.setPreferredSize(new Dimension(150, 30));

        leftComboBoxTables = WidgetFactory.createComboBox("leftComboBoxTables", queryBuilderPanel.getListNameTable());
        leftComboBoxTables.setPreferredSize(new Dimension(150, 30));
        addItemInLeftComboBoxAttributesTables();
        eventChangeLeftComboBoxTables();

        rightComboBoxTables = WidgetFactory.createComboBox("rightComboBoxTables", queryBuilderPanel.getListNameTable());
        rightComboBoxTables.setPreferredSize(new Dimension(150, 30));
        addItemInRightComboBoxAttributesTables();
        eventChangeRightComboBoxTables();

        comboBoxJoins = WidgetFactory.createComboBox("comboBoxJoins", arrayJoin);
        comboBoxJoins.setSelectedIndex(arrayJoin.length - 1);
        comboBoxJoins.setPreferredSize(new Dimension(150, 30));
    }

    /**
     * The event of changing an element in the ComboBox (Right).
     * <p>
     * Событие изменения элемента в выпадающем списке. (Правый)
     */
    private void eventChangeRightComboBoxTables() {
        rightComboBoxTables.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                rightComboBoxAttributeTables.removeAllItems();
                addItemInRightComboBoxAttributesTables();
            }
        });
    }

    /**
     * The event of changing an element in the ComboBox. (Left)
     * <p>
     * Событие изменения элемента в выпадающем списке. (Левый)
     */
    private void eventChangeLeftComboBoxTables() {
        leftComboBoxTables.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                leftComboBoxAttributesTables.removeAllItems();
                addItemInLeftComboBoxAttributesTables();
            }
        });
    }

    /**
     * Methods that add attribute values to the selected table. (Left)
     * <p>
     * Методы добавляющие  значения атрибутов выбранной таблицы. (Левый)
     */
    private void addItemInLeftComboBoxAttributesTables() {
        for (int i = 0; i < arrayTable.size(); i++) {
            if (arrayTable.get(i).getColumnName(0).equals(leftComboBoxTables.getSelectedItem().toString())) {
                for (int j = 0; j < arrayTable.get(i).getRowCount(); j++) {
                    leftComboBoxAttributesTables.addItem(arrayTable.get(i).getValueAt(j, 0).toString());
                }
            }
        }
    }

    /**
     * Methods that add attribute values to the selected table. (Right)
     * <p>
     * Методы добавляющие  значения атрибутов выбранной таблицы. (Правый)
     */
    private void addItemInRightComboBoxAttributesTables() {
        for (int i = 0; i < arrayTable.size(); i++) {
            if (arrayTable.get(i).getColumnName(0).equals(rightComboBoxTables.getSelectedItem().toString())) {
                for (int j = 0; j < arrayTable.get(i).getRowCount(); j++) {
                    rightComboBoxAttributeTables.addItem(arrayTable.get(i).getValueAt(j, 0).toString());
                }
            }
        }
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
     * Configuring the parameters of the dialog (window) created by this class.
     * <p>
     * Настройка параметров диалога (окна) созданного этим классом.
     */
    private void configurationDialog() {
        setLayout(new BorderLayout());
        setTitle("Добавить соединение (Join)");
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setIconImage(new ImageIcon("red_expert.png").getImage());
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        addWindowClosingInDialog();
        add(panelForPlacingComponents, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(600, 400);
        setVisible(true);
    }

    /**
     * A method for adding an action to a dialog (window) when closing.
     * <p>
     * Метод для добавления действия диалогу (окну) при закрытии.
     */
    private void addWindowClosingInDialog(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                queryConstructor.addAttributes(queryBuilderPanel.getListTable());
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                setVisible(false);
                dispose();
            }
        });
    }

    /**
     * The method of placing components on the panel for placing components.
     * <p>
     * Метод размещения компонентов на панели для размещения компонентов.
     */
    private void arrangeComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().setInsets(5,5,5,5).anchorCenter().fillHorizontally();
        panelForPlacingComponents.add(leftComboBoxTables, gridBagHelper.setXY(0,0).get());
        panelForPlacingComponents.add(rightComboBoxTables, gridBagHelper.setXY(2,0).get());
        panelForPlacingComponents.add(comboBoxJoins, gridBagHelper.setXY(1,0).get());
        panelForPlacingComponents.add(leftComboBoxAttributesTables, gridBagHelper.setXY(0,1).get());
        panelForPlacingComponents.add(rightComboBoxAttributeTables, gridBagHelper.setXY(2,1).get());
        panelForPlacingComponents.add(buttonAddJoinInQuery, gridBagHelper.setXY(1,2).get());
    }

    /**
     * The method in which the functionality of the button for adding connections (Join) to the request is prescribed.
     * <p>
     * Метод в котором прописан функционал кнопки для добавления соединений (Join) в запрос.
     */
    private void buttonEventAddJoin() {
        queryConstructor.addJoins(leftComboBoxTables.getSelectedItem().toString(), rightComboBoxTables.getSelectedItem().toString(), comboBoxJoins.getSelectedItem().toString(), leftComboBoxAttributesTables.getSelectedItem().toString(), rightComboBoxAttributeTables.getSelectedItem().toString());
        JOptionPane.showMessageDialog(queryBuilderPanel,"Join добавлен!","Соединение (Join)",JOptionPane.QUESTION_MESSAGE);
    }

}
