package com.example.libraryservice.mapper;

import com.example.libraryservice.dto.BookStatusDto;
import com.example.libraryservice.entity.BookStatusEntity;
import org.springframework.stereotype.Component;

@Component
public class BookStatusMapper {
    public BookStatusDto toDto(BookStatusEntity entity) {
        return BookStatusDto.builder()
                .id(entity.getId())
                .bookId(entity.getBookId())
                .takenAt(entity.getTakenAt())
                .returnAt(entity.getReturnAt())
                .build();
    }

    public BookStatusEntity toEntity(BookStatusDto dto) {
        return BookStatusEntity.builder()
                .id(dto.id())
                .bookId(dto.bookId())
                .takenAt(dto.takenAt())
                .returnAt(dto.returnAt())
                .build();
    }
}

