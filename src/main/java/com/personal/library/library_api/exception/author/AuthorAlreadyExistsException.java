package com.personal.library.library_api.exception.author;

public class AuthorAlreadyExistsException extends RuntimeException {
    public AuthorAlreadyExistsException(String firstName, String lastName) {
        super("Author already exists with name: " + firstName + " " + lastName);
    }
}

