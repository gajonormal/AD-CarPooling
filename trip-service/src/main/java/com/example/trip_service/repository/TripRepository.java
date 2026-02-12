package com.example.trip_service.repository;

import com.example.trip_service.model.Trip;
import com.example.trip_service.model.TripStatus; // Garante que tens este import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    // 1. Pesquisa por destino (apenas viagens abertas/criadas)
    List<Trip> findByDestinationAndStatus(String destination, TripStatus status);

    // 2. Listar todas por estado
    List<Trip> findByStatus(TripStatus status);

    // 3. 👇 A CORREÇÃO: Buscar VIAGENS pelo ID do Condutor (não Veículos)
    List<Trip> findByDriverId(Long driverId);

    List<Trip> findByDriverIdAndStatus(Long driverId, TripStatus status);

}