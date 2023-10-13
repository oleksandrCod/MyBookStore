package store.mybookstore.dto.cartitem;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CartItemUpdateQuantityRequestDto {
    @NotBlank
    private int quantity;
}
