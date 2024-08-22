package com.example.book.controller;

import com.example.book.dto.request.BookRequest;
import com.example.book.dto.response.BookResponse;
import com.example.book.model.Book;
import com.example.book.model.BookCategory;
import com.example.book.service.BookService;
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
public class BookController {
    private final BookService bookService;

    @GetMapping(path = "/book/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable @Nonnull UUID id) {
        final BookResponse bookResponse= bookService.getBookById(id);
        return new ResponseEntity<>(bookResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/books")
    public ResponseEntity<List<Book>> findAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok().body(books);
    }

    @PostMapping(path = "/book")
    public ResponseEntity<Book> createBook (@RequestBody BookRequest request){
        Book book = bookService.createBook(request);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @PutMapping(path = "/book/{id}")
    public ResponseEntity<Book> updateBook (@RequestBody BookRequest request,
                                            @PathVariable @Nonnull UUID id){
        Book book = bookService.updateBook(id, request);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @DeleteMapping(path = "/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable @Nonnull UUID id) {
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //FOR TESTING
    @GetMapping(path = "/bookcategories/{id}")
    public ResponseEntity<List<BookCategory>> getBooksCategories(@PathVariable @Nonnull UUID id) {
        List<BookCategory> list = bookService.getBooksCategories(id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}