package org.executequery.gui.querybuilder;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class QBMovePanel extends JPanel {

    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private volatile int locationX = 0;
    private volatile int locationY = 0;

    public QBMovePanel(JComponent table) {
        setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.GRAY), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(table);
        eventAddMouseListener();
        eventAddMouseMotionListener();
    }

    private void eventAddMouseMotionListener() {
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int setX = e.getXOnScreen() - screenX;
                int setY = e.getYOnScreen() - screenY;

                if (locationX + setX >= 0 & locationY + setY >= 0) {
                    setLocation(locationX + setX, locationY + setY);
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }

        });
    }

    private void eventAddMouseListener() {
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                screenX = e.getXOnScreen();
                screenY = e.getYOnScreen();

                locationX = getX();
                locationY = getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

        });
    }
}
