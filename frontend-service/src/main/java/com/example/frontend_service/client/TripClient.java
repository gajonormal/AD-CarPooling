package com.example.frontend_service.client;

import com.example.frontend_service.dto.TripDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

// Cabo direto ao container trip-service na porta 8082
@FeignClient(name = "trip-service", url = "http://trip-service:8082")
public interface TripClient {

    @PostMapping("/trips")
    TripDTO createTrip(@RequestBody TripDTO trip);
    

    @GetMapping("/trips")
    List<TripDTO> getAllTrips();
}