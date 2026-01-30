package com.example.booking_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TripDTO {
    private Long id;
    private String destination;
    private Long driverId;
    private LocalDateTime departureTime;

    // --- NOVO CAMPO OBRIGATÓRIO ---
    // Necessário para o Booking saber quanto custa a viagem
    private double price;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }

    // --- GETTER E SETTER DO PREÇO ---
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}