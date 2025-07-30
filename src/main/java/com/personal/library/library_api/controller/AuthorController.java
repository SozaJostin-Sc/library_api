package com.personal.library.library_api.controller;

import com.personal.library.library_api.dto.authors.CreateAuthorDTO;
import com.personal.library.library_api.dto.authors.UpdateAuthorDTO;
import com.personal.library.library_api.model.Author;
import com.personal.library.library_api.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/author")
    public ResponseEntity<List<Author>> getAll(){
        return ResponseEntity.ok(authorService.getAll());
    }

    @GetMapping("/author/{id}")
    public ResponseEntity<Author> getById(@PathVariable Long id){
        return ResponseEntity.ok(authorService.getById(id));
    }

    @PostMapping("/author")
    public ResponseEntity<Author> createAuthor(@Valid @RequestBody CreateAuthorDTO dto){
        return ResponseEntity.ok(authorService.create(dto));
    }

    @PatchMapping("/author/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @Valid @RequestBody UpdateAuthorDTO dto){
        return ResponseEntity.ok(authorService.update(id, dto));
    }

    @DeleteMapping("/author/delete/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id){
        authorService.delete(id);
        return ResponseEntity.ok("Author deleted succesfully");
    }

    @DeleteMapping("/author/{id}")
    public ResponseEntity<String> activateAuthor(@PathVariable Long id){
        authorService.activate(id);
        return ResponseEntity.ok("Author activated succesfully");
    }

}
