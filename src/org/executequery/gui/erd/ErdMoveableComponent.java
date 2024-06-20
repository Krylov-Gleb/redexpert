/*
 * ErdMoveableComponent.java
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

package org.executequery.gui.erd;

import org.executequery.components.OutlineDragPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author Takis Diakoumis
 */
public abstract class ErdMoveableComponent extends JComponent {

    /**
     * The controller for the ERD viewer
     */
    protected ErdViewerPanel parent;

    // ----------------------------
    // --- For mouse selections ---
    // ----------------------------

    /**
     * The initial x position
     */
    protected double xDifference;
    /**
     * The initial x position
     */
    protected double yDifference;
    /**
     * Whether a drag is in progress
     */
    protected boolean dragging;
    /**
     * The outline dragging panel
     */
    protected OutlineDragPanel outlinePanel;
    /**
     * Whether this table has focus
     */
    protected boolean selected;

    // ----------------------------

    /**
     * The current magnification
     */
    protected static double scale;
    /**
     * The table's in-focus border
     */
    protected static Border focusBorder;
    /**
     * The table's focus border stroke
     */
    protected static BasicStroke focusBorderStroke;
    protected Color tableBackground;

    public ErdMoveableComponent(ErdViewerPanel parent) {
        this.parent = parent;
        scale = parent.getScaleIndex();
        focusBorder = BorderFactory.createLineBorder(Color.BLUE, 2);
        focusBorderStroke = new BasicStroke(2.0f);
    }

    /**
     * <p>Sets this component as selected and having
     * the current focus.
     *
     * @param <code>true</code> to select |
     *                          <code>false</code> otherwise
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * <p>Returns whether this table is currently selected.
     *
     * @return whether this table is selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * <p>Sets the current view scale to the specified value.
     *
     * @param the current view scale
     */
    public void setScale(double scale) {
        ErdMoveableComponent.scale = scale;
    }

    /**
     * <p>Sends this component to the front over all others.
     */
    public void toFront() {
        if (getParent() instanceof JLayeredPane)
            ((JLayeredPane) getParent()).moveToFront(this);
    }

    /**
     * <p>Indicates that this component has been deselected.
     *
     * @param the event causing the deselection
     */
    public void deselected(MouseEvent e) {
        dragging = false;

        finishedDragging();

    }

    public void finishedDragging() {
        if (outlinePanel != null) {
            setBounds(outlinePanel.getBounds());
            parent.removeOutlinePanel(outlinePanel);
            parent.resizeCanvas();
            outlinePanel = null;
        }
    }

    /**
     * <p>Indicates that this component is being dragged.
     *
     * @param the event causing the drag
     */
    public void dragging(MouseEvent e) {

        if (e.isControlDown())
            return;

        if (dragging) {
            outlinePanel.setLocation((int) ((e.getX() / scale) - xDifference + getX()),
                    (int) ((e.getY() / scale) - yDifference + getY()));
            parent.repaintLayeredPane();
        }

    }

    public void changeSize(MouseEvent e, int location) {
        if (dragging) {
            int x = (int) (e.getX() / scale);
            int y = (int) (e.getY() / scale);
            int xDiff = (int) (xDifference - x);
            int yDiff = (int) (yDifference - y);
            int width = getBounds().width;
            int height = getBounds().height;
            switch (location) {
                case GridBagConstraints.NORTHWEST:
                    width = xDiff + width;
                    height = yDiff + height;
                    outlinePanel.setBounds(getBounds().x - xDiff, getBounds().y - yDiff, width, height);
                    break;
                case GridBagConstraints.NORTHEAST:
                    width = width - xDiff;
                    height = yDiff + height;
                    outlinePanel.setBounds(getBounds().x, getBounds().y - yDiff, width, height);
                    break;
                case GridBagConstraints.SOUTHEAST:
                    width = width - xDiff;
                    height = height - yDiff;
                    outlinePanel.setBounds(getBounds().x, getBounds().y, width, height);
                    break;
                case GridBagConstraints.SOUTHWEST:
                    width = xDiff + width;
                    height = height - yDiff;
                    outlinePanel.setBounds(getBounds().x - xDiff, getBounds().y, width, height);
                    break;
                case GridBagConstraints.NORTH:
                    height = yDiff + height;
                    outlinePanel.setBounds(getBounds().x, getBounds().y - yDiff, width, height);
                    break;
                case GridBagConstraints.EAST:
                    width = width - xDiff;
                    outlinePanel.setBounds(getBounds().x, getBounds().y, width, height);
                    break;
                case GridBagConstraints.SOUTH:
                    height = height - yDiff;
                    outlinePanel.setBounds(getBounds().x, getBounds().y, width, height);
                    break;
                case GridBagConstraints.WEST:
                    width = xDiff + width;
                    outlinePanel.setBounds(getBounds().x - xDiff, getBounds().y, width, height);
                    break;
            }
            parent.repaintLayeredPane();
        }
    }

    /**
     * <p>Indicates that this component has been selected.
     *
     * @param the event causing the selection
     */
    public void selected(MouseEvent e) {

        //calculateDragging(e);
    }

    public void calculateDragging(MouseEvent e) {
        if (!e.isControlDown()) {
            toFront();
            outlinePanel = new OutlineDragPanel(getBounds(), focusBorder);
            parent.addOutlinePanel(outlinePanel);
        }
        xDifference = e.getX() / scale;
        yDifference = e.getY() / scale;
        dragging = true;
    }

    /**
     * <p>Indicates that this component has been double-clicked.
     *
     * @param the event causing the double-click
     */
    public abstract void doubleClicked(MouseEvent e);

    public Color getTableBackground() {
        return tableBackground;
    }

    public void setTableBackground(Color tableBackground) {
        this.tableBackground = tableBackground;
    }

    public int checkChangeSizeCoords(int x, int y) {
        if (boundsFromPoint(getBounds().x, getBounds().y).contains(x, y)) {
            return GridBagConstraints.NORTHWEST;
        } else if (boundsFromPoint(getBounds().x + getBounds().width, getBounds().y).contains(x, y)) {
            return GridBagConstraints.NORTHEAST;
        } else if (boundsFromPoint(getBounds().x + getBounds().width, getBounds().y + getBounds().height).contains(x, y)) {
            return GridBagConstraints.SOUTHEAST;
        } else if (boundsFromPoint(getBounds().x, getBounds().y + getBounds().height).contains(x, y)) {
            return GridBagConstraints.SOUTHWEST;
        } else if (boundsFromLine(getBounds().x, getBounds().y, getBounds().x + getBounds().width, getBounds().y).contains(x, y)) {
            return GridBagConstraints.NORTH;
        } else if (boundsFromLine(getBounds().x + getBounds().width, getBounds().y, getBounds().x + getBounds().width, getBounds().y + getBounds().height).contains(x, y)) {
            return GridBagConstraints.EAST;
        } else if (boundsFromLine(getBounds().x, getBounds().y + getBounds().height, getBounds().x + getBounds().width, getBounds().y + getBounds().height).contains(x, y)) {
            return GridBagConstraints.SOUTH;
        } else if (boundsFromLine(getBounds().x, getBounds().y, getBounds().x, getBounds().y + getBounds().height).contains(x, y)) {
            return GridBagConstraints.WEST;
        } else return -1;
    }

    protected Rectangle boundsFromPoint(int x, int y) {
        return new Rectangle(x - 10, y - 10, 20, 20);
    }

    protected Rectangle boundsFromLine(int x, int y, int x2, int y2) {
        return new Rectangle(x - 10, y - 10, x2 - x + 20, y2 - y + 20);
    }

}















