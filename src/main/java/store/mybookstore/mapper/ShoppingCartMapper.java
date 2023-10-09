package store.mybookstore.mapper;

import org.mapstruct.Mapper;
import store.mybookstore.config.MapperConfig;
import store.mybookstore.dto.shppingcart.ShoppingCartResponseDto;
import store.mybookstore.model.ShoppingCart;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);
}
