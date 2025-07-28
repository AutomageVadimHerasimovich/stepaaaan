package com.example.libraryservice.service;

import com.example.libraryservice.config.RabbitProps;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.example.libraryservice.entity.BookStatusEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.converter.MessageConverter;

@RequiredArgsConstructor
@Component
@Slf4j
public class BookStatusListener {
    private final BookStatusService bookStatusService;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitProps rabbitProps;
    private final MessageConverter messageConverter;

    @RabbitListener(queues = "#{rabbitProps.bookIdQueue}")
    public void receiveBookId(Object message) {
        log.info("Received message: {} of type {}", message, message != null ? message.getClass().getName() : "null");

        try {
            // Обработка Message объекта
            if (message instanceof Message) {
                Message amqpMessage = (Message) message;
                Object payload = messageConverter.fromMessage(amqpMessage);
                log.info("Extracted payload: {} of type {}", payload, payload != null ? payload.getClass().getName() : "null");

                if (payload != null) {
                    processPayload(payload);
                } else {
                    log.error("Null payload extracted from Message");
                }
                return;
            }

            // Прямая обработка разных типов данных
            processPayload(message);

        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
        }
    }

    private void processPayload(Object payload) {
        // Обработка ID книги в разных форматах
        if (payload instanceof Long) {
            // Стандартный случай - получен Long ID
            processBookId((Long) payload);
        } else if (payload instanceof Integer) {
            // Преобразуем Integer в Long для совместимости
            processBookId(((Integer) payload).longValue());
        } else if (payload instanceof Number) {
            // Любой другой числовой тип
            processBookId(((Number) payload).longValue());
        } else if (payload instanceof String) {
            String strPayload = (String) payload;
            if (strPayload.startsWith("delete:")) {
                // Команда на удаление
                try {
                    Long bookId = Long.parseLong(strPayload.substring(7));
                    bookStatusService.deleteBookStatusByBookId(bookId);
                    log.info("Deleted book status for book ID: {}", bookId);
                } catch (NumberFormatException e) {
                    log.error("Failed to parse book ID from delete message: {}", strPayload, e);
                }
            } else {
                // Попытка преобразовать строку в ID книги
                try {
                    Long bookId = Long.parseLong(strPayload);
                    processBookId(bookId);
                } catch (NumberFormatException e) {
                    log.error("Failed to parse book ID from string message: {}", strPayload, e);
                }
            }
        } else {
            log.error("Unsupported payload type received: {}", payload != null ? payload.getClass().getName() : "null");
        }
    }

    private void processBookId(Long bookId) {
        log.info("Creating new book status for book ID: {}", bookId);
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
