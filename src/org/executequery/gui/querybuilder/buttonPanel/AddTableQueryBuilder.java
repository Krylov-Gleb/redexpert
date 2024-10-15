package org.executequery.gui.querybuilder.buttonPanel;

import org.executequery.databasemediators.DatabaseConnection;
import org.executequery.databaseobjects.impl.DefaultDatabaseHost;
import org.executequery.gui.WidgetFactory;
import org.executequery.gui.browser.ConnectionsTreePanel;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.underworldlabs.swing.ConnectionsComboBox;
import org.underworldlabs.swing.layouts.GridBagHelper;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The AddTableQueryBuilder
 *
 * @author Krylov Gleb
 */

public class AddTableQueryBuilder extends JFrame {

    // --- Fields ---

    private DefaultDatabaseHost defaultDatabaseHost;

    // --- GUI Components ---

    private QueryBuilderPanel queryBuilderPanel;
    private JPanel mainPanel;
    private JLabel labelConnect;
    private ConnectionsComboBox connectionsCombo;
    private JLabel labelWhoTable;
    private JComboBox<String> comboBoxTable;
    private JButton buttonCreate;

    // --- Designer ---

    public AddTableQueryBuilder(QueryBuilderPanel queryBuilderPanel) {
        this.queryBuilderPanel = queryBuilderPanel;
        init();
    }

    /**
     * Method for initialization.
     */
    private void init() {
        mainPanel = WidgetFactory.createPanel("Add Table Panel");
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        labelConnect = WidgetFactory.createLabel("Подключение");
        labelWhoTable = WidgetFactory.createLabel("Таблица");

        connectionsCombo = WidgetFactory.createConnectionComboBox("connectionBox", true);
        connectionsCombo.setMaximumSize(new Dimension(200, 30));

        defaultDatabaseHost = new DefaultDatabaseHost(connectionsCombo.getSelectedConnection());

        comboBoxTable = WidgetFactory.createComboBox("comboBoxTable", defaultDatabaseHost.getTableNames());
        comboBoxTable.setPreferredSize(new Dimension(200, 30));

        buttonCreate = WidgetFactory.createButton("CreateTable", "Создать", event -> {
            TableQueryBuilder tableQueryBuilder = new TableQueryBuilder(getColumns(comboBoxTable.getSelectedItem().toString()),comboBoxTable.getSelectedItem().toString());
            queryBuilderPanel.addTableInInputPanel(tableQueryBuilder.getTable());
            queryBuilderPanel.addListTableInInputPanel(tableQueryBuilder.getJTable());
        });

        arrangeComponent();
    }

    /**
     * A method for placing components
     */
    private void arrangeComponent() {
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.add(labelConnect, new GridBagHelper().anchorCenter().setX(0).setY(0).setInsets(5, 5, 5, 5).get());
        mainPanel.add(connectionsCombo, new GridBagHelper().anchorCenter().setX(0).setY(10).setInsets(5, 5, 5, 5).get());
        mainPanel.add(labelWhoTable, new GridBagHelper().anchorCenter().setX(0).setY(20).setInsets(5, 5, 5, 5).get());
        mainPanel.add(comboBoxTable, new GridBagHelper().anchorCenter().setX(0).setY(30).setInsets(5, 5, 5, 5).get());
        mainPanel.add(buttonCreate, new GridBagHelper().anchorCenter().setX(0).setY(40).setInsets(5, 5, 5, 5).get());

        setLayout(new BorderLayout());
        setTitle("Add Table");
        setLocationRelativeTo(queryBuilderPanel);
        add(mainPanel, BorderLayout.CENTER);
    }


    private static DefaultDatabaseHost getDefaultDatabaseHost(DatabaseConnection connection) {
        return ConnectionsTreePanel.getPanelFromBrowser().getDefaultDatabaseHostFromConnection(connection);
    }


    public List<String> getColumns(String table) {
        return getDefaultDatabaseHost(connectionsCombo.getSelectedConnection()).getColumnNames(table);
    }

}
