package com.example.repository;

import com.example.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, Long> {

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.rentPositions rp LEFT JOIN FETCH rp.book")
    List<Order> findAllWithRentOrderPositionsAndBooks();

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.rentPositions rp " +
            "LEFT JOIN FETCH rp.book WHERE o.id = :orderId")
    Optional<Order> findByIdWithDetails(Long orderId);

}
