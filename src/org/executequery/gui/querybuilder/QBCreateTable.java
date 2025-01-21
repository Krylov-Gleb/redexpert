package org.executequery.gui.querybuilder;

import org.executequery.localization.Bundles;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class QBCreateTable {

    private final QBPanel queryBuilderPanel;
    private final QueryConstructor queryConstructor;
    private final String nameTable;
    private final List<String> columnsTable;
    private Object[] column;
    private Object[][] dataTable;
    private JTable createCustomTable;
    private QBMovePanel movePanel;
    private DefaultTableModel defaultTableModel;

    public QBCreateTable(List<String> columnsTable, String nameTable, QBPanel queryBuilderPanel, QueryConstructor queryConstructor) {
        this.columnsTable = columnsTable;
        this.nameTable = nameTable;
        this.queryBuilderPanel = queryBuilderPanel;
        this.queryConstructor = queryConstructor;
        init();
    }

    private void init() {
        initColumn();
        addDataTables();
        initDefaultTableModal();
        initTable();
        initScrollPane();
    }

    private void initMovePanel(JScrollPane scrollPane) {
        movePanel = new QBMovePanel(scrollPane);
    }

    private void initScrollPane() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(createCustomTable);
        initMovePanel(scrollPane);
    }

    private void initTable() {
        createCustomTable = new JTable(defaultTableModel);
        createCustomTable.getTableHeader().setReorderingAllowed(false);
        createCustomTable.getTableHeader().setEnabled(false);
        createCustomTable.setToolTipText(Bundles.get("common.table") + nameTable);
        createCustomTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    JPopupMenu popupMenu = new JPopupMenu();

                    JMenuItem menuItemOne = new JMenuItem(Bundles.get("QueryBuilder.CreateTable.itemMenuSelectAll"));
                    popupMenu.add(menuItemOne).addActionListener(event -> {
                        for (int i = 0; i < createCustomTable.getRowCount(); i++) {
                            createCustomTable.setValueAt(true, i, 1);
                        }
                    });

                    JMenuItem menuItemTwo = new JMenuItem(Bundles.get("QueryBuilder.CreateTable.itemMenuRemoveAllSelections"));
                    popupMenu.add(menuItemTwo).addActionListener(event -> {
                        for (int i = 0; i < createCustomTable.getRowCount(); i++) {
                            createCustomTable.setValueAt(false, i, 1);
                        }
                    });

                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private void initDefaultTableModal() {
        defaultTableModel = new DefaultTableModel(dataTable, column) {

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

        defaultTableModel.addTableModelListener(e -> {
            if (queryConstructor.getChangingValueClick()) {
                queryBuilderPanel.addStepBackActionInHistory("Set Attribute " + queryConstructor.getAttribute());
                queryConstructor.setAttributes(queryBuilderPanel.getListTable());
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
            } else {
                queryConstructor.setAttributes(queryBuilderPanel.getListTable());
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                queryConstructor.setChangingValueClick(true);
            }
        });
    }

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

    private void initColumn() {
        column = new Object[2];
        column[0] = nameTable;
        column[1] = Bundles.get("common.selectFields");
    }

    public JPanel getMovePanelTable() {
        return movePanel;
    }

    public JTable getJTable() {
        return createCustomTable;
    }

}
