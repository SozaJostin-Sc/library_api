package com.personal.library.library_api.exception.customers;

public class CustomerEmailExistsException extends RuntimeException {
    public CustomerEmailExistsException(String message) {
        super(message);
    }
}
