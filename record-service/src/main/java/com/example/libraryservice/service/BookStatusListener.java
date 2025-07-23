package com.example.libraryservice.service;

import com.example.libraryservice.config.RabbitProps;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.example.libraryservice.entity.BookStatusEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class BookStatusListener {
    private final BookStatusService bookStatusService;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitProps rabbitProps;

    @RabbitListener(queues = "#{rabbitProps.bookIdQueue}")
    public void receiveBookId(Long bookId) {
        BookStatusEntity status = new BookStatusEntity();
        status.setBookId(bookId);
        status.setTakenAt(null);
        status.setReturnAt(null);
        bookStatusService.addBookStatus(status);
    }

    @RabbitListener(queues = "#{rabbitProps.busyBooksRequestQueue}")
    public void onBusyBooksRequest(String request) {
        var busyIds = bookStatusService.getBusyBookIds();
        rabbitTemplate.convertAndSend(rabbitProps.getBusyBooksQueue(), busyIds);
    }
}
