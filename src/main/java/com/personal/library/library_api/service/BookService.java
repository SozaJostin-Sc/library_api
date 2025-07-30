package com.personal.library.library_api.service;

import com.personal.library.library_api.dto.book.CreateBookDTO;
import com.personal.library.library_api.dto.book.UpdateBookDTO;
import com.personal.library.library_api.exception.book.BookAlreadyDeletedException;
import com.personal.library.library_api.exception.book.BookAlreadyExistsException;
import com.personal.library.library_api.exception.book.BookNotFoundException;
import com.personal.library.library_api.exception.book.DuplicateIsbnException;
import com.personal.library.library_api.exception.categories.CategoryNotFoundException;
import com.personal.library.library_api.model.Author;
import com.personal.library.library_api.model.Book;
import com.personal.library.library_api.model.Categories;
import com.personal.library.library_api.repository.AuthorRepository;
import com.personal.library.library_api.repository.BookRepository;
import com.personal.library.library_api.repository.CategoryRepository;
import com.personal.library.library_api.util.author.AuthorUtils;
import com.personal.library.library_api.util.book.BookUtils;
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
public class BookService {
    //Private instances
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    // Default value const
    private static final String DEFAULT_LANGUAGE = "Unknown";
    private static final int DEFAULT_PAGES = 1;
    private static final int DEFAULT_STOCK = 1;

    /// GET ALL BOOKS
    @Cacheable(value = "books", key = "'all'")
    public List<Book> getAll(){
        return bookRepository.findAllByOrderByBookIdAsc();
    }

    /// Get books by some tittle
    @Cacheable(value = "books", key = "#title.toLowerCase()")
    public List<Book> getAllByTitle(String title){
        if (title == null || title.trim().isEmpty()) {
            log.error("Title cannot be empty");
            throw new IllegalArgumentException("Title cannot be empty");
        }
        return bookRepository.findAllByTitleContainingIgnoreCase(title);
    }

    /// GET BOOK BY ID
    @Cacheable(value = "books", key = "#id", unless = "#result == null") //save cache with key: "books:id"
    public Book getById(Long id){
        return bookRepository.findById(id).orElseThrow(() -> {
            log.error("Author with id {} not found", id);
            return new BookNotFoundException(id);
        });
    }


    /// CREATE NEW BOOK
    @Transactional
    @CacheEvict(value = "books", allEntries = true)
    public Book create(CreateBookDTO dto) {
        //Find if the category exist
        Categories category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(
                () -> new CategoryNotFoundException(dto.getCategoryId()));

        //Get all author id
        List<Long> requestedAuthorIds = dto.getAuthorId();
        // Find all on db
        List<Author> foundAuthors = authorRepository.findAllByAuthorIdIn(requestedAuthorIds);

        AuthorUtils.validateAuthorsId(foundAuthors, requestedAuthorIds);
        BookUtils.validateBookTitleUniqueness(dto.getTitle(), null, bookRepository);

        /// validate if isbnExist
        if(bookRepository.existsByIsbn(dto.getIsbn())){
            log.warn("Attempt to create duplicate ISBN: {}", dto.getIsbn());
            throw new DuplicateIsbnException(dto.getIsbn());
        }

        Book book = Book.builder()
                .title(dto.getTitle())
                .isbn(dto.getIsbn())
                .publicationYear(dto.getPublicationYear())
                .pages(dto.getPages() != null ? dto.getPages() : DEFAULT_PAGES )
                .language(dto.getLanguage() != null ? dto.getLanguage() : DEFAULT_LANGUAGE)
                .stock(dto.getStock() != null ? dto.getStock() : DEFAULT_STOCK)
                .authors(foundAuthors)
                .categoryId(category)
                .status(true)
                .build();

        log.info("Book create successfully");
        return bookRepository.save(book);
    }


    /// UPDATE BOOKS
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "books", key = "#id"),
            @CacheEvict(value = "books", key = "'all'")
    })
    public Book update(Long id, UpdateBookDTO dto){
        //Validate dto null body
        if(dto == null){
            log.warn("Update body in Book with id {} is empty", id);
            throw new IllegalArgumentException("At least one field must be provides for update");
        }

        List<Long> updateAuthorIds = dto.getAuthorId();
        List<Author> foundAuthor = authorRepository.findAllByAuthorIdIn(updateAuthorIds);


        //Validate at leats one field not null
        if (dto.getTitle() == null &&
                dto.getIsbn() == null &&
                dto.getPublicationYear() == null &&
                dto.getPages() == null &&
                dto.getLanguage() == null &&
                dto.getStock() == null &&
                dto.getAuthorId() == null &&
                dto.getCategoryId() == null) {
            throw new IllegalArgumentException("At least one field must be provided for update");
        }

        //Validate not duplicate title
        BookUtils.validateBookTitleUniqueness(dto.getTitle(), id, bookRepository);

        //Get the book by id
        Book existingBook = getById(id);


        if (dto.getTitle() != null) {
            existingBook.setTitle(dto.getTitle());
        }

        if (dto.getIsbn() != null && !dto.getIsbn().equals(existingBook.getIsbn())) {
            if(bookRepository.existsByIsbn(dto.getIsbn())){
                throw new DuplicateIsbnException(dto.getIsbn());
            }
            existingBook.setIsbn(dto.getIsbn());
        }

        if (dto.getPublicationYear() != null ){
            existingBook.setPublicationYear(dto.getPublicationYear());
        }

        if (dto.getPages() != null) {
            existingBook.setPages(dto.getPages());
        }
        if (dto.getLanguage() != null) {
            existingBook.setLanguage(dto.getLanguage());
        }
        if (dto.getStock() != null) {
            existingBook.setStock(dto.getStock());
        }

        if(dto.getAuthorId() != null) {
            AuthorUtils.validateAuthorsId(foundAuthor, updateAuthorIds);
            existingBook.setAuthors(foundAuthor);
        }

        if (dto.getCategoryId() != null) {
            Categories category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(dto.getCategoryId()));
            existingBook.setCategoryId(category);
        }

        log.info("Book with id {} update successfully", id);
        return bookRepository.save(existingBook);
    }


    /// DELETING BOOK
    @Transactional
    @CacheEvict(value = "books", key = "#id")
    public void delete(Long id){
        //validate existence
        Book book = getById(id);

        if(Boolean.TRUE.equals(book.getStatus())){
            book.setStatus(false);
            bookRepository.save(book);
            log.info("Book with id {} deactivated successfully", id);
        }else{
            log.warn("Book with id {} is already deactivated", id);
            throw new BookAlreadyDeletedException(id);
        }

    }

    @Transactional
    @CacheEvict(value = "books", key = "#id")
    public void activate(Long id){
        //validate existence
        Book book = getById(id);

        if(Boolean.TRUE.equals(book.getStatus())){
            log.warn("Book with id {} is already active", id);
            throw new BookAlreadyExistsException("Book already activated, status: true");
        }

        book.setStatus(true);
        bookRepository.save(book);
        log.info("Book with id {} activated successfully", id);
    }

}
