package com.github.vadimher.library.service;

import com.github.vadimher.library.entity.Book;
import com.github.vadimher.library.service.BookPublisher;
import com.github.vadimher.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookPublisher bookPublisher;

    @Autowired
    public BookService(BookRepository bookRepository, BookPublisher bookPublisher) {
        this.bookRepository = bookRepository;
        this.bookPublisher = bookPublisher;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Optional<Book> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public Book save(Book book) {
        try {
            Book saved = bookRepository.save(book);
            bookPublisher.sendBookId(saved.getId());
            return saved;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("ISBN busy");
        }
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
    } 
}