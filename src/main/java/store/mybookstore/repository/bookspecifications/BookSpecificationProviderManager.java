package store.mybookstore.repository.bookspecifications;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import store.mybookstore.exception.NoSuchSpecificationProvider;
import store.mybookstore.model.Book;
import store.mybookstore.repository.specifications.SpecificationProvider;
import store.mybookstore.repository.specifications.SpecificationProviderManager;

@Component
@RequiredArgsConstructor
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationsProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationsProviders
                .stream()
                .filter(b -> b.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new NoSuchSpecificationProvider(
                        "Can't find correct specification provider for key:" + key));
    }
}
