package com.example.vehicle_service.repository;

import com.example.vehicle_service.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // Método mágico do Spring Data JPA:
    // Encontra todos onde o campo "isRented" é falso
    List<Vehicle> findByIsRentedFalse();
}