package com.github.vadimher.library.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String root() {
        return "login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/user-dashboard")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String userDashboard() {
        return "user-dashboard";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}