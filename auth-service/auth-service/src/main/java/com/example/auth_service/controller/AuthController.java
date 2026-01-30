package com.example.auth_service.controller;

import com.example.auth_service.model.User;
import com.example.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String login(@RequestBody User user) {
        // CORREÇÃO: Usamos getEmail() porque mudaste o AuthService para validar email!
        return service.login(user.getEmail(), user.getPassword());
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