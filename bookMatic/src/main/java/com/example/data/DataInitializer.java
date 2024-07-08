package com.example;

import com.example.model.*;
import com.example.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = Logger.getLogger(DataInitializer.class.getName());

    private final BookRepository bookRepository;
    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;
    private final RentPositionRepository rentPositionRepository;
    private final VendingMachineRepository vendingMachineRepository;
    private final LibrarianRepository librarianRepository;

    public DataInitializer(BookRepository bookRepository, ClientRepository clientRepository,
                           OrderRepository orderRepository, RentPositionRepository rentPositionRepository,
                           VendingMachineRepository vendingMachineRepository, LibrarianRepository librarianRepository) {
        this.bookRepository = bookRepository;
        this.clientRepository = clientRepository;
        this.orderRepository = orderRepository;
        this.rentPositionRepository = rentPositionRepository;
        this.vendingMachineRepository = vendingMachineRepository;
        this.librarianRepository = librarianRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (databaseIsEmpty()) {
            initData();
        }
    }

    private boolean databaseIsEmpty() {
        boolean isEmpty = bookRepository.count() == 0 &&
                clientRepository.count() == 0 &&
                orderRepository.count() == 0 &&
                rentPositionRepository.count() == 0 &&
                vendingMachineRepository.count() == 0 &&
                librarianRepository.count() == 0;
        logger.info("Database is empty: " + isEmpty);
        return isEmpty;
    }

    @Transactional
    private void initData() {
        List<Book> books = createBooks();
        bookRepository.saveAll(books);
        logger.info("Most expensive book: " + books.stream().max((b1, b2) -> Double.compare(b1.getPrice(), b2.getPrice())).get());

        createPastOrders(books);
        createLibrarians();

        logger.info("Data initialization completed.");
    }

    private List<Book> createBooks() {
        Book book1 = new Book("The Great Gatsby", "F. Scott Fitzgerald", 10.0, LocalDateTime.of(1925, 4, 10, 0, 0), "Classic", FictionType.NOVEL);
        Book book2 = new Book("1984", "George Orwell", 15.0, LocalDateTime.of(1949, 6, 8, 0, 0), "Dystopian", FictionType.NOVEL);
        Book book3 = new Book("Sapiens", "Yuval Noah Harari", 20.0, LocalDateTime.of(2011, 1, 1, 0, 0), "History", NonFictionType.HISTORY);
        Book book4 = new Book("Educated", "Tara Westover", 12.0, LocalDateTime.of(2018, 2, 20, 0, 0), "Memoir", NonFictionType.MEMOIR);
        Book book5 = new Book("The Catcher in the Rye", "J.D. Salinger", 10.0, LocalDateTime.of(1951, 7, 16, 0, 0), "Classic", FictionType.NOVEL);

        List<Book> books = new ArrayList<>();
        Collections.addAll(books, book1, book2, book3, book4, book5);
        return books;
    }

    private void createPastOrders(List<Book> books) {
        createPastOrder(books.get(0), books.get(1), LocalDateTime.now().minusDays(10));
        createPastOrder(books.get(2), books.get(3), LocalDateTime.now().minusDays(5));
        createPastOrder(books.get(1), books.get(4), LocalDateTime.now().minusDays(3));
        createPastOrder(books.get(3), books.get(0), LocalDateTime.now().minusDays(7));
    }

    private void createPastOrder(Book book1, Book book2, LocalDateTime orderDate) {
        Client c1 = new Client("John", "Doe", LocalDateTime.now(), "john.doe@mail.com", "123456789", 500);
        Client c2 = new Client("Jane", "Smith", LocalDateTime.now(), "jane.smith@mail.com", "987654321", 300);

        clientRepository.save(c1);
        clientRepository.save(c2);

        Order order = new Order(orderDate);
        order.setPlacedBy(c1);
        order.setStatus(OrderStatus.COMPLETED);
        Random random = new Random();
        RentPosition rentPosition1 = new RentPosition(order, book1, LocalDateTime.now().minusDays(random.nextInt(30)), LocalDateTime.now());
        RentPosition rentPosition2 = new RentPosition(order, book2, LocalDateTime.now().minusDays(random.nextInt(30)), LocalDateTime.now());

        orderRepository.save(order);
        rentPositionRepository.save(rentPosition1);
        rentPositionRepository.save(rentPosition2);
    }

    private void createLibrarians() {
        Librarian librarian = new Librarian("Emma", "Stone", LocalDateTime.of(1984, 1, 1, 12, 0), "emma.stone@mail.com", "678909765678", ContractType.PART_TIME, 10000);
        librarianRepository.save(librarian);
    }
}
