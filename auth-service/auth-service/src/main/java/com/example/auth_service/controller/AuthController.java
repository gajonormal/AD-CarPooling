package com.example.auth_service.controller;

import com.example.auth_service.model.Review;
import com.example.auth_service.model.User;
import com.example.auth_service.repository.ReviewRepository;
import com.example.auth_service.repository.UserRepository;
import com.example.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    // ==========================================
    //       LOGIN E REGISTO (ATUALIZADO)
    // ==========================================

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User loggedUser = service.login(user.getEmail(), user.getPassword());

        if (loggedUser != null) {
            // --- NOVO: BLOQUEIO DE SEGURANÇA ---
            if (loggedUser.isSuspended()) {
                return ResponseEntity.status(403).body("CONTA SUSPENSA: Contacte o administrador.");
            }
            return ResponseEntity.ok(loggedUser);
        }
        return ResponseEntity.status(401).body("Email ou Password errados");
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return service.saveUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        return service.getUserById(id);
    }

    // ==========================================
    //       PERFIL E AVALIAÇÕES
    // ==========================================

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userUpdates) {
        return userRepository.findById(id)
                .map(user -> {
                    if (userUpdates.getName() != null) user.setName(userUpdates.getName());
                    if (userUpdates.getEmail() != null && !userUpdates.getEmail().isEmpty()) {
                        user.setEmail(userUpdates.getEmail());
                    }
                    if (userUpdates.getPassword() != null && !userUpdates.getPassword().isEmpty()) {
                        user.setPassword(userUpdates.getPassword());
                    }
                    if (userUpdates.getPreferences() != null) user.setPreferences(userUpdates.getPreferences());

                    return ResponseEntity.ok(userRepository.save(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{targetUserId}/rate")
    public ResponseEntity<?> rateUser(@PathVariable Long targetUserId, @RequestBody Review review) {
        return userRepository.findById(targetUserId).map(targetUser -> {
            review.setTargetUserId(targetUserId);
            review.setDate(LocalDateTime.now());
            reviewRepository.save(review);

            double currentRating = targetUser.getCurrentRating() != null ? targetUser.getCurrentRating() : 5.0;
            int currentCount = targetUser.getRatingCount() != null ? targetUser.getRatingCount() : 0;

            double currentTotal = currentRating * currentCount;
            double newTotal = currentTotal + review.getRating();
            int newCount = currentCount + 1;

            targetUser.setRatingCount(newCount);
            targetUser.setCurrentRating(newTotal / newCount);

            userRepository.save(targetUser);

            return ResponseEntity.ok("Avaliação registada com sucesso!");
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/reviews")
    public List<Review> getUserReviews(@PathVariable Long id) {
        return reviewRepository.findByTargetUserId(id);
    }

    // ==========================================
    //       NOVO: FUNCIONALIDADES DE ADMIN
    // ==========================================

    // 1. Listar TODOS os utilizadores (Para o Admin Service ver a tabela)
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 2. Suspender/Ativar Conta (Para o botão do Admin funcionar)
    @PutMapping("/users/{id}/suspend")
    public ResponseEntity<?> toggleSuspension(@PathVariable Long id) {
        return userRepository.findById(id).map(user -> {
            user.setSuspended(!user.isSuspended()); // Inverte o estado atual
            userRepository.save(user);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}