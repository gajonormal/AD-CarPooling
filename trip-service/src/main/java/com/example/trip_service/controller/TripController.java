package com.example.trip_service.controller;

import com.example.trip_service.dto.UserDTO;
import com.example.trip_service.dto.VehicleDTO;
import com.example.trip_service.feign.VehicleClient;
import com.example.trip_service.model.Trip;
import com.example.trip_service.model.TripStatus;
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

    @PostMapping
    public Trip createTrip(@RequestBody Trip trip) {
        return service.createTrip(trip);
    }

    @GetMapping
    public List<Trip> getAllTrips() {
        return service.getAllTrips();
    }

    @GetMapping("/search")
    public List<Trip> searchTrips(@RequestParam String destination) {
        return service.searchTrips(destination);
    }

    @GetMapping("/{id}")
    public Trip getTripById(@PathVariable Long id) {
        return service.getTripById(id);
    }

    @PutMapping("/{id}/status")
    public Trip updateStatus(@PathVariable Long id, @RequestParam String status) {
        return service.updateStatus(id, status);
    }

    @PostMapping("/{id}/reduce")
    public boolean reduceSeat(@PathVariable Long id) {
        return service.reduceSeat(id);
    }

    // --- NOVOS MÉTODOS PARA O HISTÓRICO ---

    // 8. Buscar histórico do Condutor
    @GetMapping("/history/driver/{driverId}")
    public List<Trip> getDriverHistory(@PathVariable Long driverId) {
        // Busca todas as viagens do condutor com status FINISHED
        return service.getTripsByDriverAndStatus(driverId, TripStatus.FINISHED);
    }

    // 9. Buscar detalhes de várias viagens (útil para o histórico do passageiro)
    @PostMapping("/history/list")
    public List<Trip> getTripsByIds(@RequestBody List<Long> tripIds) {
        return service.getTripsByIds(tripIds);
    }

    // --- MANTENDO O TEU MÉTODO DE DETALHES ---
    @GetMapping("/{id}/full-details")
    public String getTripFullDetails(@PathVariable Long id) {
        Trip trip = service.getTripById(id);
        if (trip == null) return "Viagem não encontrada";

        String driverName = "Desconhecido";
        try {
            UserDTO user = userClient.getUserById(trip.getDriverId());
            if (user != null) driverName = user.getName();
        } catch (Exception e) {
            driverName = "Erro Auth";
        }

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