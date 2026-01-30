package com.example.auth_service.service;

import com.example.auth_service.model.User;
import com.example.auth_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        repository.save(user);
        return "Utilizador registado com sucesso!";
    }

    // Dentro do AuthService.java
    public User getUserById(Long id) {
        return repository.findById(id).orElse(null);
    }
}