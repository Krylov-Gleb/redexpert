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
 * A class that creates a dialog (window) to add to the sorting request (OrderBy).
 * <p>
 * Класс создающий диалог (окно) для добавления в запрос сортировки (OrderBy).
 *
 * @author Krylov Gleb
 */
public class OrderBy extends JDialog {

    // --- Fields that are passed through the constructor ----
    // --- Поля, которые передаются через конструктор ---

    private QueryConstructor queryConstructor;
    private QBPanel queryBuilderPanel;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelPlacingComponents;
    private JPanel panelPlacingCheckBoxesInScrollPane;
    private JPanel panelButton;
    private JScrollPane scrollPaneAttributesOrderBy;
    private JLabel labelSearch;
    private JLabel labelAscAndDesc;
    private JComboBox<String> comboBoxAscDesc;
    private JTextField textFieldSearch;
    private JButton buttonClose;
    private JButton buttonClear;

    /**
     * A dialog (window) is created.
     * A method is used to initialize fields.
     * <p>
     * Создаётся диалог (окно).
     * Используется метод для инициализации полей.
     */
    public OrderBy(QBPanel queryBuilderPanel, QueryConstructor createStringQuery) {
        this.queryBuilderPanel = queryBuilderPanel;
        this.queryConstructor = createStringQuery;
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
        initComboBox();
        initScrollPane();
        arrangeComponents();
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
     * A method for initializing labels.
     * <p>
     * Метод для инициализации меток.
     */
    private void initLabel() {
        labelSearch = WidgetFactory.createLabel(Bundles.get("common.search.button"));
        labelAscAndDesc = WidgetFactory.createLabel(Bundles.get("QueryBuilder.OrderBy.labelAddAscAndDesc"));
    }

    /**
     * A method for initializing the ScrollPane and placing checkboxes on it.
     * <p>
     * Метод для инициализации scrollPane и размещения на ней флажков.
     */
    private void initScrollPane() {
        scrollPaneAttributesOrderBy = new JScrollPane();
        scrollPaneAttributesOrderBy.setPreferredSize(new Dimension(200, 300));
        arrangeCheckBoxesOnScrollPane();
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
            queryConstructor.setOrderBy("");
            clearSelectedCheckBoxes();
            queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
        });

