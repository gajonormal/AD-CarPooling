package com.example.auth_service.controller;

import com.example.auth_service.model.User;
import com.example.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    // --- AQUI ESTÁ A ALTERAÇÃO ---
    // 1. Mudamos de GET para POST (Segurança)
    // 2. O Frontend envia um JSON com { "email": "...", "password": "..." }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User loggedUser = service.login(user.getEmail(), user.getPassword());

        if (loggedUser != null) {
            return ResponseEntity.ok(loggedUser); // 200 OK + JSON do User
        }
        return ResponseEntity.status(401).body("Email ou Password errados");
    }

    // O resto mantém-se igual
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return service.saveUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        System.out.println("Recebi pedido para o ID: " + id);
        return service.getUserById(id);
    }
}