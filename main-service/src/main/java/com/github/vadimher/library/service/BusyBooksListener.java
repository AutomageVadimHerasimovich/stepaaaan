package com.github.vadimher.library.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class BusyBooksListener {
    private final BusyBooksService busyBooksService;

    public BusyBooksListener(BusyBooksService busyBooksService) {
        this.busyBooksService = busyBooksService;
    }

    @RabbitListener(queues = "busyBooksQueue")
    public void receiveBusyBookIds(String busyIdsJson) {
        System.out.println("Получено из RabbitMQ: " + busyIdsJson);
        try {
            ObjectMapper mapper = new ObjectMapper();
            Long[] busyIds = mapper.readValue(busyIdsJson, Long[].class);
            busyBooksService.updateBusyBooks(Arrays.asList(busyIds));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}