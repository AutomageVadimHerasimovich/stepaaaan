package com.example.libraryservice.service;

import com.example.libraryservice.config.RabbitProps;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BusyBooksPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitProps rabbitProps;

    public void sendBusyBookIds(List<Long> busyIds) {
        rabbitTemplate.convertAndSend(rabbitProps.getBusyBooksQueue(), busyIds);
    }
}