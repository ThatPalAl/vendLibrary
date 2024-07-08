package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "\"order\"")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placedBy")
    Client placedBy;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RentPosition> rentPositions = new HashSet<>();

    public Order() {
        this.status = OrderStatus.NEW;
    }

    public Order(LocalDateTime orderDate) {
        setOrderDate(orderDate);
        this.status = OrderStatus.NEW;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        if (orderDate == null) {
            throw new IllegalArgumentException("Order date cannot be null");
        }
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        if (!EnumSet.allOf(OrderStatus.class).contains(status)) {
            throw new IllegalArgumentException("Status must be within possible OrderStatus types");
        }
        this.status = status;
    }

    public Client getPlacedBy() {
        return placedBy;
    }

    public void setPlacedBy(Client placedBy) {
        if (this.placedBy == placedBy) {
            return;
        }
        if (this.placedBy != null) {
            Client oldClient = this.placedBy;
            this.placedBy = null;
            oldClient.removeRentOrder(this);
        }
        if (placedBy != null) {
            this.placedBy = placedBy;
        }
    }

    public List<RentPosition> getRentPositions() {
        return new ArrayList<>(rentPositions);
    }

    public void addRentPosition(RentPosition position) {
        if (position == null) {
            throw new IllegalArgumentException("Rent position is null or Book unrelated");
        }
        rentPositions.add(position);
    }

    public void removeRentPosition(RentPosition rentPosition) {
        if (rentPosition != null && rentPositions.contains(rentPosition)) {
            rentPositions.remove(rentPosition);
            if (rentPosition.getBook() != null) {
                rentPosition.getBook().removeRentPosition(rentPosition);
            }
            this.setPlacedBy(null);
            rentPosition.removeRentPosition();
        }
    }

    @Override
    public String toString() {
        return "RentOrder{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", status=" + status +
                ", placedBy=" + (placedBy != null ? placedBy.getFirstName() + " " + placedBy.getLastName() : null) +
                ", rentPositions=" + rentPositions +
                '}';
    }
}
