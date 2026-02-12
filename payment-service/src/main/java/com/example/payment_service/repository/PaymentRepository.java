package com.example.payment_service.repository;

import com.example.payment_service.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Encontrar pagamento por ID da reserva
    Payment findByBookingId(Long bookingId);
    List<Payment> findByUserId(Long userId);
}