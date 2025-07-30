package com.personal.library.library_api.util.loan;

import com.personal.library.library_api.exception.loans.InvalidLoanDateException;
import com.personal.library.library_api.exception.loans.InvalidReturnDateException;

import java.time.LocalDate;

public class LoanUtils {

    private LoanUtils(){
        throw new UnsupportedOperationException("Utility class");
    }

    public static void validateLoanDates(LocalDate loanDate, LocalDate returnDate, int MAX_LOAN_DAYS) {
        LocalDate today = LocalDate.now();

        // Validar fecha de préstamo
        if(loanDate == null) {
            throw new InvalidLoanDateException("Loan date cannot be null");
        }

        if(loanDate.isAfter(today)) {
            throw new InvalidLoanDateException("Loan date cannot be in the future");
        }

        // Validar fecha de devolución si existe
        if(returnDate != null) {
            if(returnDate.isBefore(loanDate)) {
                throw new InvalidReturnDateException("Return date cannot be before loan date");
            }

            if(loanDate.plusDays(MAX_LOAN_DAYS).isBefore(returnDate)) {
                throw new InvalidReturnDateException("Return date cannot be more than " + MAX_LOAN_DAYS + " days after loan date");
            }
        }
    }

}
