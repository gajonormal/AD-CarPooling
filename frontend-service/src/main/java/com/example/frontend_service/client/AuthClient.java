package com.example.frontend_service.client;

import com.example.frontend_service.dto.ReviewDTO;
import com.example.frontend_service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*; // Importante para os novos mapeamentos

// O URL direto ajuda a evitar erros de rede no Docker, mantemos isso.
@FeignClient(name = "auth-service", url = "http://auth-service:8081")
public interface AuthClient {

    // --- 1. LOGIN (Mantém-se igual) ---
    @PostMapping("/auth/login")
    UserDTO login(@RequestBody UserDTO user);

    // --- 2. REGISTO (É útil teres aqui também) ---
    @PostMapping("/auth/register")
    String register(@RequestBody UserDTO user);

    // --- 3. OBTER DADOS DO PERFIL (Novo) ---
    // Vai buscar nome, telemóvel, rating, etc.
    // Backend: @GetMapping("/{id}") dentro de /auth
    @GetMapping("/auth/{id}")
    UserDTO getUser(@PathVariable("id") Long id);

    // --- 4. ATUALIZAR PERFIL (Novo) ---
    // Envia os dados novos para guardar
    // Backend: @PutMapping("/{id}") dentro de /auth
    @PutMapping("/auth/{id}")
    UserDTO updateUser(@PathVariable("id") Long id, @RequestBody UserDTO user);

    @PostMapping("/reviews")
    ReviewDTO createReview(@RequestBody ReviewDTO review);

    // No AuthClient.java
    @GetMapping("/reviews/user/{userId}/average")
    double getUserAverage(@PathVariable("userId") Long userId);
}