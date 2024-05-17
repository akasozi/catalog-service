package co.ke.cloudflow.catalogservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record Book (
        @NotBlank(message = "The book isbn must be defined")
        @Pattern(regexp = "^([0-9]{10}|[0-9]{13})$", message = "This ISBN format must be valid.")
        String isbn,
        @NotBlank(message = "This book title must be defined.")
        String title,

        @NotBlank(message = "This book author must be defined.")
        String author,

        @NotNull(message = "This book price must be defined.")
                @Positive(message = "The book price must be greated than zero.")
        Double price
) {
}
