package com.personal.library.library_api.exception.book;

import java.util.List;

public class BooksIdsNotFoundException extends RuntimeException {
    public BooksIdsNotFoundException(List<Long> idList) {
        super("Books not found: " + idList);
    }
}
