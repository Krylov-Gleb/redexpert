package org.executequery.gui.querybuilder;

import org.executequery.base.TabView;
import org.executequery.gui.editor.QueryEditor;
import org.executequery.gui.editor.QueryEditorTextPanel;
import org.executequery.gui.querybuilder.WorkQuryEditor.CreateStringQuery;
import org.executequery.gui.querybuilder.toolBar.QueryBuilderToolBar;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * The main class for working with the query constructor.
 * <p>
 * Главный класс для работы с конструктором запросов.
 *
 * @author Krylov Gleb
 */
public class QueryBuilderPanel extends JPanel implements TabView {

    // --- Fields ---
    // --- Поля ---

    public static final String TITLE = "Query Builder";
    public static final String FRAME_ICON = "icon_table_validation";

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelForPlacingComponents;
    private JSplitPane splitPaneForPanelOutputElementAndPanelOutputTestingQuery;
    private QueryEditorTextPanel panelOutputTestingQuery;
    private JPanel panelOutputGUIComponents;
    private QueryBuilderToolBar toolBarPanel;
    private ArrayList<JTable> listTablesOnPanelOutputGUIComponents;
    private CreateStringQuery createStringQuery;

    /**
     * Creating the main panel of the query designer.
     * <p>
     * Создаём главную панель конструктора запросов.
     */
    public QueryBuilderPanel() {
        init();
    }

    /**
     * A method for initializing fields.
     * <p>
     * Метод для инициализации полей.
     */
    private void init() {
        panelForPlacingComponents = new JPanel(new BorderLayout());

        createStringQuery = new CreateStringQuery();

        toolBarPanel = new QueryBuilderToolBar(this, createStringQuery);

        panelOutputTestingQuery = new QueryEditorTextPanel(new QueryEditor());

        listTablesOnPanelOutputGUIComponents = new ArrayList<>();

        initInputElementPanel();
        initSplitPane();

        arrangeComponent();
    }

    private void initSplitPane() {
        splitPaneForPanelOutputElementAndPanelOutputTestingQuery = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelOutputGUIComponents, panelOutputTestingQuery);
        splitPaneForPanelOutputElementAndPanelOutputTestingQuery.setDividerLocation(600);
    }

    private void initInputElementPanel() {
        panelOutputGUIComponents = new JPanel();
        panelOutputGUIComponents.setLayout(new GridBagLayout());
        panelOutputGUIComponents.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    /**
     * A method for arranging components.
     * <p>
     * Метод для расположения компонентов.
     */
    private void arrangeComponent() {
        arrangeComponentsInPanelForPlacingComponents();
        configurationDialog();
    }

    /**
     * A method for configuring the parameters of a dialog (window).
     * <p>
     * Метод для настройки параметров диалога (окна).
     */
    private void configurationDialog() {
        setLayout(new BorderLayout());
        add(panelForPlacingComponents, BorderLayout.CENTER);
    }

    /**
     * Placing components on the panel to place components.
     * <p>
     * Размещение компонентов на панели для размещения компонентов.
     */
    private void arrangeComponentsInPanelForPlacingComponents() {
        panelForPlacingComponents.add(splitPaneForPanelOutputElementAndPanelOutputTestingQuery, BorderLayout.CENTER);
        panelForPlacingComponents.add(toolBarPanel, BorderLayout.NORTH);
    }

    /**
     * A method for adding a table to the output panel.
     * <p>
     * Метод для добавлении таблицы на панель вывода.
     */
    public void addTableInInputPanel(JScrollPane scrollPane) {
        if (isTableUnique(scrollPane)) {
            addTable(scrollPane);
        }
    }

    /**
     * A method describing the functionality for adding tables to the output panel.
     * <p>
     * Метод описывающий функционал для добавления таблиц на панель вывода.
     */
    private void addTable(JScrollPane scrollPane) {
        panelOutputGUIComponents.add(scrollPane);
        panelOutputGUIComponents.revalidate();
        panelOutputGUIComponents.repaint();
    }

    /**
     * A method for checking the table for uniqueness.
     * <p>
     * Метод для проверки таблицы на уникальность.
     */
    private boolean isTableUnique(JScrollPane scrollPane) {
        boolean Check = true;

        for (int i = 0; i < panelOutputGUIComponents.getComponents().length; i++) {
            if (panelOutputGUIComponents.getComponents()[i] == scrollPane) {
                Check = false;
            }
        }

        return Check;
    }

    /**
     * A method for deleting a table from the output panel.
     * <p>
     * Метод для удаления таблицы с панели вывода.
     */
    public void removeTableInInputPanel(String nameTable) {
        for (int i = 0; i < getListTable().size(); i++) {
            if (nameTable.equals(getListTable().get(i).getColumnName(0))) {
                removeTable(i);
            }
        }

    }

    /**
     * A method describing the functionality for removing tables from the output panel.
     * <p>
     * Метод описывающий функционал для удаления таблиц с панели вывода.
     */
    private void removeTable(int indexTable) {
        panelOutputGUIComponents.remove(indexTable);
        panelOutputGUIComponents.revalidate();
        panelOutputGUIComponents.repaint();
    }

    /**
     * The method for getting the output panel of the elements.
     * <p>
     * Метод для получения панели вывода элементов.
     */
    public JPanel getOutputPanel() {
        return panelOutputGUIComponents;
    }

    /**
     * A method for adding tables to the list of tables that are in the output panel.
     * <p>
     * Метод для добавления таблиц в список таблиц которые находятся на панели вывода.
     */
    public void addTableInListTable(JTable table) {
        listTablesOnPanelOutputGUIComponents.add(table);

    }

    /**
     * A method for removing tables from the list of tables that are in the output panel.
     * <p>
     * Метод для удаления таблиц из списка таблиц которые находятся на панели вывода.
     */
    public void removeTableInListTable(String nameTable) {
        for (int i = 0; i < listTablesOnPanelOutputGUIComponents.size(); i++) {
            if (listTablesOnPanelOutputGUIComponents.get(i).getColumnName(0).equals(nameTable)) {
                listTablesOnPanelOutputGUIComponents.remove(i);
            }
        }
    }

    /**
     * A method for getting a list of tables that are in the output panel.
     * <p>
     * Метод для получения списка таблиц которые находятся на панели вывода.
     */
    public ArrayList<JTable> getListTable() {
        return listTablesOnPanelOutputGUIComponents;
    }

    /**
     * A method for changing the text in the test query output panel.
     * <p>
     * Метод для смены текста на панели вывода тестовых запросов.
     */
    public void setTextInPanelOutputTestingQuery(String swapText) {
        panelOutputTestingQuery.setQueryAreaText(swapText);
    }

    /**
     * A method for transferring a query from the test query output panel to the text panel in the query designer.
     * <p>
     * Метод для переноса запроса из панели вывода тестовых запросов в текстовую панель в конструкторе запросов.
     */
    public void rescheduleQueryFromPanelOutputTestingQueryInEditorTextQueryEditor(QueryEditor queryEditor) {
        queryEditor.setEditorText(panelOutputTestingQuery.getQueryAreaText());
    }

    /**
     * A method for getting table names from the element output panel.
     * <p>
     * Метод для получения имён таблиц из панели вывода элементов.
     */
    public String[] getNamesTablesFromOutputPanel() {

        ArrayList<JTable> arrayTable = getListTable();
        String[] nameTableInInputPanel = new String[arrayTable.size()];

        for (int i = 0; i < arrayTable.size(); i++) {
            nameTableInInputPanel[i] = arrayTable.get(i).getColumnName(0);
        }

        return nameTableInInputPanel;
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
