package com.example.frontend_service.controller;

import com.example.frontend_service.client.BookingClient; // Tem a certeza que importas isto!
import com.example.frontend_service.client.TripClient;
import com.example.frontend_service.client.VehicleClient;
import com.example.frontend_service.dto.BookingDTO;
import com.example.frontend_service.dto.TripDTO;
import com.example.frontend_service.dto.UserDTO;
import com.example.frontend_service.dto.VehicleDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    @Autowired private TripClient tripClient;
    @Autowired private VehicleClient vehicleClient;
    @Autowired private BookingClient bookingClient; // Injetar BookingClient

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);

        // --- CONDUTOR ---
        if ("DRIVER".equalsIgnoreCase(user.getType())) {
            try {
                List<VehicleDTO> myCars = vehicleClient.getVehiclesByOwner(user.getId());
                model.addAttribute("myCars", myCars);

                // Buscar viagens do condutor
                List<TripDTO> allTrips = tripClient.getAllTrips();
                List<TripDTO> myTrips = allTrips.stream()
                        .filter(t -> t.getDriverId() != null && t.getDriverId().equals(user.getId()))
                        .collect(Collectors.toList());
                model.addAttribute("myTrips", myTrips);

                // 👇 LÓGICA NOVA: Buscar pedidos pendentes para estas viagens
                Map<Long, List<BookingDTO>> pendingBookings = new HashMap<>();

                for (TripDTO trip : myTrips) {
                    try {
                        // Vai ao BookingService buscar as reservas desta viagem
                        List<BookingDTO> bookings = bookingClient.getBookingsByTrip(trip.getId());

                        // Filtra só as PENDING
                        List<BookingDTO> pending = bookings.stream()
                                .filter(b -> "PENDING".equals(b.getStatus()))
                                .collect(Collectors.toList());

                        if (!pending.isEmpty()) {
                            pendingBookings.put(trip.getId(), pending);
                        }
                    } catch (Exception ex) {
                        System.out.println("Sem reservas para a viagem " + trip.getId());
                    }
                }
                // Envia o mapa para o HTML
                model.addAttribute("pendingBookings", pendingBookings);

            } catch (Exception e) {
                model.addAttribute("myTrips", new ArrayList<>());
                model.addAttribute("pendingBookings", new HashMap<>());
            }
            return "dashboard-driver";
        }

        // --- PASSAGEIRO ---
        else {
            try {
                List<TripDTO> all = tripClient.getAllTrips();
                List<TripDTO> active = all.stream()
                        .filter(t -> "CREATED".equals(t.getStatus()))
                        .collect(Collectors.toList());
                model.addAttribute("trips", active);
            } catch (Exception e) {
                model.addAttribute("trips", new ArrayList<>());
            }
            return "dashboard-passenger";
        }
    }
}