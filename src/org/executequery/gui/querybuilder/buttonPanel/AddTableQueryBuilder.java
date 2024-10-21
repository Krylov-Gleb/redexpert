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
 * This class creates a window for adding tables to the components panel (InputPanel).
 *
 * @author Krylov Gleb
 */
public class AddTableQueryBuilder extends JFrame {

    // --- The field that is passed through the constructor ---

    private QueryBuilderPanel queryBuilderPanel;

    // --- GUI Components ---

    private JPanel mainPanel;
    private JLabel labelConnect;
    private JLabel labelWhoTable;
    private JButton buttonCreate;
    private JComboBox<String> comboBoxTable;
    private ConnectionsComboBox connectionsCombo;

    // --- Other field ---

    private DefaultDatabaseHost defaultDatabaseHost;

    /**
     * A new window is being created for adding tables.
     * The components of the graphical interface of the window are initialized.
     *
     * @param queryBuilderPanel
     */
    public AddTableQueryBuilder(QueryBuilderPanel queryBuilderPanel) {
        this.queryBuilderPanel = queryBuilderPanel;
        init();
    }

    /**
     * The GUI elements are initialized and actions are added to the buttons.
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
            TableQueryBuilder tableQueryBuilder = new TableQueryBuilder(getColumns(comboBoxTable.getSelectedItem().toString()), comboBoxTable.getSelectedItem().toString());
            queryBuilderPanel.addTableInInputPanel(tableQueryBuilder.getTable());
            queryBuilderPanel.addListTableInInputPanel(tableQueryBuilder.getJTable());
        });

        arrangeComponent();
    }

    /**
     * The components are placed on the panel and their parameters are set.
     */
    private void arrangeComponent() {
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.add(labelConnect, new GridBagHelper().anchorCenter().setX(0).setY(0).setInsets(5, 5, 5, 5).get());
        mainPanel.add(connectionsCombo, new GridBagHelper().anchorCenter().setX(0).setY(10).setInsets(5, 5, 5, 5).get());
        mainPanel.add(labelWhoTable, new GridBagHelper().anchorCenter().setX(0).setY(20).setInsets(5, 5, 5, 5).get());
        mainPanel.add(comboBoxTable, new GridBagHelper().anchorCenter().setX(0).setY(30).setInsets(5, 5, 5, 5).get());
        mainPanel.add(buttonCreate, new GridBagHelper().anchorCenter().setX(0).setY(40).setInsets(5, 5, 5, 5).get());

        setLayout(new BorderLayout());
        setTitle("Добавить таблицу");
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setIconImage(new ImageIcon("red_expert.png").getImage());
        setLocationRelativeTo(queryBuilderPanel);
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * A method for getting the database host by the connection used.
     *
     * @param connection
     * @return DefaultDatabaseHost
     */
    private static DefaultDatabaseHost getDefaultDatabaseHost(DatabaseConnection connection) {
        return ConnectionsTreePanel.getPanelFromBrowser().getDefaultDatabaseHostFromConnection(connection);
    }


    /**
     * Returns a list of columns from the specified table.
     *
     * @param table
     * @return List<String>
     */
    public List<String> getColumns(String table) {
        return getDefaultDatabaseHost(connectionsCombo.getSelectedConnection()).getColumnNames(table);
    }

}
