package com.example.booking_service.feign;

import com.example.booking_service.dto.PaymentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// O nome "payment-service" tem de ser igual ao que puseste no application.properties do Payment
@FeignClient(name = "payment-service")
public interface PaymentClient {

    @PostMapping("/payments")
    Object processPayment(@RequestBody PaymentDTO payment);
}