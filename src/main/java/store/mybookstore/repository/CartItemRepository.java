package store.mybookstore.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybookstore.dto.cartitem.CartItemResponseDto;
import store.mybookstore.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItemResponseDto> findAllByShoppingCartId(Long id);
}
