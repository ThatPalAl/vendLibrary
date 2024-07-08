package com.example.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.EnumSet;

@Entity
@DiscriminatorValue("EMPLOYEE")
public abstract class Employee extends Person {

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type")
    private ContractType contractType;

    @Column(name = "earnings")
    private int earnings;

    public Employee() {
    }

    public Employee(String firstName, String lastName, LocalDateTime birthDate, String email, String phoneNumber,
                    ContractType contractType, int earnings) {
        super(firstName, lastName, birthDate, email, phoneNumber);
        setContractType(contractType);
        setEarnings(earnings);
    }

    public int getEarnings() {
        return earnings;
    }

    public void setEarnings(int earnings) {
        if (earnings < 0) {
            throw new IllegalArgumentException("Earnings cannot be negative");
        }
        this.earnings = earnings;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        if (contractType == null) {
            throw new IllegalArgumentException("Contract type cannot be null");
        }
        if(!EnumSet.allOf(ContractType.class).contains(contractType)) {
            throw new IllegalArgumentException("Contract type must be declared as one of the possible Contract types");
        }
        this.contractType = contractType;
    }

    public abstract int calculateRate();

    @Override
    public String toString() {
        return "Employee named: " + getFirstName() + " " + getLastName() + " born: " + getBirthDate() +
                ", email='" + getEmail() + '\'' + ", phoneNumber='" + getPhoneNumber() + " with contract type: " +
                contractType;
    }
}
