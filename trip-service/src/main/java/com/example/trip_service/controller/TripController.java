package com.example.trip_service.controller;

import com.example.trip_service.dto.UserDTO;
import com.example.trip_service.model.Trip;
import com.example.trip_service.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripService service;

    // Criar Viagem: POST /trips
    @PostMapping
    public Trip createTrip(@RequestBody Trip trip) {
        return service.createTrip(trip);
    }

    // Listar Todas: GET /trips
    @GetMapping
    public List<Trip> getAllTrips() {
        return service.getAllTrips();
    }

    // Procurar por destino: GET /trips/search?destination=Lisboa
    @GetMapping("/search")
    public List<Trip> searchTrips(@RequestParam String destination) {
        return service.searchTrips(destination);
    }

    // Ver detalhe de uma viagem: GET /trips/{id}
    @GetMapping("/{id}")
    public Trip getTripById(@PathVariable Long id) {
        return service.getTripById(id);
    }
    @Autowired
    private com.example.trip_service.feign.UserClient userClient; // Injetar o Feign

    @GetMapping("/{id}/full-details")
    public String getTripWithDriverName(@PathVariable Long id) {
        // 1. Ir buscar a viagem localmente
        Trip trip = service.getTripById(id);

        if (trip == null) return "Viagem não encontrada";

        // 2. Usar o Feign para ir buscar o nome ao Auth Service
        // O try-catch serve para não dar erro se o Auth Service estiver em baixo
        String driverName = "Desconhecido";
        try {
            UserDTO user = userClient.getUserById(trip.getDriverId());
            if (user != null) {
                driverName = user.getName();
            }
        } catch (Exception e) {
            // ADICIONA ESTA LINHA ABAIXO PARA VER O ERRO NO CONSOLE
            e.printStackTrace();

            driverName = "Erro ao obter condutor: " + e.getMessage();
        }

        return "Viagem para " + trip.getDestination() + " conduzida por: " + driverName;
    }
}