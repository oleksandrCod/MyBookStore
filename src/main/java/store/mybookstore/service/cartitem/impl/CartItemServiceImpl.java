package store.mybookstore.service.cartitem.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.mybookstore.dto.cartitem.CartItemRequestDto;
import store.mybookstore.dto.cartitem.CartItemResponseDto;
import store.mybookstore.dto.cartitem.CartItemUpdateQuantityRequestDto;
import store.mybookstore.exception.EntityNotFoundException;
import store.mybookstore.mapper.CartItemMapper;
import store.mybookstore.model.CartItem;
import store.mybookstore.model.ShoppingCart;
import store.mybookstore.model.User;
import store.mybookstore.repository.BookRepository;
import store.mybookstore.repository.CartItemRepository;
import store.mybookstore.repository.ShoppingCartRepository;
import store.mybookstore.service.cartitem.CartItemService;
import store.mybookstore.service.shoppingcart.impl.ShoppingCartManager;
import store.mybookstore.service.user.UserService;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartManager shoppingCartManager;

    @Override
    public Set<CartItemResponseDto> findItemsByCartId(Long id) {
        return cartItemRepository
                .findAllByShoppingCartId(id)
                .stream()
                .map(cartItemMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public CartItemResponseDto save(CartItemRequestDto requestDto) {
        CartItem cartItem = new CartItem();
        User user = userService.getLoggedInUser();
        cartItem.setBook(bookRepository.findById(requestDto.getBookId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find book by id")));
        cartItem.setQuantity(requestDto.getQuantity());
        setShoppingCart(user, cartItem);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemResponseDto update(CartItemUpdateQuantityRequestDto updateDto, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find item by id"));
        cartItem.setQuantity(updateDto.getQuantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void delete(Long cartItemId) {
        cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find item by id:" + cartItemId));
        cartItemRepository.deleteById(cartItemId);
    }

    private void setShoppingCart(User user, CartItem cartItem) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .findShoppingCartByUserId(user.getId()).orElseGet(
                        () -> shoppingCartManager.registerNewShoppingCar(user));
        cartItem.setShoppingCart(shoppingCart);
        if (shoppingCart.getCartItems().isEmpty()) {
            shoppingCart.setCartItems(List.of(cartItem));
        } else {
            shoppingCart.getCartItems().add(cartItem);
        }
    }
}
