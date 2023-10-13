package store.mybookstore.service;

import java.util.List;
import store.mybookstore.dto.BookDto;
import store.mybookstore.dto.CreateBookRequestDto;

public interface BookService {
    BookDto createBook(CreateBookRequestDto requestDto);

    List<BookDto> getAll();

    BookDto getBookById(Long id);
}
