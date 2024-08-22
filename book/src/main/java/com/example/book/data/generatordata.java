package com.example.book.data;

import com.github.javafaker.Faker;

import com.example.book.model.Book;
import com.example.book.model.BookCategory;

import com.example.book.repository.BookRepository;
import com.example.book.repository.BookCategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class generatordata {

    private final BookRepository bookRepository;
    private final BookCategoryRepository categoryRepository;

    @PostConstruct
    public void generateData(){
        Faker faker = new Faker();

        for (int i = 0; i < 10; i++){
            Book book = new Book();
            book.setAuthor(faker.book().author());
            book.setName(faker.book().title());
            book.setIsbn(faker.lorem().characters(10));
            book.setDescription(faker.lorem().characters(20,50));
            book.setPagesCount(faker.number().numberBetween(100,400));

            bookRepository.save(book);
        }

        for (int i = 0; i < 10; i++){
            BookCategory category = new BookCategory();

            category.setName(faker.book().genre());
            category.addBook(bookRepository.findAll().get(faker.number().numberBetween(0,9)));
            category.addBook(bookRepository.findAll().get(faker.number().numberBetween(0,9)));

            categoryRepository.save(category);
        }


    }

}