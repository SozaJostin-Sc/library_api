package com.personal.library.library_api.util.author;

import com.personal.library.library_api.exception.author.AuthorAlreadyExistsException;
import com.personal.library.library_api.exception.author.AuthorsIdNotFoundException;
import com.personal.library.library_api.model.Author;
import com.personal.library.library_api.repository.AuthorRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class AuthorUtils {

    private AuthorUtils(){
        throw new UnsupportedOperationException("Utility class");
    }

    public static void validateAuthorsId(List<Author> foundAuthors, List<Long> requestedAuthorIds) {
        if (foundAuthors.size() != requestedAuthorIds.size()) {
            Set<Long> foundIds = foundAuthors.stream()
                    .map(Author::getAuthorId)
                    .collect(Collectors.toSet());

            List<Long> missingIds = requestedAuthorIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            log.error("Authors not found with IDs: {}", missingIds);
            throw new AuthorsIdNotFoundException(missingIds);
        }
    }

    public static void validateAuthorNameUniqueness(String firstName, String lastName, Long excludedId, AuthorRepository authorRepository) {
        boolean exists = (excludedId == null)
                ? authorRepository.existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName)
                : authorRepository.existsByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndAuthorIdNot(firstName, lastName, excludedId);

        if (exists) {
            log.warn("Author name {} {} already exists (excludedId: {})", firstName, lastName, excludedId);
            throw new AuthorAlreadyExistsException(firstName, lastName);
        }
    }

}
