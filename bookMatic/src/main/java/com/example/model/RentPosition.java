package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rent_positions")
public class RentPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentPositionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "rent_date", nullable = false)
    private LocalDateTime rentDate;

    @Column(name = "return_date", nullable = false)
    private LocalDateTime returnDate;

    public RentPosition() {
    }

    public RentPosition(Order order, Book book, LocalDateTime rentDate, LocalDateTime returnDate) {
        setRentOrder(order);
        setBook(book);
        setRentDate(rentDate);
        setReturnDate(returnDate);
    }

    public RentPosition(Order order, Book book, LocalDateTime rentDate) {
        setRentOrder(order);
        setBook(book);
        setRentDate(rentDate);
    }

    public Long getId() {
        return rentPositionId;
    }

    public Order getRentOrder() {
        return order;
    }

    public void setRentOrder(Order order) {
        if (this.order != order) {
            if (this.order != null) {
                this.order.removeRentPosition(this);
            }
            this.order = order;
            if (order != null) {
                order.addRentPosition(this);
            }
        }
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        if (this.book != book) {
            if (this.book != null) {
                this.book.removeRentPosition(this);
            }
            this.book = book;
            if (book != null) {
                book.addRentPosition(this);
            }
        }
    }

    public LocalDateTime getRentDate() {
        return rentDate;
    }

    public void setRentDate(LocalDateTime rentDate) {
        if (rentDate == null) {
            throw new IllegalArgumentException("Rent date cannot be null");
        }
        this.rentDate = rentDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        if (returnDate == null) {
            throw new IllegalArgumentException("Return date cannot be null");
        }
        if (returnDate.isBefore(rentDate)) {
            throw new IllegalArgumentException("Return date cannot be before rent date");
        }
        this.returnDate = returnDate;
    }

    public void removeRentPosition() {
        this.order = null;
        this.book = null;
    }

    @Override
    public String toString() {
        return "RentPosition{" +
                "id=" + rentPositionId +
                ", rentOrder=" + (order != null ? order.getId() : null) +
                ", book=" + (book != null ? book.getTitle() : null) +
                ", rentDate=" + rentDate +
                ", returnDate=" + returnDate +
                '}';
    }
}
