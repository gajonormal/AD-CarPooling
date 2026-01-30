package com.example.booking_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tripId;      // ID da viagem (vem do Trip Service)
    private Long passengerId; // ID do utilizador (vem do Auth Service)

    private LocalDateTime bookingDate;
    private String status; // Ex: "CONFIRMED", "CANCELLED", "PAID"

    // --- NOVO CAMPO OBRIGATÓRIO (CUSTOS) ---
    // Armazena o valor que este passageiro específico vai pagar
    private Double price;

    // Construtor vazio obrigatório
    public Booking() {
        this.bookingDate = LocalDateTime.now();
        this.status = "CONFIRMED";
        this.price = 0.0; // Inicializamos a zero para não dar erro
    }

    // --- Getters e Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Novos Getters e Setters para o Preço
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}