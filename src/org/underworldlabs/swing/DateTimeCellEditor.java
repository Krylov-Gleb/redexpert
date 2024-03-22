package org.underworldlabs.swing;

import com.github.lgooddatepicker.zinternaltools.InternalUtilities;
import org.executequery.gui.resultset.RecordDataItem;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.EventObject;

public class DateTimeCellEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

    private final EQDateTimePicker dateTimePicker;
    private JTable table;
    private int col;

    private final int minimumRowHeightInPixels;
    private final int minimumColWidthInPixels;
    private int oldRowHeightInPixels;
    private int oldColWidthInPixels;

    public DateTimeCellEditor() {

        dateTimePicker = new EQDateTimePicker();
        dateTimePicker.getDatePicker().getSettings().setGapBeforeButtonPixels(0);

        minimumRowHeightInPixels = dateTimePicker.getPreferredSize().height;
        minimumColWidthInPixels = dateTimePicker.getPreferredSize().width;
    }

    @Override
    public Object getCellEditorValue() {

        if (dateTimePicker.getStringValue().isEmpty())
            return null;

        try {
            return Timestamp.valueOf(dateTimePicker.getStringValue());

        } catch (IllegalArgumentException e) {
            return dateTimePicker.getStringValue();
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;
        this.col = column;

        setCellEditorValue(value);
        adjustTableRowHeight();
        adjustTableColWidth();

        return dateTimePicker;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.table = table;
        this.col = column;

        Color color = isSelected ? table.getSelectionBackground() : table.getBackground();
        dateTimePicker.setBackground(color);
        dateTimePicker.getTimePicker().setBackground(color);
        dateTimePicker.getDatePicker().setBackground(color);
        dateTimePicker.getDatePicker().getComponentDateTextField().setBackground(color);

        setCellEditorValue(value);
        adjustTableRowHeight();
        adjustTableColWidth();

        return dateTimePicker;
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        return !(e instanceof MouseEvent) || ((MouseEvent) e).getClickCount() >= 1;
    }

    private void setCellEditorValue(Object value) {

        if (value == null) {
            dateTimePicker.setDateTime(null);
            return;
        }

        if (value instanceof LocalDateTime) {
            dateTimePicker.setDateTime((LocalDateTime) value);
            return;
        }

        if (value instanceof RecordDataItem) {
            RecordDataItem item = ((RecordDataItem) value);

            if (item.getDisplayValue() instanceof LocalDateTime) {
                dateTimePicker.setDateTime((LocalDateTime) item.getDisplayValue());

            } else if (item.getDisplayValue() instanceof Timestamp) {
                dateTimePicker.setDateTime(((Timestamp) item.getDisplayValue()).toLocalDateTime());

            } else if (item.getDisplayValue() instanceof String) {

                String dateTime = (String) item.getDisplayValue();
                String date = dateTime.substring(0, dateTime.indexOf(' '));

                int indexTimezone = dateTime.indexOf('+');
                if (indexTimezone < 0)
                    indexTimezone = dateTime.indexOf('-');

                String time = dateTime.substring(dateTime.indexOf(' ') + 1, indexTimezone);
                LocalDateTime localDateTime = Timestamp.valueOf(date + " " + time).toLocalDateTime();

                dateTimePicker.setDateTime(localDateTime);

            } else
                dateTimePicker.setDateTime(null);

        } else {
            String shorterText = InternalUtilities.safeSubstring(value.toString(), 0, 100);
            dateTimePicker.getDatePicker().setText(shorterText);
        }
    }

    private void adjustTableRowHeight() {
        oldRowHeightInPixels = table.getRowHeight();

        if (table.getRowHeight() < minimumRowHeightInPixels)
            table.setRowHeight(minimumRowHeightInPixels);
    }

    private void adjustTableColWidth() {
        oldColWidthInPixels = table.getColumnModel().getColumn(col).getWidth();

        if (table.getColumnModel().getColumn(col).getWidth() < minimumColWidthInPixels) {
            table.getColumnModel().getColumn(col).setWidth(minimumColWidthInPixels);
            table.getColumnModel().getColumn(col).setPreferredWidth(minimumColWidthInPixels);
        }
    }

    private void restoreTableRowHeigh() {
        table.setRowHeight(oldRowHeightInPixels);
    }

    private void restoreTableColWidth() {
        table.getColumnModel().getColumn(col).setWidth(oldColWidthInPixels);
        table.getColumnModel().getColumn(col).setPreferredWidth(oldColWidthInPixels);
    }

    public void restoreCellSize() {
        restoreTableRowHeigh();
        restoreTableColWidth();
    }

}