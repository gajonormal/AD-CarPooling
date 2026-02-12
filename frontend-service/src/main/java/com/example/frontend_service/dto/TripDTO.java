package com.example.frontend_service.dto;

import java.time.LocalDateTime;

public class TripDTO {

    private Long id;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private double price;
    private int availableSeats;
    private Long driverId;

    // 👇 ESTES ERAM OS QUE FALTAVAM E CAUSAVAM O ERRO 500 👇
    private Long vehicleId;
    private String status;  // "CREATED", "FINISHED", etc.

    // Construtores
    public TripDTO() {}

    public TripDTO(Long id, String origin, String destination, double price, int availableSeats, Long driverId, Long vehicleId, String status) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.price = price;
        this.availableSeats = availableSeats;
        this.driverId = driverId;
        this.vehicleId = vehicleId;
        this.status = status;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    // 👇 Getters e Setters dos Novos Campos 👇
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}