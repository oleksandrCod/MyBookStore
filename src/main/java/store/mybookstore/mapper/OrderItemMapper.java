package store.mybookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import store.mybookstore.config.MapperConfig;
import store.mybookstore.dto.orderitem.OrderItemResponseDto;
import store.mybookstore.model.OrderItem;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mappings({@Mapping(target = "bookId", source = "book.id")})
    OrderItemResponseDto toDto(OrderItem orderItem);
}
