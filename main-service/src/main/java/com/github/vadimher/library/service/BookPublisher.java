package com.github.vadimher.library.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BookPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final String queueName = "bookIdQueue";

    public BookPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendBookId(Long bookId) {
        rabbitTemplate.convertAndSend(queueName, bookId);
    }
}
