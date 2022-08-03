/*
 * ListSelectionPanel.java
 *
 * Copyright (C) 2002-2017 Takis Diakoumis
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.underworldlabs.swing;

import org.executequery.localization.Bundles;
import org.underworldlabs.swing.actions.ActionUtilities;
import org.underworldlabs.swing.util.IconUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * List selection panel base.
 *
 * @author Takis Diakoumis
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class ListSelectionPanel extends ActionPanel
        implements ListSelection {

    /**
     * the available object list
     */
    private JList availableList;

    /**
     * the selected object list
     */
    private JList selectedList;

    /**
     * the selections made collection
     */
    private Vector selections;

    /**
     * the available objects collection
     */
    private Vector available;

    /**
     * label above the available object list
     */
    private JLabel availableLabel;

    /**
     * label above the selected object list
     */
    private JLabel selectedLabel;

    private static final int DEFAULT_ROW_HEIGHT = 20;

    private List<ListSelectionPanelListener> listeners;

    public ListSelectionPanel() {
        this(null);
    }

    public ListSelectionPanel(Vector v) {
        this(Bundles.get(ListSelectionPanel.class, "AvailableColumns"),
                Bundles.get(ListSelectionPanel.class, "SelectedColumns"), v);
    }

    public ListSelectionPanel(String availLabel, String selectLabel) {
        this(availLabel, selectLabel, null);
    }

    public ListSelectionPanel(String availLabel, String selectLabel, Vector v) {
        super(new GridBagLayout());
        try {
            listeners = new ArrayList<>();
            init();
            selections = new Vector();
            createAvailableList(v);
            setLabelText(availLabel, selectLabel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() throws Exception {
        // create the labels
        availableLabel = new JLabel();
        selectedLabel = new JLabel();

        // initialise the buttons
        JButton selectOneButton = ActionUtilities.createButton(
                this,
                "selectOneAction",
//                    IconUtilities.loadDefaultIconResource("SelectOne16.png", true),
                IconUtilities.loadDefaultIconResource("Forward16.png", true),
                bundleString("selectOneAction"));

        JButton selectAllButton = ActionUtilities.createButton(
                this,
                "selectAllAction",
                IconUtilities.loadDefaultIconResource("SelectAll16.png", true),
                bundleString("selectAllAction"));

        JButton removeOneButton = ActionUtilities.createButton(
                this,
                "removeOneAction",
//                    IconUtilities.loadDefaultIconResource("RemoveOne16.png", true),
                IconUtilities.loadDefaultIconResource("Previous16.png", true),
                bundleString("removeOneAction"));

        JButton removeAllButton = ActionUtilities.createButton(
                this,
                "removeAllAction",
                IconUtilities.loadDefaultIconResource("RemoveAll16.png", true),
                bundleString("removeAllAction"));

        // reset the button insets
        Insets buttonInsets = UIManager.getInsets("Button.margin");
        if (buttonInsets != null) {
            selectOneButton.setMargin(buttonInsets);
            selectAllButton.setMargin(buttonInsets);
            removeOneButton.setMargin(buttonInsets);
            removeAllButton.setMargin(buttonInsets);
        }

        JButton moveUpButton = ActionUtilities.createButton(
                this,
                "Up16.png",
                bundleString("moveSelectionUp"),
                "moveSelectionUp");

        JButton moveDownButton = ActionUtilities.createButton(
                this,
                "Down16.png",
                bundleString("moveSelectionDown"),
                "moveSelectionDown");

        // initialise the lists
        availableList = new JList();
        selectedList = new JList();

        availableList.setFixedCellHeight(DEFAULT_ROW_HEIGHT);
        selectedList.setFixedCellHeight(DEFAULT_ROW_HEIGHT);

        // create the list scroll panes
        JScrollPane availableScrollPane = new JScrollPane(availableList);
        JScrollPane selectedScrollPane = new JScrollPane(selectedList);

        Dimension listDim = new Dimension(180, 185);
        availableScrollPane.setPreferredSize(listDim);
        selectedScrollPane.setPreferredSize(listDim);

        GridBagConstraints gbc = new GridBagConstraints();

        // first column - available list
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(availableLabel, gbc);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridy++;
        gbc.insets.top = 2;
        add(availableScrollPane, gbc);

        // second column - selection buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.insets.top = 10;
        gbc.insets.bottom = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        buttonPanel.add(selectOneButton, gbc);
        gbc.gridy++;
        gbc.insets.top = 0;
        buttonPanel.add(selectAllButton, gbc);
        gbc.gridy++;
        buttonPanel.add(removeOneButton, gbc);
        gbc.gridy++;
        buttonPanel.add(removeAllButton, gbc);

        gbc.gridy = 0;
        gbc.gridx = 1;
        gbc.insets.left = 5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        add(buttonPanel, gbc);

        // third column - selected list
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(selectedLabel, gbc);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridy++;
        gbc.insets.top = 2;
        gbc.insets.bottom = 0;
        add(selectedScrollPane, gbc);

        // fourth column - move buttons
        JPanel buttonMovePanel = new JPanel(new GridBagLayout());
        gbc.insets.top = 10;
        gbc.insets.left = 0;
        gbc.insets.bottom = 5;
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        buttonMovePanel.add(moveUpButton, gbc);
        gbc.gridy++;
        gbc.insets.top = 0;
        buttonMovePanel.add(new JLabel(bundleString("Move")), gbc);
        gbc.gridy++;
        buttonMovePanel.add(moveDownButton, gbc);

        gbc.gridy = 0;
        gbc.gridx = 3;
        gbc.insets.left = 5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        add(buttonMovePanel, gbc);

        ListMouseSelectionListener mouseSelectionListener = new ListMouseSelectionListener();
        availableList.addMouseListener(mouseSelectionListener);
        selectedList.addMouseListener(mouseSelectionListener);
    }

    public void setLabelText(String avail, String select) {
        availableLabel.setText(avail);
        selectedLabel.setText(select);
    }

    public void clear() {
        if (available != null) {
            available.clear();
            availableList.setListData(available);
        }
        if (selections != null) {
            selections.clear();
            selectedList.setListData(selections);
        }
        fireChange(ListSelectionPanelEvent.CLEAR);
    }

    public void createAvailableList(List values) {
        createAvailableList(values.toArray(new Object[values.size()]));
    }

    public void createAvailableList(Object[] values) {
        available = new Vector(values.length);
        for (int i = 0; i < values.length; i++) {
            available.add(values[i]);
        }

        availableList.setListData(available);
        selections.clear();
        selectedList.setListData(selections);
        fireChange(ListSelectionPanelEvent.ADD);
    }

    public void createAvailableList(Vector v) {
        if (v == null) {
            return;
        }

        available = v;
        availableList.setListData(available);
        selections.clear();
        selectedList.setListData(selections);
        fireChange(ListSelectionPanelEvent.ADD);
    }

    public void addAvailableItem(Object obj) {
        if (available == null)
            available = new Vector();
        available.add(obj);
        availableList.setListData(available);
        selections.clear();
        selectedList.setListData(selections);
        fireChange(ListSelectionPanelEvent.ADD);
    }

    public void removeAllAction() {
        if (selections == null || selections.size() == 0) {
            return;
        }
        for (int i = 0, n = selections.size(); i < n; i++) {
            available.add(selections.elementAt(i));
        }

        availableList.setListData(available);
        selections.clear();
        selectedList.setListData(selections);
        fireChange(ListSelectionPanelEvent.DESELECT);
    }

    public void removeOneAction() {
        if (selectedList.isSelectionEmpty()) {
            return;
        }

        int index = selectedList.getSelectedIndex();
        List selectedObjects = selectedList.getSelectedValuesList();
        for (Object selection : selectedObjects) {
            available.add(selection);
            selections.remove(selection);
        }

        selectedList.setListData(selections);
        availableList.setListData(available);
        selectedList.setSelectedIndex(index);
        fireChange(ListSelectionPanelEvent.DESELECT);
    }

    public void selectAllAction() {
        if (available == null) {
            return;
        }
        for (int i = 0, n = available.size(); i < n; i++) {
            selections.add(available.elementAt(i));
        }
        selectedList.setListData(selections);
        available.clear();
        availableList.setListData(available);
        fireChange(ListSelectionPanelEvent.SELECT);
    }

    public void selectOneAction() {
        if (availableList.isSelectionEmpty()) {
            return;
        }

        int index = availableList.getSelectedIndex();
        List selectedObjects = availableList.getSelectedValuesList();
        for (Object selection : selectedObjects) {
            selections.add(selection);
            available.remove(selection);
        }

        availableList.setListData(available);
        selectedList.setListData(selections);
        availableList.setSelectedIndex(index);
        fireChange(ListSelectionPanelEvent.SELECT);
    }

    public void selectOneAction(int indexAvailable) {

        Object selection = available.get(indexAvailable);
        selections.add(selection);
        available.remove(selection);

        availableList.setListData(available);
        selectedList.setListData(selections);
        fireChange(ListSelectionPanelEvent.SELECT);
    }

    public void selectOneStringAction(String object) {
        if (available.size() <= 0 || !(available.get(0) instanceof String))
            return;
        for (int i = 0; i < getAvailableValues().size(); i++) {
            if (getAvailableValues().get(i).toString().contentEquals(object)) {
                selectOneAction(i);
                break;
            }
        }
    }

    public void selectOneObjectAction(Object object) {

        for (int i = 0; i < getAvailableValues().size(); i++) {
            if (getAvailableValues().get(i).equals(object)) {
                selectOneAction(i);
                break;
            }
        }
    }

    public Vector getSelectedValues() {
        return selections;
    }

    public Vector getAvailableValues() {
        return available;
    }

    public boolean hasSelections() {
        return selections.size() > 0;
    }

    public void moveSelectionDown() {
        if (selectedList.isSelectionEmpty() ||
                selectedList.getSelectedIndex() == selections.size() - 1) {
            return;
        }

        int index = selectedList.getSelectedIndex();
        Object move = selectedList.getSelectedValue();
        selections.removeElementAt(index);
        selections.add(index + 1, move);
        selectedList.setListData(selections);
        selectedList.setSelectedIndex(index + 1);
        fireChange(ListSelectionPanelEvent.MOVE);
    }

    public void moveSelectionUp() {
        if (selectedList.isSelectionEmpty() ||
                selectedList.getSelectedIndex() == 0) {
            return;
        }

        int index = selectedList.getSelectedIndex();
        Object move = selectedList.getSelectedValue();
        selections.removeElementAt(index);
        selections.add(index - 1, move);
        selectedList.setListData(selections);
        selectedList.setSelectedIndex(index - 1);
        fireChange(ListSelectionPanelEvent.MOVE);
    }

    public void addListSelectionPanelListener(ListSelectionPanelListener listener) {
        listeners.add(listener);
    }

    public void removeListSelectionPanelListener(ListSelectionPanelListener listener) {
        listeners.remove(listener);
    }

    private void fireChange(int type) {
        ListSelectionPanelEvent event = new ListSelectionPanelEvent(this, type);
        for (ListSelectionPanelListener listener : listeners)
            listener.changed(event);
    }


    class ListMouseSelectionListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {

            if (e.getClickCount() == 2) {

                Object source = e.getSource();
                if (source == availableList) {

                    selectOneAction();

                } else if (source == selectedList) {

                    removeOneAction();
                }

            }

        }

    }
}

