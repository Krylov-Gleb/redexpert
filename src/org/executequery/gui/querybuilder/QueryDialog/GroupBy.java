package org.executequery.gui.querybuilder.QueryDialog;

import org.executequery.gui.IconManager;
import org.executequery.gui.WidgetFactory;
import org.executequery.gui.browser.BrowserConstants;
import org.executequery.gui.querybuilder.QBPanel;
import org.executequery.gui.querybuilder.QueryConstructor;
import org.executequery.localization.Bundles;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

/**
 * This class creates a dialog (window) that adds a grouping (Group By) to the request.
 * <p>
 * Этот класс создаёт диалог (окно) который добавляет в запрос группировку (Group By).
 */
public class GroupBy extends JDialog {

    // --- Fields that are passed through the constructor ----
    // --- Поля, которые передаются через конструктор ---

    private QueryConstructor queryConstructor;
    private QBPanel queryBuilderPanel;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelPlacingComponents;
    private JPanel panelPlacingCheckBoxInScrollPane;
    private JPanel panelButton;
    private JLabel labelSearch;
    private JTextField textFieldSearch;
    private JScrollPane scrollPaneCheckBoxesAttribute;
    private JButton buttonClose;
    private JButton buttonClear;

    /**
     * A dialog (window) is created.
     * A method is used to initialize fields.
     * <p>
     * Создаётся диалог (окно).
     * Используется метод для инициализации полей.
     */
    public GroupBy(QueryConstructor queryConstructor, QBPanel queryBuilderPanel) {
        this.queryConstructor = queryConstructor;
        this.queryBuilderPanel = queryBuilderPanel;
        init();
    }

    /**
     * A method for initializing fields.
     * <p>
     * Метод для инициализации полей.
     */
    private void init() {
        initPanel();
        initLabel();
        initButton();
        initTextField();
        intiScrollPane();
        arrangeComponents();
    }

    /**
     * A method for initializing the ScrollPane and placing checkboxes on it.
     * <p>
     * Метод для инициализации scrollPane и размещения на ней флажков.
     */
    private void intiScrollPane() {
        scrollPaneCheckBoxesAttribute = new JScrollPane();
        scrollPaneCheckBoxesAttribute.setPreferredSize(new Dimension(100, 300));
        arrangeCheckBoxesInScrollPane();
    }

    /**
     * A method for initializing buttons.
     * <p>
     * Метод для инициализации кнопок.
     */
    private void initButton() {
        buttonClose = WidgetFactory.createButton("buttonClose", Bundles.get("common.close.button"), event -> {
            closeDialog();
        });

        buttonClear = WidgetFactory.createButton("buttonClear", Bundles.get("common.clear.button"), event -> {
            eventClearGroupBy();
        });

        placingButtonsInPanel();
    }

