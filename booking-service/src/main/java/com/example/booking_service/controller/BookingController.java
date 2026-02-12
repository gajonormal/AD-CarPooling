package com.example.booking_service.controller;

import com.example.booking_service.dto.BookingResponseDTO;
import com.example.booking_service.dto.TripDTO;
import com.example.booking_service.dto.UserDTO;
import com.example.booking_service.feign.AuthClient;
import com.example.booking_service.feign.TripClient;
import com.example.booking_service.model.Booking;
import com.example.booking_service.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService service;

    @Autowired private TripClient tripClient;
    @Autowired private AuthClient authClient;

    // 1. CRIAR RESERVA
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        try {
            Booking created = service.createBooking(booking);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    // 👇 2. LISTAR POR VIAGEM (FALTAVA ESTE!!!)
    // Sem isto, o Dashboard do Condutor não vê os pedidos.
    @GetMapping("/trip/{tripId}")
    public List<Booking> getBookingsByTrip(@PathVariable Long tripId) {
        return service.getBookingsByTrip(tripId);
    }

    // 3. ACEITAR (FALTAVA ESTE!!!)
    @PostMapping("/{id}/accept")
    public void acceptBooking(@PathVariable Long id) {
        service.acceptBooking(id);
    }

    // 4. REJEITAR (FALTAVA ESTE!!!)
    @PostMapping("/{id}/reject")
    public void rejectBooking(@PathVariable Long id) {
        service.rejectBooking(id);
    }

    // 5. LISTAR TODAS (Genérico)
    @GetMapping
    public List<Booking> getAllBookings() {
        return service.getAllBookings();
    }

    // 6. DETALHES COMPLETOS
    @GetMapping("/{id}/full")
    public ResponseEntity<?> getFullBookingDetails(@PathVariable Long id) {
        Booking booking = service.getBookingById(id);
        if (booking == null) return ResponseEntity.notFound().build();

        BookingResponseDTO response = new BookingResponseDTO();
        response.setBookingId(booking.getId());
        response.setStatus(booking.getStatus());
        response.setPrice(booking.getPrice());

        try {
            UserDTO user = authClient.getUserById(booking.getPassengerId());
            response.setPassengerName(user != null ? user.getName() : "Desconhecido");
        } catch (Exception e) { response.setPassengerName("Erro Auth"); }

        try {
            TripDTO trip = tripClient.getTripById(booking.getTripId());
            if (trip != null) {
                response.setDestination(trip.getDestination());
                response.setDepartureTime(trip.getDepartureTime());
            }
        } catch (Exception e) { response.setDestination("Erro Trip"); }

        return ResponseEntity.ok(response);
    }

    // 7. FINALIZAR PAGAMENTOS
    @PostMapping("/trips/{tripId}/finish-payments")
    public ResponseEntity<String> finishTripPayments(@PathVariable Long tripId, @RequestParam Double totalPrice) {
        try {
            service.processPaymentsForTrip(tripId, totalPrice);
            return ResponseEntity.ok("Pagamentos processados e divididos com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar pagamentos: " + e.getMessage());
        }
    }
}