package store.mybookstore.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import store.mybookstore.model.Order;

@Data
public class OrderUpdateStatusRequestDto {
    @NotBlank
    private Order.Status status;
}
