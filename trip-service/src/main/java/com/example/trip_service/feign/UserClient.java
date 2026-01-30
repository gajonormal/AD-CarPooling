package com.example.trip_service.feign;

import com.example.trip_service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface UserClient {
    // O prefixo "/auth" vem do @RequestMapping do Controller
    // O "/{id}" vem do @GetMapping que acabaste de criar
    @GetMapping("/auth/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);
}