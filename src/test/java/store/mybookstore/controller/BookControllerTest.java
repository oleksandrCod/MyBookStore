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
        category = new Category();
        category.setId(id);
        category.setName("test_category");

        book = new Book();
        book.setId(id);
        book.setAuthor("Author A");
        book.setTitle("Title A");
        book.setDescription("Description A");
        book.setPrice(BigDecimal.valueOf(10.9));
        book.setIsbn("10001000");
        book.setCoverImage("https://example.com/default-cover-image.jpg");
        book.setCategories(Set.of(category));

        createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setAuthor(book.getAuthor());
        createBookRequestDto.setIsbn(book.getIsbn());
        createBookRequestDto.setPrice(book.getPrice());
        createBookRequestDto.setTitle(book.getTitle());
        createBookRequestDto.setCoverImage(book.getCoverImage());
        createBookRequestDto.setCategoryId(Set.of(id));

        bookDto = new BookDto();
        bookDto.setId(id);
        bookDto.setAuthor(book.getAuthor());
        bookDto.setTitle(book.getTitle());
        bookDto.setDescription(book.getDescription());
        bookDto.setPrice(book.getPrice());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setCategoryId(Set.of(id));

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
}
