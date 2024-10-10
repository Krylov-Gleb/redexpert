package org.executequery.gui.querybuilder;

import org.executequery.base.TabView;
import org.executequery.gui.editor.QueryEditor;
import org.executequery.gui.querybuilder.inputPanel.QueryBuilderInputPanel;
import org.executequery.gui.querybuilder.toolBar.QueryBuilderToolBarPanel;

import javax.swing.*;
import java.awt.*;

public class QueryBuilderPanel extends JPanel implements TabView {

    public static final String TITLE = "Query Builder";
    public static final String FRAME_ICON = "icon_table_validation";

    private JPanel mainPanel;
    private QueryBuilderToolBarPanel toolBarPanel;
    private QueryBuilderInputPanel inputElementPanel;

    private QueryEditor queryEditor;
    private JSplitPane splitPane;

    public QueryBuilderPanel(){
        init();
    }

    private void init(){

        mainPanel = new JPanel(new BorderLayout());
        queryEditor = new QueryEditor();
        toolBarPanel = new QueryBuilderToolBarPanel();
        inputElementPanel = new QueryBuilderInputPanel();

        splitPane = new JSplitPane(SwingConstants.VERTICAL,queryEditor,inputElementPanel);

        arrangeComponent();
    }

    private void arrangeComponent(){

        mainPanel.add(toolBarPanel,BorderLayout.NORTH);
        mainPanel.add(splitPane,BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(mainPanel,BorderLayout.CENTER);
    }

    @Override
    public boolean tabViewClosing() {
        return true;
    }

    @Override
    public boolean tabViewSelected() {
        return true;
    }

    @Override
    public boolean tabViewDeselected() {
        return true;
    }
}
