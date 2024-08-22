package com.example.book.repository;

import com.example.book.model.Book;
import com.example.book.model.BookCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

    Page<Book> findByNameContainingIgnoreCase(PageRequest pageRequest, String contains);
    Optional<Book> findByIsbn (String isbn);

    @Query("SELECT c FROM BookCategory c JOIN c.books b WHERE b.id = ?1")
    List<BookCategory> findBooksCategories(UUID bookId);



}