package org.executequery.gui.querybuilder.toolBar;

import org.executequery.gui.WidgetFactory;
import org.executequery.gui.querybuilder.ExecuteQueryEditor;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.executequery.gui.querybuilder.WorkQuryEditor.CreateStringQuery;
import org.executequery.gui.querybuilder.buttonPanel.AddTableQueryBuilder;
import org.underworldlabs.swing.ConnectionsComboBox;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * The QueryBuilderToolBarPanel
 *
 * @author Krylov Gleb
 */

public class QueryBuilderToolBarPanel extends JToolBar {

    // --- Fields ---

    private CreateStringQuery createStringQuery;

    // --- GUI Components ---

    private JButton addTable;
    private JButton selectButton;
    private JButton joinButton;
    private JButton columnButton;
    private JButton conditionButton;
    private JButton createQuery;
    private JButton removeTable;
    private ConnectionsComboBox connectionsCombo;
    private QueryBuilderPanel queryBuilderPanel;

    // --- Designer ---

    public QueryBuilderToolBarPanel(QueryBuilderPanel queryBuilderPanel) {
        this.queryBuilderPanel = queryBuilderPanel;
        init();
    }

    /**
     * Method for initialization
     */
    private void init() {
        createStringQuery = new CreateStringQuery(queryBuilderPanel);

        // --- Button ---

        addTable = WidgetFactory.createButton("Add Table", "Add Table", event -> {
            AddTableQueryBuilder addTableQueryBuilder = new AddTableQueryBuilder(queryBuilderPanel);
            addTableQueryBuilder.setVisible(true);
            addTableQueryBuilder.setSize(400, 300);
        });

        removeTable = WidgetFactory.createButton("Remove Table", "Remove Table", event -> {
            queryBuilderPanel.removeTableInInputPanel();
            queryBuilderPanel.removeListTableInInputPanel();
        });

        selectButton = WidgetFactory.createButton("Select Button", "Select");
        joinButton = WidgetFactory.createButton("Join Button", "Join");
        columnButton = WidgetFactory.createButton("Column Button", "Column");
        conditionButton = WidgetFactory.createButton("Condition Button", "Condition");

        createQuery = WidgetFactory.createButton("Create Query", "Create Query", event -> {

            ArrayList<JTable> arrayTableInInputPanel = queryBuilderPanel.getListTableInInputPanel();

            if (!arrayTableInInputPanel.isEmpty()) {

                ArrayList<String> Attribute = new ArrayList<>();

                createStringQuery.setTables(arrayTableInInputPanel.get(0).getColumnName(0));

                for (int i = 0; i < arrayTableInInputPanel.get(0).getRowCount(); i++) {
                    if ((boolean) arrayTableInInputPanel.get(0).getValueAt(i, 1)) {
                        Attribute.add((String) arrayTableInInputPanel.get(0).getValueAt(i, 0));
                    }
                }

                createStringQuery.setAttribute(Attribute);
                ExecuteQueryEditor executeQueryEditor = new ExecuteQueryEditor(createStringQuery);

            }

        });

        // ComboBox to view the current connection
        connectionsCombo = WidgetFactory.createConnectionComboBox("connectionsCombo", true);
        connectionsCombo.setMaximumSize(new Dimension(200, 30));
        connectionsCombo.setPreferredSize(new Dimension(connectionsCombo.getWidth(), 30));

        arrangeComponent();
    }

    /**
     * A method for placing components
     */
    private void arrangeComponent() {
        setPreferredSize(new Dimension(getWidth(), 45));
        add(connectionsCombo);
        add(addTable);
        add(removeTable);
        add(selectButton);
        add(joinButton);
        add(columnButton);
        add(conditionButton);
        add(createQuery);
    }

}
