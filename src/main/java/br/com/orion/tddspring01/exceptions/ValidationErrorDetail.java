package br.com.orion.tddspring01.exceptions;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * ErrorDetail
 */
@Builder
@Getter
@Setter
public class ValidationErrorDetail {

    private int statusCode;

    private String message;

    private LocalDateTime timestamp;

    private Map<String, String> errors;
}