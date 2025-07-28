package com.github.vadimher.library.service;

import com.github.vadimher.library.entity.BookEntity;
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

    public List<BookEntity> findAll() {
        return bookRepository.findAll();
    }

    public Optional<BookEntity> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Optional<BookEntity> findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public BookEntity save(BookEntity book) {
        try {
            boolean isNewBook = book.getId() == null;
            BookEntity saved = bookRepository.save(book);

            if (isNewBook) {
                bookPublisher.sendBookId(saved.getId());
            }

            return saved;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("ISBN busy");
        }
    }

    public void delete(Long id) {
        bookPublisher.sendDeleteBookRequest(id);
        bookRepository.deleteById(id);
    } 
}