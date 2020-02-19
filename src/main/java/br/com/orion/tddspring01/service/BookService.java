package br.com.orion.tddspring01.service;

import br.com.orion.tddspring01.model.Book;
import br.com.orion.tddspring01.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * BookService
 */
@Service
@RequiredArgsConstructor
public class BookService implements IBookService {

    private final BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> getById(Long id) {
        return bookRepository.findById(id);
    }

    public void delete(Book book) {
        isNullBook(book);

        bookRepository.delete(book);
    }

    public Book update(Book book) {
        isNullBook(book);

        Optional<Book> bookUpdated = bookRepository.findById(book.getId());
        if (bookUpdated.isEmpty()) {
            bookUpdated.get().setAuthor(book.getAuthor());
            bookUpdated.get().setTitle(book.getTitle());
            bookRepository.save(bookUpdated.get());
            return bookUpdated.get();

        }
        return null;
    }

    @Override
    public Page<Book> find(Book filter, Pageable pageRequest) {
        Example<Book> example = Example.of(filter,
                ExampleMatcher
                        .matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return bookRepository.findAll(example, pageRequest);
    }

    @Override
    public Optional<Book> getBookByIsbn(String isbn) {
        return Optional.empty();
    }

    private void isNullBook(Book book) {
        if (book == null || book.getId() == null) {
            throw new IllegalArgumentException("Id must not be empty");
        }
    }

}