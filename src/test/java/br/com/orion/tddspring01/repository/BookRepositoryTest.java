package br.com.orion.tddspring01.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.orion.tddspring01.model.Book;

/**
 * BookRepositoryTest
 */
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository bookRepository;
    
    @Test
    @DisplayName("Must return true when exists a book in the database with the Isbn informed")
    public void returnTrueWhenIsbnExists() {
        String isbn = "123";
        Book book = Book.builder().author("Joao Silva").title("Livro A").isbn(isbn).build();
        entityManager.persist(book);
        boolean existIsbn = bookRepository.existsByIsbn(isbn);

        Assertions.assertThat(existIsbn).isTrue();
    }

    @Test
    @DisplayName("Must return false when not exists a book in the database with the Isbn informed")
    public void returnFalseWhenIsbnExists() {
        String isbn = "123";
        boolean existIsbn = bookRepository.existsByIsbn(isbn);
        Assertions.assertThat(existIsbn).isFalse();
    }

    @Test
    @DisplayName("Must save a book")
    public void saveBook() {
        Book book = Book.builder().author("Joao Silva").title("Livro A").isbn("123").build();

        Book savedBook = bookRepository.save(book);

        Assertions.assertThat(savedBook.getId()).isNotNull();
    }

    @Test
    public void findById() {
        Book book = Book.builder().author("Joao Silva").title("Livro A").isbn("123").build();
        entityManager.persist(book);
        Optional<Book> foundBook = bookRepository.findById(book.getId());

        Assertions.assertThat(foundBook.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Delete a book")
    public void delete() {
        Book book = Book.builder().author("Joao Silva").title("Livro A").isbn("123").build();
        entityManager.persist(book);

        Book foundBook = entityManager.find(Book.class, book.getId());

        bookRepository.delete(foundBook);

        Book deletedBook = entityManager.find(Book.class, book.getId());

        Assertions.assertThat(deletedBook).isNull();

    }

}