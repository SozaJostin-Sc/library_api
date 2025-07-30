package com.personal.library.library_api.repository;

import com.personal.library.library_api.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByOrderByBookIdAsc();
    boolean existsByTitleIgnoreCase(String title);
    boolean existsByTitleIgnoreCaseAndBookIdNot(String title, Long id);
    boolean existsByIsbn(String isbn);
    List<Book> findAllByTitleContainingIgnoreCase(String title);  // Books who contain a word (case-sensitive)
    List<Book> findAllByBookIdIn(List<Long> ids);
}
