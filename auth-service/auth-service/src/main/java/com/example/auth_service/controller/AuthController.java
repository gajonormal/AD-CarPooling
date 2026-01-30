package com.example.auth_service.controller;

import com.example.auth_service.model.User;
import com.example.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth") // <--- Isto é o que o Gateway procura!
public class AuthController {

    @Autowired
    private AuthService service;

    // Rota para testar no Browser: http://localhost:8080/auth/login
    @GetMapping("/login")
    public String loginPage() {
        return "OK";
    }

    // Rota para registar utilizadores (via Postman)
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return service.saveUser(user);
    }
    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        System.out.println("Recebi pedido para o ID: " + id); // Para veres no log
        return service.getUserById(id);
    }
}