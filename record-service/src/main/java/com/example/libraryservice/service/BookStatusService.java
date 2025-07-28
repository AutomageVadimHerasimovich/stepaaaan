package com.example.libraryservice.service;

import com.example.libraryservice.config.RabbitProps;
import com.example.libraryservice.entity.BookStatusEntity;
import com.example.libraryservice.repository.BookStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookStatusService {

    private final BookStatusRepository bookStatusRepository;
    private final RabbitProps rabbitProps;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public BookStatusEntity addBookStatus(BookStatusEntity bookStatus) {
        return bookStatusRepository.save(bookStatus);
    }

    public List<BookStatusEntity> getAllBookStatuses() {
        return bookStatusRepository.findAll();
    }

    public List<Long> getBusyBookIds() {
        return bookStatusRepository.findAll()
                .stream()
                .filter(bs -> bs.getTakenAt() != null || bs.getReturnAt() != null)
                .map(BookStatusEntity::getBookId)
                .toList();
    }

    public void sendBusyBookIdsToRabbit() {
        List<Long> busyIds = getBusyBookIds();
        rabbitTemplate.convertAndSend(rabbitProps.getBusyBooksQueue(), busyIds);
    }

    public Optional<BookStatusEntity> getBookStatusById(Long id) {
        return bookStatusRepository.findById(id);
    }

    public Optional<BookStatusEntity> updateBookStatus(Long id, BookStatusEntity updatedFields) {
        return bookStatusRepository.findById(id).map(existing -> {
            if (updatedFields.getBookId() != null) {
                existing.setBookId(updatedFields.getBookId());
            }
            existing.setTakenAt(updatedFields.getTakenAt());
            existing.setReturnAt(updatedFields.getReturnAt());
            return bookStatusRepository.save(existing);
        });
    }

    /**
     * Удаляет статус книги по ID книги
     * @param bookId ID книги
     */
    public void deleteBookStatusByBookId(Long bookId) {
        log.info("Attempting to delete book status for book ID: {}", bookId);
        List<BookStatusEntity> statuses = bookStatusRepository.findAll()
                .stream()
                .filter(status -> bookId.equals(status.getBookId()))
                .toList();

        if (statuses.isEmpty()) {
            log.warn("No book status found for book ID: {}", bookId);
        } else {
            log.info("Found {} status(es) to delete for book ID: {}", statuses.size(), bookId);
            statuses.forEach(status -> {
                bookStatusRepository.deleteById(status.getId());
                log.info("Deleted book status with ID: {} for book ID: {}", status.getId(), bookId);
            });
        }
    }
}