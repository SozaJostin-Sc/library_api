package com.personal.library.library_api.exception.loans;

public class ReturnedLoanException extends RuntimeException {
    public ReturnedLoanException(String message) {
        super(message);
    }
}
