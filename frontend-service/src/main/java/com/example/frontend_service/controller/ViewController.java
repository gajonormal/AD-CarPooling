package com.example.frontend_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String loginPage() {
        return "login"; // Abre login.html
    }

    // --- NOVO: Rota para a Dashboard ---
    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "dashboard"; // Abre dashboard.html (que vamos criar a seguir)
    }
}