package br.com.orion.tddspring01.service;

import org.springframework.stereotype.Service;

import br.com.orion.tddspring01.exceptions.BookAlreadyLoanedException;
import br.com.orion.tddspring01.model.Loan;
import br.com.orion.tddspring01.repository.LoanRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanService implements ILoanService {

    private final LoanRepository repository;

    @Override
    public Loan save(Loan loan) {
        if (repository.isBookAlreadyLoaned(loan.getBook())) {
            throw new BookAlreadyLoanedException("Book was already loaned");
        }
        return repository.save(loan);
    }
}
