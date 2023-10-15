package store.mybookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybookstore.dto.order.OrderRequestDto;
import store.mybookstore.dto.order.OrderResponseDto;
import store.mybookstore.dto.order.OrderUpdateStatusRequestDto;
import store.mybookstore.dto.orderitem.OrderItemResponseDto;
import store.mybookstore.service.order.OrderService;

@Tag(name = "Order management", description = "Allows the user to manage endpoints of order.")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Open new order",
            description = "Allows the user to open new order from his cart.")
    public OrderResponseDto openOrder(@RequestBody OrderRequestDto requestDto) {
        return orderService.openOrder(requestDto);
    }

    @GetMapping
    @Operation(summary = "Get order history",
            description = "Allows the user to get full order history.")
    public List<OrderResponseDto> getOrderHistory(Pageable pageable) {
        return orderService.getOrdersHistory(pageable);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole(ROLE_ADMIN)")
    @Operation(summary = "Update order status",
            description = "Allows the user with role admin to update order status")
    public OrderResponseDto updateOrderStatus(@PathVariable Long id,
                                              OrderUpdateStatusRequestDto requestDto) {
        return orderService.update(id, requestDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get specific order item",
            description = "Allows the user to get a specific order item.")
    public OrderItemResponseDto getOrderItem(@PathVariable Long id) {
        return orderService.getOrderItemById(id);
    }
}
