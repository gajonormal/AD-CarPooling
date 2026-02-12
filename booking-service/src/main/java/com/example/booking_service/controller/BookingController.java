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

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {
        try {
            Booking created = service.createBooking(booking);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @GetMapping("/trip/{tripId}")
    public List<Booking> getBookingsByTrip(@PathVariable Long tripId) {
        return service.getBookingsByTrip(tripId);
    }

    @PostMapping("/{id}/accept")
    public void acceptBooking(@PathVariable Long id) {
        service.acceptBooking(id);
    }

    @PostMapping("/{id}/reject")
    public void rejectBooking(@PathVariable Long id) {
        service.rejectBooking(id);
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return service.getAllBookings();
    }

    // ==========================================================
    //       NOVOS MÉTODOS PARA O HISTÓRICO 📜
    // ==========================================================

    /**
     * Retorna os IDs de todos os passageiros de uma viagem.
     * Útil para o condutor ver quem participou no histórico.
     */
    @GetMapping("/trip/{tripId}/passengers")
    public List<Long> getPassengerIdsByTrip(@PathVariable Long tripId) {
        return service.getPassengerIdsByTrip(tripId);
    }

    /**
     * Retorna os IDs das viagens onde um passageiro participou.
     * Útil para montar o histórico do passageiro.
     */
    @GetMapping("/user/{passengerId}/trips")
    public List<Long> getTripIdsByUser(@PathVariable Long passengerId) {
        return service.getTripIdsByPassenger(passengerId);
    }

    // ==========================================================
    //       MÉTODOS EXISTENTES (FULL DETAILS & PAYMENTS)
    // ==========================================================

    @GetMapping("/{id}/full")
    public ResponseEntity<?> getFullBookingDetails(@PathVariable Long id) {
        Booking booking = service.getBookingById(id);
        if (booking == null) return ResponseEntity.notFound().build();

        // 👇 AQUI: Declarar e instanciar o DTO (Isto resolve o erro a vermelho)
        BookingResponseDTO response = new BookingResponseDTO();

        response.setBookingId(booking.getId());
        response.setStatus(booking.getStatus());
        response.setPrice(booking.getPrice());

        // Buscar nome do passageiro via Auth Service
        try {
            UserDTO user = authClient.getUserById(booking.getPassengerId());
            response.setPassengerName(user != null ? user.getName() : "Desconhecido");
        } catch (Exception e) {
            response.setPassengerName("Erro Auth Service");
        }

        // Buscar detalhes da viagem via Trip Service
        try {
            TripDTO trip = tripClient.getTripById(booking.getTripId());
            if (trip != null) {
                response.setDestination(trip.getDestination());
                response.setDepartureTime(trip.getDepartureTime());
            }
        } catch (Exception e) {
            response.setDestination("Erro Trip Service");
        }

        return ResponseEntity.ok(response);
    }

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