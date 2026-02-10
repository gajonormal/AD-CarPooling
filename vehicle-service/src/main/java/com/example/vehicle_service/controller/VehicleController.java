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

    // 1. Criar novo carro (Admin ou Empresa)
    @PostMapping
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
        vehicle.setRented(false); // Nasce sempre livre
        return repository.save(vehicle);
    }

    // 2. Listar APENAS carros livres (Para o Cliente escolher)
    @GetMapping("/available")
    public List<Vehicle> getAvailableVehicles() {
        return repository.findByIsRentedFalse();
    }

    // 3. Listar TODOS (Para o Admin ver o inventário)
    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return repository.findAll();
    }

    // 4. ALUGAR (Muda o estado para Ocupado)
    @PostMapping("/{id}/rent")
    public Vehicle rentVehicle(@PathVariable Long id) {
        Vehicle v = repository.findById(id).orElse(null);

        if (v == null) {
            throw new RuntimeException("Veículo não encontrado!");
        }
        if (v.isRented()) {
            throw new RuntimeException("Este carro já está alugado!");
        }

        v.setRented(true); // Bloqueia o carro
        return repository.save(v);
    }

    // 5. DEVOLVER (Muda o estado para Livre)
    @PostMapping("/{id}/return")
    public Vehicle returnVehicle(@PathVariable Long id) {
        Vehicle v = repository.findById(id).orElse(null);

        if (v == null) {
            throw new RuntimeException("Veículo não encontrado!");
        }

        v.setRented(false); // Liberta o carro
        return repository.save(v);
    }
}