package br.com.orion.tddspring01.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.orion.tddspring01.exceptions.BookAlreadyLoanedException;
import br.com.orion.tddspring01.model.Book;
import br.com.orion.tddspring01.model.Loan;
import br.com.orion.tddspring01.repository.LoanRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("Test")
public class LoanServiceTest {

    ILoanService service;

    @MockBean
    LoanRepository repository;

    @BeforeEach
    public void setUp() {
        service = new LoanService(repository);
    }

    @Test
    @DisplayName("Must save a loan")
    public void save() {
        Book book = Book.builder().id(1L).title("Book A").build();
        Loan loan = Loan.builder().customer("Paul").book(book).loanDate(LocalDate.now()).build();

        Loan loanSaved = Loan.builder().id(1L).customer("Paul").book(book).loanDate(LocalDate.now()).build();

        when(repository.isBookAlreadyLoaned(book)).thenReturn(false);
        when(service.save(loan)).thenReturn(loanSaved);

        loanSaved = service.save(loan);

        assertThat(loanSaved.getId()).isNotNull();
        assertThat(loanSaved.getCustomer()).isEqualTo("Paul");
        assertThat(loanSaved.getLoanDate()).isEqualTo(LocalDate.now());

        Mockito.verify(repository, Mockito.times(1)).save(loan);
    }

    @Test
    @DisplayName("Must return the error BookAlreadyLoanedException")
    public void bookAreadyLoaned() {
        String errorMessage = "Book was already loaned";
        Book book = Book.builder().id(1L).title("Book A").build();
        Loan loanToSave = Loan.builder().customer("Paul").book(book).loanDate(LocalDate.now()).build();

        when(repository.isBookAlreadyLoaned(book)).thenReturn(true);

        Throwable ex = Assertions.catchThrowable(() -> service.save(loanToSave));
        assertThat(ex).isInstanceOf(BookAlreadyLoanedException.class).hasMessage(errorMessage);

        verify(repository, never()).save(loanToSave);

    }

}
