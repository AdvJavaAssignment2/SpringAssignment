package com.controllers;

import com.models.Book;
import com.models.User;
import com.repository.BookRepository;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    PasswordEncoder passwordEncoder;

    @Autowired
    public void PasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public UserController(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String addUser(User user, Model model) {
        if (userRepository.findByUserName(user.getUsername()) != null) {
            model.addAttribute("message", "User exists!");
            return "register";
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.addUser(user);
            return "redirect:/user/login";
        }
    }

    @GetMapping("/profile/librarian")
    public String librarianProfile(Model model) {
        model.addAttribute("students", userRepository.getAllStudents());
        model.addAttribute("books", bookRepository.getAllBooks());
        return "librarian_profile";
    }

    @GetMapping("/profile/student")
    public String studentProfile(Model model, Authentication authentication) {
        User user = userRepository.findByUserName(authentication.getName());
        model.addAttribute("books", user.getBorrowedBooks());
        return "student_profile";
    }

    @GetMapping("/return/{bookId}/{username}")
    public String returnBook(@PathVariable("bookId") Long id, @PathVariable("username") String username) {
        User user = userRepository.findByUserName(username);
        Book book = user.findBorrowedBook(id);
        userRepository.returnBook(user, book);
        return "redirect:/user/profile/librarian";
    }

    @GetMapping("/update/{username}")
    public String update(@PathVariable("username") String username, Model model) {
        User user = userRepository.findByUserName(username);
        user.setPassword("");
        model.addAttribute("user", user);
        model.addAttribute("borrowedBooks", user.getBorrowedBooks());
        model.addAttribute("allBooks", bookRepository.getAllBooks());
        return "student";
    }

    @PostMapping("/update")
    public String updateStudent(@RequestParam("oldUsername") String oldUsername, User user) {
        User user1 = userRepository.findByUserName(oldUsername);
        user1.setUsername(user.getUsername());
        user1.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.update(user1);
        return "redirect:/user/profile/librarian";
    }

    @GetMapping("/delete/{username}")
    public String delete(@PathVariable("username") String username) {
        User user = userRepository.findByUserName(username);
        if (user.getBorrowedBooks().size() == 0)
            userRepository.deleteStudent(user);
        return "redirect:/user/profile/librarian";
    }

    @GetMapping("/restore/{username}")
    public String restore(@PathVariable("username") String username) {
        User user = userRepository.findByUserName(username);
        userRepository.restoreStudent(user);
        return "redirect:/user/profile/librarian";
    }

    @PostMapping("/borrow/{username}")
    public String borrowBook(@RequestParam("addBook") Long bookId, @PathVariable("username") String username) {
        User user = userRepository.findByUserName(username);
        Book book = bookRepository.getBook(bookId);
        if (book.getCount() > 0)
            userRepository.borrowBook(user, book);
        return "redirect:/user/profile/librarian";
    }
}
