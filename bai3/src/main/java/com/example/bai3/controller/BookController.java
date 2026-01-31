package com.example.bai3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.bai3.model.Book;
import com.example.bai3.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // 📌 GET ALL
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    // 📌 GET BY ID
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable int id) {
        return bookService.getBookById(id);
    }

    // 📌 ADD BOOK
    @PostMapping
    public String addBook(@RequestBody Book book) {
        bookService.addBook(book);
        return "Book added successfully!";
    }

    // 📌 UPDATE BOOK
    @PutMapping("/{id}")
    public String updateBook(
            @PathVariable int id,
            @RequestBody Book updatedBook) {

        bookService.updateBook(id, updatedBook);
        return "Book updated successfully!";
    }

    // 📌 DELETE BOOK
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);
        return "Book deleted successfully!";
    }
}
