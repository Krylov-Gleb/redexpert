package org.executequery.gui.querybuilder;

import org.executequery.base.TabView;
import org.executequery.gui.WidgetFactory;
import org.executequery.gui.editor.QueryEditor;
import org.executequery.gui.editor.QueryEditorTextPanel;
import org.executequery.localization.Bundles;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;

/**
 * The main class for working with the query constructor (QueryBuilder)
 * <p>
 * Главный класс для работы с конструктором запросов (QueryBuilder)
 *
 * @author Krylov Gleb
 */
public class QBPanel extends JPanel implements TabView {

    // --- Constants ---
    // --- Константы ---

    public static final String TITLE = Bundles.get("common.queryBuilder");
    public static final String FRAME_ICON = "icon_table_validation";

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelPlacingComponents;
    private JSplitPane splitPanePanelGUIComponentsAndPanelTestingQuery;
    private QueryEditorTextPanel panelTestingQuery;
    private JPanel panelGUIComponents;
    private JScrollPane scrollPanePlacingComponents;
    private QBToolBar toolBar;
    private ArrayList<JTable> listTablesPanelGUIComponents;
    private QueryConstructor queryConstructor;

    /**
     * Creating the main panel of the query builder.
     * A method is used to initialize fields.
     * <p>
     * Создаём главную панель конструктора запросов.
     * Используется метод для инициализации полей.
     */
    public QBPanel() {
        init();
    }

    /**
     * A method for initializing fields.
     * <p>
     * Метод для инициализации полей.
     */
    private void init() {
        initPanel();
        initQueryConstructor();
        initToolBar();
        initQueryEditorTextPanel();
        initArrays();
        initPanelGUIComponents();
        intScrollPane();
        initSplitPane();
        arrangeComponent();
    }

    /**
     * The method for initializing JPanel.
     * <p>
     * Метод для инициализации JPanel.
     */
    private void initPanel() {
        panelPlacingComponents = WidgetFactory.createPanel("panelPlacingComponents");
        panelPlacingComponents.setLayout(new BorderLayout());
    }

    /**
     * Method for initializing QueryConstructor
     * <p>
     * Метод для инициализации QueryConstructor
     */
    private void initQueryConstructor() {
        queryConstructor = new QueryConstructor(this);
    }

    /**
     * A method for initializing the toolbar.
     * <p>
     * Метод для инициализации панели инструментов (ToolBar).
     */
    private void initToolBar() {
        toolBar = new QBToolBar(this, queryConstructor);
    }

    /**
     * QueryEditorTextPanel initialization method
     * <p>
     * Метод для инициализации QueryEditorTextPanel.
     */
    private void initQueryEditorTextPanel() {
        panelTestingQuery = new QueryEditorTextPanel(new QueryEditor());
        panelTestingQuery.getQueryArea().setEditable(false);
    }

    /**
     * A method for initializing arrays.
     * <p>
     * Метод для инициализации массивов.
     */
    private void initArrays() {
        listTablesPanelGUIComponents = new ArrayList<>();
    }

