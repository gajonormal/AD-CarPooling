package com.example.frontend_service.dto;

import java.time.LocalDateTime; // ⚠️ Garante que tens este import!

public class BookingDTO {

    private Long id;
    private Long tripId;
    private Long passengerId;
    private Double price;
    private String status;

    // 👇 O CAMPO QUE FALTAVA!
    private LocalDateTime bookingDate;

    // Construtores
    public BookingDTO() {}

    public BookingDTO(Long id, Long tripId, Long passengerId, Double price, String status, LocalDateTime bookingDate) {
        this.id = id;
        this.tripId = tripId;
        this.passengerId = passengerId;
        this.price = price;
        this.status = status;
        this.bookingDate = bookingDate;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }

    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // 👇 GETTERS E SETTERS DO CAMPO NOVO
    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }
}