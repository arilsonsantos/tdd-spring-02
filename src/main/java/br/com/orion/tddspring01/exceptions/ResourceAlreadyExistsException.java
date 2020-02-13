package br.com.orion.tddspring01.exceptions;

import lombok.Getter;
import lombok.Setter;

/**
 * ResourceAlreadyExistsException
 */
@Getter
@Setter
public class ResourceAlreadyExistsException extends ApplicationException {
    
    private static final long serialVersionUID = 1L;

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
    
}