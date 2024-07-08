package com.example.service;

import com.example.model.Book;
import com.example.model.Order;
import com.example.model.RentPosition;
import com.example.repository.BookRepository;
import com.example.repository.OrderRepository;
import com.example.repository.RentPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RentPositionRepository rentPositionRepository;

    @Transactional(readOnly = true)
    public List<Order> getAllRentOrders() {
        return orderRepository.findAllWithRentOrderPositionsAndBooks();
    }

    @Transactional
    public Order saveOrder(Order order) {
        orderRepository.save(order);
        return order;
    }

    @Transactional(readOnly = true)
    public Order getOrderWithDetails(Long orderId) {
        return orderRepository.findByIdWithDetails(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Rent order not found with id: " + orderId));
    }

    @Transactional(readOnly = true)
    public List<Book> getAvailableBooksForRentOrder(Order order) {
        List<Book> allBooks = bookRepository.findAllWithOrderPositions();
        Set<String> existingBookNames = new HashSet<>();
        for (RentPosition orderPosition : order.getRentPositions()) {
            existingBookNames.add(orderPosition.getBook().getTitle());
        }

        return allBooks.stream()
                .filter(book -> !existingBookNames.contains(book.getTitle()))
                .toList();
    }

    @Transactional(readOnly = true)
    public Book getBookByName(String bookTitle) {
        return bookRepository.findByTitle(bookTitle).orElse(null);
    }

    @Transactional
    public void addPositionToOrder(Long orderId, String bookTitle, LocalDate returnDate) {
        Order order = getOrderWithDetails(orderId);
        Optional<Book> optionalBook = bookRepository.findByTitle(bookTitle);
        Book book = optionalBook.orElseThrow(() -> new IllegalArgumentException("Book not found with title: " +
                bookTitle));
        RentPosition newPosition = new RentPosition(order, book, LocalDateTime.now());
        order.addRentPosition(newPosition);
        orderRepository.save(order);
    }
}
