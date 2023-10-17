package store.mybookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import store.mybookstore.model.Book;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Verify findBooksByCategoriesId() method.")
    @Sql(scripts = {"classpath:database/fill-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/delete-data-for-book-entity.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findBooksByCategoriesId_WithTwoBook_ReturnListOfTwoBook() {
        List<Book> actual = bookRepository.findBooksByCategoriesId(1L);
        assertEquals(actual.size(), 2);
        assertEquals(1L, actual.get(0).getId());
        assertEquals(2L, actual.get(1).getId());
    }
}
