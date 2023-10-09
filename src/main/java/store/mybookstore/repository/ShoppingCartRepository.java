package store.mybookstore.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybookstore.model.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> getByUserId(Long id);
}
