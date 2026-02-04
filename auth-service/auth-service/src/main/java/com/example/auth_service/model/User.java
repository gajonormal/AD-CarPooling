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

    private String email; // <--- O CAMPO QUE FALTAVA

    private String type;

    // --- CONSTRUTORES ---

    public User() {
    }

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.type = type;
    }

    // --- GETTERS E SETTERS MANUAIS ---

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

    public String getEmail() { // <--- Getter do Email
        return email;
    }

    public void setEmail(String email) { // <--- Setter do Email
        this.email = email;
    }
    public String getType() { return type; }       // <--- Novo Getter
    public void setType(String type) { this.type = type; } // <--- Novo Setter
}