package com.personal.library.library_api.util.book;

import com.personal.library.library_api.exception.book.BookAlreadyExistsException;
import com.personal.library.library_api.exception.book.BookUnavailableException;
import com.personal.library.library_api.exception.book.BooksIdsNotFoundException;
import com.personal.library.library_api.model.Book;
import com.personal.library.library_api.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class BookUtils {

    private BookUtils(){
        throw new UnsupportedOperationException("Utility class");
    }


    public static void validateBooksAvailability(List<Book> books) {
        List<Book> unavailableBooks = books.stream()
                .filter(book -> book.getStock() <= 0 || !Boolean.TRUE.equals(book.getStatus()))
                .toList();

        if(!unavailableBooks.isEmpty()) {
            throw new BookUnavailableException("The following books are not available: " +
                    unavailableBooks.stream()
                            .map(Book::getTitle)
                            .collect(Collectors.joining(", ")));
        }
    }

    public static void validateBooksId(List<Book> books, List<Long> ids) {
        if (books.size() != ids.size()) {
            Set<Long> foundIds = books.stream()
                    .map(Book::getBookId)
                    .collect(Collectors.toSet());

            List<Long> missingIds = ids.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            log.error("Books not found with IDs: {}", missingIds);
            throw new BooksIdsNotFoundException(missingIds);
        }
    }



    public static void reduceBookStock(List<Book> books, BookRepository bookRepository){
        books.forEach(book -> {
            if(book.getStock() <= 0){
                throw new BookUnavailableException("Book " + book.getTitle() + " is out of stock");
            }

            book.setStock(book.getStock() - 1);
            bookRepository.save(book);
            log.debug("Stock decrease for book ID: {}. New stock: {}", book.getBookId(), book.getStock());
        });

    }


    public static void releaseBookStock(List<Book> books, BookRepository bookRepository){
        books.forEach(book -> {
            book.setStock(book.getStock() + 1);
            bookRepository.save(book);
            log.debug("Stock increase for book ID: {}, New Stock: {}", book.getBookId(), book.getStock());
        });
    }

    public static void validateBookTitleUniqueness(String title, Long excludedId, BookRepository bookRepository) {
        boolean exists = (excludedId == null)
                ? bookRepository.existsByTitleIgnoreCase(title)
                : bookRepository.existsByTitleIgnoreCaseAndBookIdNot(title, excludedId);

        if (exists) {
            log.warn("Book title {} already exists (excludedId: {})", title, excludedId);
            throw new BookAlreadyExistsException(title);
        }
    }


}
