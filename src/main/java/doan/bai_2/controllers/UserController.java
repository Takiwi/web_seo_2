package doan.bai_2.controllers;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import doan.bai_2.dto.user.requests.RegisterRequest;
import doan.bai_2.dto.user.responses.UserResponse;
import doan.bai_2.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    
    // register
    @GetMapping("/register")
    public String register(RegisterRequest request) {
        return "access/register";
    }

    @PostMapping("/register")
    public String register(@Valid RegisterRequest request, Model model) {
        UserResponse response = userService.register(request);
        model.addAttribute("user", response.getUser());

        return "redirect:/login";
    }

    // login
    @GetMapping("/login")
    public String login() {
        return "access/login";
    }
}
