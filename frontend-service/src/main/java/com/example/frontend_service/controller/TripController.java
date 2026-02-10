package com.example.frontend_service.controller;

import com.example.frontend_service.client.TripClient;
import com.example.frontend_service.dto.TripDTO;
import com.example.frontend_service.dto.UserDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TripController {

    @Autowired
    private TripClient tripClient;

    // 1. Mostrar o formulário de CRIAÇÃO (Só Condutor)
    @GetMapping("/trips/new")
    public String showCreateTripForm(Model model, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        if (!"DRIVER".equalsIgnoreCase(user.getType())) return "redirect:/dashboard";

        model.addAttribute("trip", new TripDTO());
        return "create-trip";
    }

    // 2. Processar a CRIAÇÃO (Só Condutor)
    @PostMapping("/trips")
    public String createTrip(@ModelAttribute TripDTO trip, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");

        if (user == null) return "redirect:/login";
        if (!"DRIVER".equalsIgnoreCase(user.getType())) return "redirect:/dashboard";

        try {
            trip.setDriverId(user.getId());
            tripClient.createTrip(trip);
            System.out.println(">>> Viagem criada por: " + user.getName());
        } catch (Exception e) {
            System.out.println(">>> Erro ao criar viagem: " + e.getMessage());
        }

        return "redirect:/dashboard"; // Mudei para Dashboard para ele ver logo os botões
    }

    // 3. PESQUISA (FALTAVA ISTO!) 🔍
    // É chamado quando o passageiro usa a barra de pesquisa
    @GetMapping("/trips/search")
    public String searchTrips(@RequestParam(required = false) String destination, Model model, HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";

        try {
            // Vai buscar todas as viagens
            List<TripDTO> allTrips = tripClient.getAllTrips();

            // Se o utilizador escreveu um destino, filtramos a lista
            if (destination != null && !destination.isEmpty()) {
                List<TripDTO> filteredTrips = allTrips.stream()
                        .filter(t -> t.getDestination().toLowerCase().contains(destination.toLowerCase()))
                        .collect(Collectors.toList());
                model.addAttribute("trips", filteredTrips);
            } else {
                model.addAttribute("trips", allTrips);
            }

        } catch (Exception e) {
            model.addAttribute("trips", new ArrayList<>());
        }

        // Reutilizamos o HTML do dashboard do passageiro, mas agora com resultados filtrados
        // Ou podes criar um "search-results.html" se preferires
        UserDTO user = (UserDTO) session.getAttribute("user");
        model.addAttribute("user", user);
        return "dashboard-passenger";
    }
}