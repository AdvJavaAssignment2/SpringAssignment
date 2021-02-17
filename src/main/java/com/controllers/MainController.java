package com.controllers;

import com.models.Book;
import com.models.User;
import com.repository.BookRepository;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class MainController {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public MainController(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String index(Model model) {
        List<Book> availableBooks = bookRepository
                .getAllBooks().stream()
                .filter(book -> !book.getEvent().equals("deleted"))
                .collect(Collectors.toList());
        model.addAttribute("books", availableBooks);
        return "index";
    }

    @GetMapping("/borrow/{id}")
    public String borrow(@PathVariable("id") Long id, Authentication authentication) {
        Book book = bookRepository.getBook(id);
        User user = userRepository.findByUserName(authentication.getName());
        if (user.check(book)) {
            userRepository.borrowBook(user, book);
        }
        return "redirect:/";
    }
}
