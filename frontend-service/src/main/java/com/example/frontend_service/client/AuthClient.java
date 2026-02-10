package com.example.frontend_service.client;

import com.example.frontend_service.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// Define quem vamos chamar: o "auth-service"
// O URL direto ajuda a evitar erros de rede no Docker
@FeignClient(name = "auth-service", url = "http://auth-service:8081")
public interface AuthClient {

    // Este método envia o Email/Pass e recebe o Utilizador se estiver tudo ok
    @PostMapping("/auth/login")
    UserDTO login(@RequestBody UserDTO user);
}