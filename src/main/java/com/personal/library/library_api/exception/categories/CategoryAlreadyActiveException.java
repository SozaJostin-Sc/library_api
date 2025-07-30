package com.personal.library.library_api.exception.categories;

public class CategoryAlreadyActiveException extends RuntimeException {
    public CategoryAlreadyActiveException(String message) {
        super(message);
    }
}
