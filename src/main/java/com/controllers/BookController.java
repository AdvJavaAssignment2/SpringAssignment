package com.controllers;

import com.models.Book;
import com.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/book")
public class BookController {

    private final BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostMapping("/add")
    public String addBook(Book book) {
        if (book.getCount() < 0)
            return "redirect:/user/profile/librarian";
        bookRepository.addBook(book);
        return "redirect:/user/profile/librarian";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, Model model) {
        model.addAttribute("book", bookRepository.getBook(id));
        return "book";
    }

    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable("id") Long id, Book book) {
        if (book.getCount() < 0)
            return "redirect:/user/profile/librarian";
        Book book1 = bookRepository.getBook(id);
        book1.setName(book.getName());
        book1.setAuthor(book.getAuthor());
        book1.setCount(book.getCount());
        bookRepository.updateBook(book1);
        return "redirect:/user/profile/librarian";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        Book book = bookRepository.getBook(id);
        bookRepository.deleteBook(book);
        return "redirect:/user/profile/librarian";
    }

    @GetMapping("/restore/{id}")
    public String restoreBook(@PathVariable("id") Long id) {
        Book book = bookRepository.getBook(id);
        book.setEvent("restored");
        bookRepository.updateBook(book);
        return "redirect:/user/profile/librarian";
    }
}
