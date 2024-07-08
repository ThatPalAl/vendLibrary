package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("BAKER")
public class Librarian extends Employee {

    @Column(name = "bakes_produced", nullable = true)
    private int booksRefurbished = 0;

    @OneToMany(mappedBy = "refurbishedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Book> books = new HashSet<>();

    public Librarian() {
    }

    public Librarian(String firstName, String lastName, LocalDateTime birthDate, String email, String phoneNumber,
                     ContractType contractType, int earnings) {
        super(firstName, lastName, birthDate, email, phoneNumber, contractType, earnings);
        setBooksRefurbished(0);
    }

    public int getBooksRefurbished() {
        return booksRefurbished;
    }

    public void setBooksRefurbished(int bakesProduced) throws IllegalArgumentException {
        if (bakesProduced < 0) {
            throw new IllegalArgumentException("Produced bakes value cannot be set to less than 0");
        }
        this.booksRefurbished = bakesProduced;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void refurbishBook(Book book) {
        books.add(book);
        book.setRefurbishedBy(this);
    }

    public void removeRefurbishdBook(Book book, Librarian newLibrarian) {
        books.remove(book);
        book.setRefurbishedBy(newLibrarian);
    }

    @Override
    public int calculateRate() {
        int baseRate = 50;
        double efficiencyBonus = 1.0;

        if (booksRefurbished > 100 && booksRefurbished < 200) {
            efficiencyBonus = 1.1;
        } else if (booksRefurbished > 200) {
            efficiencyBonus = 1.2;
        }

        int rate = (int) ((getEarnings() + (booksRefurbished * baseRate)) * efficiencyBonus);
        return rate;
    }

    @Override
    public String toString() {
        return "Baker named: " + getFirstName() + " " + getLastName() + " born: " + getBirthDate() +
                ", email='" + getEmail() + '\'' + ", phoneNumber='" + getPhoneNumber() + " with number of bakes" +
                " produced: " + booksRefurbished + " and contract type: " + super.getContractType() + " and earnings: " +
                super.getEarnings();
    }
}
