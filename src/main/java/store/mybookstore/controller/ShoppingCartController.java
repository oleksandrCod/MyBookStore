package store.mybookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import store.mybookstore.dto.cartitem.CartItemRequestDto;
import store.mybookstore.dto.cartitem.CartItemResponseDto;
import store.mybookstore.dto.cartitem.CartItemUpdateQuantityRequestDto;
import store.mybookstore.dto.shoppingcart.ShoppingCartResponseDto;
import store.mybookstore.service.cartitem.CartItemService;
import store.mybookstore.service.shoppingcart.ShoppingCartService;

@Tag(name = "Shopping cart management.",
        description = "Endpoints for interaction with Shopping cart entity.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;

    @GetMapping
    @Operation(summary = "Get users Shopping cart.",
            description = "Return for logged in user, his shopping cart.")
    public ShoppingCartResponseDto getUsersShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @PostMapping
    @Operation(summary = "Add new book to the Shopping cart.",
            description = "Map book to the item model and put it in cart.")
    public CartItemResponseDto addBookToTheCart(@RequestBody CartItemRequestDto requestDto) {
        return shoppingCartService.addBookToCart(requestDto);
    }

    @PutMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Update book quantity.",
            description = "Update quantity of books in the shopping cart.")
    public CartItemResponseDto update(@RequestBody CartItemUpdateQuantityRequestDto updateDto,
                                      @PathVariable Long cartItemId) {
        return cartItemService.update(updateDto, cartItemId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Delete book.", description = "Delete book from shopping cart.")
    public void delete(@PathVariable Long cartItemId) {
        cartItemService.delete(cartItemId);
    }
}
