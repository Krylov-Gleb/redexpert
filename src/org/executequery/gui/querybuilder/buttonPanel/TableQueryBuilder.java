package org.executequery.gui.querybuilder.buttonPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * A class for creating tables.
 *
 * @author Krylov Gleb
 */
public class TableQueryBuilder {

    // --- Fields ---

    private List<String> columnsTable;
    private String nameTable;
    private Object[] column;
    private Object[][] tableData;

    // --- GUI Components ---

    private JTable table;
    private JScrollPane scrollPane;
    private DefaultTableModel defaultTableModel;

    /**
     * Creates a table using the passed table name and its columns.
     * Initializes the fields of the class.
     *
     * @param columnsTable
     * @param nameTable
     */
    public TableQueryBuilder(List<String> columnsTable, String nameTable) {
        this.columnsTable = columnsTable;
        this.nameTable = nameTable;
        init();
    }

    /**
     * A method for initializing class fields.
     * As well as creating a table (columns and rows in it).
     */
    private void init() {

        column = new Object[2];
        column[0] = nameTable;
        column[1] = "Add In Select";

        tableData = new Object[columnsTable.size()][column.length];

        for (int i = 0; i < tableData.length; i++) {
            for (int j = 0; j < tableData[i].length; j++) {
                if (j == 0) {
                    tableData[i][0] = columnsTable.get(i);
                } else {
                    tableData[i][j] = false;
                }
            }
        }

        defaultTableModel = new DefaultTableModel(tableData, column) {

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

        table = new JTable(defaultTableModel);

        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(200, 200));
    }

    /**
     * Returns a JTable wrapped in a ScrollPane.
     * This is necessary to display the column name.
     *
     * @return JScrollPane (JTable -> JScrollPane)
     */
    public JScrollPane getTable() {
        return scrollPane;
    }

    /**
     * Returns a table.
     *
     * @return JTable
     */
    public JTable getJTable() {
        return table;
    }

}
