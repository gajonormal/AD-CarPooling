package com.example.trip_service.repository;

import com.example.trip_service.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    // Método extra para procurar viagens por Destino (útil para o passageiro)
    List<Trip> findByDestination(String destination);

    // Método para procurar por Origem e Destino
    List<Trip> findByOriginAndDestination(String origin, String destination);
}
