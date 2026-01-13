package com.github.vadimher.library.mapper;

import com.github.vadimher.library.dto.BookDto;
import com.github.vadimher.library.entity.BookEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDto bookToBookDto(BookEntity book);
    BookEntity bookDtoToBook(BookDto bookDto);
}