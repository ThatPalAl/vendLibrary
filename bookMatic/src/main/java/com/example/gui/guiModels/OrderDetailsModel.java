package com.example.gui.guiModels;

import com.example.model.Order;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;

public class OrderDetailsModel extends AbstractTableModel {

    private final Order order;
    private final String[] columnNames = {"Order Date", "Books"};
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy:MM:dd-HH:mm");

    public OrderDetailsModel(Order order) throws IllegalArgumentException {
        if (order == null || order.getRentPositions().isEmpty()) {
            throw new IllegalArgumentException("No orders to load");
        }
        this.order = order;
    }

    @Override
    public int getRowCount() {
        return 1; // Since it's a single order
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return switch (columnIndex) {
            case 0 -> order.getOrderDate().format(DATE_TIME_FORMATTER);
            case 1 -> order.getRentPositions().stream()
                    .map(op -> op.getBook().getTitle() + " x" + op.getRentOrder())
                    .reduce((b1, b2) -> b1 + ", " + b2)
                    .orElse("");
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
