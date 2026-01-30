package com.example.auth_service.repository; // <--- Nota o 'auth_service' com underscore

import com.example.auth_service.model.User; // <--- Importa o User do teu pacote correto
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}