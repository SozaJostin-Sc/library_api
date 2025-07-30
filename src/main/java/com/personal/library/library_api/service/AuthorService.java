package com.personal.library.library_api.service;


import com.personal.library.library_api.dto.authors.CreateAuthorDTO;
import com.personal.library.library_api.dto.authors.UpdateAuthorDTO;
import com.personal.library.library_api.exception.author.*;
import com.personal.library.library_api.model.Author;
import com.personal.library.library_api.repository.AuthorRepository;
import com.personal.library.library_api.util.author.AuthorUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorService {
    private final AuthorRepository authorRepository;

    /// GET ALL AUTHORS
    @Cacheable(value = "author", key = "'all'")
    public List<Author> getAll(){
        return authorRepository.findAllByOrderByAuthorIdAsc();
    }

    @Cacheable(value = "author", key = "#id")
    public Author getById(Long id){
        return authorRepository.findById(id)
                .orElseThrow(() -> {
            log.error("Author not found with id: {}", id);
            return new AuthorNotFoundException(id);
        });
    }

    @Transactional
    @CacheEvict(value = "author", allEntries = true)
    public Author create(CreateAuthorDTO dto){

        AuthorUtils.validateAuthorNameUniqueness(dto.getFirstName(), dto.getLastName(), null, authorRepository);

        Author author = Author.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .bio(dto.getBio() != null ? dto.getBio() : "")
                .status(true)
                .build();
        log.info("Author create successfully");
        return authorRepository.save(author);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "author", key = "#id"),
            @CacheEvict(value = "author", key = "'all'")
    })
    public Author update(Long id,UpdateAuthorDTO dto){
        Author existingAuthor = getById(id);

        if (dto == null) {
            log.warn("Update body in Author with id {} is empty", id);
            throw new AuthorEmptyException("At least one field must be provided for update");
        }

        if ((dto.getFirstName() == null || dto.getFirstName().trim().isEmpty()) && (dto.getLastName() == null ||
                dto.getLastName().trim().isEmpty()) && (dto.getBio() == null || dto.getBio().trim().isEmpty())) {
            log.warn("Update request for Author with id {} has no fields to update", id);
            throw new AuthorEmptyException("At least one field must be provided for update");
        }

        AuthorUtils.validateAuthorNameUniqueness(dto.getFirstName(), dto.getLastName(), id, authorRepository);

        if(dto.getFirstName() != null){
            existingAuthor.setFirstName(dto.getFirstName().trim());
        }

        if(dto.getLastName() != null){
            existingAuthor.setLastName(dto.getLastName().trim());
        }

        if(dto.getBio() != null){
            existingAuthor.setBio(dto.getBio().trim());
        }

        log.info("Author update successfully");
        return authorRepository.save(existingAuthor);
    }

    @Transactional
    @CacheEvict(value = "author", key = "#id")
    public void delete(Long id){
        //Validate existence
        Author author = getById(id);

        if(Boolean.TRUE.equals(author.getStatus())){
            author.setStatus(false);
            log.info("Author with id {} deleted successfully", id);
            authorRepository.save(author);
        }else{
            throw new AuthorAlreadyDeletedException(id);
        }

    }

    @Transactional
    @CacheEvict(value = "author", key = "#id")
    public void activate(Long id){
        //Validate existence
        Author author = getById(id);

        if(Boolean.TRUE.equals(author.getStatus())){
            throw new AuthorAlreadyActiveException("Author with id " + id + " is already active");
        }

        author.setStatus(true);
        log.info("Author with id {} activated successfully", id);
        authorRepository.save(author);

    }



}
