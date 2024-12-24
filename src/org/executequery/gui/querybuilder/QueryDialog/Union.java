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

/**
 * This class creates a dialog (window) for adding unions to a query.
 * <p>
 * Это класс создаёт диалог (окно) для добавления союзов (Union) в запрос.
 *
 * @author Krylov Gleb
 */
public class Union extends JDialog {

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
    private JScrollPane scrollPaneUnions;
    private JButton buttonAddUnion;
    private JButton buttonRemoveUnion;
    private JButton buttonClose;

    /**
     * A dialog (window) is created.
     * A method is used to initialize fields.
     * <p>
     * Создаётся диалог (окно).
     * Используется метод для инициализации полей.
     */
    public Union(QueryConstructor queryConstructor, QBPanel queryBuilderPanel) {
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
        initScrollPane();
        initTextField();
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
    }

    /**
     * A method for initializing the ScrollPane and placing checkboxes on it.
     * <p>
     * Метод для инициализации scrollPane и размещения на ней флажков.
     */
    private void initScrollPane() {
        scrollPaneUnions = new JScrollPane();
        scrollPaneUnions.setPreferredSize(new Dimension(100, 320));
        scrollPaneUnions.setViewportView(panelPlacingCheckBoxInScrollPane);
        arrangeCheckBoxesInScrollPane();
    }

    /**
     * A method for initializing buttons.
     * <p>
     * Метод для инициализации кнопок.
     */
    private void initButton() {
        buttonAddUnion = WidgetFactory.createButton("buttonAddUnion",Bundles.get("common.add.button"), event -> {
            eventAddUnion();
            arrangeCheckBoxesInScrollPane();
        });

        buttonRemoveUnion = WidgetFactory.createButton("buttonRemoveUnion",Bundles.get("common.delete.button"), event -> {
            eventRemoveUnion();
            arrangeCheckBoxesInScrollPane();
        });

        buttonClose = WidgetFactory.createButton("buttonClose",Bundles.get("common.close.button"),event -> {
            closeDialog();
        });

        placingButtonsInPanel();
    }

