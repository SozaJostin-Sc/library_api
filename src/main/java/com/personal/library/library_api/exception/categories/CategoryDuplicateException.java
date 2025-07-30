package com.personal.library.library_api.exception.categories;

public class CategoryDuplicateException extends RuntimeException {
    public CategoryDuplicateException(String message) {
        super("Category wiht name: " + message + " already exists");
    }
}
