package br.com.orion.tddspring01.exceptions;

/**
 * ErrorDetail
 */
public class ApplicationException extends RuntimeException  {

    public ApplicationException(String message) {
        super(message);
    }

}