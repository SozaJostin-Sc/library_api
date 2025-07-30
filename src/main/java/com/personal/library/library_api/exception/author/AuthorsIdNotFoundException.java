package com.personal.library.library_api.exception.author;

import java.util.List;

public class AuthorsIdNotFoundException extends RuntimeException {
    public AuthorsIdNotFoundException(List<Long> idList) {
        super("Authors not found: " + idList);
    }
}
