package store.mybookstore.service.category;

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
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import store.mybookstore.dto.book.BookDtoWithoutCategoryIds;
import store.mybookstore.dto.category.CategoryCreateRequestDto;
import store.mybookstore.dto.category.CategoryDto;
import store.mybookstore.exception.EntityNotFoundException;
import store.mybookstore.mapper.BookMapper;
import store.mybookstore.mapper.CategoryMapper;
import store.mybookstore.model.Book;
import store.mybookstore.model.Category;
import store.mybookstore.repository.BookRepository;
import store.mybookstore.repository.CategoryRepository;
import store.mybookstore.service.category.impl.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    private static Long id = 1L;
    private static Category category;
    private static CategoryCreateRequestDto requestDto;
    private static CategoryDto categoryDto;
    private static Book book;
    private static BookDtoWithoutCategoryIds withoutCategoryIds;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeAll
    static void beforeAll() {
        category = getDefaultCategory();

        requestDto = getDefaultCategoryCreateRequestDto(category);

        categoryDto = getDefaultCategoryDto(category);

        book = getDefaultBook(category);

        withoutCategoryIds = getDefaultBookDtoWithoutCategoryIds(book);

    }

    @Test
    @DisplayName("Verify save() method.")
    void save_WithValidRequest_ReturnCategoryDto() {
        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.save(requestDto);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(actual, categoryDto);
    }

    @Test
    @DisplayName("Verify findAll() method.")
    void findAll_ReturnListWithOneCategoryDto() {
        Pageable pageable = Pageable.unpaged();
        Page<Category> page = new PageImpl<>(List.of(category));

        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        when(categoryRepository.findAll(pageable)).thenReturn(page);
        List<CategoryDto> actual = categoryService.findAll(pageable);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(categoryDto.getName(), actual.get(0).getName());
    }

    @Test
    @DisplayName("Verify getById() method.")
    void getById_WithValidId_ReturnCategoryDto() {
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.getById(id);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(actual, categoryDto);
    }

    @Test
    @DisplayName("Verify exception throwing with invalid id.")
    void getById_InvalidId_ThrowEntityNotFoundException() {
        Long invalidId = -1L;
        String expectedMessage = "Can't find category with input id:" + invalidId;
        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(invalidId));

        assertEquals(exception.getMessage(), expectedMessage);
    }

    @Test
    @DisplayName("Verify update() method.")
    void update_ValidRequest_ReturnCategoryDto() {
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actual = categoryService.update(id, requestDto);

        EqualsBuilder.reflectionEquals(actual, categoryDto);
    }

    @Test
    @DisplayName("Verify deleteById() method.")
    void deleteById_AnyId_CallOnce() {
        doNothing().when(categoryRepository).deleteById(anyLong());

        categoryService.deleteById(anyLong());

        verify(categoryRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Verify getBooksByCategoryId() method.")
    void getBooksByCategoryId_WithValidId_ReturnListOfBookDto() {
        Pageable pageable = Pageable.unpaged();

        when(bookRepository.findBooksByCategoriesId(id)).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategoryIds(book)).thenReturn(withoutCategoryIds);

        List<BookDtoWithoutCategoryIds> actual = categoryService.getBooksByCategoryId(pageable, id);

        assertEquals(actual.get(0), withoutCategoryIds);
    }

    private static BookDtoWithoutCategoryIds getDefaultBookDtoWithoutCategoryIds(Book book) {
        return new BookDtoWithoutCategoryIds()
                .setId(id)
                .setAuthor(book.getAuthor())
                .setTitle(book.getTitle())
                .setDescription(book.getDescription())
                .setPrice(book.getPrice())
                .setIsbn(book.getIsbn())
                .setCoverImage(book.getCoverImage());
    }

    private static Book getDefaultBook(Category category) {
        return new Book()
                .setId(id)
                .setAuthor("Author A")
                .setTitle("Title A")
                .setDescription("Description A")
                .setPrice(BigDecimal.valueOf(10))
                .setIsbn("10001000")
                .setCoverImage("https://example.com/default-cover-image.jpg")
                .setCategories(Set.of(category));
    }

    private static CategoryDto getDefaultCategoryDto(Category category) {
        return new CategoryDto()
                .setId(id)
                .setName(category.getName())
                .setDescription(category.getDescription());

    }

    private static CategoryCreateRequestDto getDefaultCategoryCreateRequestDto(Category category) {
        return new CategoryCreateRequestDto()
                .setName(category.getName())
                .setDescription(category.getDescription());
    }

    private static Category getDefaultCategory() {
        return new Category()
                .setId(id)
                .setName("Category A")
                .setDescription("Description A");
    }
}
