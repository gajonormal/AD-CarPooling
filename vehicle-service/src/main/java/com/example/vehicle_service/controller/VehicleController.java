package com.example.vehicle_service.controller;

import com.example.vehicle_service.model.Vehicle;
import com.example.vehicle_service.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // Importa isto
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService service;

    @PostMapping
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
        return service.createVehicle(vehicle);
    }

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return service.getAllVehicles();
    }

    @GetMapping("/{id}")
    public Vehicle getVehicleById(@PathVariable Long id) {
        return service.getVehicleById(id);
    }

    @GetMapping("/owner/{ownerId}")
    public List<Vehicle> getVehiclesByOwner(@PathVariable Long ownerId) {
        return service.getVehiclesByOwner(ownerId);
    }

    // 👇 ADICIONA ESTE MÉTODO QUE ESTAVA EM FALTA 👇
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable("id") Long id) {
        service.deleteVehicle(id); // Garante que este método existe no teu VehicleService
        return ResponseEntity.noContent().build();
    }
}