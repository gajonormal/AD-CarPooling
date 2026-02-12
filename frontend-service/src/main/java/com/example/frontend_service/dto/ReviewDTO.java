package com.example.frontend_service.dto;

import java.time.LocalDateTime;

public class ReviewDTO {
    private Long id;
    private Long reviewerId;   // Quem avalia
    private Long targetUserId; // Quem é avaliado
    private int rating;        // 1 a 5
    private String comment;
    private LocalDateTime date;

    // Construtor Vazio
    public ReviewDTO() {
        this.date = LocalDateTime.now();
    }

    // Getters e Setters
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