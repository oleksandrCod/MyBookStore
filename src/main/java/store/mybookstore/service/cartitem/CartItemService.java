package store.mybookstore.service.cartitem;

import store.mybookstore.dto.cartitem.CartItemQuantityRequestDto;
import store.mybookstore.dto.cartitem.CartItemRequestDto;
import store.mybookstore.dto.cartitem.CartItemResponseDto;

public interface CartItemService {
    CartItemResponseDto createCartItem(CartItemRequestDto cartItemRequestDto);

    CartItemResponseDto update(Long cartItemId, CartItemQuantityRequestDto requestDto);

    void remove(Long cartIteId);
}
