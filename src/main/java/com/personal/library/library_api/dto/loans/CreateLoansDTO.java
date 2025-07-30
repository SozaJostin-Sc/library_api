package com.personal.library.library_api.dto.loans;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateLoansDTO {
    @NotNull(message = "Customer id cannot be null")
    private Long customerId;

    @NotEmpty(message = "Book list cannot be empty")
    private List<Long> bookIds;

    @PastOrPresent(message = "Loan date cannot be in the future")
    @NotNull(message = "Loan date cannot be empty")
    private LocalDate loanDate;

    @Future(message = "Return date cannot be in the past")
    @NotNull(message = "Return date cannot be empty")
    private LocalDate returnDate;

    @AssertTrue(message = "Return date must be after or equal to loan date")
    public boolean isReturnDateValid() {
        return loanDate != null && returnDate != null && !loanDate.isAfter(returnDate);
    }
}

