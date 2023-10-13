package store.mybookstore.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import store.mybookstore.exception.EntityNotFoundException;
import store.mybookstore.model.Book;
import store.mybookstore.repository.BookRepository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public Book createBook(Book book) {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(book);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new EntityNotFoundException("Can't save book:" + book + " to the storage.", e);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return book;
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Book book = entityManager.find(Book.class, id);
            return book != null ? Optional.of(book) : Optional.empty();
        } catch (Exception e) {
            throw new EntityNotFoundException("Can't get book with id:" + id, e);
        }
    }

    @Override
    public List<Book> getAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager
                    .createQuery("FROM Book b", Book.class)
                    .getResultList();
        } catch (Exception e) {
            throw new EntityNotFoundException("Can't get result list of books, from storage", e);
        }
    }
}
