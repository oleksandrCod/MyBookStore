package store.mybookstore.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import store.mybookstore.config.MapperConfig;
import store.mybookstore.dto.cartitem.CartItemResponseDto;
import store.mybookstore.model.CartItem;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    CartItemResponseDto toDto(CartItem cartItem);

    @AfterMapping
    default void setBookId(@MappingTarget CartItemResponseDto cartItemResponseDto,
                           CartItem cartItem) {
        cartItemResponseDto.setBookId(cartItem.getBook().getId());
    }

    @AfterMapping
    default void setBookTitle(@MappingTarget CartItemResponseDto cartItemResponseDto,
                              CartItem cartItem) {
        cartItemResponseDto.setBookTitle(cartItem.getBook().getTitle());
    }
}
