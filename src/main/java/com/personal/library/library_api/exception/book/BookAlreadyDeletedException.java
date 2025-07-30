package com.personal.library.library_api.exception.book;

public class BookAlreadyDeletedException extends RuntimeException {
    public BookAlreadyDeletedException(Long id) {
        super("Book already deleted with id: " + id);
    }
}
