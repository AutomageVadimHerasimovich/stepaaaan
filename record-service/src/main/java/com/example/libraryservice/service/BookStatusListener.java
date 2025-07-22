package com.example.libraryservice.service;

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

    @RabbitListener(queues = "bookIdQueue")
    public void receiveBookId(Long bookId) {
        // Создаём новую запись BookStatus с bookId, остальные поля null
        BookStatusEntity status = new BookStatusEntity();
        status.setBookId(bookId);
        status.setTakenAt(null);
        status.setReturnAt(null);
        bookStatusService.addBookStatus(status);
    }

    @RabbitListener(queues = "busyBooksRequestQueue")
    public void onBusyBooksRequest(String request) {
        // При получении любого сообщения отправляем список занятых книг
        var busyIds = bookStatusService.getBusyBookIds();
        rabbitTemplate.convertAndSend("busyBooksQueue", busyIds);
    }
}
