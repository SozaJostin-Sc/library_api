package com.personal.library.library_api.dto.loans;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateLoansDTO {
    private Long customerId;
    private List<Long> bookIds;
    @PastOrPresent(message = "Loan date cannot be in the future")
    private LocalDate loanDate;
    @Future(message = "Return date cannot be in today or the future")
    private LocalDate returnDate;


    @AssertTrue(message = "Return date must be after or equal to loan date")
    public boolean isReturnDateValid() {
        return loanDate != null && returnDate != null && !loanDate.isAfter(returnDate);
    }
}
