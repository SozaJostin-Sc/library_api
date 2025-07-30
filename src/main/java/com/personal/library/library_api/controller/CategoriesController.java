package com.personal.library.library_api.controller;

import com.personal.library.library_api.dto.category.CreateCategoryDTO;
import com.personal.library.library_api.dto.category.UpdateCategoryDTO;
import com.personal.library.library_api.model.Categories;
import com.personal.library.library_api.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CategoriesController {

    private final CategoryService categoryService;

    @GetMapping("/category")
    public ResponseEntity<List<Categories>> getCategory(){
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<Categories> getCategoryById(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @PostMapping("/category")
    public ResponseEntity<Categories> createCategory(@Valid @RequestBody CreateCategoryDTO category){
        return ResponseEntity.ok(categoryService.create(category));
    }

    @PatchMapping("/category/{id}")
    public ResponseEntity<Categories> updateCategory(@PathVariable Long id, @Valid @RequestBody UpdateCategoryDTO updateCategory){
        return ResponseEntity.ok(categoryService.update(id, updateCategory));
    }

    @DeleteMapping("/category/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        categoryService.delete(id);
        return ResponseEntity.ok("Category successfully deleted with id " + id);
    }

    @DeleteMapping("/category/activate/{id}")
    public ResponseEntity<String> activatedCategory(@PathVariable Long id){
        categoryService.activate(id);
        return ResponseEntity.ok("Category successfully activated with id " + id);
    }


}
