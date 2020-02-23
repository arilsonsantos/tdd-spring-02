package br.com.orion.tddspring01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.orion.tddspring01.model.Book;

import java.util.Optional;

/**
 * BookRepository
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbn(String isbn);

    Optional<Book> getBookByIsbn(String isbn);
}