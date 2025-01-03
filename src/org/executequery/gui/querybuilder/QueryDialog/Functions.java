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
 * This class creates a dialog (window) that adds functions to the request.
 * <p>
 * Этот класс создаёт диалог (окно) который добавляет в запрос функции.
 *
 * @author Krylov Gleb
 */
public class Functions extends JDialog {

    // --- Fields that are passed through the constructor ----
    // --- Поля, которые передаются через конструктор ---

    private final QueryConstructor queryConstructor;
    private final QBPanel queryBuilderPanel;

    // --- GUI Components ---
    // --- Компоненты графического интерфейса ---

    private JPanel panelPlacingComponents;
    private JPanel panelPlacingCheckBoxInScrollPane;
    private JPanel panelButton;
    private JScrollPane scrollPanelCheckBoxFunctions;
    private JLabel labelFunctions;
    private JLabel labelParamFunctions;
    private JLabel labelAliasFunctions;
    private JLabel labelSearchFunctions;
    private JComboBox comboBoxFunction;
    private JTextField textFieldParamFunctions;
    private JTextField textFieldAlias;
    private JTextField textFieldSearchFunctions;
    private JButton buttonClose;
    private JButton buttonRemoveFunctions;
    private JButton buttonAddFunctions;

    // --- Arrays ---
    // --- Массивы ---

    private final String[] functions = new String[]{"", "RDB$GET_CONTEXT", "RDB$SET_CONTEXT", "CREATE_FILE", "DELETE_FILE", "READ_FILE", "CHECK_DDL_RIGHTS",
            "CHECK_DML_RIGHTS", "RDB$ROLE_IN_USE", "RDB$SYSTEM_PRIVILEGE", "MAKE_DBKEY", "RDB$TRACE_MSG", "RDB$ERROR", "RDB$GET_TRANSACTION_CN",
            "ABS", "ACOS", "ACOSH", "ASIN", "ASINH", "ATAN", "ATAN2", "ATANH", "CEILING", "COS", "COSH", "COT", "EXP", "FLOOR", "LN", "LOG", "LOG10",
            "MOD", "PI", "POWER", "RAND", "ROUND", "SIGN", "SIN", "SINH", "SQRT", "TAN", "TANH", "TRUNC", "ASCII_CHAR", "ASCII_VAL", "BIT_LENGTH",
            "CHARACTER_LENGTH", "DAMLEV", "LEFT", "LOWER", "LPAD", "OCTET_LENGTH", "OVERLAY", "POSITION", "REGEXP_SUBSTR", "REPLACE",
            "REVERSE", "RIGHT", "RPAD", "SUBSTRING", "TRIM", "UPPER", "UNICODE_CHAR", "UNICODE_VAL", "AT", "DATEADD", "DATEDIFF", "EXTRACT",
            "FIRST_DAY", "LAST_DAY", "UTC_TIMESTAMP", "COMPARE_DECFLOAT", "NORMALIZE_DECFLOAT", "QUANTIZE", "TOTALORDER", "BASE64_ENCODE",
            "BASE64_DECODE", "HEX_ENCODE", "HEX_DECODE", "BLOB_APPEND", "HASH", "HASH_CP", "CRC32", "ENCRYPT", "DECRYPT", "RSA_PRIVATE", "RSA_PUBLIC",
            "RSA_ENCRYPT", "RSA_DECRYPT", "RSA_SIGN", "RSA_VERIFY", "CAST", "BIN_AND", "BIN_NOT", "BIN_OR", "BIN_SHL", "BIN_SHR", "BIN_XOR", "CHAR_TO_UUID",
            "GEN_UUID", "UUID_TO_CHAR", "GEN_ID", "NEXT VALUE FOR", "CASE-WHEN-ELSE", "COALESCE", "DECODE", "IIF", "MAXVALUE", "MINVALUE", "NULLIF",
            "GREATEST", "LEAST", "CPU_LOAD", "LDAP_ATTR", "AVG", "COUNT", "LIST", "MAX", "MIN", "SUM", "CORR", "COVAR_POP", "COVAR_SAMP", "STDDEV_POP",
            "STDDEV_SAMP", "VAR_POP", "VAR_SAMP", "REGR_AVGX", "REGR_AVGY", "REGR_COUNT", "REGR_INTERCEPT", "REGR_R2", "REGR_SLOPE", "REGR_SXX",
            "REGR_SXY", "REGR_SYY"};

