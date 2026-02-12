package com.example.frontend_service.dto;

public class VehicleDTO {

    private Long id;
    private String brand;
    private String model;
    private String licensePlate;

    // 👇 O CAMPO CRÍTICO QUE FALTAVA 👇
    private Long ownerId;

    // --- CONSTRUTORES ---
    public VehicleDTO() {}

    public VehicleDTO(Long id, String brand, String model, String licensePlate, Long ownerId) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.licensePlate = licensePlate;
        this.ownerId = ownerId;
    }

    // --- GETTERS E SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
}