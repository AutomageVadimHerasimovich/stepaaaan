package com.example.libraryservice.service;

import com.example.libraryservice.service.BookStatusService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;

@Profile("!test")
@Component
public class BusyBooksStartupSender {

    private final BookStatusService bookStatusService;
    private final RabbitTemplate rabbitTemplate;

    public BusyBooksStartupSender(BookStatusService bookStatusService, RabbitTemplate rabbitTemplate) {
        this.bookStatusService = bookStatusService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void sendBusyBooksOnStartup() {
        var busyIds = bookStatusService.getBusyBookIds();
        rabbitTemplate.convertAndSend("busyBooksQueue", busyIds);
    }
}