package com.example.auth_service.controller;

import com.example.auth_service.model.Review;
import com.example.auth_service.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository repository;

    // 1. Criar uma nova avaliação
    @PostMapping
    public Review createReview(@RequestBody Review review) {
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new RuntimeException("A nota deve ser entre 1 e 5");
        }
        return repository.save(review);
    }

    // 2. Ver as avaliações de um utilizador (ex: condutor)
    @GetMapping("/user/{userId}")
    public List<Review> getUserReviews(@PathVariable Long userId) {
        return repository.findByTargetUserId(userId);
    }

    // 3. Calcular a média de estrelas de um utilizador (Opcional mas valorizado)
    @GetMapping("/user/{userId}/average")
    public double getUserAverage(@PathVariable Long userId) {
        List<Review> reviews = repository.findByTargetUserId(userId);
        if (reviews.isEmpty()) return 0.0;

        double sum = 0;
        for (Review r : reviews) {
            sum += r.getRating();
        }
        return sum / reviews.size();
    }
}