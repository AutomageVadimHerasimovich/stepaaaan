package com.github.vadimher.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class BusyBooksListener {
    private final BusyBooksService busyBooksService;

    @RabbitListener(queues = "#{rabbitProps.getBusyBooksQueue}")
    public void receiveBusyBookIds(List<Long> busyIds) {
        System.out.println("Список занятых книг из RabbitMQ: " + busyIds);
        busyBooksService.updateBusyBooks(busyIds);
    }
}