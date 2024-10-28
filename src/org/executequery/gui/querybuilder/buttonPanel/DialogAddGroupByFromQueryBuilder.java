package org.executequery.gui.querybuilder.buttonPanel;

import org.executequery.gui.WidgetFactory;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.executequery.gui.querybuilder.WorkQuryEditor.CreateStringQuery;
import org.underworldlabs.swing.ConnectionsComboBox;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * A class for creating a dialog (window) for adding groupings to a query.
 * <p>
 * Класс для создания диалога (окна) для добавления группировок в запрос.
 */
public class DialogAddGroupByFromQueryBuilder extends JDialog {

    // --- Elements accepted using the constructor. ----
    // --- Поля, которые передаются через конструктор. ---

    private CreateStringQuery createStringQueryConstructor;
    private QueryBuilderPanel queryBuilderPanelConstructor;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelForPlacingComponents;
    private JPanel panelArrangeCheckBoxInScrollPane;
    private JLabel labelSearch;
    private JLabel labelConnections;
    private JTextField textFieldSearch;
    private JButton buttonForAddGroupByInQuery;
    private JButton buttonForUsingSearch;
    private JScrollPane scrollPaneFromOutputAttributes;
    private ConnectionsComboBox connections;

    /**
     * Creating a dialog (window) to add groupings to the query.
     * <p>
     * Создаём диалог (окно) для добавления группировок в запрос.
     */
    public DialogAddGroupByFromQueryBuilder(CreateStringQuery createStringQuery, QueryBuilderPanel queryBuilderPanel) {
        this.createStringQueryConstructor = createStringQuery;
        this.queryBuilderPanelConstructor = queryBuilderPanel;
        init();
    }

    /**
     * A method for initializing fields.
     * <p>
     * Метод для инициализации полей.
     */
    private void init() {
        initPanels();
        initLabels();
        initComboBox();
        initTextField();
        intiScrollPane();
        initButton();
        arrangeComponents();
    }

    /**
     * A method for placing components.
     * <p>
     * Метод для размещения компонентов.
     */
    private void arrangeComponents() {
        arrangeComponentsInPanelForPlacingComponents();
        configurationDialog();
    }

    private void initButton() {
        buttonForUsingSearch = WidgetFactory.createButton("buttonSearch","Начать поиск");

        buttonForAddGroupByInQuery = WidgetFactory.createButton("Create Group By Button", "Добавить",event -> {
            eventButtonAddGroupBy();
        });
    }

    private void intiScrollPane() {
        scrollPaneFromOutputAttributes = new JScrollPane();
        scrollPaneFromOutputAttributes.setViewportView(panelArrangeCheckBoxInScrollPane);
        scrollPaneFromOutputAttributes.setPreferredSize(new Dimension(100,300));
        addCheckBoxesInScrollPane();
    }

    private void initTextField() {
        textFieldSearch = WidgetFactory.createTextField("textFieldSearch");
    }

    private void initComboBox() {
        connections = WidgetFactory.createConnectionComboBox("connectionBox", true);
        connections.setMaximumSize(new Dimension(200, 30));
    }

    private void initLabels() {
        labelConnections = WidgetFactory.createLabel("Подключение:");
        labelSearch = WidgetFactory.createLabel("Поиск:");
    }

    private void initPanels() {
        panelForPlacingComponents = WidgetFactory.createPanel("Main Panel Group By");
        panelForPlacingComponents.setLayout(new GridBagLayout());
        panelForPlacingComponents.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        panelArrangeCheckBoxInScrollPane = new JPanel();
        panelArrangeCheckBoxInScrollPane.setLayout(new BoxLayout(panelArrangeCheckBoxInScrollPane,BoxLayout.Y_AXIS));
    }

    /**
     * A method for adding checkboxes to the scrollbar.
     * <p>
     * Метод для добавления флажков панель прокрутки.
     */
    private void addCheckBoxesInScrollPane() {
        if(!createStringQueryConstructor.getAttribute().isEmpty()) {
            for (int i = 0; i < createStringQueryConstructor.getAttribute().split(",").length; i++) {
                JCheckBox checkBox = new JCheckBox(createStringQueryConstructor.getAttribute().split(",")[i]);
                panelArrangeCheckBoxInScrollPane.add(checkBox);
            }
        }
    }

    /**
     * A method for configuring the parameters of a dialog (window).
     * <p>
     * Метод для настройки параметров диалога (окна).
     */
    private void configurationDialog() {
        setLayout(new BorderLayout());
        add(panelForPlacingComponents, BorderLayout.CENTER);
        setTitle("Группировка");
        setIconImage(new ImageIcon("red_expert.png").getImage());
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(600, 500);
        setVisible(true);
    }

    /**
     * A method for placing components on the component placement panel.
     * <p>
     * Метод для размещения компонентов на панели размещения компонентов.
     */
    private void arrangeComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(5,5,5,5).fillHorizontally();
        panelForPlacingComponents.add(labelConnections,gridBagHelper.setXY(0,0).setMinWeightX().get());
        panelForPlacingComponents.add(connections,gridBagHelper.setXY(1,0).setMaxWeightX().spanX().get());
        panelForPlacingComponents.add(labelSearch,gridBagHelper.setXY(0,1).setMinWeightX().setWidth(1).get());
        panelForPlacingComponents.add(textFieldSearch,gridBagHelper.setXY(1,1).setMaxWeightX().get());
        panelForPlacingComponents.add(buttonForUsingSearch,gridBagHelper.setXY(2,1).setMinWeightX().setWidth(1).get());
        panelForPlacingComponents.add(scrollPaneFromOutputAttributes, gridBagHelper.setXY(0, 2).spanX().setMaxWeightX().get());
        panelForPlacingComponents.add(buttonForAddGroupByInQuery,gridBagHelper.setXY(1,3).setMinWeightX().setWidth(1).get());
    }

    /**
     * The event method of the button for adding a grouping to the query.
     * <p>
     * Метод события кнопки добавления группировки в запрос.
     */
    private void eventButtonAddGroupBy() {

        ArrayList<String> NameAttributes = new ArrayList<>();

        Component[] arrayComponents = panelArrangeCheckBoxInScrollPane.getComponents();
        JCheckBox[] arrayCheckBox = new JCheckBox[panelArrangeCheckBoxInScrollPane.getComponents().length];

        for(int i = 0; i < arrayComponents.length; i++){
            arrayCheckBox[i] = (JCheckBox) arrayComponents[i];
        }

        for(int i = 0; i < arrayCheckBox.length; i++){
            if(arrayCheckBox[i].isSelected()) {
                NameAttributes.add(arrayCheckBox[i].getText());
            }
        }

        createStringQueryConstructor.addGroupBy(NameAttributes);
        queryBuilderPanelConstructor.setTextInPanelOutputTestingQuery(createStringQueryConstructor.getQuery());
        CloseDialog();
    }

    /**
     * The method for closing the dialog (window).
     * <p>
     * Метод для закрытия диалога (окна).
     */
    private void CloseDialog() {
        setVisible(false);
        dispose();
    }

}
