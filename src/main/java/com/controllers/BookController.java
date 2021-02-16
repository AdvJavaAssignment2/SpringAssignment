package com.controllers;

import com.models.Book;
import com.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/borrow/{id}")
    public String borrow(@PathVariable("id") Long id,Model model) {
        Book book = bookRepository.getBook(id);
        if (book.getCount() == 0) {
            model.addAttribute("error", "This book is out of stock!");
        }
        return "index";
    }
}
