package store.mybookstore.service.book.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import store.mybookstore.dto.book.BookDto;
import store.mybookstore.dto.book.CreateBookRequestDto;
import store.mybookstore.dto.book.records.BookSearchParameters;
import store.mybookstore.exception.EntityNotFoundException;
import store.mybookstore.mapper.BookMapper;
import store.mybookstore.model.Book;
import store.mybookstore.repository.BookRepository;
import store.mybookstore.repository.bookspecifications.BookSpecificationBuilder;
import store.mybookstore.service.book.BookService;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto createBook(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> getAll() {
        return bookRepository
                .findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository
                .findById(id).orElseThrow(
                        () -> new EntityNotFoundException("Can't get book with id:" + id));
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateById(Long id, CreateBookRequestDto requestDto) {
        Book bookById = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't update book by id:" + id));
        bookById.setTitle(requestDto.getTitle());
        bookById.setAuthor(requestDto.getAuthor());
        bookById.setPrice(requestDto.getPrice());
        bookById.setIsbn(requestDto.getIsbn());
        bookById.setDescription(requestDto.getDescription());
        bookById.setCoverImage(requestDto.getCoverImage());
        return bookMapper.toDto(bookRepository.save(bookById));
    }

    @Override
    public List<BookDto> search(BookSearchParameters parameters) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(parameters);
        return bookRepository
                .findAll(bookSpecification)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
