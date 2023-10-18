package store.mybookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import store.mybookstore.dto.book.BookDtoWithoutCategoryIds;
import store.mybookstore.dto.category.CategoryCreateRequestDto;
import store.mybookstore.dto.category.CategoryDto;
import store.mybookstore.model.Category;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    private static Category category;
    private static CategoryCreateRequestDto requestDto;
    private static CategoryDto categoryDto;
    private static Long id = 1L;
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        category = new Category();
        category.setId(id);
        category.setName("test_category");
        category.setDescription("simple");

        requestDto = new CategoryCreateRequestDto();
        requestDto.setName(category.getName());
        requestDto.setDescription("simple");

        categoryDto = new CategoryDto();
        categoryDto.setId(id);
        categoryDto.setName("test_category");
        categoryDto.setDescription("simple");

        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify createCategory() method.")
    @Sql(scripts = {"classpath:database/fill-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/delete-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_ValidRequest_ReturnCategoryDto() throws Exception {
        String request = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        assertEquals(categoryDto.getName(), actual.getName());
    }

    @Test
    @WithMockUser
    @DisplayName("Verify getAll() method. Return 3 categories.")
    @Sql(scripts = {"classpath:database/fill-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/delete-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_ExpectedThreeCategoryDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto[] actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryDto[].class);

        assertNotNull(actual);
        assertEquals(3, actual.length);
        assertEquals("a", actual[0].getName());
        assertEquals("b", actual[1].getName());
        assertEquals("c", actual[2].getName());
    }

    @Test
    @WithMockUser
    @DisplayName("Verify getCategoryById() method.")
    @Sql(scripts = {"classpath:database/fill-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/delete-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCategoryById_ValidId_ReturnCategoryDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(actual, categoryDto);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify updateCategoryById() method.")
    @Sql(scripts = {"classpath:database/fill-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/delete-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategoryById_ValidId_ReturnCategoryDto() throws Exception {
        String request = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(put("/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(actual, categoryDto);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Verify deleteCategoryById() method.")
    @Sql(scripts = {"classpath:database/fill-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/delete-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCategoryById_ValidId_CallOnes() throws Exception {
        MvcResult result = mockMvc.perform(delete("/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @WithMockUser
    @DisplayName("Verify getBooksByCategoryId() method.")
    @Sql(scripts = {"classpath:database/fill-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/delete-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBooksByCategoryId_ValidId_ReturnThreeBookDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/{id}/books", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDtoWithoutCategoryIds[] actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDtoWithoutCategoryIds[].class);
        assertNotNull(actual);
        assertEquals(2, actual.length);
        assertEquals("Author A", actual[0].getAuthor());
    }
}
