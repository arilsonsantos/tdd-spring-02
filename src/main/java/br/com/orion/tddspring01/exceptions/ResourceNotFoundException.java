package br.com.orion.tddspring01.exceptions;

/**
 * ResourceNotFoundException
 */
public class ResourceNotFoundException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
    
}