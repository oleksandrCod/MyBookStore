package store.mybookstore.service.shoppingcart.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.mybookstore.dto.cartitem.CartItemRequestDto;
import store.mybookstore.dto.cartitem.CartItemResponseDto;
import store.mybookstore.dto.shoppingcart.ShoppingCartResponseDto;
import store.mybookstore.model.ShoppingCart;
import store.mybookstore.model.User;
import store.mybookstore.repository.ShoppingCartRepository;
import store.mybookstore.service.cartitem.CartItemService;
import store.mybookstore.service.shoppingcart.ShoppingCartService;
import store.mybookstore.service.user.UserService;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemService cartItemService;
    private final ShoppingCartManager shoppingCartManager;

    @Override
    public ShoppingCartResponseDto getShoppingCart() {
        User loggedInUser = userService.getLoggedInUser();
        ShoppingCart shoppingCart = shoppingCartRepository
                .findShoppingCartByUserId(loggedInUser.getId())
                .orElseGet(() -> shoppingCartManager.registerNewShoppingCar(loggedInUser));
        ShoppingCartResponseDto shoppingCartResponseDto = new ShoppingCartResponseDto();
        shoppingCartResponseDto.setId(shoppingCart.getId());
        shoppingCartResponseDto.setUserId(loggedInUser.getId());
        shoppingCartResponseDto.setCartItems(cartItemService
                .findItemsByCartId(shoppingCart.getId()));
        return shoppingCartResponseDto;
    }

    @Override
    public CartItemResponseDto addBookToCart(CartItemRequestDto requestDto) {
        return cartItemService.save(requestDto);
    }
}
