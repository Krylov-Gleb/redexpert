package org.executequery.gui.querybuilder.buttonPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * A class for creating tables.
 * <p>
 * Класс для создания таблиц.
 *
 * @author Krylov Gleb
 */
public class CreatorTableFromQueryBuilder {

    // --- Fields ---
    // --- Поля ---

    private Object[] column;
    private Object[][] dataTable;

    // --- Elements accepted using the constructor. ----
    // --- Поля, которые передаются через конструктор. ---

    private String nameTableConstructor;
    private List<String> columnsTableConstructor;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JTable createCustomTable;
    private JScrollPane scrollPaneForPlaceTable;
    private DefaultTableModel defaultTableModel;

    /**
     * Creating a table.
     * <p>
     * Создаём таблицу.
     */
    public CreatorTableFromQueryBuilder(List<String> columnsTable, String nameTable) {
        this.columnsTableConstructor = columnsTable;
        this.nameTableConstructor = nameTable;
        init();
    }

    /**
     * A method for initializing fields.
     * <p>
     * Метод для инициализации полей.
     */
    private void init() {
        initColumn();
        initDataTables();
        initDefaultTableModal();
        initTable();
        initScrollPane();
    }

    private void initScrollPane() {
        scrollPaneForPlaceTable = new JScrollPane(createCustomTable);
        scrollPaneForPlaceTable.setPreferredSize(new Dimension(200, 200));
    }

    private void initTable() {
        createCustomTable = new JTable(defaultTableModel);
    }

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
    }

    private void initDataTables() {
        dataTable = new Object[columnsTableConstructor.size()][column.length];

        for (int i = 0; i < dataTable.length; i++) {
            for (int j = 0; j < dataTable[i].length; j++) {
                if (j == 0) {
                    dataTable[i][0] = columnsTableConstructor.get(i);
                } else {
                    dataTable[i][j] = false;
                }
            }
        }
    }

    private void initColumn() {
        column = new Object[2];
        column[0] = nameTableConstructor;
        column[1] = "Add In Select";
    }

    /**
     * The method returns a table in the scroll bar so that the column names are visible in the table.
     * <p>
     * Метод возвращает таблицу на панели прокрутки что бы у таблицы были видны имена колонок.
     */
    public JScrollPane getTable() {
        return scrollPaneForPlaceTable;
    }

    /**
     * The method returns a table.
     * <p>
     * Метод возвращает таблицу.
     */
    public JTable getJTable() {
        return createCustomTable;
    }

}
