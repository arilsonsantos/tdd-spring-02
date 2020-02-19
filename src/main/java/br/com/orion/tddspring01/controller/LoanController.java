package br.com.orion.tddspring01.controller;

import br.com.orion.tddspring01.model.Book;
import br.com.orion.tddspring01.model.Loan;
import br.com.orion.tddspring01.model.dto.LoanDto;
import br.com.orion.tddspring01.service.IBookService;
import br.com.orion.tddspring01.service.ILoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final ILoanService loanService;
    private final IBookService bookService;

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    public Long create(@RequestBody LoanDto loanDto){
        final Book book = bookService.getBookByIsbn(loanDto.getIsbn()).get();
        Loan loan = Loan.builder().book(book).customer(loanDto.getCustomer()).loanDate(LocalDate.now()).build();
        loan = loanService.save(loan);

        return loan.getId();

    }
}
