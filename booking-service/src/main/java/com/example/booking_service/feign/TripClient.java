package com.example.booking_service.feign;

import com.example.booking_service.dto.TripDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "trip-service")
public interface TripClient {
    @GetMapping("/trips/{id}")
    TripDTO getTripById(@PathVariable("id") Long id);
}