package org.executequery.gui.querybuilder.buttonPanel;

import org.executequery.gui.WidgetFactory;
import org.executequery.gui.querybuilder.QueryBuilderPanel;
import org.executequery.gui.querybuilder.WorkQuryEditor.CreateStringQuery;
import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;

/**
 * This class is responsible for creating a window for adding additions to the request (First, Skip, Distinct).
 *
 * @author Krylov Gleb
 */
public class AddAdditionalSelectSettingsPanel extends JFrame {

    // --- Elements accepted using the constructor ---
    private CreateStringQuery createStringQuery;
    private QueryBuilderPanel queryBuilderPanel;

    // --- GUI Components ---

    private JComboBox<String> distinctComboBox;
    private JPanel mainPanel;
    private JLabel firstLabel;
    private JLabel skipLabel;
    private JLabel distinctLabel;
    private JTextField firstTextField;
    private JTextField skipTextField;
    private JButton createButton;

    /**
     * A window is created for setting additional parameters in the request.
     * The components of the graphical interface are initialized.
     *
     * @param createStringQuery
     */
    public AddAdditionalSelectSettingsPanel(CreateStringQuery createStringQuery, QueryBuilderPanel queryBuilderPanel) {
        this.createStringQuery = createStringQuery;
        this.queryBuilderPanel = queryBuilderPanel;
        init();
    }

    /**
     * The components of the graphical interface are initialized and the functionality of the buttons is set.
     */
    private void init() {
        mainPanel = WidgetFactory.createPanel("Additional Select Settings Panel");

        firstLabel = WidgetFactory.createLabel("Укажите число (FIRST)");
        firstTextField = WidgetFactory.createTextField("Input First Select");

        skipLabel = WidgetFactory.createLabel("Укажите число (SKIP)");
        skipTextField = WidgetFactory.createTextField("Input Skip Select");

        distinctLabel = WidgetFactory.createLabel("Мы используем DISTINCT?");

        distinctComboBox = WidgetFactory.createComboBox("Distinct ComboBox", new String[]{"DISTINCT", "ALL"});
        distinctComboBox.setSelectedIndex(1);
        distinctComboBox.setPreferredSize(new Dimension(100, 30));

        createButton = WidgetFactory.createButton("Create Select Setting Panel", "Создать", event -> {

            String distinct;
            String skip = "";
            String first = "";

            distinct = distinctComboBox.getSelectedItem().toString();

            if (!skipTextField.getText().isEmpty()) {
                try {
                    Integer.parseInt(skipTextField.getText());
                    skip = skipTextField.getText();
                } catch (NumberFormatException numberFormatException) {

                }

            } else {
                skip = "";
            }

            if (!firstTextField.getText().isEmpty()) {
                try {
                    Integer.parseInt(firstTextField.getText());
                    first = firstTextField.getText();
                } catch (NumberFormatException numberFormatException) {

                }

            } else {
                first = "";
            }

            createStringQuery.addFirst(first);
            createStringQuery.addSkip(skip);
            createStringQuery.setDistinct(distinct);

            queryBuilderPanel.setTextInQueryEditorTextPanel(createStringQuery.getQuery());
            setVisible(false);
            dispose();
        });

        arrangeComponents();
    }

    /**
     * We place the GUI components in the window and set the parameters for the window.
     */
    private void arrangeComponents() {
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        mainPanel.add(firstLabel, new GridBagHelper().anchorCenter().setX(0).setY(0).setInsets(5, 5, 5, 5).get());
        mainPanel.add(firstTextField, new GridBagHelper().anchorCenter().setX(0).setY(10).setInsets(5, 5, 5, 5).get());
        mainPanel.add(skipLabel, new GridBagHelper().anchorCenter().setX(0).setY(20).setInsets(5, 5, 5, 5).get());
        mainPanel.add(skipTextField, new GridBagHelper().anchorCenter().setX(0).setY(30).setInsets(5, 5, 5, 5).get());
        mainPanel.add(distinctLabel, new GridBagHelper().anchorCenter().setX(0).setY(40).setInsets(5, 5, 5, 5).get());
        mainPanel.add(distinctComboBox, new GridBagHelper().anchorCenter().setX(0).setY(50).setInsets(5, 5, 5, 5).get());
        mainPanel.add(createButton, new GridBagHelper().anchorCenter().setX(0).setY(60).setInsets(5, 10, 5, 5).get());

        setLayout(new BorderLayout());
        setTitle("Дополнительные настройки");
        setIconImage(new ImageIcon("red_expert.png").getImage());
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);
    }


}
