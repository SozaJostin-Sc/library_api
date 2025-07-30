package com.personal.library.library_api.repository;

import com.personal.library.library_api.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author>  findAllByOrderByAuthorIdAsc();
    List<Author> findAllByAuthorIdIn(List<Long> id);
    boolean existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
    boolean existsByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndAuthorIdNot(String firstName, String lastName, Long id);

}