    /**
     * A method for placing buttons in a panel to place buttons.
     * <p>
     * Метод для размещения кнопок в панели для размещения кнопок.
     */
    private void placingButtonsInPanel() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(5, 5, 5, 5).fillHorizontally();
        panelButton.add(new Label(" "), gridBagHelper.setXY(0, 0).setMaxWeightX().get());
        panelButton.add(buttonClose, gridBagHelper.nextCol().setMaxWeightX().get());
        panelButton.add(buttonClear, gridBagHelper.nextCol().setMaxWeightX().get());
        panelButton.add(new Label(" "), gridBagHelper.nextCol().setMaxWeightX().get());
    }

    /**
     * The method for clearing GroupBy.
     * <p>
     * Метод для очистки GroupBy.
     */
    private void eventClearGroupBy() {
        queryConstructor.setGroupBy("");

        for (int i = 0; i < panelPlacingCheckBoxInScrollPane.getComponents().length; i++) {
            JCheckBox checkBox = (JCheckBox) panelPlacingCheckBoxInScrollPane.getComponent(i);
            if (checkBox.isSelected()) {
                checkBox.setSelected(false);
            }
        }

        scrollPaneCheckBoxesAttribute.revalidate();
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    /**
     * A method for initializing a text field.
     * <p>
     * Метод для инициализации текстового поля.
     */
    private void initTextField() {
        textFieldSearch = WidgetFactory.createTextField("textFieldSearch");
        textFieldSearch.setToolTipText(Bundles.get("QueryBuilder.GroupBy.searchAttribute"));
        textFieldSearch.setMinimumSize(new Dimension(200, 25));
        textFieldSearch.setPreferredSize(new Dimension(200, 25));
        textFieldSearch.setMaximumSize(new Dimension(200, 25));
        textFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                textChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                textChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                textChanged();
            }

            private void textChanged() {
                panelPlacingCheckBoxInScrollPane = WidgetFactory.createPanel("panelPlacingCheckBoxInScrollPane");
                panelPlacingCheckBoxInScrollPane.setLayout(new BoxLayout(panelPlacingCheckBoxInScrollPane, BoxLayout.Y_AXIS));

                ArrayList<String> arrayAttributes = new ArrayList<>();

                for (int i = 0; i < queryBuilderPanel.getListTable().size(); i++) {
                    if (queryConstructor.getTable().contains(queryBuilderPanel.getListTable().get(i).getColumnName(0))) {
                        for (int j = 0; j < queryBuilderPanel.getListTable().get(i).getRowCount(); j++) {
                            arrayAttributes.add(queryBuilderPanel.getListTable().get(i).getColumnName(0) + "." + queryBuilderPanel.getListTable().get(i).getValueAt(j, 0).toString());
                        }
                    }
                }

                if (!queryConstructor.getAttribute().isEmpty()) {
                    StringBuilder stringBuilder = new StringBuilder(queryConstructor.getGroupBy());

                    for (int i = 0; i < arrayAttributes.size(); i++) {
                        if (arrayAttributes.get(i).contains(textFieldSearch.getText().toUpperCase())) {
                            JCheckBox checkBox = new JCheckBox(arrayAttributes.get(i));
                            checkBox.setToolTipText(Bundles.get("QueryBuilder.GroupBy.toolTipTextCheckBoxAttribute"));
                            checkBox.addItemListener(new ItemListener() {
                                @Override
                                public void itemStateChanged(ItemEvent e) {
                                    if (checkBox.isSelected()) {
                                        addGroupBy(checkBox);
                                    } else {
                                        removeGroupBy(checkBox);
                                    }
                                }
                            });

                            if (stringBuilder.toString().contains(checkBox.getText())) {
                                checkBox.setSelected(true);
                            }

                            panelPlacingCheckBoxInScrollPane.add(checkBox);
                        }
                    }
                }
                scrollPaneCheckBoxesAttribute.setViewportView(panelPlacingCheckBoxInScrollPane);
                scrollPaneCheckBoxesAttribute.revalidate();
            }
        });
    }

    /**
     * A method for initializing labels.
     * <p>
     * Метод для инициализации меток.
     */
    private void initLabel() {
        labelSearch = WidgetFactory.createLabel(Bundles.get("common.search.button"));
    }

    /**
     * The method for initializing JPanel.
     * <p>
     * Метод для инициализации JPanel.
     */
    private void initPanel() {
        panelPlacingComponents = WidgetFactory.createPanel("panelPlacingComponents");
        panelPlacingComponents.setLayout(new GridBagLayout());

        panelButton = WidgetFactory.createPanel("panelButton");
        panelButton.setLayout(new GridBagLayout());
    }

    /**
     * A method for adding checkboxes to the scrollbar.
     * <p>
     * Метод для добавления флажков панель прокрутки.
     */
    private void arrangeCheckBoxesInScrollPane() {
        panelPlacingCheckBoxInScrollPane = WidgetFactory.createPanel("panelPlacingCheckBoxInScrollPane");
        panelPlacingCheckBoxInScrollPane.setLayout(new BoxLayout(panelPlacingCheckBoxInScrollPane, BoxLayout.Y_AXIS));

        ArrayList<String> arrayAttributes = new ArrayList<>();

        for (int i = 0; i < queryBuilderPanel.getListTable().size(); i++) {
            if (queryConstructor.getTable().contains(queryBuilderPanel.getListTable().get(i).getColumnName(0))) {
                for (int j = 0; j < queryBuilderPanel.getListTable().get(i).getRowCount(); j++) {
                    arrayAttributes.add(queryBuilderPanel.getListTable().get(i).getColumnName(0) + "." + queryBuilderPanel.getListTable().get(i).getValueAt(j, 0).toString());
                }
            }
        }

        if (!queryConstructor.getAttribute().isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(queryConstructor.getGroupBy());

            for (int i = 0; i < arrayAttributes.size(); i++) {
                JCheckBox checkBox = new JCheckBox(arrayAttributes.get(i));
                checkBox.setToolTipText(Bundles.get("QueryBuilder.GroupBy.toolTipTextCheckBoxAttribute"));
                checkBox.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (checkBox.isSelected()) {
                            addGroupBy(checkBox);
                        } else {
                            removeGroupBy(checkBox);
                        }
                    }
                });

                if (stringBuilder.toString().contains(checkBox.getText())) {
                    checkBox.setSelected(true);
                }

                panelPlacingCheckBoxInScrollPane.add(checkBox);
            }
        }
        scrollPaneCheckBoxesAttribute.setViewportView(panelPlacingCheckBoxInScrollPane);
        scrollPaneCheckBoxesAttribute.revalidate();
    }

    /**
     * A method for placing components and configuring the dialog.
     * <p>
     * Метод для размещения компонентов и настройки диалога.
     */
    private void arrangeComponents() {
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
        add(panelPlacingComponents, BorderLayout.CENTER);
        setTitle(Bundles.get("QueryBuilder.GroupBy.title"));
        setResizable(false);
        setIconImage(getAndCreateIconDialog().getImage());
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(600, 430);
        setVisible(true);
    }

    /**
     * A method for placing components on the component placement panel.
     * <p>
     * Метод для размещения компонентов на панели размещения компонентов.
     */
    private void arrangeComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(10, 5, 10, 5).fillHorizontally();
        panelPlacingComponents.add(labelSearch, gridBagHelper.setXY(0, 0).setMinWeightX().get());
        panelPlacingComponents.add(textFieldSearch, gridBagHelper.nextCol().setMaxWeightX().get());
        panelPlacingComponents.add(scrollPaneCheckBoxesAttribute, gridBagHelper.previousCol().nextRow().spanX().setMaxWeightX().get());
        panelPlacingComponents.add(panelButton, gridBagHelper.nextRow().spanX().spanY().setMaxWeightX().get());
    }

    /**
     * A method that implements the functionality of adding a grouping (GroupBy) to a query.
     * <p>
     * Метод реализующий функционал добавления группировки (GroupBy) в запрос.
     */
    public void addGroupBy(JCheckBox checkBox) {
        if (!queryConstructor.getGroupBy().contains(checkBox.getText())) {
            StringBuilder stringBuilder = new StringBuilder(queryConstructor.getGroupBy());

            if (stringBuilder.toString().isEmpty()) {
                stringBuilder.append("GROUP BY ").append(checkBox.getText());
            } else {
                stringBuilder.append(",").append(checkBox.getText());
            }

            queryConstructor.setGroupBy(stringBuilder.toString());
            queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
        }
    }

    /**
     * A method that implements the functionality of deleting a grouping (GroupBy) from a query.
     * <p>
     * Метод реализующий функционал удаления группировки (GroupBy) из запроса.
     */
    public void removeGroupBy(JCheckBox checkBox) {
        if (queryConstructor.getGroupBy().contains(checkBox.getText())) {
            StringBuilder stringBuilder = new StringBuilder(queryConstructor.getGroupBy());

            if (stringBuilder.charAt(stringBuilder.indexOf(checkBox.getText()) - 1) == ',') {
                stringBuilder.replace(stringBuilder.indexOf(checkBox.getText()) - 1, stringBuilder.indexOf(checkBox.getText()) - 1 + checkBox.getText().length() + 1, "");
                queryConstructor.setGroupBy(stringBuilder.toString());
                queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                return;
            }

            if (stringBuilder.charAt(stringBuilder.indexOf(checkBox.getText()) - 1) == ' ') {
                if (stringBuilder.toString().split(",").length == 1) {
                    stringBuilder.replace(0, stringBuilder.length(), "");
                    queryConstructor.setGroupBy(stringBuilder.toString());
                    queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                    return;
                }
                if (stringBuilder.toString().split(",").length > 1) {
                    stringBuilder.replace(stringBuilder.indexOf(checkBox.getText()), stringBuilder.indexOf(checkBox.getText()) + checkBox.getText().length() + 1, "");
                    queryConstructor.setGroupBy(stringBuilder.toString());
                    queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                    return;
                }
            }
        }
    }

    /**
     * A method for creating and receiving a dialog icon.
     * <p>
     * Метод для создания и получения иконки диалога.
     */
    private ImageIcon getAndCreateIconDialog() {
        return IconManager.getIcon(BrowserConstants.APPLICATION_IMAGE, "svg", 512, IconManager.IconFolder.BASE);
    }

    /**
     * The method for closing the dialog (window).
     * <p>
     * Метод для закрытия диалога (окна).
     */
    private void closeDialog() {
        setVisible(false);
        dispose();
    }

}
