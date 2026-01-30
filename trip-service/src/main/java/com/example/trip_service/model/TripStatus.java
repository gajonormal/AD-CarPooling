package com.example.trip_service.model;

public enum TripStatus {
    CREATED,    // Criada, à espera de passageiros
    FULL,       // Cheia
    IN_PROGRESS,// A decorrer
    FINISHED,   // Acabou (pode gerar pagamentos)
    CANCELED    // Cancelada
}
