package com.example.frontend_service.controller;

import com.example.frontend_service.client.TripClient;
import com.example.frontend_service.client.VehicleClient;
import com.example.frontend_service.dto.TripDTO;
import com.example.frontend_service.dto.UserDTO;
import com.example.frontend_service.dto.VehicleDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private TripClient tripClient;

    @Autowired
    private VehicleClient vehicleClient;

    // Esta é a página principal depois do Login
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        UserDTO user = (UserDTO) session.getAttribute("user");

        // 1. Segurança: Se não houver login, manda para fora
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);

        // 2. Lógica para CONDUTOR
        if ("DRIVER".equalsIgnoreCase(user.getType())) {
            try {
                // Vai buscar os carros do condutor para mostrar na dashboard
                // (Precisas de ter este método no VehicleClient, se não tiveres, usamos lista vazia por agora)
                // List<VehicleDTO> myCars = vehicleClient.getMyVehicles(user.getId());
                // model.addAttribute("myCars", myCars);

                // Vai buscar as viagens que ele publicou
                // List<TripDTO> myTrips = tripClient.getDriverTrips(user.getId());
                // model.addAttribute("myTrips", myTrips);
            } catch (Exception e) {
                System.out.println("Erro ao carregar dados do condutor");
            }
            return "dashboard-driver"; // HTML específico para condutor
        }

        // 3. Lógica para PASSAGEIRO
        else {
            try {
                // Aqui vamos buscar as viagens disponíveis para ele pesquisar
                List<TripDTO> availableTrips = tripClient.getAllTrips();
                model.addAttribute("trips", availableTrips);
            } catch (Exception e) {
                model.addAttribute("trips", List.of());
            }
            return "dashboard-passenger"; // HTML específico para passageiro
        }
    }
}