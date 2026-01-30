package com.example.auth_service.repository;

import com.example.auth_service.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Encontrar todas as reviews que um utilizador recebeu
    List<Review> findByTargetUserId(Long targetUserId);
}