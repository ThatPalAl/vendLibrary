package com.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@Builder
@ToString
public class VendingMachine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;

    @OneToMany(mappedBy = "vendingMachine", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Repair> repairs = new HashSet<>();

    @OneToMany(mappedBy = "vendingMachine", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Book> books = new HashSet<>();

    public VendingMachine() {
    }

    public VendingMachine(String model) {
        setModel(model);
    }

    public Long getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) throws IllegalArgumentException {
        if(model == null || model.isEmpty()){
            throw new IllegalArgumentException("Order canont be null");
        }
        if(model.length() < 2 || model.length() > 20){
            throw new IllegalArgumentException("Models's name is too long or too short (from 3 to 19 chars)");
        }
        this.model = model;
    }

    public Set<Repair> getRepairs() {
        return repairs;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Bake cannot be null");
        }
        book.setVendingMachine(this);
        books.add(book);
    }

    public void addRepair(ServiceTechnician technician, LocalDateTime date) throws IllegalArgumentException {
        if (technician == null || date == null) {
            throw new IllegalArgumentException("Technician and date cannot be null");
        }
        for (Repair repair : repairs) {
            if (repair.getVendingMachine().equals(this) && repair.getServiceTechnician().equals(technician) &&
                    repair.getRepairDate().equals(date)) {
                throw new IllegalArgumentException("Repair with this vending machine, technician, " +
                        "and the same date is already in the system.");
            }
        }

        Repair repair = new Repair(this, technician, date);
        repairs.add(repair);
        technician.addRepairInternal(repair);
    }

    void addRepairInternal(Repair repair) {
        repairs.add(repair);
    }

    void removeRepairInternal(Repair repair) {
        repairs.remove(repair);
    }

    public void removeRepair(Repair repair) throws IllegalArgumentException {
        if(repair == null){
            throw new IllegalArgumentException("Can't remove a repair which is null");
        }
        if (repairs.contains(repair)) {
            repairs.remove(repair);
            if (repair.getServiceTechnician() != null) {
                repair.getServiceTechnician().removeRepairInternal(repair);
            }
            repair.setVendingMachine(null);
            repair.setServiceTechnician(null);
        }
    }
}
