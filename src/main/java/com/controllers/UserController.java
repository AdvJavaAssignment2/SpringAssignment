package com.controllers;

import com.models.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String validateUser(@ModelAttribute("user") User user, Model model, HttpServletRequest request) {
        User validatedUser = userService.validateUser(user.getUsername(), user.getPassword());
        HttpSession session = request.getSession();
        if (validatedUser == null) {
            model.addAttribute("message", "User not found!");
            return "login";
        } else {
            session.setAttribute("user", user.getUsername());
            session.setMaxInactiveInterval(-1);
            if (validatedUser.getRole().getName().equals("ROLE_LIBRARIAN")) {
                model.addAttribute("username", session.getAttribute("user"));
                return "librarian_profile";
            }
            if (validatedUser.getRole().getName().equals("ROLE_STUDENT")) return "student_profile";
        }
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/user/login";
    }
}
