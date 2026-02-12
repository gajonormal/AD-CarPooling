package com.example.frontend_service.controller;

import com.example.frontend_service.client.*;
import com.example.frontend_service.dto.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HistoryController {

    @Autowired private TripClient tripClient;
    @Autowired private BookingClient bookingClient;
    @Autowired private AuthClient authClient;
    @Autowired private VehicleClient vehicleClient;

    @GetMapping("/history")
    public String showHistory(HttpSession session, Model model) {
        UserDTO currentUser = (UserDTO) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";

        List<TripHistoryDTO> fullHistory = new ArrayList<>();

        // --- 1. BUSCAR VIAGENS COMO CONDUTOR ---
        try {
            List<TripDTO> drivenTrips = tripClient.getDriverHistory(currentUser.getId());
            for (TripDTO trip : drivenTrips) {
                fullHistory.add(mapToHistoryDTO(trip, "CONDUTOR", currentUser.getId()));
            }
        } catch (Exception e) {
            System.err.println("Erro no histórico de condutor: " + e.getMessage());
        }

        // --- 2. BUSCAR VIAGENS COMO PASSAGEIRO ---
        try {
            List<Long> passengerTripIds = bookingClient.getTripIdsByUser(currentUser.getId());
            if (passengerTripIds != null && !passengerTripIds.isEmpty()) {
                List<TripDTO> passengerTrips = tripClient.getTripsByIds(passengerTripIds);
                for (TripDTO trip : passengerTrips) {
                    fullHistory.add(mapToHistoryDTO(trip, "PASSAGEIRO", currentUser.getId()));
                }
            }
        } catch (Exception e) {
            System.err.println("Erro no histórico de passageiro: " + e.getMessage());
        }

        model.addAttribute("history", fullHistory);
        return "history";
    }

    // --- NOVO MÉTODO: SUBMETER REVIEW ---
    @PostMapping("/history/review")
    public String submitReview(@RequestParam Long targetUserId,
                               @RequestParam int rating,
                               @RequestParam String comment,
                               HttpSession session) {
        UserDTO currentUser = (UserDTO) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";

        ReviewDTO review = new ReviewDTO();
        review.setReviewerId(currentUser.getId());
        review.setTargetUserId(targetUserId);
        review.setRating(rating);
        review.setComment(comment);

        try {
            authClient.createReview(review); // Envia para o Auth Service
        } catch (Exception e) {
            System.err.println("Erro ao submeter avaliação: " + e.getMessage());
        }

        return "redirect:/history?success=review";
    }

    private TripHistoryDTO mapToHistoryDTO(TripDTO trip, String role, Long currentUserId) {
        TripHistoryDTO dto = new TripHistoryDTO();
        dto.setTripId(trip.getId());
        dto.setOrigin(trip.getOrigin());
        dto.setDestination(trip.getDestination());
        dto.setDepartureTime(trip.getDepartureTime().toString());
        dto.setUserRole(role);
        dto.setDriverId(trip.getDriverId()); // Importante para o passageiro avaliar o condutor

        // --- LÓGICA DO PREÇO REAL ---
        try {
            List<BookingDTO> bookings = bookingClient.getBookingsByTrip(trip.getId());
            if (role.equals("PASSAGEIRO")) {
                bookings.stream()
                        .filter(b -> b.getPassengerId().equals(currentUserId))
                        .findFirst()
                        .ifPresent(myBooking -> dto.setPrice(myBooking.getPrice()));
            } else {
                long numPassageiros = bookings.stream().filter(b -> "COMPLETED".equals(b.getStatus())).count();
                dto.setPrice(trip.getPrice() / (numPassageiros + 1));
            }
        } catch (Exception e) {
            dto.setPrice(trip.getPrice());
        }

        // --- RESTANTE DO MAPEAMENTO ---
        try {
            UserDTO driver = authClient.getUser(trip.getDriverId());
            dto.setDriverName(driver != null ? driver.getName() : "Desconhecido");
        } catch (Exception e) { dto.setDriverName("Erro"); }

        try {
            if (trip.getVehicleId() != null) {
                VehicleDTO v = vehicleClient.getVehicleById(trip.getVehicleId());
                dto.setVehicleInfo(v.getBrand() + " " + v.getModel());
            }
        } catch (Exception e) { dto.setVehicleInfo("N/A"); }

        // --- BUSCAR NOMES E IDS DOS PASSAGEIROS (Para Review) ---
        try {
            List<Long> pIds = bookingClient.getPassengerIdsByTrip(trip.getId());
            dto.setPassengerIds(pIds); // Guarda a lista de IDs para o Modal de avaliação do Condutor

            List<String> pNames = new ArrayList<>();
            for (Long id : pIds) {
                UserDTO pUser = authClient.getUser(id);
                if (pUser != null) pNames.add(pUser.getName());
            }
            dto.setPassengerNames(pNames);
        } catch (Exception e) {
            dto.setPassengerNames(new ArrayList<>());
            dto.setPassengerIds(new ArrayList<>());
        }

        return dto;
    }
}