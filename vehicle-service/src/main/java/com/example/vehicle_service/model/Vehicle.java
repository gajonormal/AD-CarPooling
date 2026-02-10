package com.example.vehicle_service.model;

import jakarta.persistence.*;

@Entity
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;
    private String licensePlate;
    private String location;     // Ex: "Lisboa - Aeroporto"

    @Column(name = "vehicle_year")
    private int year;

    // --- NOVOS CAMPOS PARA CAR SHARING ---
    private double pricePerHour; // Quanto custa alugar
    private boolean isRented;    // true = ocupado, false = livre

    // Construtor Vazio
    public Vehicle() {}

    // Getters e Setters (Essenciais!)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(double pricePerHour) { this.pricePerHour = pricePerHour; }

    public boolean isRented() { return isRented; }
    public void setRented(boolean rented) { isRented = rented; }
}