package com.example.frontend_service.client;

import com.example.frontend_service.dto.TripDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "trip-service", url = "http://trip-service:8082")
public interface TripClient {

    @PostMapping("/trips")
    TripDTO createTrip(@RequestBody TripDTO trip);

    @GetMapping("/trips")
    List<TripDTO> getAllTrips();

    @GetMapping("/trips/{id}")
    TripDTO getTripById(@PathVariable("id") Long id);

    @PutMapping("/trips/{id}/status")
    void updateStatus(@PathVariable("id") Long id, @RequestParam("status") String status);

    // --- NOVOS MÉTODOS PARA O HISTÓRICO ---

    @GetMapping("/trips/history/driver/{driverId}")
    List<TripDTO> getDriverHistory(@PathVariable("driverId") Long driverId);

    @PostMapping("/trips/history/list")
    List<TripDTO> getTripsByIds(@RequestBody List<Long> tripIds);
}
