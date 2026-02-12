package com.example.frontend_service.dto;

import java.util.List;

public class TripHistoryDTO {
    private Long tripId;
    private String origin;
    private String destination;
    private String departureTime;
    private double price;
    private String status; // Para confirmar que é FINISHED

    // Info do Veículo
    private String vehicleInfo; // "Marca Modelo (Matrícula)"

    // Participantes
    private Long driverId;
    private String driverName;
    private List<String> passengerNames;
    private List<Long> passengerIds;

    // Campo utilitário para o HTML
    private String userRole; // "CONDUTOR" ou "PASSAGEIRO"

    // Construtor Vazio
    public TripHistoryDTO() {}

    // Getters e Setters
    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getVehicleInfo() { return vehicleInfo; }
    public void setVehicleInfo(String vehicleInfo) { this.vehicleInfo = vehicleInfo; }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public List<String> getPassengerNames() { return passengerNames; }
    public void setPassengerNames(List<String> passengerNames) { this.passengerNames = passengerNames; }

    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }

    public List<Long> getPassengerIds() { return passengerIds; }
    public void setPassengerIds(List<Long> passengerIds) { this.passengerIds = passengerIds; }
}