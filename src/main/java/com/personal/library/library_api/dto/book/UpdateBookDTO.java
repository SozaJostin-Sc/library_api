package com.personal.library.library_api.dto.book;

import com.personal.library.library_api.util.isbn.ISBN;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UpdateBookDTO {
    @Size(max = 150, message = "Tittle large than 150")
    private String title;

    @ISBN(type = ISBN.Type.BOTH, message = "Invalid isbn format")
    private String isbn;

    private Integer publicationYear;

    @Min(value = 0, message = "Pages cannot be less than 0")
    private Integer pages;
    private String language;
    private List<Long> authorId;
    private Long categoryId;

    @Min(value = 0, message = "Stock cannot be less than 0")
    private Integer stock;
}
