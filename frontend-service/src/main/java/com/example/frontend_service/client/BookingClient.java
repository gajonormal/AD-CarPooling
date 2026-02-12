package com.example.frontend_service.client;

import com.example.frontend_service.dto.BookingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "booking-service", url = "http://booking-service:8083")
public interface BookingClient {

    @PostMapping("/bookings")
    BookingDTO createBooking(@RequestBody BookingDTO booking);

    @GetMapping("/bookings")
    List<BookingDTO> getAllBookings();

    // Rota para finalizar pagamentos
    @PostMapping("/bookings/trips/{tripId}/finish-payments")
    void finishTripPayments(@PathVariable("tripId") Long tripId,
                            @RequestParam("totalPrice") Double totalPrice);

    // 👇 O QUE FALTA PARA A CONFIRMAÇÃO FUNCIONAR:

    // 1. Condutor Aceita
    @PostMapping("/bookings/{id}/accept")
    void acceptBooking(@PathVariable("id") Long id);

    // 2. Condutor Rejeita
    @PostMapping("/bookings/{id}/reject")
    void rejectBooking(@PathVariable("id") Long id);

    // 3. Ver pedidos de uma viagem específica (Para listar no Dashboard)
    @GetMapping("/bookings/trip/{tripId}")
    List<BookingDTO> getBookingsByTrip(@PathVariable("tripId") Long tripId);
}