    /**
     * Method for initializing SplitPane.
     * <p>
     * Метод для инициализации SplitPane.
     */
    private void initSplitPane() {
        splitPanePanelGUIComponentsAndPanelTestingQuery = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panelTestingQuery,scrollPanePlacingComponents);
        splitPanePanelGUIComponentsAndPanelTestingQuery.setDividerLocation(650);
    }

    /**
     * Method for initializing ScrollPane.
     * <p>
     * Метод для инициализации ScrollPane.
     */
    private void intScrollPane() {
        scrollPanePlacingComponents = new JScrollPane();
        scrollPanePlacingComponents.setViewportView(panelGUIComponents);
    }

    /**
     * A method for initializing the graphic component layout panel.
     * <p>
     * Метод для инициализации панели расположения графических компонентов.
     */
    private void initPanelGUIComponents() {
        panelGUIComponents = WidgetFactory.createPanel("panelGUIComponents");
        panelGUIComponents.setLayout(new BoxLayout(panelGUIComponents,BoxLayout.Y_AXIS));
        panelGUIComponents.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    /**
     * A method for placing components as well as setting up a dialog (window).
     * <p>
     * Метод для размещения компонентов а так же настройки диалога (окна).
     */
    private void arrangeComponent() {
        arrangeComponentsInPanelForPlacingComponents();
        configurationDialog();
    }

    /**
     * A method for configuring the parameters of a dialog (window) created by this class.
     * <p>
     * Метод для настройки параметров диалога (окна) созданного этим классом.
     */
    private void configurationDialog() {
        setLayout(new BorderLayout());
        add(panelPlacingComponents, BorderLayout.CENTER);
    }

    /**
     * A method for placing components in a panel for placing components.
     * <p>
     * Метод для размещения компонентов в панели для размещения компонентов.
     */
    private void arrangeComponentsInPanelForPlacingComponents() {
        panelPlacingComponents.add(splitPanePanelGUIComponentsAndPanelTestingQuery, BorderLayout.CENTER);
        panelPlacingComponents.add(toolBar, BorderLayout.NORTH);
    }

    public void addBlockInPanelBlocks(JPanel panel){
        panelGUIComponents.add(panel);
        panelGUIComponents.revalidate();
        panelGUIComponents.repaint();
    }

    public void clearBlocksPanel(){
        panelGUIComponents.removeAll();
    }

    /**
     * The method for getting the output panel of the elements.
     * <p>
     * Метод для получения панели вывода элементов.
     */
    public JPanel getPanelGUIComponents() {
        return panelGUIComponents;
    }

    /**
     * A method for adding tables to the list of tables that are in the output panel.
     * <p>
     * Метод для добавления таблиц в список таблиц которые находятся на панели вывода.
     */
    public void addTableInListTable(JTable table) {
        listTablesPanelGUIComponents.add(table);
    }

    /**
     * A method for removing tables from the list of tables that are in the output panel.
     * <p>
     * Метод для удаления таблиц из списка таблиц которые находятся на панели вывода.
     */
    public void removeTableInListTable(String nameTable) {
        for (int i = 0; i < listTablesPanelGUIComponents.size(); i++) {
            if (listTablesPanelGUIComponents.get(i).getColumnName(0).equals(nameTable)) {
                listTablesPanelGUIComponents.remove(i);
            }
        }
    }

    /**
     * A method for getting a list of tables that are in the output panel.
     * <p>
     * Метод для получения списка таблиц которые находятся на панели вывода.
     */
    public ArrayList<JTable> getListTable() {
        return listTablesPanelGUIComponents;
    }

    /**
     * A method for getting a list of table names that are in the output panel.
     * <p>
     * Метод для получения списка имён таблиц которые находятся на панели вывода.
     */
    public ArrayList<String> getListNameTable() {
        ArrayList<String> nameTables = new ArrayList<>();

        for (int i = 0; i < listTablesPanelGUIComponents.size(); i++) {
            nameTables.add(listTablesPanelGUIComponents.get(i).getColumnName(0));
        }

        return nameTables;
    }

    /**
     * A method for changing the query in the test query output panel.
     * <p>
     * Метод для смены запроса в панели вывода тестовых запросов.
     */
    public void setTextInPanelOutputTestingQuery(String Query) {
        panelTestingQuery.setQueryAreaText(Query);
    }

    /**
     * The method for getting the request that is written to the test query output panel.
     * <p>
     * Метод для получения запроса который записан в панель вывода тестовых запросов.
     */
    public String getTestQuery() {
        return panelTestingQuery.getQueryAreaText();
    }


    /**
     * The method for applying the test query.
     * <p>
     * Метод для применения тестового запроса.
     */
    public void applyTestQuery(QueryEditor queryEditor) {
        queryEditor.setEditorText(panelTestingQuery.getQueryAreaText());
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
