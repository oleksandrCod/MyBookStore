package store.mybookstore.dto.cartitem;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CartItemUpdateQuantityRequestDto {
    @NotBlank
    private int quantity;
}
