package com.github.vadimher.library.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record UserDto(
    Long id,
    String username,
    String password,
    String role
) {}

