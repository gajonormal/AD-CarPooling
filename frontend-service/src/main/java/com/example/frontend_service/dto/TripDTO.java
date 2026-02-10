package com.example.frontend_service.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

public class TripDTO {
    private Long id;
    private String origin;
    private String destination;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // Importante para o HTML
    private LocalDateTime departureTime;

    private double price;
    private int availableSeats;
    private Long driverId; // Vamos preencher isto automaticamente com a sessão
    private Long vehicleId; // Opcional por agora
    private String status;

    // --- Getters e Setters (Podes gerar com Alt+Insert) ---
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
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}