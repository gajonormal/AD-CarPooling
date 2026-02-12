package com.example.trip_service.service;

import com.example.trip_service.model.Trip;
import com.example.trip_service.model.TripStatus;
import com.example.trip_service.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {

    @Autowired
    private TripRepository repository;

    // --- CRIAR VIAGEM ---
    public Trip createTrip(Trip trip) {
        trip.setStatus(TripStatus.CREATED);

        if (trip.getOriginLat() == null) trip.setOriginLat(0.0);
        if (trip.getOriginLon() == null) trip.setOriginLon(0.0);
        if (trip.getDestLat() == null) trip.setDestLat(0.0);
        if (trip.getDestLon() == null) trip.setDestLon(0.0);

        return repository.save(trip);
    }

    public List<Trip> getAllTrips() {
        return repository.findByStatus(TripStatus.CREATED);
    }

    public List<Trip> searchTrips(String destination) {
        return repository.findByDestinationAndStatus(destination, TripStatus.CREATED);
    }

    public Trip getTripById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // --- ATUALIZAR ESTADO ---
    public Trip updateStatus(Long id, String statusStr) {
        Trip trip = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Viagem não encontrada"));

        try {
            TripStatus newStatus = TripStatus.valueOf(statusStr.toUpperCase());
            trip.setStatus(newStatus);
            return repository.save(trip);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado inválido: " + statusStr);
        }
    }

    // --- REDUZIR LUGARES ---
    public boolean reduceSeat(Long tripId) {
        Trip trip = repository.findById(tripId).orElse(null);

        if (trip != null && trip.getAvailableSeats() > 0) {
            trip.setAvailableSeats(trip.getAvailableSeats() - 1);

            if (trip.getAvailableSeats() == 0) {
                if (trip.getStatus() == TripStatus.CREATED) {
                    trip.setStatus(TripStatus.FULL);
                }
            }
            repository.save(trip);
            return true;
        }
        return false;
    }

    // ==========================================================
    //       NOVO: MÉTODOS PARA O HISTÓRICO 📜
    // ==========================================================

    // 1. Histórico do Condutor: Viagens dele que estão FINISHED
    public List<Trip> getTripsByDriverAndStatus(Long driverId, TripStatus status) {
        return repository.findByDriverIdAndStatus(driverId, status);
    }

    // 2. Histórico do Passageiro: O TripService recebe uma lista de IDs do BookingService
    // e devolve os detalhes dessas viagens
    public List<Trip> getTripsByIds(List<Long> ids) {
        return repository.findAllById(ids);
    }
}