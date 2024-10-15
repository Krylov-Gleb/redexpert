package org.executequery.gui.querybuilder.buttonPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * The TableQueryBuilder
 *
 * @author Krylov Gleb
 */

public class TableQueryBuilder {

    // --- Fields ---

    private List<String> ColumnsTable;
    private String NameTable;
    private Object[] Column;
    private Object[][] TableData;
    private DefaultTableModel defaultTableModel;

    // --- GUI Components ---

    private JTable table;
    private JScrollPane scrollPane;

    // --- Designer ---

    public TableQueryBuilder(List<String> ColumnsTable, String NameTable) {
        this.ColumnsTable = ColumnsTable;
        this.NameTable = NameTable;
        init();
    }

    /**
     * Method for initialization.
     */
    private void init() {

        Column = new Object[2];
        Column[0] = NameTable;
        Column[1] = "Add In Select";

        TableData = new Object[ColumnsTable.size()][Column.length];

        for (int i = 0; i < TableData.length; i++) {
            for (int j = 0; j < TableData[i].length; j++) {
                if (j == 0) {
                    TableData[i][0] = ColumnsTable.get(i);
                } else {
                    TableData[i][j] = false;
                }
            }
        }

        defaultTableModel = new DefaultTableModel(TableData, Column) {

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
