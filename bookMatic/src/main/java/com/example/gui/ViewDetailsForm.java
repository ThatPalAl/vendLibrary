package com.example.gui;

import com.example.gui.guiModels.OrderDetailsModel;
import com.example.model.Book;
import com.example.model.Order;
import com.example.model.RentPosition;
import com.example.model.OrderStatus;
import com.example.service.BookService;
import com.example.service.OrderService;
import org.springframework.context.ApplicationContext;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class ViewDetailsForm extends JFrame {

    private final OrderService orderService;
    private final BookService bookService;
    private final Order currentOrder;
    private final MainFrame mainFrame;
    private final JTable orderDetailsTable;
    private final OrderDetailsModel orderDetailsModel;
    private boolean orderChanged = false;

    public ViewDetailsForm(ApplicationContext context, Order pastOrder, MainFrame mainFrame) {
        System.out.println("view details constructor start");
        this.orderService = context.getBean(OrderService.class);
        this.bookService = context.getBean(BookService.class);
        this.mainFrame = mainFrame;

        setTitle("Order details");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        System.out.println("view details constructor Middle");
        currentOrder = new Order();
        currentOrder.setOrderDate(LocalDateTime.now());
        System.out.println("view details constructor Middle 2");
        currentOrder.setPlacedBy(pastOrder.getPlacedBy());

        System.out.println("view details constructor Middle3");
        List<Book> detailedBooks = bookService.retrieveBooks();
        for (RentPosition position : pastOrder.getRentPositions()) {
            Book book = detailedBooks.stream().filter(b -> b.getId().equals(position.getBook().getId())).findFirst().orElse(null);
            if (book != null) {
                RentPosition newPosition = new RentPosition(currentOrder, book, position.getRentDate());
                currentOrder.addRentPosition(newPosition);
            }
        }
        System.out.println("view details constructor middle 4 ");
        orderDetailsModel = new OrderDetailsModel((List<Order>) currentOrder);
        orderDetailsTable = new JTable(orderDetailsModel);

        JScrollPane orderDetailsScrollPane = new JScrollPane(orderDetailsTable);
        orderDetailsScrollPane.setPreferredSize(new Dimension(600, 400));
        add(orderDetailsScrollPane, BorderLayout.CENTER);

        JPanel addPositionPanel = new JPanel(new FlowLayout());

        JButton addPositionButton = new JButton("Add new position");
        addPositionButton.addActionListener(e -> openAddBakeDialog());
        addPositionPanel.add(addPositionButton);

        JButton confirmButton = new JButton("Create new order");
        confirmButton.addActionListener(e -> confirmAndPay());
        addPositionPanel.add(confirmButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> cancelOrder());
        addPositionPanel.add(cancelButton);


        add(addPositionPanel, BorderLayout.SOUTH);
        adjustColumnWidths();
    }

    private void openAddBakeDialog() {
        System.out.println("Before openinig add position form");
        AddRentPositionForm addPositionForm = new AddRentPositionForm(this, orderService, currentOrder);
        addPositionForm.setVisible(true);
    }


    private void confirmAndPay() {
        if (currentOrder.getRentPositions().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items in the current order to purchase");
            return;
        }

        StringBuilder orderSummary = new StringBuilder();
        orderSummary.append("Creating new order based on a past one.\n");
        orderSummary.append("Order Date: ").append(LocalDateTime.now()).append("\n")
                .append("Items:\n");

        for (RentPosition position : currentOrder.getRentPositions()) {
            Book book = position.getBook();
            orderSummary.append("\t").append(book.getTitle()).append("\n");
        }

        double fullPrice = 0;
        for (RentPosition position : currentOrder.getRentPositions()) {
            Book book = position.getBook();
            double positionPrice = book.getPrice();
            fullPrice += positionPrice;
        }

        orderSummary.append("Full price: ").append(fullPrice);

        JTextArea orderSummaryTextArea = new JTextArea(orderSummary.toString());
        orderSummaryTextArea.setEditable(false);
        orderSummaryTextArea.setPreferredSize(new Dimension(500, 400));
        JScrollPane scrollPane = new JScrollPane(orderSummaryTextArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        int confirmOrder = JOptionPane.showConfirmDialog(
                this, scrollPane, "Confirm Order", JOptionPane.YES_NO_OPTION);
        if (confirmOrder == JOptionPane.YES_OPTION) {
            currentOrder.setStatus(OrderStatus.COMPLETED);
            orderService.saveOrder(currentOrder);
            JOptionPane.showMessageDialog(this, "Order Confirmed");
            mainFrame.dispose();
            this.dispose();
            System.exit(0);
        } else if (confirmOrder == JOptionPane.NO_OPTION) {
            currentOrder.setStatus(OrderStatus.CANCELLED);
            orderService.saveOrder(currentOrder);
            JOptionPane.showMessageDialog(this, "Order Cancelled");
            dispose();
        }
    }

    private void cancelOrder() {
        if (orderChanged) {
            currentOrder.setStatus(OrderStatus.CANCELLED);
            orderService.saveOrder(currentOrder);
            dispose();
        } else {
            dispose();
        }
    }

    private void adjustColumnWidths() {
        TableColumn descriptionColumn = orderDetailsTable.getColumnModel().getColumn(1);
        TableColumn priceColumn = orderDetailsTable.getColumnModel().getColumn(2);
        TableColumn amountColumn = orderDetailsTable.getColumnModel().getColumn(3);

        descriptionColumn.setPreferredWidth(300);
        priceColumn.setPreferredWidth(50);
        amountColumn.setPreferredWidth(50);
    }
}
