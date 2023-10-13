package store.mybookstore.repository;

import java.util.List;
import java.util.Optional;
import store.mybookstore.model.Book;

public interface BookRepository {
    Book createBook(Book book);

    Optional<Book> getBookById(Long id);

    List<Book> getAll();
}
