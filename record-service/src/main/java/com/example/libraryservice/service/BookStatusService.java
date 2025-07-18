package com.example.libraryservice.service;

import com.example.libraryservice.entity.BookStatus;
import com.example.libraryservice.repository.BookStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class BookStatusService {

    private final BookStatusRepository bookStatusRepository;

    public BookStatusService(BookStatusRepository bookStatusRepository) {
        this.bookStatusRepository = bookStatusRepository;
    }

    public BookStatus addBookStatus(BookStatus bookStatus) {
        return bookStatusRepository.save(bookStatus);
    }

    public List<BookStatus> getAllBookStatuses() {
        return bookStatusRepository.findAll();
    }

    public List<Long> getBusyBookIds() {
        return bookStatusRepository.findAll()
                .stream()
                .filter(bs -> bs.getTakenAt() != null || bs.getReturnAt() != null)
                .map(BookStatus::getBookId)
                .toList();
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendBusyBookIdsToRabbit() {
        List<Long> busyIds = getBusyBookIds();
        rabbitTemplate.convertAndSend("busyBooksQueue", busyIds);
    }

    public Optional<BookStatus> getBookStatusById(Long id) {
        return bookStatusRepository.findById(id);
    }
}