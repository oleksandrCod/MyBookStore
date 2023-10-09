package store.mybookstore.service.shoppingcart.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.mybookstore.dto.cartitem.CartItemRequestDto;
import store.mybookstore.dto.cartitem.CartItemResponseDto;
import store.mybookstore.dto.shppingcart.ShoppingCartResponseDto;
import store.mybookstore.mapper.CartItemMapper;
import store.mybookstore.model.CartItem;
import store.mybookstore.model.ShoppingCart;
import store.mybookstore.model.User;
import store.mybookstore.repository.CartItemRepository;
import store.mybookstore.repository.ShoppingCartRepository;
import store.mybookstore.service.shoppingcart.ShoppingCartService;
import store.mybookstore.service.user.UserService;

@RequiredArgsConstructor
@Service
public class ShoppingCartImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserService userService;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartResponseDto getShoppingCart() {
        User loginedUser = userService.getLoginedUser();
        ShoppingCart shoppingCart = shoppingCartRepository
                .getByUserId(loginedUser.getId())
                .orElseGet(() -> registerNewShoppingCart(loginedUser));
        Long shoppingCartId = shoppingCart.getId();
        ShoppingCartResponseDto shoppingCartResponseDto = new ShoppingCartResponseDto();
        shoppingCartResponseDto.setId(shoppingCartId);
        shoppingCartResponseDto.setUserId(loginedUser.getId());
        shoppingCartResponseDto.setCartItems(cartItemRepository
                .findAllByShoppingCartId(shoppingCartId));
        return shoppingCartResponseDto;
    }

    @Override
    public ShoppingCart registerNewShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public CartItemResponseDto save(CartItemRequestDto requestDto) {
        CartItem model = cartItemMapper.toModel(requestDto);
        return cartItemMapper.toDto(cartItemRepository.save(model));
    }
}
