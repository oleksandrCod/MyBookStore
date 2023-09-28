package store.mybookstore.service;

import java.util.List;
import store.mybookstore.dto.BookDto;
import store.mybookstore.dto.CreateBookRequestDto;
import store.mybookstore.dto.records.BookSearchParameters;

public interface BookService {
    BookDto createBook(CreateBookRequestDto requestDto);

    List<BookDto> getAll();

    BookDto getBookById(Long id);

    void deleteById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto requestDto);

    List<BookDto> search(BookSearchParameters parameters);
}
