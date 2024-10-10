package org.executequery.gui.querybuilder.inputPanel;

import javax.swing.*;
import java.awt.*;

public class QueryBuilderInputPanel extends JPanel {

    public QueryBuilderInputPanel(){
        init();
    }

    private void init(){
        arrangeComponent();
    }

    private void arrangeComponent(){
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

}
