package com.example.booking_service.feign;

import com.example.booking_service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface AuthClient {
    @GetMapping("/auth/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);
}