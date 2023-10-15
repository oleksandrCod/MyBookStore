package store.mybookstore.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import store.mybookstore.dto.orderitem.OrderItemResponseDto;

@Data
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private Set<OrderItemResponseDto> orderItems;
    private LocalDateTime orderTime;
    private BigDecimal totalPrice;
    private String status;
}
