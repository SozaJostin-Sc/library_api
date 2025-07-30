package com.personal.library.library_api.exception.book;

public class BookWithStockException extends RuntimeException {
    public BookWithStockException(Long id) {
        super("Cannot delete book with id " + id + "because it has stock > 0");
    }
}
