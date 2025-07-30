package com.personal.library.library_api.controller;

import com.personal.library.library_api.dto.book.CreateBookDTO;
import com.personal.library.library_api.dto.book.UpdateBookDTO;
import com.personal.library.library_api.model.Book;
import com.personal.library.library_api.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/book")
    public ResponseEntity<List<Book>> getAllBooks(){
        return ResponseEntity.ok(bookService.getAll());
    }

    @GetMapping("/book/search")
    public ResponseEntity<List<Book>> getAllBooksByTitle(@RequestParam String title){
        return ResponseEntity.ok(bookService.getAllByTitle(title));
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id){
        return ResponseEntity.ok(bookService.getById(id));
    }

    @PostMapping("/book")
    public ResponseEntity<Book> createBook(@Valid @RequestBody CreateBookDTO dto){
        Book book = bookService.create(dto);
        return ResponseEntity.ok(book);
    }

    @PatchMapping("/book/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody UpdateBookDTO dto){
        return ResponseEntity.ok(bookService.update(id, dto));
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id){
        bookService.delete(id);
        return ResponseEntity.ok("Book delete successfully");
    }

    @PatchMapping("/book/activate/{id}")
    public ResponseEntity<String> activateBook(@PathVariable Long id){
        bookService.activate(id);
        return ResponseEntity.ok("Book successfully activated");
    }
}