        placingButtonsInPanel();
    }

    /**
     * A method for placing buttons in a panel to place buttons.
     * <p>
     * Метод для размещения кнопок в панели для размещения кнопок.
     */
    private void placingButtonsInPanel() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(10, 5, 10, 5).fillHorizontally();
        panelButton.add(new Label(" "), gridBagHelper.setXY(0, 0).setMaxWeightX().get());
        panelButton.add(buttonClose, gridBagHelper.nextCol().setMaxWeightX().get());
        panelButton.add(buttonClear, gridBagHelper.nextCol().setMaxWeightX().get());
        panelButton.add(new Label(" "), gridBagHelper.nextCol().setMaxWeightX().get());
    }

    /**
     * Method for clearing checkboxes (CheckBox)
     * <p>
     * Метод для очистки галочек в флажках (CheckBox)
     */
    private void clearSelectedCheckBoxes() {
        for (int i = 0; i < panelPlacingCheckBoxesInScrollPane.getComponents().length; i++) {
            JCheckBox checkBox = (JCheckBox) panelPlacingCheckBoxesInScrollPane.getComponent(i);
            if (checkBox.isSelected()) {
                checkBox.setSelected(false);
            }
        }

        scrollPaneAttributesOrderBy.revalidate();
    }

    /**
     * A method for initializing a text field.
     * <p>
     * Метод для инициализации текстового поля.
     */
    private void initTextField() {
        textFieldSearch = WidgetFactory.createTextField("textFieldSearch");
        textFieldSearch.setToolTipText(Bundles.get("QueryBuilder.OrderBy.searchAttribute"));
        textFieldSearch.setMinimumSize(new Dimension(200, 25));
        textFieldSearch.setPreferredSize(new Dimension(200, 25));
        textFieldSearch.setMaximumSize(new Dimension(200, 25));
        eventAddDocumentListenerInTextFields();
    }

    /**
     * Search implementation.
     * <p>
     * Реализация поиска.
     */
    private void eventAddDocumentListenerInTextFields() {
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
                panelPlacingCheckBoxesInScrollPane = WidgetFactory.createPanel("panelPlacingCheckBoxesInScrollPane");
                panelPlacingCheckBoxesInScrollPane.setLayout(new BoxLayout(panelPlacingCheckBoxesInScrollPane, BoxLayout.Y_AXIS));

                ArrayList<String> arrayAttributes = new ArrayList<>();

                for (int i = 0; i < queryBuilderPanel.getListTable().size(); i++) {
                    if (queryConstructor.getTable().contains(queryBuilderPanel.getListTable().get(i).getColumnName(0))) {
                        for (int j = 0; j < queryBuilderPanel.getListTable().get(i).getRowCount(); j++) {
                            arrayAttributes.add(queryBuilderPanel.getListTable().get(i).getColumnName(0) + "." + queryBuilderPanel.getListTable().get(i).getValueAt(j, 0).toString());
                        }
                    }
                }

                if (!queryConstructor.getAttribute().isEmpty()) {
                    StringBuilder stringBuilder = new StringBuilder(queryConstructor.getOrderBy());

                    for (int i = 0; i < arrayAttributes.size(); i++) {
                        if (arrayAttributes.get(i).contains(textFieldSearch.getText().toUpperCase())) {
                            JCheckBox checkBox = new JCheckBox(arrayAttributes.get(i));
                            checkBox.setToolTipText(Bundles.get("QueryBuilder.OrderBy.toolTipTextCheckBoxAttribute"));
                            checkBox.addItemListener(new ItemListener() {
                                @Override
                                public void itemStateChanged(ItemEvent e) {
                                    if (checkBox.isSelected()) {
                                        addOrderBy(checkBox);
                                    } else {
                                        removeOrderBy(checkBox);
                                    }
                                }
                            });

                            if (stringBuilder.toString().contains(checkBox.getText())) {
                                checkBox.setSelected(true);
                            }

                            panelPlacingCheckBoxesInScrollPane.add(checkBox);
                        }
                    }
                }

                scrollPaneAttributesOrderBy.setViewportView(panelPlacingCheckBoxesInScrollPane);
                scrollPaneAttributesOrderBy.revalidate();
            }
        });
    }

    /**
     * A method for initializing drop-down lists (comboBox).
     * <p>
     * Метод для инициализации выпадающих списков (comboBox).
     */
    private void initComboBox() {
        comboBoxAscDesc = WidgetFactory.createComboBox("comboBoxAscDesc", new String[]{"ASC", "DESC"});
        comboBoxAscDesc.setToolTipText(Bundles.get("QueryBuilder.OrderBy.sortingOrder"));
    }

    /**
     * A method for placing components as well as setting up a dialog (window).
     * <p>
     * Метод для размещения компонентов а так же настройки диалога (окна).
     */
    private void arrangeComponents() {
        arrangeComponentsInPanelForPlacingComponents();
        configurationDialog();
    }

    /**
     * A method for placing CheckBoxes on a ScrollPane.
     * <p>
     * Метод для размещения CheckBoxes на ScrollPane.
     */
    private void arrangeCheckBoxesOnScrollPane() {
        panelPlacingCheckBoxesInScrollPane = WidgetFactory.createPanel("panelPlacingCheckBoxesInScrollPane");
        panelPlacingCheckBoxesInScrollPane.setLayout(new BoxLayout(panelPlacingCheckBoxesInScrollPane, BoxLayout.Y_AXIS));

        ArrayList<String> arrayAttributes = new ArrayList<>();

        for (int i = 0; i < queryBuilderPanel.getListTable().size(); i++) {
            if (queryConstructor.getTable().contains(queryBuilderPanel.getListTable().get(i).getColumnName(0))) {
                for (int j = 0; j < queryBuilderPanel.getListTable().get(i).getRowCount(); j++) {
                    arrayAttributes.add(queryBuilderPanel.getListTable().get(i).getColumnName(0) + "." + queryBuilderPanel.getListTable().get(i).getValueAt(j, 0).toString());
                }
            }
        }

        if (!queryConstructor.getAttribute().isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(queryConstructor.getOrderBy());

            for (int i = 0; i < arrayAttributes.size(); i++) {
                JCheckBox checkBox = new JCheckBox(arrayAttributes.get(i));
                checkBox.setToolTipText(Bundles.get("QueryBuilder.OrderBy.toolTipTextCheckBoxAttribute"));
                checkBox.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (checkBox.isSelected()) {
                            addOrderBy(checkBox);
                        } else {
                            removeOrderBy(checkBox);
                        }
                    }
                });

                if (stringBuilder.toString().contains(checkBox.getText())) {
                    checkBox.setSelected(true);
                }

                panelPlacingCheckBoxesInScrollPane.add(checkBox);
            }
        }

        scrollPaneAttributesOrderBy.setViewportView(panelPlacingCheckBoxesInScrollPane);
        scrollPaneAttributesOrderBy.revalidate();
    }

    /**
     * A method for placing components in a panel for placing components.
     * <p>
     * Метод для размещения компонентов в панели для размещения компонентов.
     */
    private void arrangeComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(10, 5, 10, 5).fillHorizontally();
        panelPlacingComponents.add(labelSearch, gridBagHelper.setXY(0, 0).setMinWeightX().get());
        panelPlacingComponents.add(textFieldSearch, gridBagHelper.nextCol().setMaxWeightX().get());
        panelPlacingComponents.add(scrollPaneAttributesOrderBy, gridBagHelper.previousCol().nextRow().spanX().setMaxWeightX().get());
        panelPlacingComponents.add(labelAscAndDesc, gridBagHelper.nextRow().spanX().setMinWeightX().get());
        panelPlacingComponents.add(comboBoxAscDesc, gridBagHelper.nextRow().spanX().setMinWeightX().get());
        panelPlacingComponents.add(panelButton, gridBagHelper.nextRow().spanX().spanY().setMaxWeightX().get());
    }

    /**
     * A method for configuring the parameters of a dialog (window) created by this class.
     * <p>
     * Метод для настройки параметров диалога (окна) созданного этим классом.
     */
    private void configurationDialog() {
        setLayout(new BorderLayout());
        add(panelPlacingComponents, BorderLayout.CENTER);
        setTitle(Bundles.get("QueryBuilder.OrderBy.title"));
        setResizable(false);
        setIconImage(getAndCreateIconDialog().getImage());
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(600, 505);
        setVisible(true);
    }

    /**
     * A method that implements the functionality of adding sorting (OrderBy) to a query.
     * <p>
     * Метод реализующий функционал добавления сортировки (OrderBy) в запрос.
     */
    public void addOrderBy(JCheckBox checkBox) {
        if (!queryConstructor.getOrderBy().contains(checkBox.getText())) {
            StringBuilder stringBuilder = new StringBuilder(queryConstructor.getOrderBy());

            if (stringBuilder.toString().isEmpty()) {
                stringBuilder.append("ORDER BY ").append(checkBox.getText()).append(" ").append(comboBoxAscDesc.getSelectedItem().toString());
            } else {
                stringBuilder.append(",").append(checkBox.getText()).append(" ").append(comboBoxAscDesc.getSelectedItem().toString());
            }

            queryConstructor.setOrderBy(stringBuilder.toString());
            queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
        }
    }

    /**
     * A method that implements the functionality of removing sorting (OrderBy) from a query.
     * <p>
     * Метод реализующий функционал удаления сортировки (OrderBy) из запроса.
     */
    public void removeOrderBy(JCheckBox checkBox) {
        if (queryConstructor.getOrderBy().contains(checkBox.getText())) {
            StringBuilder stringBuilder = new StringBuilder(queryConstructor.getOrderBy());
            String[] orderByElements = stringBuilder.substring(stringBuilder.indexOf("ORDER BY") + "ORDER BY".length() + 1).split(",");

            for (int i = 0; i < orderByElements.length; i++) {
                if (orderByElements.length == 1) {
                    stringBuilder.replace(0, stringBuilder.length(), "");
                    queryConstructor.setOrderBy(stringBuilder.toString());
                    queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                    return;
                }
                if (orderByElements[i].contains(checkBox.getText())) {
                    stringBuilder.replace(stringBuilder.indexOf(orderByElements[i]), stringBuilder.indexOf(orderByElements[i]) + orderByElements[i].length() + 1, "");
                }
            }

            if (stringBuilder.lastIndexOf(",") == stringBuilder.length() - 1) {
                stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "");
            }

            queryConstructor.setOrderBy(stringBuilder.toString());
            queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
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
