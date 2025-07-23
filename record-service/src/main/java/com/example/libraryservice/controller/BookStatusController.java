package com.example.libraryservice.controller;

import com.example.libraryservice.dto.BookStatusDto;
import com.example.libraryservice.entity.BookStatusEntity;
import com.example.libraryservice.mapper.BookStatusMapper;
import com.example.libraryservice.service.BookStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/book-status")
public class BookStatusController {

    private final BookStatusService bookStatusService;
    private final BookStatusMapper bookStatusMapper;

    @PostMapping
    public ResponseEntity<BookStatusDto> addBookStatus(@RequestBody BookStatusDto bookStatusDto) {
        BookStatusEntity saved = bookStatusService.addBookStatus(bookStatusMapper.toEntity(bookStatusDto));
        return ResponseEntity.ok(bookStatusMapper.toDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<BookStatusDto>> getAllBookStatuses() {
        List<BookStatusDto> dtos = bookStatusService.getAllBookStatuses().stream()
                .map(bookStatusMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookStatusDto> getBookStatusById(@PathVariable Long id) {
        return bookStatusService.getBookStatusById(id)
                .map(bookStatusMapper::toDto)
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