    /**
     * A method for placing buttons in a panel to place buttons.
     * <p>
     * Метод для размещения кнопок в панели для размещения кнопок.
     */
    private void placingButtonsInPanel() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(10,5,10,5).fillHorizontally();
        panelButton.add(buttonAddUnion,gridBagHelper.setXY(0,0).setMaxWeightX().get());
        panelButton.add(buttonRemoveUnion,gridBagHelper.nextCol().setMaxWeightX().get());
        panelButton.add(buttonClose,gridBagHelper.nextRow().setMaxWeightX().get());
    }

    /**
     * A method for initializing a text field.
     * <p>
     * Метод для инициализации текстового поля.
     */
    private void initTextField() {
        textFieldSearch = WidgetFactory.createTextField("textFieldSearch");
        textFieldSearch.setToolTipText(Bundles.get("QueryBuilder.Union.EnterTheUnion"));
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

            private void textChanged(){
                panelPlacingCheckBoxInScrollPane = WidgetFactory.createPanel("panelPlacingCheckBoxInScrollPane");
                panelPlacingCheckBoxInScrollPane.setLayout(new BoxLayout(panelPlacingCheckBoxInScrollPane, BoxLayout.Y_AXIS));

                if (!queryConstructor.getUnion().isEmpty()) {
                    String[] splitUnionText = queryConstructor.getUnion().split("(?<=UNION)");

                    for (int i = 0; i < splitUnionText.length-1; i++) {
                        if(splitUnionText[i].contains(textFieldSearch.getText().toUpperCase())) {
                            JCheckBox checkBox = new JCheckBox(splitUnionText[i]);
                            checkBox.setToolTipText(Bundles.get("QueryBuilder.Union.toolTipTextCheckBoxUnion"));
                            panelPlacingCheckBoxInScrollPane.add(checkBox);
                        }
                    }
                }
                scrollPaneUnions.setViewportView(panelPlacingCheckBoxInScrollPane);
            }
        });
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
     * A method for configuring the parameters of a dialog (window) created by this class.
     * <p>
     * Метод для настройки параметров диалога (окна) созданного этим классом.
     */
    private void configurationDialog() {
        setLayout(new BorderLayout());
        add(panelPlacingComponents, BorderLayout.CENTER);
        setTitle(Bundles.get("QueryBuilder.Union.title"));
        setResizable(false);
        setIconImage(getAndCreateIconDialog().getImage());
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(600, 485);
        setVisible(true);
    }

    /**
     * A method for placing components in a panel for placing components.
     * <p>
     * Метод для размещения компонентов в панели для размещения компонентов.
     */
    private void arrangeComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(5, 5, 5, 5).fillHorizontally();
        panelPlacingComponents.add(labelSearch, gridBagHelper.setXY(0, 0).setMinWeightX().get());
        panelPlacingComponents.add(textFieldSearch, gridBagHelper.nextCol().setMaxWeightX().get());
        panelPlacingComponents.add(scrollPaneUnions, gridBagHelper.previousCol().nextRow().spanX().setMaxWeightX().get());
        panelPlacingComponents.add(panelButton, gridBagHelper.nextCol().nextRow().spanX().spanY().setMaxWeightX().get());
    }

    /**
     * A method that implements the functionality of adding a union to a query.
     * <p>
     * Метод реализующий функционал добавления union в запрос.
     */
    private void eventAddUnion() {
        StringBuilder stringBuilderUnionValue = new StringBuilder(queryConstructor.getUnion());
        StringBuilder stringBuilderTestQueryValue = new StringBuilder(queryBuilderPanel.getTestQuery());

        if (stringBuilderTestQueryValue.indexOf(stringBuilderUnionValue.toString()) >= 0) {
            stringBuilderTestQueryValue.replace(stringBuilderTestQueryValue.indexOf(stringBuilderUnionValue.toString()), stringBuilderTestQueryValue.indexOf(stringBuilderUnionValue.toString()) + stringBuilderUnionValue.toString().length(), "");
        }

        stringBuilderTestQueryValue.replace(stringBuilderTestQueryValue.length() - 1, stringBuilderTestQueryValue.length(), "");
        deleteOptimization(stringBuilderTestQueryValue);
        stringBuilderUnionValue.append(stringBuilderTestQueryValue.toString()).append("UNION").append("\n");

        queryConstructor.setUnion(stringBuilderUnionValue.toString());
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    /**
     * A method for removing optimization.
     * <p>
     * Метод для удаления оптимизации.
     */
    private static void deleteOptimization(StringBuilder stringBuilderTestQueryValue) {
        if (stringBuilderTestQueryValue.toString().contains("OPTIMIZE FOR ALL ROWS")) {
            stringBuilderTestQueryValue.replace(stringBuilderTestQueryValue.indexOf("OPTIMIZE FOR ALL ROWS")-1, stringBuilderTestQueryValue.indexOf("OPTIMIZE FOR ALL ROWS") + "OPTIMIZE FOR ALL ROWS".length() + 1, "");
        }

        if (stringBuilderTestQueryValue.toString().contains("OPTIMIZE FOR FIRST ROWS")) {
            stringBuilderTestQueryValue.replace(stringBuilderTestQueryValue.indexOf("OPTIMIZE FOR FIRST ROWS")-1, stringBuilderTestQueryValue.indexOf("OPTIMIZE FOR FIRST ROWS") + "OPTIMIZE FOR FIRST ROWS".length() + 1, "");
        }
    }

    /**
     * A method that implements the functionality of removing union from a query.
     * <p>
     * Метод реализующий функционал удаления union из запроса.
     */
    private void eventRemoveUnion(){
        StringBuilder stringBuilderUnionValue = new StringBuilder(queryConstructor.getUnion());
        JCheckBox[] checkBoxesFromScrollPane = getCheckBoxesFromPanelArrangeCheckBox();

        for (int i = 0; i < checkBoxesFromScrollPane.length; i++) {
            if (checkBoxesFromScrollPane[i].isSelected()) {
                if (checkBoxesFromScrollPane.length == 1) {
                    stringBuilderUnionValue.replace(0,stringBuilderUnionValue.length(),"");
                    queryConstructor.setUnion("");
                    queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                    return;
                } else {
                    if (stringBuilderUnionValue.indexOf(checkBoxesFromScrollPane[i].getText()) >= 0) {
                        stringBuilderUnionValue.replace(stringBuilderUnionValue.indexOf(checkBoxesFromScrollPane[i].getText()),
                                stringBuilderUnionValue.indexOf(checkBoxesFromScrollPane[i].getText()) + checkBoxesFromScrollPane[i].getText().length(),
                                "");
                    }
                }
            }
        }

        if(stringBuilderUnionValue.indexOf("\n") == 0){
            stringBuilderUnionValue.replace(0,1,"");
        }

        queryConstructor.setUnion(stringBuilderUnionValue.toString());
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    /**
     * A method for getting an array of checkboxes from the panel on which they are placed.
     * <p>
     * Метод для получения массива флажков из панели на которой они размещены.
     */
    private JCheckBox[] getCheckBoxesFromPanelArrangeCheckBox() {
        Component[] components = panelPlacingCheckBoxInScrollPane.getComponents();
        JCheckBox[] checkBoxes = new JCheckBox[panelPlacingCheckBoxInScrollPane.getComponents().length];

        for (int i = 0; i < components.length; i++) {
            checkBoxes[i] = ((JCheckBox) components[i]);
        }

        return checkBoxes;
    }

    /**
     * A method for placing checkboxes on a ScrollPane.
     * <p>
     * Метод для размещения флажков на ScrollPane.
     */
    private void arrangeCheckBoxesInScrollPane() {
        panelPlacingCheckBoxInScrollPane = WidgetFactory.createPanel("panelPlacingCheckBoxInScrollPane");
        panelPlacingCheckBoxInScrollPane.setLayout(new BoxLayout(panelPlacingCheckBoxInScrollPane, BoxLayout.Y_AXIS));

        if (!queryConstructor.getUnion().isEmpty()) {
            String[] splitUnionText = queryConstructor.getUnion().split("(?<=UNION)");

            for (int i = 0; i < splitUnionText.length-1; i++) {
                JCheckBox checkBox = new JCheckBox(splitUnionText[i]);
                checkBox.setToolTipText(Bundles.get("QueryBuilder.Union.toolTipTextCheckBoxUnion"));
                panelPlacingCheckBoxInScrollPane.add(checkBox);
            }
        }

        scrollPaneUnions.setViewportView(panelPlacingCheckBoxInScrollPane);
    }

    /**
     * A method for creating and receiving a dialog icon.
     * <p>
     * Метод для создания и получения иконки диалога.
     */
    private ImageIcon getAndCreateIconDialog(){
       return IconManager.getIcon(BrowserConstants.APPLICATION_IMAGE,"svg",512, IconManager.IconFolder.BASE);
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
