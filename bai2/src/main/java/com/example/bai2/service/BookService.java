package com.example.bai2.service;

import org.springframework.stereotype.Service;

import com.example.bai2.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private List<Book> books = new ArrayList<>();

    public BookService() {
        books.add(new Book(1, "Java Co Ban", "Nguyen Van A"));
        books.add(new Book(2, "Spring Boot", "Tran Van B"));
    }

    // Lấy tất cả sách
    public List<Book> getAllBooks() {
        return books;
    }

    // Lấy sách theo ID
    public Book getBookById(int id) {
        Optional<Book> book = books.stream()
                .filter(b -> b.getId() == id)
                .findFirst();
        return book.orElse(null);
    }

    // Thêm sách
    public void addBook(Book book) {
        books.add(book);
    }

    // ✅ CẬP NHẬT SÁCH (PUT)
    public void updateBook(int id, Book updatedBook) {
        books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .ifPresent(book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setAuthor(updatedBook.getAuthor());
                });
    }

    // ✅ XOÁ SÁCH (DELETE)
    public void deleteBook(int id) {
        books.removeIf(book -> book.getId() == id);
    }
}
