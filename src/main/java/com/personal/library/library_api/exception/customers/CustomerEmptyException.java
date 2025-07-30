package com.personal.library.library_api.exception.customers;

public class CustomerEmptyException extends RuntimeException {
    public CustomerEmptyException(String message) {
        super(message);
    }
}
