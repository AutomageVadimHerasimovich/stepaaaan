package com.github.vadimher.library.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record BookDto(
    Long id,
    String title,
    String author,
    String isbn,
    String genre,
    String description
) {}
