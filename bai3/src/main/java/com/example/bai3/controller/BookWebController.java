package com.example.bai3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.bai3.model.Book;
import com.example.bai3.service.BookService;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookWebController {

    @Autowired
    private BookService bookService;

    // Hiển thị danh sách sách
    @GetMapping
    public String getAllBooks(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "books";
    }

    // Hiển thị form thêm sách mới
    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    // Xử lý thêm sách mới
    @PostMapping("/add")
    public String addBook(@ModelAttribute Book book) {
        // Tạo ID mới (ID lớn nhất + 1)
        int maxId = bookService.getAllBooks().stream()
                .mapToInt(Book::getId)
                .max()
                .orElse(0);
        book.setId(maxId + 1);
        
        bookService.addBook(book);
        return "redirect:/books";
    }

    // Hiển thị form sửa sách
    @GetMapping("/edit/{id}")
    public String showEditBookForm(@PathVariable int id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "edit-book";
    }

    // Xử lý cập nhật sách
    @PostMapping("/edit/{id}")
    public String updateBook(@PathVariable int id, @ModelAttribute Book book) {
        bookService.updateBook(id, book);
        return "redirect:/books";
    }

    // Xóa sách
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}
