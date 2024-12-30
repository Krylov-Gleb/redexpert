package org.executequery.gui.querybuilder;

import org.executequery.base.TabView;
import org.executequery.gui.WidgetFactory;
import org.executequery.gui.editor.QueryEditor;
import org.executequery.gui.editor.QueryEditorTextPanel;
import org.executequery.localization.Bundles;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
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
    private JSplitPane mainSplitPanePanel;
    private JSplitPane splitPanePanelGUIComponentsAndPanelTestingQuery;
    private QueryEditorTextPanel panelTestingQuery;
    private JPanel blocksPanel;
    private JPanel panelGUIComponents;
    private JScrollPane scrollPanePlacingBlocks;
    private JScrollPane scrollPanePlacingGUIComponents;
    private QBToolBar toolBar;
    private ArrayList<JTable> listTablesPanelGUIComponents;
    private QueryConstructor queryConstructor;

    // --- Other fields ---
    // --- Остальные поля ---

    private int locationNewTableX = 50;
    private int locationNewTableY = 50;
    private ArrayList<String> arrayListCoordinateDeleteComponents;

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

        blocksPanel = WidgetFactory.createPanel("blocksPanel");
        blocksPanel.setLayout(new BoxLayout(blocksPanel,BoxLayout.Y_AXIS));
        blocksPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        panelGUIComponents = WidgetFactory.createPanel("panelGUIComponents");
        panelGUIComponents.setPreferredSize(new Dimension(100000,100000));
        panelGUIComponents.setLayout(null);
        panelGUIComponents.setBorder(BorderFactory.createLineBorder(Color.GRAY));
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
        panelTestingQuery.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
    }

    /**
     * A method for initializing arrays.
     * <p>
     * Метод для инициализации массивов.
     */
    private void initArrays() {
        listTablesPanelGUIComponents = new ArrayList<>();
        arrayListCoordinateDeleteComponents = new ArrayList<>();
    }

    /**
     * Method for initializing SplitPane.
     * <p>
     * Метод для инициализации SplitPane.
     */
    private void initSplitPane() {
        splitPanePanelGUIComponentsAndPanelTestingQuery = new JSplitPane(JSplitPane.VERTICAL_SPLIT,scrollPanePlacingGUIComponents,panelTestingQuery);
        splitPanePanelGUIComponentsAndPanelTestingQuery.setDividerLocation(550);

        mainSplitPanePanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,splitPanePanelGUIComponentsAndPanelTestingQuery, scrollPanePlacingBlocks);
        mainSplitPanePanel.setDividerLocation(800);
    }

    /**
     * Method for initializing ScrollPane.
     * <p>
     * Метод для инициализации ScrollPane.
     */
    private void intScrollPane() {
        scrollPanePlacingBlocks = new JScrollPane();
        scrollPanePlacingBlocks.setViewportView(blocksPanel);

        scrollPanePlacingGUIComponents = new JScrollPane();
        scrollPanePlacingGUIComponents.setViewportView(panelGUIComponents);
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
        panelPlacingComponents.add(mainSplitPanePanel, BorderLayout.CENTER);
        panelPlacingComponents.add(toolBar, BorderLayout.NORTH);
    }

    public void addBlockInPanelBlocks(JLabel label){
        blocksPanel.add(label);
        blocksPanel.revalidate();
        blocksPanel.repaint();
    }

    public void clearBlocksPanel(){
        blocksPanel.removeAll();
    }

    /**
     * The method for getting the output panel of the elements.
     * <p>
     * Метод для получения панели вывода элементов.
     */
    public JPanel getBlocksPanel() {
        return blocksPanel;
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

    public void addBlockInBlocksPanel(String text,Color colorBorder,Color colorBackground){
        JLabel label = new JLabel();
        label.setFont(new Font("Arial",Font.BOLD,18));
        label.setOpaque(true);
        label.setBackground(colorBackground);
        label.setText(text);
        label.setBorder(new CompoundBorder(BorderFactory.createLineBorder(colorBorder,5,true),BorderFactory.createEmptyBorder(10,10,10,10)));
        addBlockInPanelBlocks(label);
    }

    /**
     * A method for adding a table to the output panel of graphical components.
     * <p>
     * Метод для добавления таблицы на панель вывода графических компонентов.
     */
    public void addTableInPanelGUIComponents(JPanel panel) {
        JScrollPane scrollPane = (JScrollPane) panel.getComponent(0);
        if (isTableUnique(scrollPane)) {
            if (arrayListCoordinateDeleteComponents.isEmpty()) {
                if (panelGUIComponents.getComponents().length > 0) {
                    if (locationNewTableX >= 800) {
                        locationNewTableX = 50;
                        locationNewTableY = locationNewTableY + 250;
                    } else {
                        locationNewTableX = locationNewTableX + 250;
                    }
                } else {
                    locationNewTableX = 50;
                    locationNewTableY = 50;
                }
                if (locationNewTableX == 50) {
                    JViewport viewport = scrollPane.getViewport();
                    JTable table = (JTable) viewport.getComponent(0);
                    JTableHeader header = table.getTableHeader();
                    header.setBackground(new Color(255, 91, 91));
                    header.setForeground(Color.WHITE);
                }
                if (locationNewTableX == 300) {
                    JViewport viewport = scrollPane.getViewport();
                    JTable table = (JTable) viewport.getComponent(0);
                    JTableHeader header = table.getTableHeader();
                    header.setBackground(new Color(174, 0, 0));
                    header.setForeground(Color.WHITE);
                }
                if (locationNewTableX == 550) {
                    JViewport viewport = scrollPane.getViewport();
                    JTable table = (JTable) viewport.getComponent(0);
                    JTableHeader header = table.getTableHeader();
                    header.setBackground(new Color(116, 2, 2));
                    header.setForeground(Color.WHITE);
                }
                if (locationNewTableX == 800) {
                    JViewport viewport = scrollPane.getViewport();
                    JTable table = (JTable) viewport.getComponent(0);
                    JTableHeader header = table.getTableHeader();
                    header.setBackground(new Color(61, 0, 0));
                    header.setForeground(Color.WHITE);
                }
                panel.setBounds(locationNewTableX, locationNewTableY, 220, 220);
            } else {
                if (panelGUIComponents.getComponents().length == 0) {
                    arrayListCoordinateDeleteComponents.clear();
                    locationNewTableX = 50;
                    locationNewTableY = 50;
                    if (locationNewTableX == 50) {
                        JViewport viewport = scrollPane.getViewport();
                        JTable table = (JTable) viewport.getComponent(0);
                        JTableHeader header = table.getTableHeader();
                        header.setBackground(new Color(255, 91, 91));
                        header.setForeground(Color.WHITE);
                    }
                    if (locationNewTableX == 300) {
                        JViewport viewport = scrollPane.getViewport();
                        JTable table = (JTable) viewport.getComponent(0);
                        JTableHeader header = table.getTableHeader();
                        header.setBackground(new Color(174, 0, 0));
                        header.setForeground(Color.WHITE);
                    }
                    if (locationNewTableX == 550) {
                        JViewport viewport = scrollPane.getViewport();
                        JTable table = (JTable) viewport.getComponent(0);
                        JTableHeader header = table.getTableHeader();
                        header.setBackground(new Color(116, 2, 2));
                        header.setForeground(Color.WHITE);
                    }
                    if (locationNewTableX == 800) {
                        JViewport viewport = scrollPane.getViewport();
                        JTable table = (JTable) viewport.getComponent(0);
                        JTableHeader header = table.getTableHeader();
                        header.setBackground(new Color(61, 0, 0));
                        header.setForeground(Color.WHITE);
                    }
                    panel.setBounds(locationNewTableX, locationNewTableY, 220, 220);
                } else {
                    if (Integer.parseInt(arrayListCoordinateDeleteComponents.get(0).split(",")[0]) == 50) {
                        JViewport viewport = scrollPane.getViewport();
                        JTable table = (JTable) viewport.getComponent(0);
                        JTableHeader header = table.getTableHeader();
                        header.setBackground(new Color(255, 91, 91));
                        header.setForeground(Color.WHITE);
                    }
                    if (Integer.parseInt(arrayListCoordinateDeleteComponents.get(0).split(",")[0]) == 300) {
                        JViewport viewport = scrollPane.getViewport();
                        JTable table = (JTable) viewport.getComponent(0);
                        JTableHeader header = table.getTableHeader();
                        header.setBackground(new Color(174, 0, 0));
                        header.setForeground(Color.WHITE);
                    }
                    if (Integer.parseInt(arrayListCoordinateDeleteComponents.get(0).split(",")[0]) == 550) {
                        JViewport viewport = scrollPane.getViewport();
                        JTable table = (JTable) viewport.getComponent(0);
                        JTableHeader header = table.getTableHeader();
                        header.setBackground(new Color(116, 2, 2));
                        header.setForeground(Color.WHITE);
                    }
                    if (Integer.parseInt(arrayListCoordinateDeleteComponents.get(0).split(",")[0]) == 800) {
                        JViewport viewport = scrollPane.getViewport();
                        JTable table = (JTable) viewport.getComponent(0);
                        JTableHeader header = table.getTableHeader();
                        header.setBackground(new Color(61, 0, 0));
                        header.setForeground(Color.WHITE);
                    }
                    panel.setBounds(Integer.parseInt(arrayListCoordinateDeleteComponents.get(0).split(",")[0]), Integer.parseInt(arrayListCoordinateDeleteComponents.get(0).split(",")[1]), 220, 220);
                    arrayListCoordinateDeleteComponents.remove(0);
                }
            }
        }
        panelGUIComponents.add(panel);
        panelGUIComponents.revalidate();
        panelGUIComponents.repaint();
    }

    /**
     * A method for removing a table from the output panel of graphical components.
     * <p>
     * Метод для удаления таблицы из панели вывода графических компонентов.
     */
    public void removeTableInInputPanel(String nameTable) {
        for (int i = 0; i < getListTable().size(); i++) {
            if (nameTable.equals(getListTable().get(i).getColumnName(0))) {
                JPanel panel = (JPanel) panelGUIComponents.getComponent(i);
                arrayListCoordinateDeleteComponents.add(panel.getX() + "," + panel.getY());
                panelGUIComponents.remove(i);
                panelGUIComponents.revalidate();
                panelGUIComponents.repaint();
            }
        }
    }
    /**
     * A method for checking the table for uniqueness.
     * <p>
     * Метод для проверки таблицы на уникальность.
     */
    private boolean isTableUnique(JScrollPane scrollPane) {
        boolean Check = true;
        for (int i = 0; i < panelGUIComponents.getComponents().length; i++) {
            if (panelGUIComponents.getComponents()[i] == scrollPane) {
                Check = false;
            }
        }
        return Check;
    }

    public JPanel getPanelGUIComponents(){
        return panelGUIComponents;
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
