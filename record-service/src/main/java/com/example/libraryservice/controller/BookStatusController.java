package com.example.libraryservice.controller;

import com.example.libraryservice.entity.BookStatusEntity;
import com.example.libraryservice.service.BookStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/book-status")
public class BookStatusController {

    private final BookStatusService bookStatusService;

    public BookStatusController(BookStatusService bookStatusService) {
        this.bookStatusService = bookStatusService;
    }

    @PostMapping
    public ResponseEntity<BookStatusEntity> addBookStatus(@RequestBody BookStatusEntity bookStatus) {
        BookStatusEntity saved = bookStatusService.addBookStatus(bookStatus);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<BookStatusEntity>> getAllBookStatuses() {
        return ResponseEntity.ok(bookStatusService.getAllBookStatuses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookStatusEntity> getBookStatusById(@PathVariable Long id) {
        return bookStatusService.getBookStatusById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/busy-ids")
    public ResponseEntity<List<Long>> getBusyBookIds() {
        List<Long> busyIds = bookStatusService.getBusyBookIds();
        return ResponseEntity.ok(busyIds);
    }

    @PostMapping("/send-busy-ids")
    public ResponseEntity<Void> sendBusyBookIdsToRabbit() {
        bookStatusService.sendBusyBookIdsToRabbit();
        return ResponseEntity.ok().build();
    }
}