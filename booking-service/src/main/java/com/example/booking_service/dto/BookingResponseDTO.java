package com.example.booking_service.dto;

import java.time.LocalDateTime;

public class BookingResponseDTO {
    private Long bookingId;
    private String passengerName;
    private String destination;
    private LocalDateTime departureTime;
    private String status;

    // --- NOVO CAMPO: PREÇO ---
    private double price;

    // Getters e Setters
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // --- GETTER E SETTER DO PREÇO ---
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}