package com.example.repository;

import com.example.model.RentPosition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentPositionRepository extends CrudRepository<RentPosition, Long> {

    List<RentPosition> findByRentPositionId(Long rentPositionId);
}
