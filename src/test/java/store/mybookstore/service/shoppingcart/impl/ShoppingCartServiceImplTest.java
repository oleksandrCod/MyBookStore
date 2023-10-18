package store.mybookstore.service.shoppingcart.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import store.mybookstore.dto.cartitem.CartItemRequestDto;
import store.mybookstore.dto.cartitem.CartItemResponseDto;
import store.mybookstore.dto.shoppingcart.ShoppingCartResponseDto;
import store.mybookstore.model.Book;
import store.mybookstore.model.CartItem;
import store.mybookstore.model.Category;
import store.mybookstore.model.ShoppingCart;
import store.mybookstore.model.User;
import store.mybookstore.repository.ShoppingCartRepository;
import store.mybookstore.service.cartitem.CartItemService;
import store.mybookstore.service.user.UserService;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {
    private static Long id;
    private static User user;
    private static ShoppingCart shoppingCart;
    private static ShoppingCartResponseDto shoppingCartResponseDto;
    private static CartItem cartItem;
    private static CartItemResponseDto cartItemResponseDto;
    private static CartItemRequestDto cartItemRequestDto;
    private static Book book;
    private static Category category;
    @Mock
    private UserService userService;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CartItemService cartItemService;
    @Mock
    private ShoppingCartManager shoppingCartManager;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @BeforeAll
    static void beforeAll() {
        user = new User();
        user.setRoles(Set.of());
        user.setId(id);
        user.setFirstName("Bob");
        user.setLastName("Bober");
        user.setEmail("bob@example.com");
        user.setPassword("12345678");
        user.setShippingAddress("Vilna 7");

        category = new Category();
        category.setId(id);
        category.setName("test_category");

        book = new Book();
        book.setId(id);
        book.setAuthor("Author A");
        book.setTitle("Title A");
        book.setDescription("Description A");
        book.setPrice(BigDecimal.valueOf(10));
        book.setIsbn("10001000");
        book.setCoverImage("https://example.com/default-cover-image.jpg");
        book.setCategories(Set.of(category));

        cartItem = new CartItem();
        cartItem.setQuantity(1);
        cartItem.setId(id);
        cartItem.setBook(book);

        shoppingCart = new ShoppingCart();
        shoppingCart.setId(id);
        shoppingCart.setUser(user);

        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.setCartItems(List.of(cartItem));

        cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setQuantity(1);
        cartItemRequestDto.setBookId(id);

        cartItemResponseDto = new CartItemResponseDto();
        cartItemResponseDto.setId(id);
        cartItemResponseDto.setQuantity(1);
        cartItemResponseDto.setBookId(id);
        cartItemResponseDto.setBookTitle(book.getTitle());

        shoppingCartResponseDto = new ShoppingCartResponseDto();
        shoppingCartResponseDto.setUserId(id);
        shoppingCartResponseDto.setId(id);
        shoppingCartResponseDto.setCartItems(Set.of(cartItemResponseDto));

    }

    @Test
    @DisplayName("Verify getShoppingCart() method.")
    void getShoppingCart_ValidRequest_ReturnShoppingCartResponseDto() {
        when(userService.getLoggedInUser()).thenReturn(user);
        when(shoppingCartRepository.findShoppingCartByUserId(id))
                .thenReturn(Optional.of(shoppingCart));
        when(cartItemService.findItemsByCartId(id)).thenReturn(Set.of(cartItemResponseDto));

        ShoppingCartResponseDto actual = shoppingCartService.getShoppingCart();

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(shoppingCartResponseDto, actual);
    }

    @Test
    @DisplayName("Get shoppingCart for new user.")
    void getShoppingCart_NewUserCartExist_ReturnShoppingCartResponseDto() {
        when(userService.getLoggedInUser()).thenReturn(user);
        when(shoppingCartRepository.findShoppingCartByUserId(id)).thenReturn(Optional.empty());
        when(shoppingCartManager.registerNewShoppingCar(user)).thenReturn(shoppingCart);

        ShoppingCartResponseDto actual = shoppingCartService.getShoppingCart();

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(shoppingCartResponseDto, actual);
    }

    @Test
    @DisplayName("Verify addBookToCart() method.")
    void addBookToCart_ValidBook_ReturnCartItemResponseDto() {
        when(cartItemService.save(cartItemRequestDto)).thenReturn(cartItemResponseDto);

        CartItemResponseDto actual = shoppingCartService.addBookToCart(cartItemRequestDto);

        assertNotNull(actual);
        EqualsBuilder.reflectionEquals(cartItemRequestDto, actual);
    }
}
