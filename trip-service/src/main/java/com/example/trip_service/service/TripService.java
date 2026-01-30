package com.example.trip_service.service;

import com.example.trip_service.model.Trip;
import com.example.trip_service.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {

    @Autowired
    private TripRepository repository;

    // Criar uma nova viagem
    public Trip createTrip(Trip trip) {
        return repository.save(trip);
    }

    // Listar todas as viagens
    public List<Trip> getAllTrips() {
        return repository.findAll();
    }

    // Procurar viagens por destino (ex: "Lisboa")
    public List<Trip> searchTrips(String destination) {
        return repository.findByDestination(destination);
    }

    // Obter detalhes de uma viagem específica
    public Trip getTripById(Long id) {
        return repository.findById(id).orElse(null);
    }
}