package br.com.orion.tddspring01.exceptions;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * ErrorDetail
 */
@Builder
@Getter
@Setter
public class ErrorDetail {

    private int statusCode;

    private String message;

    private LocalDateTime timestamp;

    
}