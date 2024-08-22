package com.example.book.controller;

import com.example.book.dto.request.BookCategoryRequest;
import com.example.book.model.BookCategory;
import com.example.book.service.BookCategoryService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class BookCategoryController {
    private final BookCategoryService categoryService;

    @GetMapping(path = "/category/{id}")
    public ResponseEntity<BookCategory> getBook(@PathVariable @Nonnull UUID id) {
        final BookCategory category = categoryService.getCategoryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping(path = "/categories")
    public ResponseEntity<List<BookCategory>> getAllCategories() {
        final List<BookCategory> categories = categoryService.getCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping(path = "/category")
    public ResponseEntity<BookCategory> createCategory(@RequestBody BookCategoryRequest request) {
        BookCategory category = categoryService.createCategory(request);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @PatchMapping(path = "/category/{id}")
    public ResponseEntity<BookCategory> updateCategory(@RequestBody BookCategoryRequest request,
                                                   @PathVariable @Nonnull UUID id) {
        BookCategory category = categoryService.changeCategoryName(id, request.getName());
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @DeleteMapping(path = "/category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable @Nonnull UUID id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/category/{categoryId}/book/{bookId}")
    public ResponseEntity<?> addBookToCategory (@PathVariable UUID categoryId,
                                                @PathVariable UUID bookId){
        categoryService.addBookToCategory(categoryId, bookId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/category/{categoryId}/book/{bookId}")
    public ResponseEntity<?> removeBookFromCategory (@PathVariable UUID categoryId,
                                                     @PathVariable UUID bookId){
        categoryService.removeBookFromCategory(categoryId, bookId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}