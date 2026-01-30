package com.example.booking_service.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name; // Usamos 'name' porque é onde está o "Bernardo" na BD
    private String email;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}