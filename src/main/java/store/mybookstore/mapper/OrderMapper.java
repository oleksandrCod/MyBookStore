package store.mybookstore.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import store.mybookstore.config.MapperConfig;
import store.mybookstore.dto.order.OrderResponseDto;
import store.mybookstore.model.Order;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    OrderResponseDto toDto(Order order);

    @AfterMapping
    default void mapResponse(@MappingTarget OrderResponseDto responseDto, Order order) {
        responseDto.setOrderTime(order.getOrderDate());
        responseDto.setStatus(order.getStatus().name());
        responseDto.setUserId(order.getUser().getId());
        responseDto.setTotalPrice(order.getTotal());
    }
}
