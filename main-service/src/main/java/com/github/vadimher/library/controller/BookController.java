package com.github.vadimher.library.controller;

import com.github.vadimher.library.dto.BookDto;
import com.github.vadimher.library.entity.BookEntity;
import com.github.vadimher.library.mapper.BookMapper;
import com.github.vadimher.library.service.BookService;
import com.github.vadimher.library.service.BusyBooksService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/library")
public class BookController {
    private final BookService bookService;
    private final BusyBooksService busyBooksService;
    private final RabbitTemplate rabbitTemplate;
    private final BookMapper bookMapper;

    public BookController(BookService bookService, BusyBooksService busyBooksService,
                          RabbitTemplate rabbitTemplate, BookMapper bookMapper) {
        this.bookService = bookService;
        this.busyBooksService = busyBooksService;
        this.rabbitTemplate = rabbitTemplate;
        this.bookMapper = bookMapper;
    }

    @GetMapping("/allbooks")
    public List<BookDto> getAllBooks() {
        return bookService.findAll().stream()
                .map(bookMapper::bookToBookDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(book -> ResponseEntity.ok(bookMapper.bookToBookDto(book)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDto> getBookByIsbn(@PathVariable String isbn) {
        return bookService.findByIsbn(isbn)
                .map(book -> ResponseEntity.ok(bookMapper.bookToBookDto(book)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/addbook")
    @PreAuthorize("hasRole('ADMIN')")
    public BookDto addBook(@RequestBody BookDto bookDto) {
        BookEntity book = bookMapper.bookDtoToBook(bookDto);
        return bookMapper.bookToBookDto(bookService.save(book));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        return bookService.findById(id)
                .map(existing -> {
                    BookEntity updated = bookMapper.bookDtoToBook(bookDto);
                    updated.setId(existing.getId());
                    return ResponseEntity.ok(bookMapper.bookToBookDto(bookService.save(updated)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/freebooks")
    public List<BookDto> getFreeBooks() {
        // Запросить актуальный список занятых книг через RabbitMQ
        rabbitTemplate.convertAndSend("busyBooksRequestQueue", "get");
        // Подождать обновления busyBooksService (в реальном проекте — асинхронно, тут — сразу возвращаем текущий список)
        Set<Long> busyIds = busyBooksService.getBusyBookIds();
        return bookService.findAll().stream()
                .filter(book -> !busyIds.contains(book.getId()))
                .map(bookMapper::bookToBookDto)
                .toList();
    }
}