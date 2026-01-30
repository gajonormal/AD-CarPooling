package com.example.booking_service.controller;
import com.example.booking_service.dto.BookingResponseDTO;
import com.example.booking_service.feign.AuthClient;
import com.example.booking_service.dto.TripDTO;
import com.example.booking_service.dto.UserDTO;
import com.example.booking_service.feign.TripClient;
import com.example.booking_service.model.Booking;
import com.example.booking_service.repository.BookingRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingRepository repository;

    @Autowired
    private TripClient tripClient; // <--- Injetar o Cliente
    @Autowired
    private AuthClient authClient;

    @PostMapping
    public Object createBooking(@RequestBody Booking booking) {
        try {
            System.out.println("A verificar viagem " + booking.getTripId() + "...");

            // 1. Perguntar ao Trip Service
            TripDTO trip = tripClient.getTripById(booking.getTripId());

            // Se o trip for nulo (vazio), nós forçamos o erro para ir para o 'catch'
            if (trip == null) {
                throw new RuntimeException("Viagem retornou nulo");
            }
            UserDTO user = authClient.getUserById(booking.getPassengerId());
            if (user == null) throw new RuntimeException("Utilizador não existe");
            // ----------------------------

            // 2. Se passou do 'if', grava a reserva.
            booking.setStatus("CONFIRMED");
            return repository.save(booking);

        } catch (Exception e) {
            // 3. Agora sim, vai cair aqui!
            return "Erro: Viagem " + booking.getTripId() + " não encontrada!";
        }
    }
    @GetMapping("/{id}/full")
    public Object getFullBookingDetails(@PathVariable Long id) {
        try {
            Booking booking = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Reserva ID " + id + " não encontrada."));

            BookingResponseDTO response = new BookingResponseDTO();
            response.setBookingId(booking.getId());
            response.setStatus(booking.getStatus());

            // Tentamos buscar o utilizador, se falhar, definimos como "Indisponível"
            try {
                UserDTO user = authClient.getUserById(booking.getPassengerId());
                response.setPassengerName(user != null ? user.getName() : "Utilizador não encontrado");
            } catch (Exception e) {
                response.setPassengerName("Serviço de Autenticação Offline");
            }

            // Tentamos buscar a viagem, se falhar, definimos como "Indisponível"
            try {
                TripDTO trip = tripClient.getTripById(booking.getTripId());
                if (trip != null) {
                    response.setDestination(trip.getDestination());
                    response.setDepartureTime(trip.getDepartureTime());
                } else {
                    response.setDestination("Viagem não encontrada");
                }
            } catch (Exception e) {
                response.setDestination("Serviço de Viagens Offline");
            }

            return response;

        } catch (Exception e) {
            return "Erro fatal: " + e.getMessage();
        }
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return repository.findAll();
    }

}