package com.example.trip_service.controller;

import com.example.trip_service.dto.UserDTO;
import com.example.trip_service.dto.VehicleDTO;
import com.example.trip_service.feign.VehicleClient;
import com.example.trip_service.model.Trip;
import com.example.trip_service.model.TripStatus; // <--- OBRIGATÓRIO
import com.example.trip_service.service.TripService;
import com.example.trip_service.feign.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripService service;

    @Autowired
    private UserClient userClient;

    @Autowired
    private VehicleClient vehicleClient;

    // 1. Criar Viagem
    @PostMapping
    public Trip createTrip(@RequestBody Trip trip) {
        // CORREÇÃO: Usar o Enum e não String
        trip.setStatus(TripStatus.CREATED);
        return service.createTrip(trip);
    }

    // 2. Listar Todas
    @GetMapping
    public List<Trip> getAllTrips() {
        return service.getAllTrips();
    }

    // 3. Procurar por destino
    @GetMapping("/search")
    public List<Trip> searchTrips(@RequestParam String destination) {
        return service.searchTrips(destination);
    }

    // 4. Ver detalhe básico
    @GetMapping("/{id}")
    public Trip getTripById(@PathVariable Long id) {
        return service.getTripById(id);
    }

    // 5. Alterar Estado da Viagem
    @PatchMapping("/{id}/status")
    public Trip updateStatus(@PathVariable Long id, @RequestParam TripStatus status) {
        // O Spring converte automaticamente o texto da URL (ex: ?status=IN_PROGRESS)
        // para o Enum TripStatus.IN_PROGRESS
        return service.updateTripStatus(id, status);
    }

    // 6. Detalhes Completos (Condutor + Veículo)
    @GetMapping("/{id}/full-details")
    public String getTripFullDetails(@PathVariable Long id) {
        Trip trip = service.getTripById(id);
        if (trip == null) return "Viagem não encontrada";

        // Buscar Condutor
        String driverName = "Desconhecido";
        try {
            UserDTO user = userClient.getUserById(trip.getDriverId());
            if (user != null) driverName = user.getName();
        } catch (Exception e) {
            driverName = "Erro Auth";
        }

        // Buscar Veículo
        String carDetails = "Carro não encontrado";
        try {
            if (trip.getVehicleId() != null) {
                VehicleDTO vehicle = vehicleClient.getVehicleById(trip.getVehicleId());
                if (vehicle != null) {
                    carDetails = vehicle.getBrand() + " " + vehicle.getModel() + " (" + vehicle.getLicensePlate() + ")";
                }
            }
        } catch (Exception e) {
            carDetails = "Erro Vehicle Service";
        }

        return "Viagem para " + trip.getDestination() +
                " | Estado: " + trip.getStatus() +
                " | Condutor: " + driverName +
                " | Veículo: " + carDetails;
    }
}