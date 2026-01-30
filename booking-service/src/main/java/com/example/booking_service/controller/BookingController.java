package com.example.booking_service.controller;

import com.example.booking_service.dto.BookingResponseDTO;
import com.example.booking_service.dto.PaymentDTO; // <--- NOVO
import com.example.booking_service.dto.TripDTO;
import com.example.booking_service.dto.UserDTO;
import com.example.booking_service.feign.AuthClient;
import com.example.booking_service.feign.PaymentClient; // <--- NOVO
import com.example.booking_service.feign.TripClient;
import com.example.booking_service.model.Booking;
import com.example.booking_service.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingRepository repository;
    @Autowired
    private TripClient tripClient;
    @Autowired
    private AuthClient authClient;

    @Autowired
    private PaymentClient paymentClient; // <--- 1. INJETAR O CLIENTE DE PAGAMENTOS

    // --- 1. CRIAR RESERVA (Com Pagamento Automático) ---
    @PostMapping
    public Object createBooking(@RequestBody Booking booking) {
        try {
            // A. Validar se a Viagem existe
            TripDTO trip = tripClient.getTripById(booking.getTripId());
            if (trip == null) throw new RuntimeException("Viagem não encontrada");

            // B. Validar se o Passageiro existe
            UserDTO user = authClient.getUserById(booking.getPassengerId());
            if (user == null) throw new RuntimeException("Utilizador não existe");

            // C. Definir o Preço
            double price = (trip.getPrice() != 0) ? trip.getPrice() : 0.0;
            booking.setPrice(price);
            booking.setStatus("CONFIRMED");

            // D. Gravar a Reserva (Para gerar o ID)
            Booking savedBooking = repository.save(booking);

            // E. INTEGRAR COM O PAGAMENTO (O passo do 20 valores!) 💸
            // Só fazemos isto se houver um preço a pagar
            if (price > 0) {
                try {
                    System.out.println("--- A contactar o Payment-Service para cobrar " + price + "€ ---");

                    // Criamos o DTO para enviar ao outro serviço
                    // Nota: Assumimos "MBWAY" fixo para simplificar o teste
                    PaymentDTO payment = new PaymentDTO(savedBooking.getId(), price, "MBWAY");

                    // Chamada Feign ao Payment-Service
                    paymentClient.processPayment(payment);

                } catch (Exception e) {
                    // Se o serviço de pagamentos falhar, não cancelamos a reserva,
                    // mas deixamos um aviso no log.
                    System.out.println("⚠️ Erro ao processar pagamento automático: " + e.getMessage());
                }
            }

            return savedBooking;

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao criar reserva: " + e.getMessage();
        }
    }

    // --- 2. OBTER DETALHES COMPLETOS ---
    @GetMapping("/{id}/full")
    public Object getFullBookingDetails(@PathVariable Long id) {
        try {
            Booking booking = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Reserva ID " + id + " não encontrada."));

            BookingResponseDTO response = new BookingResponseDTO();
            response.setBookingId(booking.getId());
            response.setStatus(booking.getStatus());
            response.setPrice(booking.getPrice());

            try {
                UserDTO user = authClient.getUserById(booking.getPassengerId());
                response.setPassengerName(user != null ? user.getName() : "Utilizador Desconhecido");
            } catch (Exception e) { response.setPassengerName("Erro Auth"); }

            try {
                TripDTO trip = tripClient.getTripById(booking.getTripId());
                if (trip != null) {
                    response.setDestination(trip.getDestination());
                    response.setDepartureTime(trip.getDepartureTime());
                } else { response.setDestination("Erro Trip"); }
            } catch (Exception e) { response.setDestination("Erro Trip"); }

            return response;
        } catch (Exception e) { return "Erro: " + e.getMessage(); }
    }

    // --- 3. LISTAR TODAS ---
    @GetMapping
    public List<Booking> getAllBookings() { return repository.findAll(); }
}