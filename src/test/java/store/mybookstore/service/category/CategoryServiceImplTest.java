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
        category = new Category();
        category.setId(id);
        category.setName("Category A");
        category.setDescription("Description A");

        requestDto = new CategoryCreateRequestDto();
        requestDto.setName(category.getName());
        requestDto.setDescription(category.getDescription());

        categoryDto = new CategoryDto();
        categoryDto.setId(id);
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());

        book = new Book();
        book.setId(id);
        book.setAuthor("Author A");
        book.setTitle("Title A");
        book.setDescription("Description A");
        book.setPrice(BigDecimal.valueOf(10));
        book.setIsbn("10001000");
        book.setCoverImage("https://example.com/default-cover-image.jpg");
        book.setCategories(Set.of(category));

        withoutCategoryIds = new BookDtoWithoutCategoryIds();
        withoutCategoryIds.setId(id);
        withoutCategoryIds.setAuthor(book.getAuthor());
        withoutCategoryIds.setTitle(book.getTitle());
        withoutCategoryIds.setDescription(book.getDescription());
        withoutCategoryIds.setPrice(book.getPrice());
        withoutCategoryIds.setIsbn(book.getIsbn());
        withoutCategoryIds.setCoverImage(book.getCoverImage());
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
}
