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

    @PostMapping("/bookings/trips/{tripId}/finish-payments")
    void finishTripPayments(@PathVariable("tripId") Long tripId, @RequestParam("totalPrice") Double totalPrice);

    @PostMapping("/bookings/{id}/accept")
    void acceptBooking(@PathVariable("id") Long id);

    @PostMapping("/bookings/{id}/reject")
    void rejectBooking(@PathVariable("id") Long id);

    @GetMapping("/bookings/trip/{tripId}")
    List<BookingDTO> getBookingsByTrip(@PathVariable("tripId") Long tripId);

    // --- NOVOS MÉTODOS PARA O HISTÓRICO ---

    @GetMapping("/bookings/trip/{tripId}/passengers")
    List<Long> getPassengerIdsByTrip(@PathVariable("tripId") Long tripId);

    @GetMapping("/bookings/user/{passengerId}/trips")
    List<Long> getTripIdsByUser(@PathVariable("passengerId") Long passengerId);
}