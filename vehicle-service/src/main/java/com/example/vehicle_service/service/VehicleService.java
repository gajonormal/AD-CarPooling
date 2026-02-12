package com.example.vehicle_service.service;

import com.example.vehicle_service.model.Vehicle;
import com.example.vehicle_service.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository repository;

    // Guardar novo carro
    public Vehicle createVehicle(Vehicle vehicle) {
        return repository.save(vehicle);
    }

    // Listar todos (Admin ou debug)
    public List<Vehicle> getAllVehicles() {
        return repository.findAll();
    }

    // Buscar um carro específico
    public Vehicle getVehicleById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // --- O MÉTODO NOVO QUE PRECISAMOS ---
    // Buscar apenas os carros de um condutor específico
    public List<Vehicle> getVehiclesByOwner(Long ownerId) {
        return repository.findByOwnerId(ownerId);
    }
}