package com.github.vadimher.library.mapper;

import com.github.vadimher.library.dto.BookDto;
import com.github.vadimher.library.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDto bookToBookDto(Book book);
    Book bookDtoToBook(BookDto bookDto);
}