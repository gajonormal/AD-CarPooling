package com.example.frontend_service.dto;

public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String type; // "DRIVER" ou "PASSENGER"

    // 👇 ESTE ERA O CAMPO QUE FALTAVA 👇
    private Double wallet = 0.0; // Inicializamos a 0.0 para não dar erro se vier nulo

    public UserDTO() {
    }

    public UserDTO(Long id, String name, String email, String password, String type, Double wallet) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = type;
        this.wallet = wallet != null ? wallet : 0.0;
    }

    // --- Getters e Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    // 👇 OBRIGATÓRIO TER O GETTER PARA O THYMELEAF FUNCIONAR
    public Double getWallet() { return wallet; }
    public void setWallet(Double wallet) { this.wallet = wallet; }
}