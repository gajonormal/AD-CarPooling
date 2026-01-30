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

    // --- AQUI ESTÁ A CORREÇÃO ---
    // "year" é uma palavra proíbida no SQL, por isso mudamos o nome da coluna
    @Column(name = "vehicle_year")
    private int year;

    private Long ownerId;

    // Construtores
    public Vehicle() {}

    public Vehicle(String brand, String model, String licensePlate, int year, Long ownerId) {
        this.brand = brand;
        this.model = model;
        this.licensePlate = licensePlate;
        this.year = year;
        this.ownerId = ownerId;
    }

    // Getters e Setters
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
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
}