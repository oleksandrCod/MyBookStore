package store.mybookstore.repository.bookspecifications;

import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import store.mybookstore.model.Book;
import store.mybookstore.repository.specifications.SpecificationProvider;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root
                .get("titles").in(Arrays.stream(params).toArray());
    }

    @Override
    public String getKey() {
        return "title";
    }
}
