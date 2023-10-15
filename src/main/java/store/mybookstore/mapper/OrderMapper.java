package store.mybookstore.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import store.mybookstore.config.MapperConfig;
import store.mybookstore.dto.order.OrderResponseDto;
import store.mybookstore.dto.orderitem.OrderItemResponseDto;
import store.mybookstore.model.Order;
import store.mybookstore.model.OrderItem;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    OrderResponseDto toDto(Order order);

    @Mappings({@Mapping(target = "bookId", source = "book.id")})
    OrderItemResponseDto toDto(OrderItem orderItem);

    @AfterMapping
    default void mapResponse(@MappingTarget OrderResponseDto responseDto, Order order) {
        responseDto.setOrderTime(order.getOrderDate());
        responseDto.setStatus(order.getStatus().name());
        responseDto.setUserId(order.getUser().getId());
        responseDto.setTotalPrice(order.getTotal());
    }
}
