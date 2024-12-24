package org.executequery.gui.querybuilder;

import org.executequery.localization.Bundles;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * A class that creates tables for the query constructor (QueryBuilder).
 * <p>
 * Класс создающий таблицы для конструктора запросов (QueryBuilder).
 *
 * @author Krylov Gleb
 */
public class QBCreateTable {

    // --- Elements accepted using the constructor ----
    // --- Поля, которые передаются через конструктор. ---

    private QBPanel queryBuilderPanel;
    private QueryConstructor queryConstructor;
    private String nameTable;
    private List<String> columnsTable;

    // --- Other fields ---
    // --- Остальные поля ---

    private Object[] column;
    private Object[][] dataTable;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JTable createCustomTable;
    private QBMovePanel movePanel;
    private DefaultTableModel defaultTableModel;

    /**
     * A table is being created.
     * A method is used to initialize fields.
     * <p>
     * Создаётся таблица.
     * Используется метод для инициализации полей.
     */
    public QBCreateTable(List<String> columnsTable, String nameTable, QBPanel queryBuilderPanel, QueryConstructor queryConstructor) {
        this.columnsTable = columnsTable;
        this.nameTable = nameTable;
        this.queryBuilderPanel = queryBuilderPanel;
        this.queryConstructor = queryConstructor;
        init();
    }

    /**
     * A method for initializing fields.
     * <p>
     * Метод для инициализации полей.
     */
    private void init() {
        initColumn();
        addDataTables();
        initDefaultTableModal();
        initTable();
        initScrollPane();
    }

    /**
     * A method for adding a table to a movable panel (platform).
     * <p>
     * Метод для добавления таблицы на перемещаемую панель (платформу).
     */
    private void initMovePanel(JScrollPane scrollPane){
        movePanel = new QBMovePanel(scrollPane);
    }

    /**
     * A method for initializing the ScrollPane and placing a table on it.
     * <p>
     * Метод для инициализации scrollPane и размещения на ней таблицы.
     */
    private void initScrollPane() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(createCustomTable);
        initMovePanel(scrollPane);
    }

    /**
     * A method for initializing and creating a table.
     * <p>
     * Метод для инициализации и создания таблицы.
     */
    private void initTable() {
        createCustomTable = new JTable(defaultTableModel);
        createCustomTable.getTableHeader().setReorderingAllowed(false);
        createCustomTable.getTableHeader().setEnabled(false);
        createCustomTable.setToolTipText(Bundles.get("common.table") + nameTable);
    }

    /**
     * A method for initializing and configuring the table view model (representing data inside a table).
     * <p>
     * Метод для инициализации и настройки модели представления таблицы (представление данных внутри таблицы).
     */
    private void initDefaultTableModal() {
        defaultTableModel = new DefaultTableModel(dataTable, column) {

            // --- Overloading methods for correct display of CheckBox (Boolean type) ---

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return String.class;
                    case 1:
                        return Boolean.class;
                    default:
                        return Object.class;
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }

        };

        /**
         * A method for adding table attributes when the checkbox status changes from false to true.
         * <p>
         * Метод для добавления атрибутов таблицы при смене состояния флажка с false на true.
         */
        defaultTableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                queryConstructor.setAttributes(queryBuilderPanel.getListTable());
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            }
        });
    }

    /**
     * A method for adding data to a table.
     * <p>
     * Метод для добавления данных в таблицу.
     */
    private void addDataTables() {
        dataTable = new Object[columnsTable.size()][column.length];

        for (int i = 0; i < dataTable.length; i++) {
            for (int j = 0; j < dataTable[i].length; j++) {
                if (j == 0) {
                    dataTable[i][0] = columnsTable.get(i);
                } else {
                    dataTable[i][j] = false;
                }
            }
        }
    }

    /**
     * A method for initializing table columns.
     * <p>
     * Метод для инициализации колонок таблицы.
     */
    private void initColumn() {
        column = new Object[2];
        column[0] = nameTable;
        column[1] = Bundles.get("common.selectFields");
    }

    /**
     * The method for getting the panel on which the table is located.
     * <p>
     * Метод для получения панели на которой расположена таблица.
     */
    public JPanel getMovePanelTable(){
        return movePanel;
    }

    /**
     * The method for getting the table.
     * <p>
     * Метод для получения таблицы.
     */
    public JTable getJTable() {
        return createCustomTable;
    }

}
