package com.example.service;

import com.example.model.Book;
import com.example.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public Book saveBook(Book book) {

        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public List<Book> retrieveBooks() {
        return bookRepository.findAllWithOrderPositions();
    }
}
