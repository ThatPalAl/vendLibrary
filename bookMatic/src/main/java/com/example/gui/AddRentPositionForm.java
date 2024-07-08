package com.example.gui;

import com.example.model.Book;
import com.example.model.Order;
import com.example.service.OrderService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

class AddRentPositionForm extends JDialog {
    private final JTable availableBooksTable;
    private final OrderService orderService;
    private final Order currentOrder;

    public AddRentPositionForm(JFrame parent, OrderService orderService, Order currentOrder) {
        super(parent, "Add new Position", true);
        this.orderService = orderService;
        this.currentOrder = currentOrder;

        setSize(600, 400);
        setLayout(new BorderLayout());

        availableBooksTable = new JTable();
        loadAvailableBooks();
        JScrollPane availableBakesScrollPane = new JScrollPane(availableBooksTable);
        add(availableBakesScrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("Add a selected book");
        addButton.addActionListener(e -> addSelectedBook());
        add(addButton, BorderLayout.SOUTH);
    }

    private void loadAvailableBooks() {
        List<Book> availableBooks = orderService.getAvailableBooksForRentOrder(currentOrder);
        DefaultTableModel availableBooksTableModel = new DefaultTableModel(
                new Object[]{"Book Name", "Description", "Price"}, 0);
        for (Book book : availableBooks) {
            availableBooksTableModel.addRow(new Object[]{book.getTitle(), book.getDescription(), book.getPrice()});
        }
        availableBooksTable.setModel(availableBooksTableModel);
    }

    private void addSelectedBook() {
        int selectedRow = availableBooksTable.getSelectedRow();
        LocalDate returnDate;
        if (selectedRow >= 0) {
            String bookTitle = (String) availableBooksTable.getValueAt(selectedRow, 0);
            String returnDateStr = JOptionPane.showInputDialog(this, "Enter return date for "
                    + bookTitle + ":");
            try {
                returnDate = LocalDate.parse(returnDateStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid quantity");
                return;
            }


            if (currentOrder.getId() == null) {
                Order savedOrder = orderService.saveOrder(currentOrder);
                currentOrder.setId(savedOrder.getId());
            }

            orderService.addPositionToOrder(currentOrder.getId(), bookTitle, returnDate);
            dispose();
        }
    }
}
