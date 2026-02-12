package com.example.frontend_service.controller;

import com.example.frontend_service.client.BookingClient; // ⚠️ Importante
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class TripController {

    @Autowired private TripClient tripClient;
    @Autowired private VehicleClient vehicleClient;

    @Autowired private BookingClient bookingClient; // 👇 1. ADICIONADO ISTO!

    @GetMapping("/trips/new")
    public String showCreateTripForm(Model model, HttpSession session) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        try {
            List<VehicleDTO> myCars = vehicleClient.getVehiclesByOwner(user.getId());
            model.addAttribute("cars", myCars);
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao carregar veículos.");
        }

        model.addAttribute("trip", new TripDTO());
        return "create-trip";
    }

    @PostMapping("/trips")
    public String createTrip(@ModelAttribute TripDTO trip, HttpSession session, RedirectAttributes redirectAttributes) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        try {
            trip.setDriverId(user.getId());
            trip.setStatus("CREATED");
            tripClient.createTrip(trip);
            redirectAttributes.addFlashAttribute("success", "Viagem criada com sucesso! 🚗");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao criar viagem: " + e.getMessage());
        }
        return "redirect:/dashboard";
    }

    // 👇 2. AQUI ESTAVA O PROBLEMA!
    @PostMapping("/trips/{id}/finish")
    public String finishTrip(@PathVariable Long id, @RequestParam Double price, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            // A. Mudar estado para Visual (Cinza/Concluído)
            tripClient.updateStatus(id, "FINISHED");

            // B. O QUE FALTAVA: Mandar calcular e processar os pagamentos!
            bookingClient.finishTripPayments(id, price);

            redirectAttributes.addFlashAttribute("success", "Viagem terminada e pagamentos processados! 💰");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao terminar viagem: " + e.getMessage());
        }
        return "redirect:/dashboard";
    }
}