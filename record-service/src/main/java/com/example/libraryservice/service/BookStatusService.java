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

@RequiredArgsConstructor
@Service
public class BookStatusService {

    private final BookStatusRepository bookStatusRepository;

    public BookStatusEntity addBookStatus(BookStatusEntity bookStatus) {
        return bookStatusRepository.save(bookStatus);
    }

    public List<BookStatusEntity> getAllBookStatuses() {
        return bookStatusRepository.findAll();
    }

    private final RabbitProps rabbitProps;

    public List<Long> getBusyBookIds() {
        return bookStatusRepository.findAll()
                .stream()
                .filter(bs -> bs.getTakenAt() != null || bs.getReturnAt() != null)
                .map(BookStatusEntity::getBookId)
                .toList();
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

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
}