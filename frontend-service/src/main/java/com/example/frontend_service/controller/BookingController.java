package com.example.frontend_service.controller;

import com.example.frontend_service.client.BookingClient;
import com.example.frontend_service.dto.BookingDTO;
import com.example.frontend_service.dto.UserDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller // Atenção: @Controller e não @RestController
public class BookingController {

    @Autowired
    private BookingClient bookingClient;

    // 1. Passageiro faz reserva
    @PostMapping("/bookings/create")
    public String createBooking(@RequestParam Long tripId, HttpSession session, RedirectAttributes redirectAttributes) {
        UserDTO user = (UserDTO) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        try {
            BookingDTO booking = new BookingDTO();
            booking.setTripId(tripId);
            booking.setPassengerId(user.getId());

            bookingClient.createBooking(booking);

            // 👇 A MENSAGEM QUE TU QUERIAS
            redirectAttributes.addFlashAttribute("success", "Pedido de reserva enviado com sucesso! Aguarda aprovação do condutor.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao reservar: " + e.getMessage()); // Ex: Já reservado
        }
        return "redirect:/dashboard";
    }

    // 2. Condutor Aceita
    @PostMapping("/bookings/{id}/accept")
    public String acceptBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingClient.acceptBooking(id);
            redirectAttributes.addFlashAttribute("success", "Reserva aceite! Lugar descontado. ✅");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao aceitar: " + e.getMessage());
        }
        return "redirect:/dashboard";
    }

    // 3. Condutor Rejeita
    @PostMapping("/bookings/{id}/reject")
    public String rejectBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingClient.rejectBooking(id);
            redirectAttributes.addFlashAttribute("success", "Reserva rejeitada. ❌");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao rejeitar.");
        }
        return "redirect:/dashboard";
    }
}