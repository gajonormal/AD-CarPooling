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

    // 1. REGISTAR
    public String saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
        return "Utilizador registado com sucesso!";
    }

    // 2. BUSCAR POR ID
    public User getUserById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // 3. LOGIN (Atualizado para devolver o Objeto User ou null)
    public User login(String email, String password) {
        User user = repository.findByEmail(email).orElse(null);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user; // Sucesso: Devolve o utilizador todo (com ID, Nome, etc.)
        }
        return null; // Falha
    }
}