package com.personal.library.library_api.dto.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.personal.library.library_api.util.isbn.ISBN;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class CreateBookDTO {
    @NotBlank(message = "Tittle cannot be empty")
    @Size(max = 150, message = "Tittle large than 150")
    private String title;

    @ISBN(type = ISBN.Type.BOTH, message = "Invalid isbn format")
    @NotBlank(message = "isbn cannot be empty")
    private String isbn;


    private Integer publicationYear;

    @Min(value = 0, message = "Pages cannot be less than 0")
    private Integer pages;

    private String language;

    @NotNull(message = "Author id cannot be empty")
    private List<Long> authorId;

    @JsonProperty("category_id")
    @NotNull(message = "Category id cannot be empty")
    private Long categoryId;

    @Min(value = 0, message = "Stock cannot be less than 0")
    private Integer stock;

}
