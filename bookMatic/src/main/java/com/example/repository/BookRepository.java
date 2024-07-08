package com.example.repository;

import com.example.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.rentPositions op LEFT JOIN FETCH op.order")
    List<Book> findAllWithOrderPositions();

    Optional<Book> findByTitle(String title);

}
