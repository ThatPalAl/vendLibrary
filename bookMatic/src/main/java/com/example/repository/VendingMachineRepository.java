package com.example.repository;

import com.example.model.RentPosition;
import com.example.model.VendingMachine;
import org.springframework.data.repository.CrudRepository;

public interface VendingMachineRepository extends CrudRepository<VendingMachine, Long> {

}
