package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "books")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "book_type", discriminatorType = DiscriminatorType.STRING)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "published_date", nullable = false)
    private LocalDateTime publishedDate;

    @Column(name = "description", nullable = true)
    private String description;

    @ElementCollection
    @Column(name = "genre")
    private List<String> genres = new ArrayList<>();

    @Column(name = "fiction_type", nullable = true)
    private String fictionType;

    @Column(name = "non_fiction_type", nullable = true)
    private String nonFictionType;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RentPosition> rentPositions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vending_machine_id")
    private VendingMachine vendingMachine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refurbishedBy")
    private Librarian refurbishedBy;

    public Book() {
    }

    public Book(String title, String author, double price, LocalDateTime publishedDate, String genre, FictionType fictionType) {
        setTitle(title);
        setAuthor(author);
        setPrice(price);
        setDescription(null);
        addGenre(genre);
        setPublishedDate(publishedDate);
        setFictionType(fictionType);
        setNonFictionType(null);
    }

    public Book(String title, String author, double price, LocalDateTime publishedDate, String genre, NonFictionType nonFictionType) {
        setTitle(title);
        setAuthor(author);
        setPrice(price);
        setDescription(null);
        addGenre(genre);
        setPublishedDate(publishedDate);
        setFictionType(null);
        setNonFictionType(nonFictionType);
    }

    public Book(String title, String author, double price, LocalDateTime publishedDate, String genre, FictionType fictionType, NonFictionType nonFictionType) {
        setTitle(title);
        setAuthor(author);
        setPrice(price);
        setDescription(null);
        addGenre(genre);
        setPublishedDate(publishedDate);
        setFictionType(fictionType);
        setNonFictionType(nonFictionType);
    }

    public Book(String title, String author, String description, double price, LocalDateTime publishedDate, String genre, FictionType fictionType) {
        setTitle(title);
        setAuthor(author);
        setDescription(description);
        setPrice(price);
        addGenre(genre);
        setPublishedDate(publishedDate);
        setFictionType(fictionType);
        setNonFictionType(null);
    }

    public Book(String title, String author, String description, double price, LocalDateTime publishedDate, String genre, NonFictionType nonFictionType) {
        setTitle(title);
        setAuthor(author);
        setDescription(description);
        setPrice(price);
        addGenre(genre);
        setPublishedDate(publishedDate);
        setFictionType(null);
        setNonFictionType(nonFictionType);
    }

    public Book(String title, String author, String description, double price, LocalDateTime publishedDate, String genre, FictionType fictionType, NonFictionType nonFictionType) {
        setTitle(title);
        setAuthor(author);
        setDescription(description);
        setPrice(price);
        addGenre(genre);
        setPublishedDate(publishedDate);
        setFictionType(fictionType);
        setNonFictionType(nonFictionType);
        validateTypes();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if (author == null || author.isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty");
        }
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime newPublishedDate) {
        if (newPublishedDate == null) {
            throw new IllegalArgumentException("No value for Published Date was provided - cannot be null");
        }
        if (newPublishedDate.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Published date value cannot be set to the future");
        }
        this.publishedDate = newPublishedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null && description.length() > 500) {
            throw new IllegalArgumentException("The description is too long");
        }
        this.description = description;
    }

    public List<String> getGenres() {
        return Collections.unmodifiableList(genres);
    }

    public void addGenre(String genre) {
        if (genre == null || genre.isEmpty()) {
            throw new IllegalArgumentException("Genre cannot be null or empty");
        }
        if (genres.size() >= 10) {
            throw new IllegalArgumentException("Too many genres, keep it simple");
        }
        genres.add(genre.toLowerCase());
    }

    public void removeGenre(String genreToRemove) {
        if (genres.isEmpty()) {
            throw new IllegalStateException("No genres to remove.");
        }
        if (!genres.contains(genreToRemove.toLowerCase())) {
            throw new IllegalArgumentException("Genre '" + genreToRemove + "' not found in the list. Available genres: " + genres);
        }
        genres.remove(genreToRemove.toLowerCase());
    }

    public String getFictionType() {
        return fictionType;
    }

    public void setFictionType(FictionType fictionType) {
        if (!EnumSet.allOf(FictionType.class).contains(fictionType) && fictionType != null) {
            throw new IllegalArgumentException("Fiction type must be declared as one of the possible FictionTypes");
        }
        this.fictionType = fictionType != null ? fictionType.name() : null;
    }

    public String getNonFictionType() {
        return nonFictionType;
    }

    public void setNonFictionType(NonFictionType nonFictionType) {
        if (!EnumSet.allOf(NonFictionType.class).contains(nonFictionType) && nonFictionType != null) {
            throw new IllegalArgumentException("Non-fiction type must be declared as one of the possible NonFictionTypes");
        }
        this.nonFictionType = nonFictionType != null ? nonFictionType.name() : null;
    }

    public Set<RentPosition> getRentPositions() {
        return Collections.unmodifiableSet(rentPositions);
    }

    public void addRentPosition(RentPosition rentPosition) {
        if (rentPosition == null) {
            throw new IllegalArgumentException("Invalid argument, null or duplicate");
        }
        for (RentPosition position : rentPositions) {
            if (position.getRentOrder().equals(rentPosition.getRentOrder())) {
                throw new IllegalArgumentException("Cannot insert duplicates");
            }
        }
        rentPositions.add(rentPosition);
    }

    public void removeRentPosition(RentPosition rentPosition) {
        if (rentPosition != null && rentPositions.contains(rentPosition)) {
            rentPositions.remove(rentPosition);
            if (rentPosition.getBook() != null) {
                rentPosition.getBook().removeRentPosition(rentPosition);
            }
            rentPosition.removeRentPosition();
        }
    }

    public VendingMachine getVendingMachine() {
        return vendingMachine;
    }

    public void setVendingMachine(VendingMachine vendingMachine) {
        if (vendingMachine == null) {
            throw new IllegalArgumentException("Vending machine cannot be null");
        }
        this.vendingMachine = vendingMachine;
    }

    public Librarian getRefurbishedBy() {
        return refurbishedBy;
    }


    public void setRefurbishedBy(Librarian newLibrarian) {
        this.refurbishedBy = newLibrarian;
    }
    private void validateTypes() {
        if (this.fictionType == null && this.nonFictionType == null) {
            throw new IllegalArgumentException("At least one of fictionType or nonFictionType must be provided.");
        }
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", publishedDate=" + publishedDate +
                ", description='" + description + '\'' +
                ", genres=" + genres +
                ", fictionType=" + fictionType +
                ", nonFictionType=" + nonFictionType +
                ", rentPositions=" + rentPositions +
                ", vendingMachine=" + vendingMachine +
                ", createdBy=" + (refurbishedBy != null ? refurbishedBy.getFirstName() + " "
                + refurbishedBy.getLastName() : null) +
                '}';
    }
}
