package br.com.orion.tddspring01.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.orion.tddspring01.model.Book;
import br.com.orion.tddspring01.model.Loan;

/**
 * LoanRepositoryTest
 */

@DataJpaTest
@ActiveProfiles("Test")
@ExtendWith(SpringExtension.class)
public class LoanRepositoryTest {

    @Autowired
    private LoanRepository repository;

    @Autowired
    private TestEntityManager manager;

    @Test
    @DisplayName("Must check if loan exists and it was not delivered yet")
    public void isBookAlreadyLoaned() {

        Book book = Book.builder().author("John").title("Book A").isbn("123").build();
        book = manager.persist(book);

        Loan loan = Loan.builder().customer("Paul").book(book).loanDate(LocalDate.now()).build();
        loan = manager.persist(loan);

        boolean returned = repository.isBookAlreadyLoaned(book);

        assertThat(returned).isTrue();
    }
}