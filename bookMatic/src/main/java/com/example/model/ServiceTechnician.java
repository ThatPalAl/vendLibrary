package com.example.model;

import com.example.model.validation.ValidServiceTechnician;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@Builder
@ToString
@ValidServiceTechnician()
@DiscriminatorValue("ServiceTechnician")
public class ServiceTechnician extends Employee {

    @Column(name = "repairs_done")
    private int repairsDone;

    @Column(name = "training_hours", nullable = true)
    private Integer trainingHours;

    @Size(min = 2, max = 30)
    @Column(name = "certificate", nullable = true)
    private String certificate;

    @OneToMany(mappedBy = "serviceTechnician", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Repair> repairs = new HashSet<>();

    public ServiceTechnician() {
    }

    public ServiceTechnician(String firstName, String lastName, LocalDateTime birthDate, String email, String phoneNumber,
                             ContractType contractType, int earnings, int repairsDone, Integer trainingHours) {
        super(firstName, lastName, birthDate, email, phoneNumber, contractType, earnings);
        setRepairsDone(repairsDone);
        setTrainingHours(trainingHours);
        this.certificate = null;
    }

    public ServiceTechnician(String firstName, String lastName, LocalDateTime birthDate, String email, String phoneNumber,
                             ContractType contractType, int earnings, int repairsDone, Integer trainingHours,
                             String certificate) {
        super(firstName, lastName, birthDate, email, phoneNumber, contractType, earnings);
        setRepairsDone(repairsDone);
        setTrainingHours(trainingHours);
        this.certificate = certificate;
    }

    public int getRepairsDone() {
        return repairsDone;
    }

    public void setRepairsDone(int repairsDone) {
        if (repairsDone < 0) {
            throw new IllegalArgumentException("Repairs done cannot be set to a negative value");
        }
        this.repairsDone = repairsDone;
    }

    public Integer getTrainingHours() {
        return trainingHours;
    }

    public void setTrainingHours(Integer trainingHours) {
        if (trainingHours != null && trainingHours < 0) {
            throw new IllegalArgumentException("Training hours cannot be negative");
        }
        this.trainingHours = trainingHours;
    }

    public int calculateRate() {
        double multiplier = 1.0;

        if (this.certificate != null) {
            if (getRepairsDone() <= 50) {
                multiplier = 1.15;
            } else if (getRepairsDone() > 100) {
                multiplier = 1.25;
            }

            int rate = (int) (getEarnings() + (getRepairsDone() * multiplier));
            System.out.println("Service Technician Expert's rate: " + rate);
            return rate;
        } else {
            if (getRepairsDone() <= 50) {
                multiplier = 1.1;
            } else if (getRepairsDone() > 100) {
                multiplier = 1.15;
            }

            int rate = (int) (getEarnings() + (getRepairsDone() * multiplier));
            System.out.println("Service Technician Beginner's rate: " + rate);
            return rate;
        }
    }

    public Set<Repair> getRepairs() {
        return Collections.unmodifiableSet(repairs);
    }

    public void conductRepair(VendingMachine vendingMachine, LocalDateTime repairDate)
            throws IllegalArgumentException {
        if (vendingMachine == null || repairDate == null) {
            throw new IllegalArgumentException("Vending machine and repair date cannot be null");
        }
        for (Repair r : repairs) {
            if (r.getVendingMachine().equals(vendingMachine) && r.getRepairDate().equals(repairDate)) {
                throw new IllegalArgumentException("Cannot insert duplicates");
            }
        }
        repairsDone++;
        Repair repair = new Repair(vendingMachine, this, repairDate);
        repairs.add(repair);
        vendingMachine.addRepairInternal(repair);
    }

    void addRepairInternal(Repair repair) {
        repairs.add(repair);
    }

    void removeRepairInternal(Repair repair) {
        repairs.remove(repair);
    }

    public void removeRepair(Repair repair) throws IllegalArgumentException {
        if (repair != null && repairs.contains(repair)) {
            repairs.remove(repair);
            if (repair.getVendingMachine() != null) {
                repair.getVendingMachine().removeRepairInternal(repair);
            }
            repair.setVendingMachine(null);
            repair.setServiceTechnician(null);
        }
    }

    public String getCertificate() {
        return certificate;
    }

    public void changeCertificate(String certificate) throws IllegalArgumentException {
        if(this.isBeginner()){
            throw new IllegalArgumentException("It's not possible to change the certificate for a beginner type, as beginners don't have certificates.");
        }
        this.certificate = certificate;
    }

    public boolean isBeginner() {
        return certificate == null;
    }

    public boolean isExpert() {
        return certificate != null;
    }

    public void promoteToExpert(String certificate) throws IllegalArgumentException {
        if (isExpert()) {
            throw new IllegalArgumentException("This technician is already an expert.");
        }
        this.certificate = certificate;
    }

    public void demoteToBeginner() throws IllegalArgumentException {
        if (isBeginner()) {
            throw new IllegalArgumentException("This technician is already a beginner.");
        }
        this.certificate = null;
    }

    @Override
    public String toString() {
        return "Service Technician named: " + getFirstName() + " " + getLastName() + " born: " + getBirthDate() +
                ", email='" + getEmail() + '\'' + ", phoneNumber='" + getPhoneNumber() +
                " earnings=" + super.getEarnings() +
                ", repairsDone=" + repairsDone +
                ", trainingHours=" + trainingHours +
                ", repairs=" + repairs + ", certificate: " + certificate;
    }
}