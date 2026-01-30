package com.example.vehicle_service.controller;

import com.example.vehicle_service.model.Vehicle;
import com.example.vehicle_service.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleRepository repository;

    // 1. Criar Veículo
    @PostMapping
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
        return repository.save(vehicle);
    }

    // 2. Obter Veículo por ID
    @GetMapping("/{id}")
    public Vehicle getVehicle(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    // 3. Listar Veículos de um Condutor
    @GetMapping("/owner/{ownerId}")
    public List<Vehicle> getVehiclesByOwner(@PathVariable Long ownerId) {
        return repository.findByOwnerId(ownerId);
    }
}