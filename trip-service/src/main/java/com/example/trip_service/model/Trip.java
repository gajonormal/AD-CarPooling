package com.example.trip_service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String origin;
    private String destination;
    private LocalDateTime departureTime; // Data e Hora da viagem
    private double price;
    private int availableSeats;

    // Guardamos apenas o ID do condutor (que vem do Auth Service)
    private Long driverId;

    // --- NOVOS CAMPOS ADICIONADOS (Para cumprir o Enunciado) ---

    // 1. Associação ao Carro: Para sabermos em que veículo a viagem é feita
    // O enunciado "sugere" registo de veículos, isto cumpre esse requisito funcional.
    private Long vehicleId;

    // 2. Estado da Viagem: Para gerir o ciclo de vida (Criada -> Em Curso -> Finalizada)
    @Enumerated(EnumType.STRING)
    private TripStatus status;

    // 3. Simulação de GPS (Latitude e Longitude)
    // Necessário para o requisito de "Geolocalização de viagens"
    private Double originLat;
    private Double originLon;
    private Double destLat;
    private Double destLon;

    // --- Construtores ---

    public Trip() {}

    // Construtor atualizado (opcional, podes manter o antigo se preferires instanciar via Setters)
    public Trip(String origin, String destination, LocalDateTime departureTime, double price, int availableSeats, Long driverId, Long vehicleId, TripStatus status) {
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.price = price;
        this.availableSeats = availableSeats;
        this.driverId = driverId;
        this.vehicleId = vehicleId;
        this.status = status;
    }

    // --- Getters e Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    // --- Novos Getters e Setters ---

    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }

    public TripStatus getStatus() { return status; }
    public void setStatus(TripStatus status) { this.status = status; }

    public Double getOriginLat() { return originLat; }
    public void setOriginLat(Double originLat) { this.originLat = originLat; }

    public Double getOriginLon() { return originLon; }
    public void setOriginLon(Double originLon) { this.originLon = originLon; }

    public Double getDestLat() { return destLat; }
    public void setDestLat(Double destLat) { this.destLat = destLat; }

    public Double getDestLon() { return destLon; }
    public void setDestLon(Double destLon) { this.destLon = destLon; }
}