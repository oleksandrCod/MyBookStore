package store.mybookstore.dto.shppingcart;

import java.util.List;
import lombok.Data;
import store.mybookstore.dto.cartitem.CartItemResponseDto;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private List<CartItemResponseDto> cartItems;
}
