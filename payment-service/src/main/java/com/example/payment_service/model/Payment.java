package com.example.payment_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookingId; // Ligação à Reserva (pode ser null se for o condutor)

    // 👇 O NOVO CAMPO OBRIGATÓRIO
    private Long userId;    // ID do Passageiro ou do Condutor

    private Double amount;  // Valor pago
    private String method;  // "MBWAY", "CREDIT_CARD", "DRIVER_SHARE"
    private String status;  // "PENDING", "COMPLETED"
    private LocalDateTime paymentDate;

    public Payment() {
        this.paymentDate = LocalDateTime.now();
        this.status = "PENDING";
    }

    // Construtor utilitário atualizado
    public Payment(Long bookingId, Long userId, Double amount, String method) {
        this.bookingId = bookingId;
        this.userId = userId; // <--- Guardamos o ID aqui
        this.amount = amount;
        this.method = method;
        this.status = "PENDING";
        this.paymentDate = LocalDateTime.now();
    }

    // --- Getters e Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    // 👇 Getter e Setter do UserID
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
}