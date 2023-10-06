package store.mybookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.mybookstore.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
