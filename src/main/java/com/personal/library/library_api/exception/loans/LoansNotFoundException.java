package com.personal.library.library_api.exception.loans;

public class LoansNotFoundException extends RuntimeException {
    public LoansNotFoundException(String message) {
        super(message);
    }
}
