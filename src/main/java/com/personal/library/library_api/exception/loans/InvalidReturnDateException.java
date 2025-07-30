package com.personal.library.library_api.exception.loans;

public class InvalidReturnDateException extends RuntimeException {
    public InvalidReturnDateException(String message) {
        super(message);
    }
}
