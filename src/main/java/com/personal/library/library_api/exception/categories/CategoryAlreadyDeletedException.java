package com.personal.library.library_api.exception.categories;

public class CategoryAlreadyDeletedException extends RuntimeException{
    public CategoryAlreadyDeletedException(Long id){super("Category with id: " + id + " already deleted");}
}
