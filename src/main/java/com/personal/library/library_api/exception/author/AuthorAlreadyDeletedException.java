package com.personal.library.library_api.exception.author;

public class AuthorAlreadyDeletedException extends RuntimeException {
    public AuthorAlreadyDeletedException(Long id) {
        super("Author with id " + id + " already deleted");
    }
}
