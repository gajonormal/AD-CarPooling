package com.example.frontend_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients; // <--- IMPORTA ISTO

@SpringBootApplication
@EnableFeignClients // <--- ADICIONA ESTA LINHA MÁGICA
public class FrontendServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontendServiceApplication.class, args);
    }
}