package com.personal.library.library_api.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCustomerDTO {
    @NotBlank(message = "Firs name cannot be empty")
    @Size(max = 100, message = "First name cannot be more than 100 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Size(max = 100, message = "Last name cannot be more than 100 characters")
    private String lastName;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email format isn't valid")
    private String email;

    @Pattern(regexp = "^\\+?\\d{7,15}$", message = "Invalid phone number")
    private String phone;

}
