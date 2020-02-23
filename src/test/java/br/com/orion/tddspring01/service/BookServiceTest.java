package br.com.orion.tddspring01.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.orion.tddspring01.model.Book;
import br.com.orion.tddspring01.repository.BookRepository;

/**
 * BookServiceTest
 */
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    IBookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    private void setUp() {
        this.service = new BookService(repository);
    }

    @Test
    @DisplayName("Must save a book")
    public void saveBookTest() {
        Book book = Book.builder().author("Joao Silva").title("Livro A").isbn("001").build();
        Book savedBook = Book.builder().id(1L).author("Joao Silva").title("Livro A").isbn("001").build();

        when(service.save(book)).thenReturn(savedBook);
        savedBook = service.save(book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("001");
    }

    @Test
    @DisplayName("Get a book by id")
    public void findBookById() {
        Long id = 1L;
        Book book = Book.builder().id(id).author("Joao Silva").title("Livro A").build();
        when(service.getById(id)).thenReturn(Optional.of(book));

        Optional<Book> foundBook = service.getById(id);

        assertThat(foundBook.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Must save a book")
    public void findBookByIdThatNotExist() {
        Long id = 1L;
        when(service.getById(id)).thenReturn(Optional.empty());
        Optional<Book> book = service.getById(id);

        assertThat(book.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Update a valid book")
    public void update() {
        Book updatingBook = Book.builder().id(1L).author("Joao Silva").title("Livro A").isbn("001").build();
        Book updatedBook = Book.builder().id(1L).author("Joao Silva").title("Livro A X01").isbn("001").build();

        //Usando repository
        when(repository.save(updatingBook)).thenReturn(updatedBook);

        Book book = service.save(updatingBook);

        assertThat(book.getId()).isNotNull();
        assertThat(book.getTitle()).isEqualTo("Livro A X01");
        
    }

    @Test
    @DisplayName("Update a valid book")
    public void updateInvalidBook() {
        Book book = new Book();
        String errorMessage = "Id must not be empty";

        when(repository.save(book)).thenThrow(new IllegalArgumentException(errorMessage));
        
        Throwable ex = Assertions.catchThrowable(() -> service.update(book));
        assertThat(ex).isInstanceOf(IllegalArgumentException.class);
        assertThat(ex.getMessage()).isEqualTo(errorMessage);

        //Another way
        //org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(book));
        //Mockito.verify(repository, Mockito.never()).save(book);
    }

    //To test void method
    @Test
    @DisplayName("Delete a valid book")
    public void deleteABook() {
        Book book = Book.builder().id(1L).build();

        service.delete(book);

        //To check that there's no exception
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> service.delete(book));
        
        //Two times becouse the calling above
        Mockito.verify(repository, Mockito.times(2)).delete(book);
    }

    @Test
    @DisplayName("Must return a IllegalArgumentException")
    public void deleteInvalidArgument() {
        Book book = new Book();
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(book));

        Mockito.verify(repository, Mockito.never()).delete(book);
    }

    @Test
    @DisplayName("Must filter books by properties")
    public void findBook(){
        //Cenário
        Book book = Book.builder().author("Joao Silva").title("Livro A").isbn("001").build();

        //Execução
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Book> list = Arrays.asList(book);

        Page<Book> page = new PageImpl<Book>(list, pageRequest, 1);
        when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);

        Page<Book> result = service.find(book, pageRequest);

        //Verificação
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(list);
    }

    @Test
    @DisplayName("Must return a book by Isbn")
    public void getBookByIsbn(){
        String isbn = "001";
        Book book = Book.builder().id(1L).author("Joao Silva").title("Livro A").isbn("001").build();

        when(service.getBookByIsbn(isbn)).thenReturn(Optional.of(book));

        Optional<Book> bookResult = service.getBookByIsbn(isbn);

        assertThat(bookResult.isPresent()).isTrue();
        assertThat(bookResult.get().getTitle()).isEqualTo("Livro A");

        verify(repository, times(1)).getBookByIsbn(isbn);
    }
    

}