package com.personal.library.library_api.exception.loans;

public class InvalidLoanDateException extends RuntimeException {
    public InvalidLoanDateException(String message) {
        super(message);
    }
}
