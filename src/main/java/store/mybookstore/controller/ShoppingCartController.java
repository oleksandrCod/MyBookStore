package store.mybookstore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybookstore.dto.cartitem.CartItemQuantityRequestDto;
import store.mybookstore.dto.cartitem.CartItemRequestDto;
import store.mybookstore.dto.cartitem.CartItemResponseDto;
import store.mybookstore.dto.shppingcart.ShoppingCartResponseDto;
import store.mybookstore.service.cartitem.CartItemService;
import store.mybookstore.service.shoppingcart.ShoppingCartService;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;

    @GetMapping
    ShoppingCartResponseDto getShoppingByLoggedInUser() {
        return shoppingCartService.getShoppingCart();
    }

    @PostMapping
    CartItemResponseDto addBookToTheShoppingCart(
            @RequestBody @Valid CartItemRequestDto requestDto) {
        return shoppingCartService.save(requestDto);
    }

    @PutMapping("cart-items{cartItemId}")
    CartItemResponseDto updateQuantityOfBooks(@RequestBody CartItemQuantityRequestDto requestDto,
                                              @PathVariable Long cartItemId) {
        return cartItemService.update(cartItemId, requestDto);
    }

    @DeleteMapping("/{cartItemId}")
    public void delete(@PathVariable Long cartItemId) {
        cartItemService.remove(cartItemId);
    }
}
