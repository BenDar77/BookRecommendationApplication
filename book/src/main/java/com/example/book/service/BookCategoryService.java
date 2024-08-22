package com.example.book.service;

import com.example.book.dto.request.BookCategoryRequest;
import com.example.book.model.Book;
import com.example.book.model.BookCategory;
import com.example.book.repository.BookRepository;
import com.example.book.repository.BookCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookCategoryService {

    private final BookCategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    public BookCategory createCategory(BookCategoryRequest categoryRequest) {
        if (categoryRepository.findByName(categoryRequest.getName()).isPresent()) {
            throw new RuntimeException("category already exists");
        }
        BookCategory category = BookCategory.builder()
                .name(categoryRequest.getName())
                .build();
        categoryRepository.save(category);
        return category;
    }

    public BookCategory getCategoryById(UUID id) {
        BookCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("category not found"));
        log.debug("Fetched category {} from database", category.getId());
        return category;
    }

    public void deleteCategory(UUID id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            log.info("{}: Deleted category from the database with ID: {}", this.getClass().getName(), id);
        } else {
            throw new EntityNotFoundException("Category was not found with ID: " + id);
        }
    }

    public List<BookCategory> getCategories() {
        return categoryRepository.findAll();
    }

    public BookCategory changeCategoryName(UUID id, String newName) {
        BookCategory category = getCategoryById(id);
        category.setName(newName);
        return categoryRepository.save(category);
    }

    public void addBookToCategory(UUID categoryId, UUID bookId) {
        BookCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        category.addBook(book);
        categoryRepository.save(category);
    }

    public void removeBookFromCategory(UUID categoryId, UUID bookId) {
        BookCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        category.removeBook(book);
        categoryRepository.save(category);
    }

}