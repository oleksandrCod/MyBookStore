package store.mybookstore.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybookstore.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByOrderId(Long id);
}
