package br.com.orion.tddspring01.exceptions;

public class BookAlreadyLoanedException extends ApplicationException {

    public BookAlreadyLoanedException(String message) {
        super(message);
    }

}
