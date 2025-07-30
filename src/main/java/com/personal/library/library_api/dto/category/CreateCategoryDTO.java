package com.personal.library.library_api.dto.category;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class CreateCategoryDTO {

    @NotBlank(message = "Category cannot be empty")
    @Size(max = 100, message = "Category cannot be more than 100 characters")
    private String name;

    @Size(max = 250, message = "Description cannot be more than 250 characters")
    private String description;

}
