package store.mybookstore.repository;

import org.springframework.data.jpa.domain.Specification;
import store.mybookstore.dto.records.BookSearchParameters;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParameters searchParameters);
}
