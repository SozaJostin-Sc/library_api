package com.personal.library.library_api.exception.author;

public class AuthorDeletionException extends RuntimeException {
    public AuthorDeletionException(Long authorId) {
        super("Cannot delete author with ID " + authorId + " because they are associated with existing books.");
    }
}

