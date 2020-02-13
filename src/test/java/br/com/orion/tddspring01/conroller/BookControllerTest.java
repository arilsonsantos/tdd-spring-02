package br.com.orion.tddspring01.conroller;

import br.com.orion.tddspring01.exceptions.ResourceAlreadyExistsException;
import br.com.orion.tddspring01.exceptions.ResourceNotFoundException;
import br.com.orion.tddspring01.model.Book;
import br.com.orion.tddspring01.model.dto.BookDto;
import br.com.orion.tddspring01.service.IBookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * BookControllerTest
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WebMvcTest
@ActiveProfiles("test")
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    IBookService service;

    private static final String BOOK_API = "/api/books";

    @Test
    @DisplayName("Create a new book")
    public void createABook() throws Exception {

        BookDto bookDto = createNewBook();
        Book savedBook = Book.builder().id(1L).title("Livro A").author("Joao Silva").build();

        BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(savedBook);

        String json = new ObjectMapper().writeValueAsString(bookDto);

        var request = MockMvcRequestBuilders.post(BOOK_API).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("author").value(bookDto.getAuthor()));

    }

    @Test
    @DisplayName("Try to create a invalid book")
    public void createInvalidBook() throws Exception {
        BookDto bookDto = BookDto.builder().author("Joa").build();
        String json = new ObjectMapper().writeValueAsString(bookDto);

        BDDMockito.given(service.save(Mockito.any(Book.class))).willThrow(IllegalArgumentException.class);
        Throwable ex = Assertions.catchThrowable(() -> service.save(new Book()));

        var request = MockMvcRequestBuilders.post(BOOK_API).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(json);

        Assertions.assertThat(ex).isInstanceOf(IllegalArgumentException.class);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errors").isNotEmpty())
                .andExpect(jsonPath("errors",
                        hasEntry("author", "The title must have between 5 and 10 characters")))
                .andExpect(jsonPath("errors", hasEntry("title", "must not be empty")));
    }

    @Test
    @DisplayName("Must throws exception as try to add a new book with an already ISBN added")
    public void createBookWithIsbnDuplicated() throws Exception {
        BookDto bookDto = createNewBook();
        String json = new ObjectMapper().writeValueAsString(bookDto);
        BDDMockito.given(service.save(Mockito.any(Book.class)))
                .willThrow(new ResourceAlreadyExistsException("Isbn already added."));

        var request = MockMvcRequestBuilders.post(BOOK_API).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Isbn already added."));
    }

    private BookDto createNewBook() {
        return BookDto.builder().author("Joao Silva").title("Livro A").build();
    }

    @Test
    @DisplayName("Get a book by id")
    public void findABookExist() throws Exception {
        Long id = 1L;
        Optional<Book> validBook = Optional
                .of(Book.builder().id(1L).author("Joao Silva").title("Livro A").build());

        BDDMockito.given(service.getById(id)).willReturn(validBook);

        var request = MockMvcRequestBuilders.get(BOOK_API.concat("/1"));

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("author").value("Joao Silva"));

    }

    @Test
    @DisplayName("Get a book by id that not exist")
    public void findABookNotExist() throws Exception {
        Long id = 1L;

        BDDMockito.given(service.getById(id))
                .willThrow(new ResourceNotFoundException("Book not found with ID: " + id));

        var request = MockMvcRequestBuilders.get(BOOK_API.concat("/1"));

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNotFound()).andExpect(
                MockMvcResultMatchers.jsonPath("message").value("Book not found with ID: " + id));
    }

    @Test
    @DisplayName("Must delete a book")
    public void deleteABook() throws Exception {
        Long id = 1L;

        BDDMockito.given(service.getById(anyLong())).willReturn(Optional.of(Book.builder().id(id).build()));

        var request = MockMvcRequestBuilders.delete(BOOK_API.concat("/1"));

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Must delete a book")
    public void deleteInvalidABook() throws Exception {
        Long id = 1L;
        BDDMockito.given(service.getById(id)).willReturn(Optional.empty());

        var request = MockMvcRequestBuilders.delete(BOOK_API.concat("/1"));

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNotFound()).andExpect(
                MockMvcResultMatchers.jsonPath("message").value("Book not found with ID: " + id));
        ;
    }

    @Test
    @DisplayName("Must update a book")
    public void updateABook() throws Exception {
        Long id = 1L;
        BookDto bookDto = createNewBook();
        String json = new ObjectMapper().writeValueAsString(bookDto);

        Book updatingBook = Book.builder().id(1L).title("Livro A").author("Joao Silva").build();
        Book updatedBook = Book.builder().id(1L).title("Livro A 123").author("Joao Silva").build();

        BDDMockito.given(service.getById(id)).willReturn(Optional.of(updatingBook));
        BDDMockito.given(service.update(updatingBook)).willReturn(updatedBook);

        var request = MockMvcRequestBuilders.put(BOOK_API.concat("/1")).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("author").value(bookDto.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("title").value(updatedBook.getTitle()));
    }

    @Test
    @DisplayName("Must update a book")
    public void updateABookInexistent() throws Exception {
        BookDto bookDto = createNewBook();
        String json = new ObjectMapper().writeValueAsString(bookDto);

        BDDMockito.given(service.getById(anyLong())).willReturn(Optional.empty());

        var request = MockMvcRequestBuilders.put(BOOK_API.concat("/1")).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Must filter books")
    public void findBook() throws Exception {
        Long id = 11L;

        Book book = Book.builder().id(id).author("John Silva").title("Book A").isbn("123").build();

        BDDMockito.given(service.find(Mockito.any(Book.class), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<Book>(Arrays.asList(book), PageRequest.of(1, 50), 1));


        String queryString = String.format("?/title=%s&author=%s&page=0&size=100",
                book.getTitle(),
                book.getAuthor());


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BOOK_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);



        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", Matchers.hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(100))
                .andExpect(jsonPath("pageable.pageNumber").value(0));

    }


}