package com.example.frontend_service.client;

import com.example.frontend_service.dto.SystemMetricsDTO;
import com.example.frontend_service.dto.UserDTO;
import com.example.frontend_service.dto.VehicleDTO; // <--- IMPORTANTE: Adiciona este import
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping; // <--- IMPORTANTE: Adiciona este import
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@FeignClient(name = "admin-service", url = "http://admin-service:8090") // Confirma se a porta é 8090 ou a que definiste
public interface AdminClient {

    // ==========================================
    //       UTILIZADORES & MÉTRICAS (Já tinhas)
    // ==========================================

    @GetMapping("/admin/users")
    List<UserDTO> getAllUsers();

    @PutMapping("/admin/users/{id}/suspend")
    void toggleSuspension(@PathVariable("id") Long id);

    @GetMapping("/admin/metrics")
    SystemMetricsDTO getMetrics();

    // ==========================================
    //       VEÍCULOS (NOVO) 🚗
    // ==========================================

    @GetMapping("/admin/vehicles")
    List<VehicleDTO> getAllVehicles();

    @DeleteMapping("/admin/vehicles/{id}")
    void deleteVehicle(@PathVariable("id") Long id);
}