package org.executequery.gui.querybuilder.toolBar;

import org.underworldlabs.swing.layouts.GridBagHelper;

import javax.swing.*;
import java.awt.*;

public class QueryBuilderToolBarPanel extends JPanel {

    private JButton selectButton;
    private JButton joinButton;
    private JButton columnsButton;
    private JButton sortingButton;
    private JButton miscellaneousButton;

    public QueryBuilderToolBarPanel(){
        init();
    }

    private void init(){

        selectButton = new JButton("Select");
        selectButton.addActionListener(event -> {

        });

        joinButton = new JButton("Join");
        joinButton.addActionListener(event -> {

        });

        columnsButton = new JButton("Columns");
        columnsButton.addActionListener(event ->{

        });

        sortingButton = new JButton("Sorting");
        sortingButton.addActionListener(event -> {

        });

        miscellaneousButton = new JButton("Miscellaneous");
        miscellaneousButton.addActionListener(event -> {

        });

        arrangeComponent();
    }

    private void arrangeComponent(){

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(selectButton,new GridBagHelper().setX(0).setY(0).anchorWest().fillHorizontally().setInsets(5,5,5,5).get());
        add(joinButton,new GridBagHelper().setX(10).setY(0).anchorWest().fillHorizontally().setInsets(5,5,5,5).get());
        add(columnsButton,new GridBagHelper().setX(20).setY(0).anchorWest().fillHorizontally().setInsets(5,5,5,5).get());
        add(sortingButton,new GridBagHelper().setX(30).setY(0).anchorWest().fillHorizontally().setInsets(5,5,5,5).get());
        add(miscellaneousButton,new GridBagHelper().setX(40).setY(0).anchorWest().fillHorizontally().setInsets(5,5,5,5).get());

    }

}
