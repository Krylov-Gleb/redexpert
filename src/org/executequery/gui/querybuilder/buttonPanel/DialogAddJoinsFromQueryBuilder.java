package org.executequery.gui.querybuilder.buttonPanel;

import org.executequery.gui.WidgetFactory;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.executequery.gui.querybuilder.WorkQuryEditor.CreateStringQuery;
import org.executequery.gui.querybuilder.toolBar.QueryBuilderToolBar;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

/**
 * A class that creates a dialog (window) for adding connections to a query.
 * <p>
 * Класс создающий диалог (окно) для добавления соединений (join) в запрос.
 *
 * @author Krylov Gleb
 */
public class DialogAddJoinsFromQueryBuilder extends JDialog {

    // --- Elements accepted using the constructor ----
    // --- Поля, которые передаются через конструктор. ---

    private QueryBuilderPanel queryBuilderPanelConstructor;
    private CreateStringQuery createStringQueryConstructor;
    private QueryBuilderToolBar queryBuilderToolBarPanelConstructor;

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
    public DialogAddJoinsFromQueryBuilder(QueryBuilderToolBar queryBuilderToolBarPanel, QueryBuilderPanel queryBuilderPanel, CreateStringQuery createStringQuery) {
        this.queryBuilderToolBarPanelConstructor = queryBuilderToolBarPanel;
        this.queryBuilderPanelConstructor = queryBuilderPanel;
        this.createStringQueryConstructor = createStringQuery;
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

    private void initButtons() {
        buttonAddJoinInQuery = WidgetFactory.createButton("Create Join", "Создать", event -> {
            buttonEventAddJoin();
        });
    }

    private void initPanels() {
        panelForPlacingComponents = WidgetFactory.createPanel("Main Panel");
        panelForPlacingComponents.setLayout(new GridBagLayout());
        panelForPlacingComponents.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    private void initArrays() {
        arrayTable = queryBuilderPanelConstructor.getListTable();
    }

    private void initComboBoxes() {
        leftComboBoxAttributesTables = WidgetFactory.createComboBox("Left Tables Attribute");
        leftComboBoxAttributesTables.setPreferredSize(new Dimension(150, 30));

        rightComboBoxAttributeTables = WidgetFactory.createComboBox("Right Tables Attribute");
        rightComboBoxAttributeTables.setPreferredSize(new Dimension(150, 30));

        leftComboBoxTables = WidgetFactory.createComboBox("Left Tables Combo Box", queryBuilderToolBarPanelConstructor.getArrayNamesTablesFromQueryBuilderToolBar());
        leftComboBoxTables.setPreferredSize(new Dimension(150, 30));
        addItemInLeftComboBoxAttributesTables();
        eventChangeLeftComboBoxTables();

        rightComboBoxTables = WidgetFactory.createComboBox("Right Tables Combo Box", queryBuilderToolBarPanelConstructor.getArrayNamesTablesFromQueryBuilderToolBar());
        rightComboBoxTables.setPreferredSize(new Dimension(150, 30));
        addItemInRightComboBoxAttributesTables();
        eventChangeRightComboBoxTables();

        comboBoxJoins = WidgetFactory.createComboBox("Join Combo Box", arrayJoin);
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
     * A method for placing components.
     * <p>
     * Метод для размещения компонентов.
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
        add(panelForPlacingComponents, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(600, 400);
        setVisible(true);
    }

    /**
     * The method of placing components on the panel for placing components.
     * <p>
     * Метод размещения компонентов на панели для размещения компонентов.
     */
    private void arrangeComponentsInPanelForPlacingComponents() {
        panelForPlacingComponents.add(leftComboBoxTables, new GridBagHelper().setX(0).setY(0).anchorCenter().setInsets(5, 5, 5, 5).get());
        panelForPlacingComponents.add(rightComboBoxTables, new GridBagHelper().setX(20).setY(0).anchorCenter().setInsets(5, 5, 5, 5).get());
        panelForPlacingComponents.add(comboBoxJoins, new GridBagHelper().setX(10).setY(0).anchorCenter().setInsets(5, 5, 5, 5).get());
        panelForPlacingComponents.add(leftComboBoxAttributesTables, new GridBagHelper().setX(0).setY(10).anchorCenter().setInsets(5, 5, 5, 5).get());
        panelForPlacingComponents.add(rightComboBoxAttributeTables, new GridBagHelper().setX(20).setY(10).anchorCenter().setInsets(5, 5, 5, 5).get());
        panelForPlacingComponents.add(buttonAddJoinInQuery, new GridBagHelper().setX(10).setY(20).anchorCenter().setInsets(5, 5, 5, 5).get());
    }

    /**
     * The method in which the functionality of the button for adding connections (Join) to the request is prescribed.
     * <p>
     * Метод в котором прописан функционал кнопки для добавления соединений (Join) в запрос.
     */
    private void buttonEventAddJoin() {
        createStringQueryConstructor.addJoins(leftComboBoxTables.getSelectedItem().toString(), rightComboBoxTables.getSelectedItem().toString(), comboBoxJoins.getSelectedItem().toString(), leftComboBoxAttributesTables.getSelectedItem().toString(), rightComboBoxAttributeTables.getSelectedItem().toString());
        queryBuilderPanelConstructor.setTextInPanelOutputTestingQuery(createStringQueryConstructor.getQuery());
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

}
