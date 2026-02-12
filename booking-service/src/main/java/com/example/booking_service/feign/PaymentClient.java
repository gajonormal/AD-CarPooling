package com.example.booking_service.feign;

import com.example.booking_service.dto.PaymentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// O "name" tem de ser igual ao spring.application.name do payment-service
@FeignClient(name = "payment-service")
public interface PaymentClient {

    // Isto liga diretamente ao teu método @PostMapping no PaymentController
    @PostMapping("/payments")
    PaymentDTO processPayment(@RequestBody PaymentDTO payment);
}