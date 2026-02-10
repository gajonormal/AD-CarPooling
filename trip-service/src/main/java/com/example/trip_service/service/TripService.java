package com.example.trip_service.service;

import com.example.trip_service.model.Trip;
import com.example.trip_service.model.TripStatus; // Não te esqueças de importar o Enum!
import com.example.trip_service.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TripService {

    @Autowired
    private TripRepository repository;

    // --- CRIAR VIAGEM (Atualizado) ---
    public Trip createTrip(Trip trip) {
        // 1. Regra de Negócio: Toda a viagem nova nasce com o estado CREATED
        trip.setStatus(TripStatus.CREATED);

        // 2. Prevenção de Erros: Se o user não enviar coordenadas, metemos 0.0
        // Isto cumpre o requisito de ter campos de localização, mesmo que vazios
        if (trip.getOriginLat() == null) trip.setOriginLat(0.0);
        if (trip.getOriginLon() == null) trip.setOriginLon(0.0);
        if (trip.getDestLat() == null) trip.setDestLat(0.0);
        if (trip.getDestLon() == null) trip.setDestLon(0.0);

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

    // --- NOVO MÉTODO (Obrigatório para o Controller funcionar) ---
    // Permite mudar de CREATED -> IN_PROGRESS -> FINISHED
    public Trip updateTripStatus(Long id, TripStatus newStatus) {
        Optional<Trip> tripOptional = repository.findById(id);

        if (tripOptional.isPresent()) {
            Trip trip = tripOptional.get();
            trip.setStatus(newStatus);
            return repository.save(trip);
        }
        return null; // Retorna null se a viagem não existir
    }
    // Lógica para diminuir 1 lugar
    public boolean reduceSeat(Long tripId) {
        Trip trip = repository.findById(tripId).orElse(null);

        // Se a viagem existe e tem lugares > 0
        if (trip != null && trip.getAvailableSeats() > 0) {
            trip.setAvailableSeats(trip.getAvailableSeats() - 1);

            // Se ficou com 0, muda estado para FULL
            if (trip.getAvailableSeats() == 0) {
                trip.setStatus(TripStatus.valueOf("FULL"));
            }

            repository.save(trip);
            return true; // Sucesso
        }
        return false; // Falha (não há lugares ou viagem não existe)
    }
}