package br.com.orion.tddspring01.controller;

import br.com.orion.tddspring01.exceptions.ResourceNotFoundException;
import br.com.orion.tddspring01.model.Book;
import br.com.orion.tddspring01.model.dto.BookDto;
import br.com.orion.tddspring01.service.IBookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BookController
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/books")
public class BookController {

    private final IBookService bookService;
    private final ModelMapper map;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public BookDto create(@Valid @RequestBody BookDto bookDto) {
        Book book = map.map(bookDto, Book.class);
        book = bookService.save(book);
        bookDto = map.map(book, BookDto.class);

        return bookDto;
    }

    @PutMapping("{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public BookDto update(@PathVariable Long id, @RequestBody BookDto bookDto) {
        Book book = findBookById(id);
        book.setAuthor(bookDto.getAuthor());
        book.setTitle(bookDto.getTitle());
        book = bookService.update(book);
        bookDto = map.map(book, BookDto.class);

        return bookDto;
    }

    @GetMapping("{id}")
    public BookDto getById(@PathVariable Long id) {
        Book book = findBookById(id);
        BookDto bookDto = map.map(book, BookDto.class);

        return bookDto;
    }

    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Book book = findBookById(id);
        bookService.delete(book);
    }

    private Book findBookById(Long id) {
        Book book = bookService.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
        return book;
    }

    @GetMapping
    public Page<BookDto> find(BookDto bookDto, Pageable pageRequest){
        Book filter = this.map.map(bookDto, Book.class);
        Page<Book> result = bookService.find(filter, pageRequest);
        List<BookDto> list = result.getContent().stream()
                .map(entity -> map.map(entity, BookDto.class)).collect(Collectors.toList());

        return new PageImpl<BookDto>(list, pageRequest, result.getTotalElements());
    }

}