package com.personal.library.library_api.exception.book;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(String title) {
        super("Book already exist: " + title);
    }
}
