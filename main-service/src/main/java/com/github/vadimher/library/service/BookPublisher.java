package com.github.vadimher.library.service;

import com.github.vadimher.library.config.RabbitProps;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitProps rabbitProps;

    public void sendBookId(Long bookId) {
        rabbitTemplate.convertAndSend(rabbitProps.getBookIdQueue(), bookId);
    }
}
