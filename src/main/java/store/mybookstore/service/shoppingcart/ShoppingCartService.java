package store.mybookstore.service.shoppingcart;

import store.mybookstore.dto.cartitem.CartItemRequestDto;
import store.mybookstore.dto.cartitem.CartItemResponseDto;
import store.mybookstore.dto.shppingcart.ShoppingCartResponseDto;
import store.mybookstore.model.ShoppingCart;
import store.mybookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCart();

    ShoppingCart registerNewShoppingCart(User user);

    CartItemResponseDto save(CartItemRequestDto requestDto);
}