    /**
     * A dialog (window) is created.
     * A method is used to initialize fields.
     * <p>
     * Создаётся диалог (окно).
     * Используется метод для инициализации полей.
     */
    public Functions(QBPanel queryBuilderPanel, QueryConstructor queryConstructor) {
        this.queryBuilderPanel = queryBuilderPanel;
        this.queryConstructor = queryConstructor;
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
        initTextField();
        initScrollPane();
        initComboBox();
        initButton();
        arrangeComponents();
    }

    /**
     * A method for initializing buttons.
     * <p>
     * Метод для инициализации кнопок.
     */
    private void initButton() {
        buttonAddFunctions = WidgetFactory.createButton("buttonAddFunctions", Bundles.get("common.add.button"), event -> {
            addFunctions();
            arrangeCheckBoxesInScrollPane();
        });

        buttonRemoveFunctions = WidgetFactory.createButton("buttonRemoveFunctions", Bundles.get("common.delete.button"), event -> {
            removeFunctions();
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
        panelButton.add(buttonAddFunctions, gridBagHelper.setXY(0, 0).setMaxWeightX().get());
        panelButton.add(buttonRemoveFunctions, gridBagHelper.nextCol().setMaxWeightX().get());
        panelButton.add(buttonClose, gridBagHelper.nextRow().setMaxWeightX().get());
    }

    /**
     * A method for initializing the ScrollPane and placing checkboxes on it.
     * <p>
     * Метод для инициализации scrollPane и размещения на ней флажков.
     */
    private void initScrollPane() {
        scrollPanelCheckBoxFunctions = new JScrollPane();
        scrollPanelCheckBoxFunctions.setPreferredSize(new Dimension(100, 200));
        arrangeCheckBoxesInScrollPane();
    }

    /**
     * A method for initializing a text field.
     * <p>
     * Метод для инициализации текстового поля.
     */
    private void initTextField() {
        textFieldAlias = WidgetFactory.createTextField("textFieldAlias");
        textFieldAlias.setMinimumSize(new Dimension(200, 30));
        textFieldAlias.setPreferredSize(new Dimension(200, 30));
        textFieldAlias.setMaximumSize(new Dimension(200, 30));
        textFieldAlias.setToolTipText(Bundles.get("QueryBuilder.Functions.toolTipTextAlias"));

        textFieldParamFunctions = WidgetFactory.createTextField("textFieldParamFunctions");
        textFieldParamFunctions.setMinimumSize(new Dimension(200, 30));
        textFieldParamFunctions.setPreferredSize(new Dimension(200, 30));
        textFieldParamFunctions.setMaximumSize(new Dimension(200, 30));
        textFieldParamFunctions.setToolTipText(Bundles.get("QueryBuilder.Functions.toolTipTextParamFunctions"));

        textFieldSearchFunctions = WidgetFactory.createTextField("textFieldSearchFunctions");
        textFieldSearchFunctions.setMinimumSize(new Dimension(200, 30));
        textFieldSearchFunctions.setPreferredSize(new Dimension(200, 30));
        textFieldSearchFunctions.setMaximumSize(new Dimension(200, 30));
        textFieldSearchFunctions.setToolTipText(Bundles.get("QueryBuilder.Functions.toolTipTextSearchFunctions"));
        eventAddDocumentListenerInTextFields(textFieldSearchFunctions);
    }

    /**
     * A method for initializing drop-down lists (comboBox).
     * <p>
     * Метод для инициализации выпадающих списков (comboBox).
     */
    private void initComboBox() {
        comboBoxFunction = WidgetFactory.createComboBox("comboBoxFunction", functions);
        comboBoxFunction.setMinimumSize(new Dimension(200, 30));
        comboBoxFunction.setPreferredSize(new Dimension(200, 30));
        comboBoxFunction.setMaximumSize(new Dimension(200, 30));
        comboBoxFunction.setToolTipText(Bundles.get("QueryBuilder.Functions.toolTipTextFunctions"));
        comboBoxFunction.setEditable(true);
        comboBoxFunction.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent event) {
                if (comboBoxFunction.getSelectedItem().toString().isEmpty()) {
                    comboBoxFunction.setEditable(true);
                } else {
                    comboBoxFunction.setEditable(false);
                }
            }
        });
    }

    /**
     * A method for initializing labels.
     * <p>
     * Метод для инициализации меток.
     */
    private void initLabel() {
        labelFunctions = WidgetFactory.createLabel(Bundles.get("QueryBuilder.Functions.labelFunctions"));
        labelParamFunctions = WidgetFactory.createLabel(Bundles.get("QueryBuilder.Functions.labelParamFunctions"));
        labelAliasFunctions = WidgetFactory.createLabel(Bundles.get("QueryBuilder.Functions.labelAliasFunctions"));
        labelSearchFunctions = WidgetFactory.createLabel(Bundles.get("common.search.button"));
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
        setTitle(Bundles.get("QueryBuilder.Functions.title"));
        setResizable(false);
        setIconImage(getAndCreateIconDialog().getImage());
        pack();
        setLocationRelativeTo(null);
        setModal(true);
        setSize(600, 500);
        setVisible(true);
    }

    /**
     * A method for placing components in a panel for placing components.
     * <p>
     * Метод для размещения компонентов в панели для размещения компонентов.
     */
    private void arrangeComponentsInPanelForPlacingComponents() {
        GridBagHelper gridBagHelper = new GridBagHelper().setInsets(10, 5, 10, 5).anchorCenter().setMaxWeightX().fillHorizontally();
        panelPlacingComponents.add(scrollPanelCheckBoxFunctions, gridBagHelper.setXY(0, 0).setWidth(3).setMaxWeightX().get());
        panelPlacingComponents.add(labelSearchFunctions, gridBagHelper.nextRow().setMinWeightX().setWidth(1).get());
        panelPlacingComponents.add(textFieldSearchFunctions, gridBagHelper.nextCol().setWidth(2).setMaxWeightX().get());
        panelPlacingComponents.add(labelFunctions, gridBagHelper.previousCol().nextRow().setWidth(1).setMinWeightX().get());
        panelPlacingComponents.add(comboBoxFunction, gridBagHelper.nextCol().setWidth(2).setMaxWeightX().get());
        panelPlacingComponents.add(labelParamFunctions, gridBagHelper.previousCol().nextRow().setWidth(1).setMinWeightX().get());
        panelPlacingComponents.add(textFieldParamFunctions, gridBagHelper.nextCol().setWidth(2).setMaxWeightX().get());
        panelPlacingComponents.add(labelAliasFunctions, gridBagHelper.previousCol().nextRow().setWidth(1).setMinWeightX().get());
        panelPlacingComponents.add(textFieldAlias, gridBagHelper.nextCol().setWidth(2).setMaxWeightX().get());
        panelPlacingComponents.add(panelButton, gridBagHelper.nextRow().spanX().spanY().setMaxWeightX().get());
    }

    /**
     * A method that implements the functionality of removing functions from a query.
     * <p>
     * Метод реализующий функционал удаления функций из запроса.
     */
    private void removeFunctions() {
        StringBuilder stringBuilderFunctions = new StringBuilder(queryConstructor.getFunctions());
        JCheckBox[] checkBoxesInScrollPane = getCheckBoxesFromPanelArrangeCheckBox();

        for (int i = 0; i < checkBoxesInScrollPane.length; i++) {
            if (checkBoxesInScrollPane[i].isSelected()) {
                if (checkBoxesInScrollPane.length == 1) {
                    stringBuilderFunctions.replace(0, stringBuilderFunctions.length(), "");
                    queryConstructor.replaceFunctions(stringBuilderFunctions.toString());
                    queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                    return;
                } else {
                    if (stringBuilderFunctions.toString().contains(checkBoxesInScrollPane[i].getText())) {
                        stringBuilderFunctions.replace(stringBuilderFunctions.indexOf(checkBoxesInScrollPane[i].getText()),
                                stringBuilderFunctions.indexOf(checkBoxesInScrollPane[i].getText()) + checkBoxesInScrollPane[i].getText().length(),
                                "");

                        queryConstructor.replaceFunctions(stringBuilderFunctions.toString());
                        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                    }
                }
            }
        }

        if(stringBuilderFunctions.toString().charAt(stringBuilderFunctions.length()-1) == ','){
            stringBuilderFunctions.deleteCharAt(stringBuilderFunctions.length()-1);
            queryConstructor.replaceFunctions(stringBuilderFunctions.toString());
            queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
        }


    }

    /**
     * A method that implements the functionality of adding functions to a query.
     * <p>
     * Метод реализующий функционал добавления функций в запрос.
     */
    private void addFunctions() {
        StringBuilder stringBuilderFunctions = new StringBuilder();
        StringBuilder stringBuilderAttributes = new StringBuilder(queryConstructor.getAttribute());

        if(!comboBoxFunction.getSelectedItem().toString().isEmpty()) {
            if (stringBuilderAttributes.toString().equals("*") || stringBuilderAttributes.toString().isEmpty()) {
                if (queryConstructor.getFunctions().isEmpty()) {
                    stringBuilderFunctions.append(comboBoxFunction.getSelectedItem().toString()).append("(").append(textFieldParamFunctions.getText()).append(")");

                    if (!textFieldAlias.getText().isEmpty()) {
                        stringBuilderFunctions.append(" AS \"").append(textFieldAlias.getText()).append("\"");
                    }

                    if (!queryConstructor.getFunctions().contains(stringBuilderFunctions)) {
                        queryConstructor.replaceFunctions(queryConstructor.getFunctions() + stringBuilderFunctions.toString());
                        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                    }
                } else {
                    stringBuilderFunctions.append(comboBoxFunction.getSelectedItem().toString()).append("(").append(textFieldParamFunctions.getText()).append(")");

                    if (!textFieldAlias.getText().isEmpty()) {
                        stringBuilderFunctions.append(" AS \"").append(textFieldAlias.getText()).append("\"");
                    }

                    if (!queryConstructor.getFunctions().contains(stringBuilderFunctions)) {
                        queryConstructor.replaceFunctions(queryConstructor.getFunctions() + "," + stringBuilderFunctions.toString());
                        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                    }
                }
            }

            if (!stringBuilderAttributes.toString().equals("*") & !stringBuilderAttributes.toString().isEmpty() & stringBuilderAttributes.toString().length() > 1) {
                stringBuilderFunctions.append(comboBoxFunction.getSelectedItem().toString()).append("(").append(textFieldParamFunctions.getText()).append(")");

                if (!textFieldAlias.getText().isEmpty()) {
                    stringBuilderFunctions.append(" AS \"").append(textFieldAlias.getText()).append("\"");
                }

                if (!queryConstructor.getFunctions().contains(stringBuilderFunctions)) {
                    if(queryConstructor.getFunctions().isEmpty()) {
                        queryConstructor.replaceFunctions(queryConstructor.getFunctions() + stringBuilderFunctions.toString());
                        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                    }
                    else{
                        queryConstructor.replaceFunctions(queryConstructor.getFunctions() + "," + stringBuilderFunctions.toString());
                        queryBuilderPanel.setTextInPanelOutputTestingQuery(queryConstructor.buildAndGetQuery());
                    }
                }
            }
        }
        else{
            JOptionPane.showMessageDialog(this,Bundles.get("QueryBuilder.Functions.notFunctions"),Bundles.get("QueryBuilder.Functions.titleNotFunctions"),JOptionPane.ERROR_MESSAGE);
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
        panelPlacingCheckBoxInScrollPane = WidgetFactory.createPanel("panelArrangeCheckBoxInScrollPane");
        panelPlacingCheckBoxInScrollPane.setLayout(new BoxLayout(panelPlacingCheckBoxInScrollPane, BoxLayout.Y_AXIS));

        if (!queryConstructor.getFunctions().isEmpty()) {
            String[] functionsNoAlias = queryConstructor.getFunctions().split("(?<=\\),)");

            for (int i = 0; i < functionsNoAlias.length; i++) {
                String[] functionsYesAlias = functionsNoAlias[i].split("(?<=\",)");
                for (int j = 0; j < functionsYesAlias.length; j++) {
                    JCheckBox checkBox = new JCheckBox(functionsYesAlias[j]);
                    checkBox.setToolTipText(Bundles.get("QueryBuilder.Functions.toolTipTextCheckBoxFunctions"));
                    panelPlacingCheckBoxInScrollPane.add(checkBox);
                }
            }
        }

        scrollPanelCheckBoxFunctions.setViewportView(panelPlacingCheckBoxInScrollPane);
    }

    /**
     * Search implementation.
     * <p>
     * Реализация поиска.
     */
    private void eventAddDocumentListenerInTextFields(JTextField textField) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
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
                ArrayList<String> newFunctions = new ArrayList<>();

                for (int i = 0; i < functions.length; i++) {
                    if (functions[i].contains(textFieldSearchFunctions.getText().toUpperCase())) {
                        newFunctions.add(functions[i]);
                    }
                }

                if (newFunctions.isEmpty()) {
                    newFunctions.add("");
                    comboBoxFunction.setEditable(true);
                } else {
                    comboBoxFunction.setEditable(false);
                }

                if (newFunctions.size() == functions.length) {
                    comboBoxFunction.setEditable(true);
                }

                comboBoxFunction.setModel(new DefaultComboBoxModel(newFunctions.toArray()));
            }
        });
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
     * A method for closing a dialog (window) created by this class.
     * <p>
     * Метод для закрытия диалога (окна) созданного этим классом.
     */
    private void closeDialog() {
        setVisible(false);
        dispose();
    }
}
