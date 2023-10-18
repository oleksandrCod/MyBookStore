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
import store.mybookstore.dto.cartitem.CartItemRequestDto;
import store.mybookstore.dto.cartitem.CartItemResponseDto;
import store.mybookstore.dto.cartitem.CartItemUpdateQuantityRequestDto;
import store.mybookstore.dto.shoppingcart.ShoppingCartResponseDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;
    private static Long id = 1L;
    private static CartItemRequestDto cartItemRequestDto;
    private static CartItemResponseDto cartItemResponseDto;
    private static CartItemUpdateQuantityRequestDto updateQuantityRequestDto;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setQuantity(1);
        cartItemRequestDto.setBookId(id);

        cartItemResponseDto = new CartItemResponseDto();
        cartItemResponseDto.setId(id);
        cartItemResponseDto.setQuantity(1);
        cartItemResponseDto.setBookId(id);
        cartItemResponseDto.setBookTitle("Title 1");

        updateQuantityRequestDto = new CartItemUpdateQuantityRequestDto();
        updateQuantityRequestDto.setQuantity(2);
    }

    @Test
    @WithMockUser(username = "bob@example.com")
    @DisplayName("Verify getUsersShoppingCart() method.")
    @Sql(scripts = {"classpath:database/fill-data-for-shopping-cart-entity.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/delete-data-for-shopping-cart-entity.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getUsersShoppingCart_ValidRequest_ReturnShoppingCartResponseDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartResponseDto.class);

        assertNotNull(actual);
        assertEquals(1, actual.getUserId());
    }

    @Test
    @WithMockUser(username = "bob@example.com")
    @DisplayName("Verify addBookToTheCart() method.")
    @Sql(scripts = {"classpath:database/fill-data-for-shopping-cart-entity.sql",
            "classpath:database/fill-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/delete-data-for-shopping-cart-controller.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addBookToTheCart_ValidBook_ReturnCartItemResponseDto() throws Exception {
        String request = objectMapper.writeValueAsString(cartItemRequestDto);

        MvcResult result = mockMvc.perform(post("/cart")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CartItemResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CartItemResponseDto.class);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(cartItemResponseDto, actual);
    }

    @Test
    @WithMockUser(username = "bob@example.com")
    @DisplayName("Verify update() method.")
    @Sql(scripts = {"classpath:database/fill-data-for-shopping-cart-entity.sql",
            "classpath:database/fill-data-for-book-entity.sql",
            "classpath:database/fill-data-for-shopping-cart-items.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/delete-data-for-shopping-cart-controller.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_ValidRequest_Return_CartItemResponseDto() throws Exception {
        String request = objectMapper.writeValueAsString(updateQuantityRequestDto);

        MvcResult result = mockMvc.perform(put("/cart/cart-items/{cartItemId}", 1)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CartItemResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CartItemResponseDto.class);

        assertNotNull(actual);
        assertEquals(2, actual.getQuantity());
    }

    @Test
    @WithMockUser(username = "bob@example.com")
    @DisplayName("Verify delete() method.")
    @Sql(scripts = {"classpath:database/fill-data-for-shopping-cart-entity.sql",
            "classpath:database/fill-data-for-book-entity.sql",
            "classpath:database/fill-data-for-shopping-cart-items.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/delete-data-for-shopping-cart-controller.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_ValidRequest_ResponseNoContent() throws Exception {
        MvcResult result = mockMvc.perform(delete("/cart/cart-items/{cartItemId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}
