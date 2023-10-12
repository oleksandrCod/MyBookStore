package store.mybookstore.service.shoppingcart.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.mybookstore.model.ShoppingCart;
import store.mybookstore.model.User;
import store.mybookstore.repository.ShoppingCartRepository;

@Service
@RequiredArgsConstructor
public class ShoppingCartManager {

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCart registerNewShoppingCar(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCartRepository.save(shoppingCart);
    }
}
