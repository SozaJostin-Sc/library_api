package com.personal.library.library_api.exception.book;

public class DuplicateIsbnException extends RuntimeException {
    public DuplicateIsbnException(String isbn) {
        super("ISBN already exist, id: " + isbn);
    }
}
