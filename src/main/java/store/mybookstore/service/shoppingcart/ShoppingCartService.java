package store.mybookstore.service.shoppingcart;

import store.mybookstore.dto.cartitem.CartItemRequestDto;
import store.mybookstore.dto.cartitem.CartItemResponseDto;
import store.mybookstore.dto.shoppingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCart();

    CartItemResponseDto addBookToCart(CartItemRequestDto requestDto);
}
