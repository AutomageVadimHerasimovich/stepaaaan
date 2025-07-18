package com.example.libraryservice.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusyBooksPublisher {
    private final RabbitTemplate rabbitTemplate;

    public BusyBooksPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendBusyBookIds(List<Long> busyIds) {
        rabbitTemplate.convertAndSend("busyBooksQueue", busyIds);
    }
}