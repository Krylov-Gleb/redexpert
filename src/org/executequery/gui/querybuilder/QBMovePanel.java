package org.executequery.gui.querybuilder;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * This class creates a movable panel (JPanel).
 * <p>
 * Этот класс создаёт перемещаемую панель (JPanel).
 */
public class QBMovePanel extends JPanel {

    // --- Coordinates ---
    // --- Координаты ---

    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private volatile int locationX = 0;
    private volatile int locationY = 0;

    /**
     * A movable panel (JPanel) is created.
     * <p>
     * Создаётся перемещаемая панель (JPanel).
     */
    public QBMovePanel(JComponent table) {
        setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.GRAY), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(table);
        eventAddMouseListener();
        eventAddMouseMotionListener();
    }

    /**
     * A method that adds listening to mouse movement.
     * <p>
     * Метод, который добавляет возможность прослушивания движений мыши.
     */
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

    /**
     * A method for adding a mouse event listener.
     * <p>
     * Метод для добавления прослушивания событий мыши.
     */
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
