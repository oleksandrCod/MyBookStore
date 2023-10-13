package store.mybookstore.service.book;

import java.util.List;
import org.springframework.data.domain.Pageable;
import store.mybookstore.dto.book.BookDto;
import store.mybookstore.dto.book.CreateBookRequestDto;
import store.mybookstore.dto.book.records.BookSearchParameters;

public interface BookService {
    BookDto createBook(CreateBookRequestDto requestDto);

    List<BookDto> getAll(Pageable pageable);

    BookDto getBookById(Long id);

    void deleteById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto requestDto);

    List<BookDto> search(BookSearchParameters parameters);
}
