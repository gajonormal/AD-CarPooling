package com.example.booking_service.dto;

public class PaymentDTO {
    private Long bookingId;
    private Double amount;
    private String method; // MBWAY, VISA, etc.

    public PaymentDTO(Long bookingId, Double amount, String method) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.method = method;
    }

    // Getters e Setters
    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
}