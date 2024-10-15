package org.executequery.gui.querybuilder.inputPanel;

import javax.swing.*;
import java.awt.*;

/**
 * The QueryBuilderInputPanel
 *
 * @author Krylov Gleb
 */

public class QueryBuilderInputPanel extends JPanel {

    // --- Designer ---

    public QueryBuilderInputPanel(){
        init();
    }

    /**
     * Method for initialization
     */
    private void init(){
        arrangeComponent();
    }

    /**
     * A method for placing components
     */
    private void arrangeComponent(){
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

}
