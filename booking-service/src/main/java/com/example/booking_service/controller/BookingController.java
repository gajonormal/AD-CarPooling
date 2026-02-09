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
    private BookingService service; // Agora usamos o Service!

    // Precisamos destes clientes aqui SÓ para o método "full" details (opcional)
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

    // 2. LISTAR TODAS
    @GetMapping
    public List<Booking> getAllBookings() {
        return service.getAllBookings();
    }

    // 3. OBTER DETALHES COMPLETOS (Mantive a tua lógica de DTOs aqui)
    @GetMapping("/{id}/full")
    public ResponseEntity<?> getFullBookingDetails(@PathVariable Long id) {
        Booking booking = service.getBookingById(id);

        if (booking == null) return ResponseEntity.notFound().build();

        BookingResponseDTO response = new BookingResponseDTO();
        response.setBookingId(booking.getId());
        response.setStatus(booking.getStatus());
        response.setPrice(booking.getPrice());

        // Preencher dados extra (Podes mover isto para o Service depois se quiseres)
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
}