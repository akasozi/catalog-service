package com.polarbookshop.catalogservice.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public record Book(
        @NotBlank(message = "The book ISBN must be defined")
        String isbn,
        @NotBlank(message = "The book title must be defined")
        String title,
        @NotBlank(message = "The book author must be defined")
        String author,
        @NotNull(message = "The book price must be defined")
        @Positive(message = "The book price must ge greater than zero")
        Double price
) {
}
