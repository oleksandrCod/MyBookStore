package store.mybookstore.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import store.mybookstore.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(Long id);
}
