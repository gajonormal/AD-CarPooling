package com.example.trip_service.feign;

import com.example.trip_service.dto.VehicleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "vehicle-service")
public interface VehicleClient {
    @GetMapping("/vehicles/{id}")
    VehicleDTO getVehicleById(@PathVariable("id") Long id);
}