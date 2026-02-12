package com.example.frontend_service.client;

import com.example.frontend_service.dto.TripDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Cabo direto ao container trip-service na porta 8082
@FeignClient(name = "trip-service", url = "http://trip-service:8082")
public interface TripClient {

    @PostMapping("/trips")
    TripDTO createTrip(@RequestBody TripDTO trip);
    

    @GetMapping("/trips")
    List<TripDTO> getAllTrips();

    @PutMapping("/trips/{id}/status")
    void updateStatus(@PathVariable("id") Long id, @RequestParam("status") String status);
}
