package store.mybookstore.service.order;

import java.util.List;
import org.springframework.data.domain.Pageable;
import store.mybookstore.dto.order.OrderRequestDto;
import store.mybookstore.dto.order.OrderResponseDto;
import store.mybookstore.dto.order.OrderUpdateStatusRequestDto;
import store.mybookstore.dto.orderitem.OrderItemResponseDto;

public interface OrderService {

    OrderResponseDto openOrder(OrderRequestDto requestDto);

    List<OrderResponseDto> getOrdersHistory(Pageable pageable);

    OrderResponseDto update(Long id, OrderUpdateStatusRequestDto requestDto);

    OrderItemResponseDto getOrderItemById(Long id);
}
