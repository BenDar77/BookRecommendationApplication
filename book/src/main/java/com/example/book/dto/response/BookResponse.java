package com.example.book.dto.response;

import com.example.book.model.Book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private Book book;
    private Set<BookCategoryResponse> categories;
}