package com.example.booking_service.dto;

// Esta classe só serve para enviar os dados do Booking para o Payment
public class PaymentDTO {

    private Long bookingId;
    private Double amount;
    private String method;
    private Long userId; // <--- O campo que faltava para identificar a pessoa

    // Construtor Vazio
    public PaymentDTO() {}

    // Construtor Completo (O que o BookingService vai usar)
    public PaymentDTO(Long bookingId, Double amount, String method, Long userId) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.method = method;
        this.userId = userId;
    }

    // Getters e Setters
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}