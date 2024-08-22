package com.example.book.service;

import com.example.book.dto.request.BookRequest;
import com.example.book.dto.response.BookResponse;
import com.example.book.dto.response.BookCategoryResponse;
import com.example.book.model.Book;
import com.example.book.model.BookCategory;
import com.example.book.repository.BookRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final BookRepository bookRepository;


    public Book createBook (BookRequest bookRequest){
        if(bookRepository.findByIsbn(bookRequest.getIsbn()).isPresent()){
            throw new EntityExistsException("book with this ISBN already exists");
        }
        Book book = Book.builder()
                .name(bookRequest.getName())
                .description(bookRequest.getDescription())
                .isbn(bookRequest.getIsbn())
                .author(bookRequest.getAuthor())
                .pagesCount(bookRequest.getPagesCount())
                .build();
        bookRepository.save(book);
        return book;
    }

    public void deleteBook (UUID id){
        if (bookRepository.existsById(id)) {
            List<BookCategory> list = getBooksCategories(id);
            list.stream().forEach(cat -> cat.removeBook(bookRepository.getReferenceById(id)));
            bookRepository.deleteById(id);
            log.info("{}: Deleted book from the database with ID: {}", this.getClass().getName(), id);
        } else {
            throw new EntityNotFoundException("Book was not found with ID: " + id);
        }
    }

    public Book updateBook (UUID id, BookRequest request){
        Book bookToUpdate = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("book not found with id: " + id)
        );
        bookToUpdate.setName(request.getName());
        bookToUpdate.setIsbn(request.getIsbn());
        bookToUpdate.setAuthor(request.getAuthor());
        bookToUpdate.setDescription(request.getDescription());
        bookToUpdate.setPagesCount(request.getPagesCount());
        bookRepository.save(bookToUpdate);
        return bookToUpdate;
    }

    public BookResponse getBookById(UUID id){
        Book book =  bookRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("book not found with id: " + id));
        log.debug("Fetched book {} from database", book.getId());
        Set<BookCategoryResponse> set = getBooksCategories(id)
                .stream()
                .map(o-> new BookCategoryResponse(o.getId(),o.getName()))
                .collect(Collectors.toSet());
        return BookResponse.builder()
                .book(book)
                .categories(set)
                .build();
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }



    public List<BookCategory> getBooksCategories (UUID bookId){
        return bookRepository.findBooksCategories(bookId);
    }
}