package br.com.orion.tddspring01.exceptions;

public class BookAlreadyLoanedException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public BookAlreadyLoanedException(String message) {
        super(message);
    }

}
