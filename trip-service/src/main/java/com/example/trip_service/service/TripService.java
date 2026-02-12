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
        trip.setStatus(TripStatus.CREATED); // Nasce sempre como CREATED

        // Prevenção de NullPointerException nas coordenadas
        if (trip.getOriginLat() == null) trip.setOriginLat(0.0);
        if (trip.getOriginLon() == null) trip.setOriginLon(0.0);
        if (trip.getDestLat() == null) trip.setDestLat(0.0);
        if (trip.getDestLon() == null) trip.setDestLon(0.0);

        return repository.save(trip);
    }

    // Listar APENAS as viagens disponíveis (CREATED)
    // Se o condutor quiser ver o histórico, usaremos outro método no futuro
    public List<Trip> getAllTrips() {
        return repository.findByStatus(TripStatus.CREATED);
    }

    // Procurar viagens por destino QUE AINDA ESTEJAM ABERTAS
    public List<Trip> searchTrips(String destination) {
        return repository.findByDestinationAndStatus(destination, TripStatus.CREATED);
    }
    // Obter detalhes
    public Trip getTripById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // --- ATUALIZAR ESTADO (Único método necessário) ---
    public Trip updateStatus(Long id, String statusStr) {
        Trip trip = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Viagem não encontrada"));

        try {
            // Converte a String "FINISHED" para o Enum TripStatus.FINISHED
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

            // Se encheu, muda o estado automaticamente
            if (trip.getAvailableSeats() == 0) {
                // Só muda para FULL se ainda estiver em CREATED (não muda se já estiver em progresso)
                if (trip.getStatus() == TripStatus.CREATED) {
                    trip.setStatus(TripStatus.FULL);
                }
            }
            repository.save(trip);
            return true;
        }
        return false;
    }
}