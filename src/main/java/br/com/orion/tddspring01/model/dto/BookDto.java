package br.com.orion.tddspring01.model.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * BookDto
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookDto {

    private Long id;

    @NotEmpty
    @Size(min = 5, max = 10, message = "The title must have between 5 and 10 characters")
    private String author;

    @NotEmpty
    private String title;

    private String isbn;
}