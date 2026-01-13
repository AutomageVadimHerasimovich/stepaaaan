package com.example.libraryservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "queues")
public class RabbitProps {
    String busyBooksQueue;
    String bookIdQueue;
    String busyBooksRequestQueue;
}