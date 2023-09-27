package store.mybookstore.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.mybookstore.dto.BookDto;
import store.mybookstore.dto.CreateBookRequestDto;
import store.mybookstore.exception.EntityNotFoundException;
import store.mybookstore.mapper.BookMapper;
import store.mybookstore.model.Book;
import store.mybookstore.repository.BookRepository;
import store.mybookstore.service.BookService;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto createBook(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.createBook(book));
    }

    @Override
    public List<BookDto> getAll() {
        return bookRepository
                .getAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository
                .getBookById(id).orElseThrow(
                        () -> new EntityNotFoundException("Can't get book with id:" + id));
        return bookMapper.toDto(book);
    }
}
