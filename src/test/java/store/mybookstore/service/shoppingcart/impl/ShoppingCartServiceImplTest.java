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
        user = getDefaultUser();

        category = getDefaultCategory();

        book = getDefaultBook(category);

        cartItem = getDefaultCartItem(book);

        shoppingCart = getDefaultShoppingCart(user, cartItem);

        cartItem.setShoppingCart(shoppingCart);

        cartItemRequestDto = getDefaultCartItemRequestDto();

        cartItemResponseDto = getDefaultCartItemResponseDto(book);

        shoppingCartResponseDto = getDefaultShoppingCartResponseDto(cartItemResponseDto);
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

    private static ShoppingCartResponseDto getDefaultShoppingCartResponseDto(
            CartItemResponseDto cartItemResponseDto) {
        return new ShoppingCartResponseDto()
                .setUserId(id)
                .setId(id)
                .setCartItems(Set.of(cartItemResponseDto));
    }

    private static CartItemResponseDto getDefaultCartItemResponseDto(Book book) {
        return new CartItemResponseDto()
                .setId(id)
                .setQuantity(1)
                .setBookId(id)
                .setBookTitle(book.getTitle());
    }

    private static CartItemRequestDto getDefaultCartItemRequestDto() {
        return new CartItemRequestDto()
                .setQuantity(1)
                .setBookId(id);
    }

    private static ShoppingCart getDefaultShoppingCart(User user, CartItem cartItem) {
        return new ShoppingCart()
                .setId(id)
                .setUser(user)
                .setCartItems(List.of(cartItem));
    }

    private static CartItem getDefaultCartItem(Book book) {
        return new CartItem()
                .setQuantity(1)
                .setId(id)
                .setBook(book);
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

    private static Category getDefaultCategory() {
        return new Category()
                .setId(id)
                .setName("test_category");
    }

    private static User getDefaultUser() {
        return new User()
                .setRoles(Set.of())
                .setId(id)
                .setFirstName("Bob")
                .setLastName("Bober")
                .setEmail("bob@example.com")
                .setPassword("12345678")
                .setShippingAddress("Vilna 7");
    }
}
