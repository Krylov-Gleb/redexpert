package org.executequery.gui.querybuilder.QueryDialog;

import org.executequery.gui.IconManager;
import org.executequery.gui.WidgetFactory;
import org.executequery.gui.browser.BrowserConstants;
import org.executequery.gui.querybuilder.QBPanel;
import org.executequery.gui.querybuilder.QueryConstructor;
import org.executequery.localization.Bundles;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;

/**
 * This class creates a dialog (window) that adds to the with request.
 * <p>
 * Этот класс создаёт диалог (окно) который добавляет в запрос with.
 *
 * @author Krylov Gleb
 */
public class With extends JDialog {

    // --- Fields that are passed through the constructor ----
    // --- Поля, которые передаются через конструктор ---

    private QueryConstructor queryConstructor;
    private QBPanel queryBuilderPanel;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelPlacingComponents;
    private JPanel panelPlacingCheckBoxInScrollPane;
    private JPanel panelButton;
    private JScrollPane scrollPaneWith;
    private JLabel labelUsingQuery;
    private JLabel labelRecursive;
    private JLabel labelAlias;
    private JTextField textFieldAlias;
    private JTextField textFieldUsingQuery;
    private JComboBox<String> comboBoxRecursive;
    private JButton buttonAddWith;
    private JButton buttonClose;
    private JButton buttonRemoveWith;

    /**
     * A dialog (window) is created.
     * A method is used to initialize fields.
     * <p>
     * Создаётся диалог (окно).
     * Используется метод для инициализации полей.
     */
    public With(QueryConstructor queryConstructor, QBPanel queryBuilderPanel) {
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
        initScrollPane();
        initLabel();
        initTextField();
        initButton();
        initComboBox();
        arrangeComponents();
    }

    /**
     * A method for initializing labels.
     * <p>
     * Метод для инициализации меток.
     */
    private void initLabel() {
        labelUsingQuery = WidgetFactory.createLabel(Bundles.get("QueryBuilder.With.labelUsingQuery"));
        labelAlias = WidgetFactory.createLabel(Bundles.get("QueryBuilder.With.labelAlias"));
        labelRecursive = WidgetFactory.createLabel(Bundles.get("QueryBuilder.With.labelRecursive"));
    }

    /**
     * A method for initializing the ScrollPane and placing checkboxes on it.
     * <p>
     * Метод для инициализации scrollPane и размещения на ней флажков.
     */
    private void initScrollPane() {
        scrollPaneWith = new JScrollPane();
        scrollPaneWith.setPreferredSize(new Dimension(100, 200));
        arrangeCheckBoxesInScrollPane();
    }

    /**
     * A method for initializing text fields.
     * <p>
     * Метод для инициализации текстовых полей.
     */
    private void initTextField() {
        textFieldAlias = WidgetFactory.createTextField("textFieldAlias");
        textFieldAlias.setToolTipText(Bundles.get("QueryBuilder.Union.enterAlias"));
        textFieldAlias.setMinimumSize(new Dimension(200, 30));
        textFieldAlias.setPreferredSize(new Dimension(200, 30));
        textFieldAlias.setMaximumSize(new Dimension(200, 30));

        textFieldUsingQuery = WidgetFactory.createTextField("textFieldUsingQuery");
        textFieldUsingQuery.setToolTipText(Bundles.get("QueryBuilder.Union.enterRequest"));
        textFieldUsingQuery.setMinimumSize(new Dimension(200, 30));
        textFieldUsingQuery.setPreferredSize(new Dimension(200, 30));
        textFieldUsingQuery.setMaximumSize(new Dimension(200, 30));
    }


