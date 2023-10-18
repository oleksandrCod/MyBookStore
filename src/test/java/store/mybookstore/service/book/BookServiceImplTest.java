package store.mybookstore.service.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import store.mybookstore.dto.book.BookDto;
import store.mybookstore.dto.book.CreateBookRequestDto;
import store.mybookstore.dto.book.records.BookSearchParameters;
import store.mybookstore.exception.EntityNotFoundException;
import store.mybookstore.mapper.BookMapper;
import store.mybookstore.model.Book;
import store.mybookstore.model.Category;
import store.mybookstore.repository.BookRepository;
import store.mybookstore.repository.bookspecifications.BookSpecificationBuilder;
import store.mybookstore.service.book.impl.BookServiceImpl;

@ExtendWith(MockitoExtension.class)
@Accessors(chain = true)
class BookServiceImplTest {
    private static Long id = 1L;
    private static Book book;
    private static Category category;
    private static CreateBookRequestDto createBookRequestDto;
    private static BookDto bookDto;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeAll
    static void beforeAll() {
        category = getDefaultCategory();

        book = getDefaultBook(category);

        createBookRequestDto = getCreateBookRequestDto(book);

        bookDto = getDefaultBookDto(book);
    }

    @Test
    @DisplayName("Verify createBook() method works.")
    void save_ValidCreateBookRequestDto_ReturnBookDto() {
        when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actual = bookService.createBook(createBookRequestDto);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(actual, bookDto);
    }

    @Test
    @DisplayName("Verify getAll() method works.")
    void getAll_ReturnsListWithOneBookDto() {
        Pageable pageable = Pageable.unpaged();
        Page<Book> page = new PageImpl<>(List.of(book));

        when(bookMapper.toDto(book)).thenReturn(bookDto);
        when(bookRepository.findAll(pageable)).thenReturn(page);
        List<BookDto> actual = bookService.getAll(pageable);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(bookDto.getTitle(), actual.get(0).getTitle());
    }

    @Test
    @DisplayName("Given valid id, retrieve BookDto.")
    void getBookById_ValidId_ReturnBookDto() {
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        when(bookRepository.findById(id)).thenReturn(Optional.ofNullable(book));

        BookDto actual = bookService.getBookById(id);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(bookDto, actual);

    }

    @Test
    @DisplayName("Given invalid id, retrieve EntityNotFoundException exception.")
    void getBookById_InvalidId_ThrowEntityNotFoundException() {
        Long invalidId = -10L;
        String expectedMessage = "Can't get book with id:" + invalidId;

        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.getBookById(invalidId));
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Verify updateById() method works.")
    void updateById_ValidRequest_ReturnBookDto() {
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actual = bookService.updateById(id, createBookRequestDto);
        assertEquals(actual.getId(), id);
        EqualsBuilder.reflectionEquals(createBookRequestDto, actual, "id", "categoryId");
    }

    @Test
    @DisplayName("Given invalid id, retrieve EntityNotFoundException.")
    void updateById_InvalidId_ThrowEntityNotFoundException() {
        Long invalidId = -1L;
        String expectedMessage = "Can't update book by id:" + invalidId;
        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.updateById(invalidId, createBookRequestDto));
        assertEquals(exception.getMessage(), expectedMessage);
    }

    @Test
    void deleteById_AnyId_CallOnce() {
        doNothing().when(bookRepository).deleteById(anyLong());
        bookService.deleteById(anyLong());
        verify(bookRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Given valid search parameters, retrieve List of bookDto.")
    void search_SearchWithParameters_ReturnValidListOfBookDto() {
        Specification<Book> specification = null;
        BookSearchParameters bookSearchParameters = new BookSearchParameters(
                new String[]{"Author"}, new String[]{});
        when(bookSpecificationBuilder.build(bookSearchParameters)).thenReturn(specification);
        when(bookRepository.findAll(specification)).thenReturn(List.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        List<BookDto> actual = bookService.search(bookSearchParameters);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(actual.get(0), bookDto);

    }

    private static Category getDefaultCategory() {
        return new Category()
                .setId(id)
                .setName("test_category");
    }

    private static Book getDefaultBook(Category category) {
        return new Book()
                .setId(id)
                .setAuthor("Author A")
                .setTitle("Title A")
                .setDescription("Description A")
                .setPrice(BigDecimal.valueOf(10.9))
                .setIsbn("10001000")
                .setCoverImage("https://example.com/default-cover-image.jpg")
                .setCategories(Set.of(category));
    }

    private static CreateBookRequestDto getCreateBookRequestDto(Book book) {
        return new CreateBookRequestDto()
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice())
                .setTitle(book.getTitle())
                .setCoverImage(book.getCoverImage())
                .setCategoryId(Set.of(id));
    }

    private static BookDto getDefaultBookDto(Book book) {
        return new BookDto()
                .setId(id)
                .setAuthor(book.getAuthor())
                .setTitle(book.getTitle())
                .setDescription(book.getDescription())
                .setPrice(book.getPrice())
                .setIsbn(book.getIsbn())
                .setCoverImage(book.getCoverImage())
                .setCategoryId(Set.of(id));
    }
}
