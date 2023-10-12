package store.mybookstore.service.cartitem;

import java.util.Set;
import store.mybookstore.dto.cartitem.CartItemRequestDto;
import store.mybookstore.dto.cartitem.CartItemResponseDto;
import store.mybookstore.dto.cartitem.CartItemUpdateQuantityRequestDto;

public interface CartItemService {
    Set<CartItemResponseDto> findItemsByCartId(Long id);

    CartItemResponseDto save(CartItemRequestDto requestDto);

    CartItemResponseDto update(CartItemUpdateQuantityRequestDto updateDto, Long cartItemId);

    void delete(Long cartItemId);
}
