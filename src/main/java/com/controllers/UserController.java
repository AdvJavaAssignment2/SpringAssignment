package com.controllers;

import com.models.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    PasswordEncoder passwordEncoder;

    @Autowired
    public void PasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
    public String addUser(User user, @RequestParam("username") String username, Model model) {
        if (userService.validateUser(username) != null) {
            model.addAttribute("message", "User exists!");
            return "register";
        } else {
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.addUser(user);
            return "redirect:/user/login";
        }
    }

    @GetMapping("/profile/librarian")
    public String librarianProfile() {
        return "librarian_profile";
    }
}
