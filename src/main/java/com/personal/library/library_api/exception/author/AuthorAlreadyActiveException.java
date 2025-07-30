package com.personal.library.library_api.exception.author;

public class AuthorAlreadyActiveException extends RuntimeException {
    public AuthorAlreadyActiveException(String message) {
        super(message);
    }
}
