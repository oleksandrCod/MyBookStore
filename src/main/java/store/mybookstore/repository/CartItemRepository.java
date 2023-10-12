package store.mybookstore.repository;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybookstore.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Set<CartItem> findAllByShoppingCartId(Long id);
}
