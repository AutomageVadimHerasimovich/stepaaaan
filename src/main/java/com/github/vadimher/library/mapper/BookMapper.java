package com.github.vadimher.library.mapper;

import com.github.vadimher.library.dto.BookDto;
import com.github.vadimher.library.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookDto bookToBookDto(Book book);
    Book bookDtoToBook(BookDto bookDto);
}