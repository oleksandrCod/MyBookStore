package store.mybookstore.mapper;

import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import store.mybookstore.config.MapperConfig;
import store.mybookstore.dto.book.BookDto;
import store.mybookstore.dto.book.BookDtoWithoutCategoryIds;
import store.mybookstore.dto.book.CreateBookRequestDto;
import store.mybookstore.model.Book;
import store.mybookstore.model.Category;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategoryIds(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoryId(book
                .getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toSet()));
    }
}
