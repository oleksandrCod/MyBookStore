package store.mybookstore.mapper;

import org.mapstruct.Mapper;
import store.mybookstore.config.MapperConfig;
import store.mybookstore.dto.book.BookDto;
import store.mybookstore.dto.book.CreateBookRequestDto;
import store.mybookstore.model.Book;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
