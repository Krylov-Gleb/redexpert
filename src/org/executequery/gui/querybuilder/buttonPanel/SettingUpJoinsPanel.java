package org.executequery.gui.querybuilder.buttonPanel;

import org.executequery.gui.WidgetFactory;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.executequery.gui.querybuilder.WorkQuryEditor.CreateStringQuery;
import org.executequery.gui.querybuilder.toolBar.QueryBuilderToolBarPanel;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

/**
 * A class for creating a window for installing table connections in a query.
 *
 * @author Krylov Gleb
 */
public class SettingUpJoinsPanel extends JFrame {

    // --- Elements accepted using the constructor ---
    private QueryBuilderPanel queryBuilderPanel;
    private CreateStringQuery createStringQuery;
    private QueryBuilderToolBarPanel queryBuilderToolBarPanel;

    // --- GUI Components ---
    private JPanel mainPanel;
    private JComboBox<String> leftTablesComboBox;
    private JComboBox<String> leftTablesAttribute;
    private JComboBox<String> rightTablesComboBox;
    private JComboBox<String> rightTablesAttribute;
    private JComboBox<String> joinsComboBox;
    private JButton createJoin;

    // --- Other field ---
    private ArrayList<JTable> arrayTable;

    /**
     * A new table join window is created and GUI elements are initialized.
     *
     * @param queryBuilderToolBarPanel
     * @param queryBuilderPanel
     * @param createStringQuery
     */
    public SettingUpJoinsPanel(QueryBuilderToolBarPanel queryBuilderToolBarPanel, QueryBuilderPanel queryBuilderPanel, CreateStringQuery createStringQuery) {
        this.queryBuilderToolBarPanel = queryBuilderToolBarPanel;
        this.queryBuilderPanel = queryBuilderPanel;
        this.createStringQuery = createStringQuery;
        init();
    }

    /**
     * User interface elements (windows) are initialized and functionality is added to the buttons.
     */
    private void init() {
        mainPanel = WidgetFactory.createPanel("Main Panel");
        arrayTable = queryBuilderPanel.getListTableInInputPanel();
        String[] arrayJoin = new String[]{"Inner Join", "Left Join", "Right Join", "Full Outer Join", "Natural Join", "Cross Join", "Join не выбран"};

        leftTablesAttribute = WidgetFactory.createComboBox("Left Tables Attribute");
        leftTablesAttribute.setPreferredSize(new Dimension(150, 30));

        rightTablesAttribute = WidgetFactory.createComboBox("Right Tables Attribute");
        rightTablesAttribute.setPreferredSize(new Dimension(150, 30));

        leftTablesComboBox = WidgetFactory.createComboBox("Left Tables Combo Box", queryBuilderToolBarPanel.getQueryTable());
        leftTablesComboBox.setPreferredSize(new Dimension(150, 30));
        leftTablesComboBoxAddItems();
        leftTablesComboBox.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                leftTablesAttribute.removeAllItems();
                leftTablesComboBoxAddItems();
            }
        });

        rightTablesComboBox = WidgetFactory.createComboBox("Right Tables Combo Box", queryBuilderToolBarPanel.getQueryTable());
        rightTablesComboBox.setPreferredSize(new Dimension(150, 30));
        rightTablesComboBoxAddItems();
        rightTablesComboBox.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                rightTablesAttribute.removeAllItems();
                rightTablesComboBoxAddItems();
            }
        });

        joinsComboBox = WidgetFactory.createComboBox("Join Combo Box", arrayJoin);
        joinsComboBox.setSelectedIndex(arrayJoin.length - 1);
        joinsComboBox.setPreferredSize(new Dimension(150, 30));

        createJoin = WidgetFactory.createButton("Create Join", "Create Join", event -> {
            createStringQuery.addJoins(leftTablesComboBox.getSelectedItem().toString(), rightTablesComboBox.getSelectedItem().toString(), joinsComboBox.getSelectedItem().toString(), leftTablesAttribute.getSelectedItem().toString(), rightTablesAttribute.getSelectedItem().toString());
            queryBuilderPanel.setTextInQueryEditorTextPanel(createStringQuery.getQuery());
        });

        arrangeComponents();
    }

    /**
     * Components are placed in a window and parameters are set for the window.
     */
    private void arrangeComponents() {
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        mainPanel.add(leftTablesComboBox, new GridBagHelper().setX(0).setY(0).anchorCenter().setInsets(5, 5, 5, 5).get());
        mainPanel.add(rightTablesComboBox, new GridBagHelper().setX(20).setY(0).anchorCenter().setInsets(5, 5, 5, 5).get());
        mainPanel.add(joinsComboBox, new GridBagHelper().setX(10).setY(0).anchorCenter().setInsets(5, 5, 5, 5).get());
        mainPanel.add(leftTablesAttribute, new GridBagHelper().setX(0).setY(10).anchorCenter().setInsets(5, 5, 5, 5).get());
        mainPanel.add(rightTablesAttribute, new GridBagHelper().setX(20).setY(10).anchorCenter().setInsets(5, 5, 5, 5).get());
        mainPanel.add(createJoin, new GridBagHelper().setX(10).setY(20).anchorCenter().setInsets(5, 5, 5, 5).get());

        setLayout(new BorderLayout());
        setTitle("Добавить соединение (Join)");
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setIconImage(new ImageIcon("red_expert.png").getImage());
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * The functionality of setting values when changing an element in the ComboBox (Left).
     */
    private void leftTablesComboBoxAddItems() {
        for (int i = 0; i < arrayTable.size(); i++) {
            if (arrayTable.get(i).getColumnName(0).equals(leftTablesComboBox.getSelectedItem().toString())) {
                for (int j = 0; j < arrayTable.get(i).getRowCount(); j++) {
                    leftTablesAttribute.addItem(arrayTable.get(i).getValueAt(j, 0).toString());
                }
            }
        }
    }

    /**
     * The functionality of setting values when changing an element in the ComboBox (Right).
     */
    private void rightTablesComboBoxAddItems() {
        for (int i = 0; i < arrayTable.size(); i++) {
            if (arrayTable.get(i).getColumnName(0).equals(rightTablesComboBox.getSelectedItem().toString())) {
                for (int j = 0; j < arrayTable.get(i).getRowCount(); j++) {
                    rightTablesAttribute.addItem(arrayTable.get(i).getValueAt(j, 0).toString());
                }
            }
        }
    }

}
