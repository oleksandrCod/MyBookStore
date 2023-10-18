package store.mybookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import store.mybookstore.dto.book.BookDto;
import store.mybookstore.dto.book.CreateBookRequestDto;
import store.mybookstore.model.Book;
import store.mybookstore.model.Category;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;
    private static Book book;
    private static BookDto bookDto;
    private static Category category;
    private static CreateBookRequestDto createBookRequestDto;
    private static Long id = 1L;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        category = getDefaultCategory();

        book = getDefaultBook(category);

        createBookRequestDto = getCreateBookRequestDto(book);

        bookDto = getDefaultBookDto(book);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @WithMockUser
    @DisplayName("Verify getAll() method. Return 3 books.")
    @Sql(scripts = {"classpath:database/fill-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/delete-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_DefaultPageable_ReturnListOfTreeBooks() throws Exception {
        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto[] actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), BookDto[].class);

        assertEquals(3, actual.length);
        assertEquals("Book A", actual[0].getTitle());
        assertEquals("Book D", actual[1].getTitle());
        assertEquals("Book B", actual[2].getTitle());
    }

    @Test
    @WithMockUser
    @DisplayName("Verify getBookById() method.")
    @Sql(scripts = {"classpath:database/fill-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/delete-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBookById_ValidId_ReturnBookDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), BookDto.class);

        assertNotNull(actual);
        assertEquals("Book A", actual.getTitle());
    }

    @Test
    @DisplayName("Verify createBook() method.")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {"classpath:database/delete-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequestDto_Success() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);

        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    @DisplayName("Verify deleteBookById() method.")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {"classpath:database/fill-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/delete-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteBookById_ValidId_ExpectedNoContentResponse() throws Exception {
        mockMvc.perform(delete("/books/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Verify updateBookById() method.")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {"classpath:database/fill-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/delete-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateBookById_ValidId_ReturnBookDto() throws Exception {
        String request = objectMapper.writeValueAsString(createBookRequestDto);
        MvcResult result = mockMvc.perform(put("/books/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), BookDto.class);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(actual, bookDto);
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
