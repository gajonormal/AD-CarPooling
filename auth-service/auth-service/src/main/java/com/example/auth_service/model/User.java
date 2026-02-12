package com.example.auth_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String password;

    private String email;

    private String type;

    private String preferences;

    private Double currentRating = 5.0;
    private Integer ratingCount = 0;

    // --- CAMPO OBRIGATÓRIO PARA A ADMINISTRAÇÃO ---
    private boolean suspended = false; // Começa como falso (ativo)

    // --- CONSTRUTORES ---

    public User() {
    }

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.type = type;
    }

    // --- GETTERS E SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public Double getCurrentRating() {
        return currentRating;
    }

    public void setCurrentRating(Double currentRating) {
        this.currentRating = currentRating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    // --- GETTERS E SETTERS DA SUSPENSÃO (Sem isto o Admin falha!) ---
    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }
}