package com.example.frontend_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // <--- Isto define que é MVC
public class ViewController {

    // Quando acederem a http://localhost:9090/
    @GetMapping("/")
    public String loginPage() {
        return "login"; // Vai procurar o ficheiro login.html na pasta templates
    }
}
