package com.example.gui;

import com.example.gui.guiModels.MainFrameModel;
import com.example.model.Order;
import com.example.service.OrderService;
import org.springframework.context.ApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private final ApplicationContext context;
    private final OrderService orderService;
    private final JTable orderTable;

    public MainFrame(ApplicationContext context) {
        this.context = context;
        this.orderService = context.getBean(OrderService.class);

        setTitle("Order Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel exitPanel = new JPanel();
        JButton viewDetailsButton = new JButton("View Details");
        viewDetailsButton.addActionListener(e -> openSelectedOrder());
        exitPanel.add(viewDetailsButton);
        add(exitPanel, BorderLayout.SOUTH);

        JButton exitOrderHistory = new JButton("Exit");
        exitOrderHistory.addActionListener(e -> System.exit(0));
        exitPanel.add(exitOrderHistory);

        JPanel orderListPanel = new JPanel(new BorderLayout());
        orderTable = new JTable();
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(orderListPanel, BorderLayout.CENTER);

        JScrollPane orderTableScrollPane = new JScrollPane(orderTable);
        orderListPanel.add(orderTableScrollPane, BorderLayout.CENTER);
        loadPastOrders();

        setVisible(true);
    }

    private void loadPastOrders() {
        List<Order> pastOrders = orderService.getAllOrders();
        try {
            MainFrameModel mainFrameTableModel = new MainFrameModel(pastOrders);
            orderTable.setModel(mainFrameTableModel);
            adjustColumnWidths();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    private void openSelectedOrder() {
        System.out.println("Load past order start");
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow >= 0) {
            Order order = ((MainFrameModel) orderTable.getModel()).getOrderAt(selectedRow);
            Order detailedOrder = orderService.getOrderWithDetails(order.getId());
            System.out.println("Load past order middle");
            openViewDetailsFrame(detailedOrder);
            System.out.println("Load past order end");
        } else {
            JOptionPane.showMessageDialog(this, "Select one of the orders to view order details");
        }
    }

    private void openViewDetailsFrame(Order pastOrder) {
        if (pastOrder == null) {
            JOptionPane.showMessageDialog(this, "No order was selected");
            return;
        }
        ViewDetailsForm viewDetailsForm = new ViewDetailsForm(context, pastOrder, this);
        System.out.println("VIew details form");
        viewDetailsForm.setVisible(true);
    }

    private void adjustColumnWidths() {
        orderTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        orderTable.getColumnModel().getColumn(1).setPreferredWidth(320);
        orderTable.getColumnModel().getColumn(2).setPreferredWidth(100);
    }
}
