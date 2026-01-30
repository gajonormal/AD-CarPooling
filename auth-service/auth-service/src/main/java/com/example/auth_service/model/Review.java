package com.example.auth_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reviewerId;   // Quem escreveu a avaliação (ex: passageiro)
    private Long targetUserId; // Quem recebeu a avaliação (ex: condutor)

    private int rating;        // 1 a 5 estrelas
    private String comment;    // "Conduz muito bem!"
    private LocalDateTime date;

    public Review() {
        this.date = LocalDateTime.now();
    }

    // --- Getters e Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getReviewerId() { return reviewerId; }
    public void setReviewerId(Long reviewerId) { this.reviewerId = reviewerId; }

    public Long getTargetUserId() { return targetUserId; }
    public void setTargetUserId(Long targetUserId) { this.targetUserId = targetUserId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}