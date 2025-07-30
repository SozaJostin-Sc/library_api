package com.personal.library.library_api.exception.customers;

public class CustomerPhoneExistException extends RuntimeException {
    public CustomerPhoneExistException(String message) {
        super(message);
    }
}
