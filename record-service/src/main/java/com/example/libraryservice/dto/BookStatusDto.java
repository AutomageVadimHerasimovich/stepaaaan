package com.example.libraryservice.dto;

import lombok.Builder;
import java.util.Date;

@Builder(toBuilder = true)
public record BookStatusDto(
    Long id,
    Long bookId,
    Date takenAt,
    Date returnAt
) {}
