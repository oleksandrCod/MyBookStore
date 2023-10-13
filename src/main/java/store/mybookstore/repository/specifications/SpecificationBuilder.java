package store.mybookstore.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import store.mybookstore.dto.book.records.BookSearchParameters;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParameters searchParameters);
}
