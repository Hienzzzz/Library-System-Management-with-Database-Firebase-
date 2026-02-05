package project.Admin_Screen.Bookmanagement;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

class CustomCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column
        );

        // RESET defaults (important)
        setFont(table.getFont());
        setForeground(Color.BLACK);
        setBackground(Color.WHITE);
        setHorizontalAlignment(SwingConstants.LEFT);
        setVerticalAlignment(SwingConstants.CENTER);
        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        if (column == 0) {
            setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 10));
        }

        if (column == 1){
            setBorder(BorderFactory.createEmptyBorder(0, 15, 0,0));
        }

        if (column == 2){
            setBorder(BorderFactory.createEmptyBorder(0, 60, 0,0));
        }
     
        if (column == 3) {
            setHorizontalAlignment(SwingConstants.LEFT);
        }

        // Column 4 → right aligned (numbers)
        if (column == 4) {
            setHorizontalAlignment(SwingConstants.RIGHT);
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 20));
        }

        // Alternate row background
        if (row % 2 == 0) {
            setBackground(new Color(245, 245, 245));
        }

        // Preserve selection colors
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        }

        return this;
    }
}
