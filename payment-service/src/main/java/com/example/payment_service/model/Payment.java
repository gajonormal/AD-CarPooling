package com.example.payment_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookingId; // Ligação à Reserva
    private Double amount;  // Valor pago
    private String method;  // "MBWAY", "CREDIT_CARD", "PAYPAL"
    private String status;  // "PENDING", "COMPLETED"
    private LocalDateTime paymentDate;

    public Payment() {
        this.paymentDate = LocalDateTime.now();
        this.status = "PENDING";
    }

    // Construtor utilitário
    public Payment(Long bookingId, Double amount, String method) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.method = method;
        this.status = "PENDING";
        this.paymentDate = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
}