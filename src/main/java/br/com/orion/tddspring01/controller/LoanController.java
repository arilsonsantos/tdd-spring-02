package br.com.orion.tddspring01.controller;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.orion.tddspring01.exceptions.ResourceNotFoundException;
import br.com.orion.tddspring01.model.Book;
import br.com.orion.tddspring01.model.Loan;
import br.com.orion.tddspring01.model.dto.LoanDto;
import br.com.orion.tddspring01.service.IBookService;
import br.com.orion.tddspring01.service.ILoanService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final ILoanService loanService;
    private final IBookService bookService;

    @PostMapping()
    @ResponseStatus(code = HttpStatus.CREATED)
    public Long create(@RequestBody LoanDto loanDto) {
        Book book = bookService.getBookByIsbn(loanDto.getIsbn()).orElseThrow(() -> new ResourceNotFoundException("Book not found by Isbn :" + loanDto.getIsbn()));

        Loan loan = Loan.builder().book(book).customer(loanDto.getCustomer()).loanDate(LocalDate.now()).build();
        loan = loanService.save(loan);
        return loan.getId();
    }

}
