package store.mybookstore.service.order.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import store.mybookstore.dto.order.OrderRequestDto;
import store.mybookstore.dto.order.OrderResponseDto;
import store.mybookstore.dto.order.OrderUpdateStatusRequestDto;
import store.mybookstore.dto.orderitem.OrderItemResponseDto;
import store.mybookstore.exception.EntityNotFoundException;
import store.mybookstore.mapper.OrderItemMapper;
import store.mybookstore.mapper.OrderMapper;
import store.mybookstore.model.Order;
import store.mybookstore.model.OrderItem;
import store.mybookstore.model.ShoppingCart;
import store.mybookstore.model.User;
import store.mybookstore.repository.OrderItemRepository;
import store.mybookstore.repository.OrderRepository;
import store.mybookstore.repository.ShoppingCartRepository;
import store.mybookstore.service.order.OrderService;
import store.mybookstore.service.shoppingcart.impl.ShoppingCartManager;
import store.mybookstore.service.user.UserService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserService userService;
    private final ShoppingCartManager shoppingCartManager;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderResponseDto openOrder(OrderRequestDto requestDto) {
        Order order = new Order();
        User loggedInUser = userService.getLoggedInUser();
        ShoppingCart shoppingCart = shoppingCartRepository
                .findShoppingCartByUserId(loggedInUser.getId())
                .orElseGet(() -> shoppingCartManager.registerNewShoppingCar(loggedInUser));
        Set<OrderItem> orderItems = mapCartItemsToOrderItems(shoppingCart, order);
        order.setUser(loggedInUser);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.PENDING);
        order.setShippingAddress(loggedInUser.getShippingAddress());
        order.setOrderItems(orderItems);
        BigDecimal totalPrice = orderItems
                .stream()
                .map(orderItem -> orderItem
                        .getBook().getPrice().multiply(
                                new BigDecimal(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(totalPrice);
        Order save = orderRepository.save(order);
        OrderResponseDto dto = orderMapper.toDto(save);
        dto.setOrderItems(orderItems.stream()
                .map(orderItemMapper::toDto).collect(Collectors.toSet()));
        return dto;
    }

    @Override
    public List<OrderResponseDto> getOrdersHistory(Pageable pageable) {
        User loggedInUser = userService.getLoggedInUser();
        List<Order> allByUserId = orderRepository.findAllByUserId(loggedInUser.getId());
        return allByUserId
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDto update(Long id, OrderUpdateStatusRequestDto requestDto) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id"));
        order.setStatus(requestDto.getStatus());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderItemResponseDto getOrderItemById(Long id) {
        OrderItem orderItem = orderItemRepository.findByOrderId(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find order item by order id."));
        return orderItemMapper.toDto(orderItem);
    }

    private Set<OrderItem> mapCartItemsToOrderItems(ShoppingCart shoppingCart, Order order) {
        return shoppingCart.getCartItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setOrder(order);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice().multiply(
                    new BigDecimal(cartItem.getQuantity())));
            return orderItem;
        })
                .collect(Collectors.toSet());
    }
}
