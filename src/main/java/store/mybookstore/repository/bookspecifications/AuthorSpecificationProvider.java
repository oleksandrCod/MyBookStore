package store.mybookstore.repository.bookspecifications;

import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import store.mybookstore.model.Book;
import store.mybookstore.repository.specifications.SpecificationProvider;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root
                .get("authors").in(Arrays.stream(params).toArray());
    }

    @Override
    public String getKey() {
        return "author";
    }
}
