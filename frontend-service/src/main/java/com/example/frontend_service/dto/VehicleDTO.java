package com.example.frontend_service.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
// Removemos o Lombok para garantir que funciona sempre
public class VehicleDTO {

    private Long id;
    private String brand;
    private String model;
    private String licensePlate;
    private int year;
    private String location;
    private double pricePerHour;
    private boolean isRented;

    // --- CONSTRUTORES ---
    public VehicleDTO() {}

    public VehicleDTO(Long id, String brand, String model, String licensePlate, int year, String location, double pricePerHour, boolean isRented) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.licensePlate = licensePlate;
        this.year = year;
        this.location = location;
        this.pricePerHour = pricePerHour;
        this.isRented = isRented;
    }

    // --- GETTERS E SETTERS (O que o Thymeleaf precisa!) ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }
    @JsonProperty("rented")
    public boolean getIsRented() { // Atenção: Thymeleaf procura por isRented ou getIsRented
        return isRented;
    }

    public void setIsRented(boolean rented) {
        isRented = rented;
    }
}