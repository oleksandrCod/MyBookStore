package store.mybookstore.service.cartitem.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.mybookstore.dto.cartitem.CartItemQuantityRequestDto;
import store.mybookstore.dto.cartitem.CartItemRequestDto;
import store.mybookstore.dto.cartitem.CartItemResponseDto;
import store.mybookstore.mapper.CartItemMapper;
import store.mybookstore.model.CartItem;
import store.mybookstore.model.ShoppingCart;
import store.mybookstore.model.User;
import store.mybookstore.repository.BookRepository;
import store.mybookstore.repository.CartItemRepository;
import store.mybookstore.repository.ShoppingCartRepository;
import store.mybookstore.service.cartitem.CartItemService;
import store.mybookstore.service.shoppingcart.ShoppingCartService;
import store.mybookstore.service.user.UserService;

@RequiredArgsConstructor
@Service
public class CartItemServiceImpl implements CartItemService {
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartService shoppingCartService;
    private final CartItemMapper cartItemMapper;

    @Override
    public CartItemResponseDto createCartItem(CartItemRequestDto cartItemRequestDto) {
        CartItem cartItem = new CartItem();
        User loggedInUser = userService.getLoginedUser();
        Long loggedInId = loggedInUser.getId();
        cartItem.setBook(bookRepository
                .findById(cartItemRequestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find book by id:" + cartItemRequestDto.getBookId())));
        cartItem.setQuantity(cartItemRequestDto.getQuantity());
        cartItem.setShoppingCart(shoppingCartRepository
                .getByUserId(loggedInId)
                .orElseGet(() -> shoppingCartService.registerNewShoppingCart(loggedInUser)));
        putItemsToUserShoppingCart(cartItem, loggedInUser);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    private void putItemsToUserShoppingCart(CartItem cartItem, User loggedInUser) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .getByUserId(loggedInUser.getId())
                .orElseGet(() -> shoppingCartService.registerNewShoppingCart(loggedInUser));
        if (shoppingCart.getItems().isEmpty()) {
            shoppingCart.setItems(List.of(cartItem));
        } else {
            shoppingCart.getItems().add(cartItem);
        }
    }

    @Override
    public CartItemResponseDto update(Long cartItemId, CartItemQuantityRequestDto requestDto) {
        CartItem cartItem = cartItemRepository
                .findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find cart item by id:" + cartItemId));
        cartItem.setQuantity(requestDto.getQuantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void remove(Long cartItemId) {
        CartItem cartItem = cartItemRepository
                .findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find cart item by id:" + cartItemId));
        cartItemRepository.delete(cartItem);
    }
}
