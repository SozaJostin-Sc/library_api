package com.personal.library.library_api.exception.author;

public class AuthorNotFoundException extends RuntimeException{
    public AuthorNotFoundException(Long id){
        super("Author not found with id: " + id);
    }
}
