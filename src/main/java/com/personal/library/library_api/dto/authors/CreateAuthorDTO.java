package com.personal.library.library_api.dto.authors;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAuthorDTO {

    @NotNull(message = "First name cannot be empty")
    @Size(max = 100, message = "First name cannot be more than 100 characters")
    private String firstName;

    @NotNull(message = "Last name cannot be empty")
    @Size(max = 100, message = "Last name cannot be more than 100 characters")
    private String lastName;

    @Size(max = 300, message = "Bio cannot be more than 300 characters")
    private String bio;

}
