package store.mybookstore.dto.shoppingcart;

import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;
import store.mybookstore.dto.cartitem.CartItemResponseDto;

@Data
@Accessors(chain = true)
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
