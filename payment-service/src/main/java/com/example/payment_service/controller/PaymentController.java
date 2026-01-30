package com.example.payment_service.controller;

import com.example.payment_service.model.Payment;
import com.example.payment_service.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentRepository repository;

    // 1. Processar um novo pagamento (Chamado pelo Booking Service ou Frontend)
    @PostMapping
    public Payment processPayment(@RequestBody Payment payment) {
        // Simulação de processamento (na vida real, chamaria o Stripe ou SIBS)
        if (payment.getAmount() > 0) {
            payment.setStatus("COMPLETED");
        } else {
            payment.setStatus("FAILED");
        }
        return repository.save(payment);
    }

    // 2. Ver pagamento de uma reserva específica
    @GetMapping("/booking/{bookingId}")
    public Payment getPaymentByBooking(@PathVariable Long bookingId) {
        return repository.findByBookingId(bookingId);
    }

    // 3. Listar todos (para Admin)
    @GetMapping
    public List<Payment> getAllPayments() {
        return repository.findAll();
    }
}