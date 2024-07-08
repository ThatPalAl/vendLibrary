package com.example.gui.guiModels;

import com.example.model.Order;
import com.example.model.RentPosition;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderDetailsModel extends AbstractTableModel {

    private final List<Order> orders;
    private final String[] columnNames = {"Order Date", "Bakes", "Total Price"};
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy:MM:dd-HH:mm");

    public OrderDetailsModel(List<Order> orders) throws IllegalArgumentException {
        if (orders.isEmpty()) {
            throw new IllegalArgumentException("No orders to load");
        }
        this.orders = orders;
//        this.orders.forEach(order -> order.getOrderPositions().size());
    }

    @Override
    public int getRowCount() {
        return orders.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Order order = orders.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> order.getOrderDate().format(DATE_TIME_FORMATTER);
            case 1 -> order.getRentPositions().stream()
                    .map(op -> op.getBook().getTitle() + " x" + op.getRentOrder())
                    .reduce((b1, b2) -> b1 + ", " + b2)
                    .orElse("");
            case 2 -> order.getRentPositions().stream()
                    .mapToDouble(op -> op.getBook().getPrice());
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public Order getOrderAt(int rowIndex) {
        return orders.get(rowIndex);
    }
}
