package com.github.vadimher.library.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BusyBooksListener {
    private final BusyBooksService busyBooksService;

    public BusyBooksListener(BusyBooksService busyBooksService) {
        this.busyBooksService = busyBooksService;
    }

    @RabbitListener(queues = "busyBooksQueue")
    public void receiveBusyBookIds(List<Long> busyIds) {
        System.out.println("Список занятых книг из RabbitMQ: " + busyIds);
        busyBooksService.updateBusyBooks(busyIds);
    }
}