package com.example.frontend_service.client;

import com.example.frontend_service.dto.VehicleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "vehicle-service", url = "http://vehicle-service:8085")
public interface VehicleClient {

    // 1. REGISTAR O MEU CARRO (Essencial para o Condutor)
    // O condutor usa isto para dizer: "Tenho um carro novo"
    @PostMapping("/vehicles")
    VehicleDTO createVehicle(@RequestBody VehicleDTO vehicle);

    // 2. VER CARROS (Para listar na garagem)
    @GetMapping("/vehicles")
    List<VehicleDTO> getAllVehicles();

    // (Opcional) Se quiseres ver só um carro específico
    @GetMapping("/vehicles/{id}")
    VehicleDTO getVehicleById(@PathVariable("id") Long id);
}