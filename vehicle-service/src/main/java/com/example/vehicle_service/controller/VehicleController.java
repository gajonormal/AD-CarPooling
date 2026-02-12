package com.example.vehicle_service.controller;

import com.example.vehicle_service.model.Vehicle;
import com.example.vehicle_service.service.VehicleService; // Importa o Service que criámos
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService service; // Usamos o Service, não o Repository direto

    // 1. Condutor regista um novo carro
    @PostMapping
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
        return service.createVehicle(vehicle);
    }

    // 2. (Opcional) Admin ver todos os carros do sistema
    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return service.getAllVehicles();
    }

    // 3. (Opcional) Ver detalhes de um carro específico
    @GetMapping("/{id}")
    public Vehicle getVehicleById(@PathVariable Long id) {
        return service.getVehicleById(id);
    }

    // 4. Condutor vê a SUA lista de carros (Para escolher na Viagem)
    // --- ESTE É O ENDPOINT IMPORTANTE ---
    @GetMapping("/owner/{ownerId}")
    public List<Vehicle> getVehiclesByOwner(@PathVariable Long ownerId) {
        return service.getVehiclesByOwner(ownerId);
    }
}