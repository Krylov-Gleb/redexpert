package org.executequery.gui.querybuilder.inputPanel;

import javax.swing.*;
import java.awt.*;

/**
 * A class for creating a panel for displaying graphical components to the user. (Tables, connections, etc.).
 *
 * @author Krylov Gleb
 */
public class QueryBuilderInputPanel extends JPanel {

    /**
     * Creating a panel and initializing the fields.
     */
    public QueryBuilderInputPanel() {
        init();
    }

    /**
     * Method for initialization
     */
    private void init() {
        arrangeComponent();
    }

    /**
     * A method for placing components
     */
    private void arrangeComponent() {
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

}
