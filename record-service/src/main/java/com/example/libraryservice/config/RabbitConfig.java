package com.example.libraryservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

@RequiredArgsConstructor
@Configuration
public class RabbitConfig {
    private final RabbitProps rabbitProps;

    @Bean
    public Queue busyBooksQueue() {
        return new Queue(rabbitProps.getBusyBooksQueue(), true);
    }

    @Bean
    public Queue busyBooksRequestQueue() {
        return new Queue(rabbitProps.getBusyBooksRequestQueue(), true);
    }

    @Bean
    public Queue bookIdQueue() {
        return new Queue(rabbitProps.getBookIdQueue(), true);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}