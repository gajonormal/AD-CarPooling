package com.example.frontend_service.controller;

import com.example.frontend_service.client.AdminClient;
import com.example.frontend_service.dto.SystemMetricsDTO;
import com.example.frontend_service.dto.UserDTO;
import com.example.frontend_service.dto.VehicleDTO; // <--- 1. IMPORTANTE: Adiciona este import
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private AdminClient adminClient;

    // ==========================================
    //       DASHBOARD PRINCIPAL (Mistura tudo)
    // ==========================================
    @GetMapping("/admin")
    public String showAdminDashboard(HttpSession session, Model model) {
        UserDTO user = (UserDTO) session.getAttribute("user");

        // 1. Segurança: só deixa entrar se for ADMIN
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getType())) {
            return "redirect:/dashboard";
        }

        // 2. Busca dados existentes (Métricas e Users)
        SystemMetricsDTO metrics = adminClient.getMetrics();
        List<UserDTO> users = adminClient.getAllUsers();

        // 3. NOVO: Busca dados dos Veículos 🚗
        List<VehicleDTO> vehicles = adminClient.getAllVehicles();

        // 4. Envia tudo para o HTML
        model.addAttribute("metrics", metrics);
        model.addAttribute("users", users);
        model.addAttribute("vehicles", vehicles); // <--- Adicionado ao modelo

        return "admin-dashboard";
    }

    // ==========================================
    //       AÇÕES DE UTILIZADORES
    // ==========================================
    @GetMapping("/admin/suspend/{id}")
    public String suspendUser(@PathVariable Long id, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");

        // Segurança antes de agir
        if (user != null && "ADMIN".equalsIgnoreCase(user.getType())) {
            adminClient.toggleSuspension(id);
        }
        return "redirect:/admin";
    }

    // ==========================================
    //       AÇÕES DE VEÍCULOS (NOVO) 🚗
    // ==========================================
    @GetMapping("/admin/vehicle/delete/{id}")
    public String deleteVehicle(@PathVariable Long id, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");

        // Segurança: Só Admin pode apagar carros
        if (user != null && "ADMIN".equalsIgnoreCase(user.getType())) {
            adminClient.deleteVehicle(id);
        }
        return "redirect:/admin";
    }
}