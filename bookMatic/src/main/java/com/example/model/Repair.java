package com.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@Builder
@ToString
public class Repair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vending_machine_id")
    private VendingMachine vendingMachine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_technician_id")
    private ServiceTechnician serviceTechnician;

    private LocalDateTime repairDate;

    public Repair() {
    }

    public Repair(VendingMachine vendingMachine, ServiceTechnician serviceTechnician, LocalDateTime repairDate) {
        if (vendingMachine == null || serviceTechnician == null) {
            throw new IllegalArgumentException("Vending machine or service technician cannot be null");
        }
        setVendingMachine(vendingMachine);
        setServiceTechnician(serviceTechnician);
        setRepairDate(repairDate);
        vendingMachine.addRepairInternal(this);
        serviceTechnician.addRepairInternal(this);
    }

    public Long getId() {
        return id;
    }

    public VendingMachine getVendingMachine() {
        return vendingMachine;
    }

    public void setVendingMachine(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    public ServiceTechnician getServiceTechnician() {
        return serviceTechnician;
    }

    public void setServiceTechnician(ServiceTechnician serviceTechnician) {
        this.serviceTechnician = serviceTechnician;
    }

    public LocalDateTime getRepairDate() {
        return repairDate;
    }

    public void setRepairDate(LocalDateTime repairDate) {
        if (repairDate == null) {
            throw new IllegalArgumentException("Repair date cannot be null");
        }
        this.repairDate = repairDate;
    }

    public void removeRepair() {
        if (vendingMachine != null) {
            vendingMachine.removeRepairInternal(this);
        }
        if (serviceTechnician != null) {
            serviceTechnician.removeRepairInternal(this);
        }
        this.vendingMachine = null;
        this.serviceTechnician = null;
    }
}
