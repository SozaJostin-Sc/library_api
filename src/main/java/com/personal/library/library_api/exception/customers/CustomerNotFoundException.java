package com.personal.library.library_api.exception.customers;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(
            Long id
    ) {
        super("Customer with id " + id + " not found");
    }
}
