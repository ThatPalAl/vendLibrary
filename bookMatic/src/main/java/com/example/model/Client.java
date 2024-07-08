package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends Person {
    @Column(name = "loyalty_points")
    private int loyaltyPoints;

    @OneToMany(mappedBy = "placedBy", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Order> orders = new HashSet<>();
    @Id
    private Long id;

    public Client() {
    }

    public Client(String firstName, String lastName, LocalDateTime birthDate, String email, String phoneNumber, int loyaltyPoints) {
        super(firstName, lastName, birthDate, email, phoneNumber);
        this.loyaltyPoints = loyaltyPoints;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        if (loyaltyPoints < 0) {
            throw new IllegalArgumentException("Loyalty points cannot be negative");
        }
        this.loyaltyPoints = loyaltyPoints;
    }

    public Set<Order> getRentOrders() {
        return orders;
    }

    public void placeRentOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Rent order cannot be null");
        }
        if (order.getPlacedBy() != this) {
            order.placedBy = this;
            this.orders.add(order);
        }
    }

    public void removeRentOrder(Order order) {
        orders.remove(order);
        order.setPlacedBy(null);
    }

    @Override
    public String toString() {
        return "Client named: " + getFirstName() + " " + getLastName() + " born: " + getBirthDate() + ", email='" + getEmail() + '\'' + ", phoneNumber='" + getPhoneNumber() + " with " + loyaltyPoints + " loyalty points. \n List of rent orders: " + orders;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
