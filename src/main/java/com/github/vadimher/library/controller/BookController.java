package com.github.vadimher.library.controller;

import com.github.vadimher.library.entity.Book;
import com.github.vadimher.library.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Set;
import com.github.vadimher.library.mapper.BookMapper;
import com.github.vadimher.library.dto.BookDto;
import com.github.vadimher.library.service.BusyBooksService;

@RestController
@RequestMapping("/api/library")
public class BookController {
    private final BookService bookService;
    private final BusyBooksService busyBooksService;

    public BookController(BookService bookService, BusyBooksService busyBooksService) {
        this.bookService = bookService;
        this.busyBooksService = busyBooksService;
    }

    @GetMapping("/allbooks")
    public List<BookDto> getAllBooks() {
        return bookService.findAll().stream()
                .map(BookMapper.INSTANCE::bookToBookDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(book -> ResponseEntity.ok(BookMapper.INSTANCE.bookToBookDto(book)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDto> getBookByIsbn(@PathVariable String isbn) {
        return bookService.findByIsbn(isbn)
                .map(book -> ResponseEntity.ok(BookMapper.INSTANCE.bookToBookDto(book)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/addbook")
    @PreAuthorize("hasRole('ADMIN')")
    public BookDto addBook(@RequestBody BookDto bookDto) {
        Book book = BookMapper.INSTANCE.bookDtoToBook(bookDto);
        return BookMapper.INSTANCE.bookToBookDto(bookService.save(book));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        return bookService.findById(id)
                .map(existing -> {
                    Book updated = BookMapper.INSTANCE.bookDtoToBook(bookDto);
                    updated.setId(existing.getId());
                    return ResponseEntity.ok(BookMapper.INSTANCE.bookToBookDto(bookService.save(updated)));
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
        Set<Long> busyIds = busyBooksService.getBusyBookIds();
        return bookService.findAll().stream()
                .filter(book -> !busyIds.contains(book.getId()))
                .map(BookMapper.INSTANCE::bookToBookDto)
                .toList();
    }
}