    /**
     * A method for initializing drop-down lists (comboBox).
     * <p>
     * Метод для инициализации выпадающих списков (comboBox).
     */
    private void initComboBox() {
        comboBoxRecursive = WidgetFactory.createComboBox("comboBoxRecursive", new String[]{Bundles.get("common.recursive"), Bundles.get("common.not_recursive")});
        comboBoxRecursive.setToolTipText(Bundles.get("QueryBuilder.Union.useRecursion"));
        comboBoxRecursive.setMinimumSize(new Dimension(200, 30));
        comboBoxRecursive.setPreferredSize(new Dimension(200, 30));
        comboBoxRecursive.setMaximumSize(new Dimension(200, 30));
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
     * A method for initializing buttons.
     * <p>
     * Метод для инициализации кнопок.
     */
    private void initButton() {
        buttonAddWith = WidgetFactory.createButton("buttonAddWith", Bundles.get("common.add.button"), event -> {
            addWith();
            arrangeCheckBoxesInScrollPane();
        });

        buttonRemoveWith = WidgetFactory.createButton("buttonRemoveWith", Bundles.get("common.delete.button"), event -> {
            removeWith();
            arrangeCheckBoxesInScrollPane();
        });

        buttonClose = WidgetFactory.createButton("buttonClose", Bundles.get("common.close.button"), event -> {
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
        GridBagHelper gridBagHelper = new GridBagHelper().anchorNorth().setInsets(5, 5, 5, 5).fillHorizontally();
        panelButton.add(buttonAddWith, gridBagHelper.setXY(0, 0).setMaxWeightX().get());
        panelButton.add(buttonRemoveWith, gridBagHelper.nextCol().setMaxWeightX().get());
        panelButton.add(buttonClose, gridBagHelper.nextRow().setMaxWeightX().get());
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
        setTitle(Bundles.get("QueryBuilder.With.title"));
        setResizable(false);
        setIconImage(getAndCreateIconDialog().getImage());
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(600, 460);
        setVisible(true);
    }

    /**
     * A method for placing components in a panel for placing components.
     * <p>
     * Метод для размещения компонентов в панели для размещения компонентов.
     */
    private void arrangeComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().setInsets(10, 5, 10, 5).anchorCenter().setMaxWeightX().fillHorizontally();
        panelPlacingComponents.add(scrollPaneWith, gridBagHelper.setXY(0, 0).setWidth(3).setMaxWeightX().get());
        panelPlacingComponents.add(labelUsingQuery, gridBagHelper.nextRow().setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(textFieldUsingQuery, gridBagHelper.nextCol().setWidth(2).setMaxWeightX().get());
        panelPlacingComponents.add(labelAlias, gridBagHelper.previousCol().nextRow().setWidth(1).setMinWeightX().get());
        panelPlacingComponents.add(textFieldAlias, gridBagHelper.nextCol().setWidth(2).setMaxWeightX().get());
        panelPlacingComponents.add(labelRecursive, gridBagHelper.previousCol().nextRow().setWidth(1).setMinWeightX().get());
        panelPlacingComponents.add(comboBoxRecursive, gridBagHelper.nextCol().setWidth(2).setMaxWeightX().get());
        panelPlacingComponents.add(panelButton, gridBagHelper.nextRow().spanX().spanY().setMinWeightX().get());
    }

    /**
     * A method that implements the functionality of adding with to a query.
     * <p>
     * Метод реализующий функционал добавления with в запрос.
     */
    private void addWith() {
        StringBuilder stringBuilder = new StringBuilder(queryConstructor.getWith());
        StringBuilder stringBuilderUseQuery = new StringBuilder(textFieldUsingQuery.getText());

        if(!stringBuilder.toString().contains(" " + textFieldAlias.getText() + " ")) {
            if(!stringBuilder.toString().contains("\n" + textFieldAlias.getText() + " ")) {
                if (!textFieldUsingQuery.toString().isEmpty()) {
                    if (!textFieldAlias.getText().isEmpty()) {
                        if (stringBuilder.toString().isEmpty()) {
                            stringBuilder.append("WITH");

                            if (comboBoxRecursive.getSelectedIndex() == 0) {
                                stringBuilder.append(" ").append("RECURSIVE");
                            }

                            stringBuilder.append(" ").append(textFieldAlias.getText()).append(" AS (").append("\n");
                            deleteOptimization(stringBuilderUseQuery);
                            stringBuilder.append(stringBuilderUseQuery.toString().replace(";", ""));

                            stringBuilder.append(")");

                        } else {

                            stringBuilderUseQuery.replace(0, queryConstructor.getWith().length(), "");

                            if (!stringBuilder.toString().contains(stringBuilderUseQuery.toString())) {
                                if (comboBoxRecursive.getSelectedIndex() == 0) {
                                    if (stringBuilder.indexOf("RECURSIVE") < 0) {
                                        stringBuilder.replace(stringBuilder.indexOf("WITH") + "WITH".length(), stringBuilder.indexOf("WITH") + "WITH".length() + 1, " RECURSIVE ");
                                    }
                                } else {
                                    if (stringBuilder.indexOf("RECURSIVE") > 0) {
                                        stringBuilder.replace(stringBuilder.indexOf("RECURSIVE"), stringBuilder.indexOf("RECURSIVE") + "RECURSIVE ".length(), "");
                                    }
                                }

                                stringBuilder.append(",").append("\n");

                                stringBuilder.append(textFieldAlias.getText()).append(" AS (");
                                deleteOptimization(stringBuilderUseQuery);
                                stringBuilder.append(stringBuilderUseQuery.toString().replace(";", ""));

                                stringBuilder.append(")");
                            }
                        }

                        queryConstructor.setWith(stringBuilder.toString());
                        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                    }
                }
            }
        }
    }

    /**
     * A method that implements the functionality of removing with from a query.
     * <p>
     * Метод реализующий функционал удаления with из запроса.
     */
    private void removeWith() {
        StringBuilder stringBuilder = new StringBuilder(queryConstructor.getWith());
        JCheckBox[] checkBoxesInScrollPane = getCheckBoxesFromPanelArrangeCheckBox();

        for (int i = 0; i < checkBoxesInScrollPane.length; i++) {
            if (checkBoxesInScrollPane[i].isSelected()) {
                if (checkBoxesInScrollPane.length == 1) {
                    stringBuilder.replace(0, stringBuilder.length(), "");
                    queryConstructor.setWith("");
                    queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                    return;
                } else {
                    if (stringBuilder.indexOf(checkBoxesInScrollPane[i].getText()) >= 0) {
                        stringBuilder.replace(stringBuilder.indexOf(checkBoxesInScrollPane[i].getText()),
                                stringBuilder.indexOf(checkBoxesInScrollPane[i].getText()) + checkBoxesInScrollPane[i].getText().length(),
                                "");
                    }
                }
            }
        }

        if (stringBuilder.lastIndexOf(",") == stringBuilder.length() - 1) {
            stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "");
        }

        if(stringBuilder.toString().length() == "WITH RECURSIVE ".length() || stringBuilder.toString().length() == "WITH ".length()){
            stringBuilder.replace(0,stringBuilder.length(),"");
        }

        queryConstructor.setWith(stringBuilder.toString());
        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
    }

    /**
     * A method for removing optimization.
     * <p>
     * Метод для удаления оптимизации.
     */
    private static void deleteOptimization(StringBuilder stringBuilder) {
        if (stringBuilder.toString().contains("OPTIMIZE FOR ALL ROWS")) {
            stringBuilder.replace(stringBuilder.indexOf("OPTIMIZE FOR ALL ROWS")-1, stringBuilder.indexOf("OPTIMIZE FOR ALL ROWS") + "OPTIMIZE FOR ALL ROWS".length() + 1, "");
        }

        if (stringBuilder.toString().contains("OPTIMIZE FOR FIRST ROWS")) {
            stringBuilder.replace(stringBuilder.indexOf("OPTIMIZE FOR FIRST ROWS")-1, stringBuilder.indexOf("OPTIMIZE FOR FIRST ROWS") + "OPTIMIZE FOR FIRST ROWS".length() + 1, "");
        }
    }

    /**
     * A method for getting an array of checkboxes from the panel on which they are placed.
     * <p>
     * Метод для получения массива флажков из панели на которой они размещены.
     */
    private JCheckBox[] getCheckBoxesFromPanelArrangeCheckBox() {
        Component[] component = panelPlacingCheckBoxInScrollPane.getComponents();
        JCheckBox[] checkBoxes = new JCheckBox[panelPlacingCheckBoxInScrollPane.getComponents().length];

        for (int i = 0; i < component.length; i++) {
            checkBoxes[i] = ((JCheckBox) component[i]);
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

        if (!queryConstructor.getWith().isEmpty()) {
            String[] splitWithText = queryConstructor.getWith().split("(?<=\\),)");

            for (int i = 0; i < splitWithText.length; i++) {
                if (i == 0) {
                    StringBuilder stringBuilder = new StringBuilder(splitWithText[i]);

                    if (stringBuilder.indexOf("WITH RECURSIVE") >= 0) {
                        stringBuilder.replace(stringBuilder.indexOf("WITH RECURSIVE"), stringBuilder.indexOf("WITH RECURSIVE") + "WITH RECURSIVE ".length(), "");
                    } else {
                        stringBuilder.replace(stringBuilder.indexOf("WITH"), stringBuilder.indexOf("WITH") + "WITH ".length(), "");
                    }

                    splitWithText[i] = stringBuilder.toString();
                }
                panelPlacingCheckBoxInScrollPane.add(new JCheckBox(splitWithText[i]));
            }
        }

        scrollPaneWith.setViewportView(panelPlacingCheckBoxInScrollPane);
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
