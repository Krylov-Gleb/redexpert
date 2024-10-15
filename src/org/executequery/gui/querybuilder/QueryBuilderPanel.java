package org.executequery.gui.querybuilder;

import org.executequery.base.TabView;
import org.executequery.gui.editor.QueryEditor;
import org.executequery.gui.querybuilder.WorkQuryEditor.CreateStringQuery;
import org.executequery.gui.querybuilder.inputPanel.QueryBuilderInputPanel;
import org.executequery.gui.querybuilder.toolBar.QueryBuilderToolBarPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * The QueryBuilderPanel
 *
 * @author Krylov Gleb
 */

public class QueryBuilderPanel extends JPanel implements TabView {

    // --- Constants ---

    public static final String TITLE = "Query Builder";
    public static final String FRAME_ICON = "icon_table_validation";

    private ArrayList<JTable> tableListInInputPanel;

    // --- GUI Components ---

    private JPanel mainPanel;
    private QueryBuilderToolBarPanel queryBuilderToolBarPanel;
    private QueryBuilderInputPanel inputElementPanel;
    private CreateStringQuery createStringQuery;
    private QueryEditor queryEditor;

    // --- Designer ---

    public QueryBuilderPanel() {
        init();
    }

    /**
     * Method for initialization
     */
    private void init() {

        mainPanel = new JPanel(new BorderLayout());
        queryEditor = new QueryEditor();
        queryBuilderToolBarPanel = new QueryBuilderToolBarPanel(this);
        tableListInInputPanel = new ArrayList<>();

        createStringQuery = new CreateStringQuery(this);
        inputElementPanel = new QueryBuilderInputPanel();
        inputElementPanel.setLayout(new GridBagLayout());

        arrangeComponent();
    }

    /**
     * A method for placing components
     */
    private void arrangeComponent() {
        mainPanel.add(inputElementPanel, BorderLayout.CENTER);
        mainPanel.add(queryBuilderToolBarPanel, BorderLayout.NORTH);

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * A method for adding a table to the component output panel (InputPanel)
     *
     * @param scrollPane Passing the ScrollPane in which the table is stored
     */
    public void addTableInInputPanel(JScrollPane scrollPane) {

        // The table is wrapped in a scroll pane so that the headings are reflected on it
        inputElementPanel.add(scrollPane);

        // Methods for creating a real-time table
        inputElementPanel.revalidate();
        inputElementPanel.repaint();
    }

    /**
     * Method for deleting a table from the element output panel (InputPanel)
     */
    public void removeTableInInputPanel() {

        // I get an array of components on the output panel (InputPanel)
        if (inputElementPanel.getComponents().length > 0) {
            // Deleting the table
            inputElementPanel.remove(inputElementPanel.getComponents().length - 1);
        }
        inputElementPanel.revalidate();
        inputElementPanel.repaint();
    }

    /**
     * Method for getting the output panel (InputPanel)
     *
     * @return JPanel
     */
    public JPanel getInputPanel() {
        return inputElementPanel;
    }

    /**
     * Returns the created QueryEditor
     *
     * @return QueryEditor
     */
    public QueryEditor getQueryEditor() {
        return queryEditor;
    }

    /**
     * The table added to the output panel is added to the array (ArrayList).
     *
     * @param table
     */
    public void addListTableInInputPanel(JTable table) {
        tableListInInputPanel.add(table);
    }

    /**
     * Deletes the table deleted from the output panel from the array (ArrayList)
     */
    public void removeListTableInInputPanel() {
        if (tableListInInputPanel.size() > 0) {
            tableListInInputPanel.remove(tableListInInputPanel.size() - 1);
        }
    }

    /**
     * Returns tables located on the output panel (InputPanel)
     *
     * @return ArrayList<JTable>
     */
    public ArrayList<JTable> getListTableInInputPanel() {
        return tableListInInputPanel;
    }

    @Override
    public boolean tabViewClosing() {
        return true;
    }

    @Override
    public boolean tabViewSelected() {
        return true;
    }

    @Override
    public boolean tabViewDeselected() {
        return true;
    }
}
