package com.example.booking_service.service;

import com.example.booking_service.dto.PaymentDTO;
import com.example.booking_service.dto.TripDTO;
import com.example.booking_service.dto.UserDTO;
import com.example.booking_service.feign.AuthClient;
import com.example.booking_service.feign.PaymentClient;
import com.example.booking_service.feign.TripClient;
import com.example.booking_service.model.Booking;
import com.example.booking_service.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository repository;
    @Autowired
    private TripClient tripClient;
    @Autowired
    private AuthClient authClient;
    @Autowired
    private PaymentClient paymentClient;

    // --- LÓGICA DE CRIAR RESERVA ---
    public Booking createBooking(Booking booking) {

        // 1. Validar Passageiro (Auth Service)
        // Usa o UserDTO que me mostraste
        UserDTO user = authClient.getUserById(booking.getPassengerId());
        if (user == null) {
            throw new RuntimeException("Passageiro não encontrado.");
        }

        // 2. Obter Detalhes da Viagem (Trip Service)
        // Usa o TripDTO que me mostraste
        TripDTO trip = tripClient.getTripById(booking.getTripId());
        if (trip == null) {
            throw new RuntimeException("Viagem não encontrada.");
        }

        // 3. TENTAR REDUZIR LUGAR (Trip Service)
        // ⚠️ IMPORTANTE: O teu TripClient precisa do método reduceSeat()
        boolean lugarReservado = tripClient.reduceSeat(booking.getTripId());
        if (!lugarReservado) {
            throw new RuntimeException("Não foi possível reservar: Viagem cheia ou inexistente.");
        }

        // 4. Configurar a Reserva
        booking.setPrice(trip.getPrice()); // Vem do TripDTO
        booking.setStatus("CONFIRMED");
        booking.setBookingDate(LocalDateTime.now());

        // 5. Guardar na Base de Dados
        Booking savedBooking = repository.save(booking);

        // 6. Processar Pagamento (Payment Service)
        if (booking.getPrice() > 0) {
            try {
                System.out.println("--- A processar pagamento de " + booking.getPrice() + "€ ---");

                // Usa o PaymentDTO com o construtor que criaste
                PaymentDTO payment = new PaymentDTO(savedBooking.getId(), booking.getPrice(), "MBWAY");

                paymentClient.processPayment(payment);
            } catch (Exception e) {
                System.out.println("⚠️ Erro no pagamento (mas a reserva ficou feita): " + e.getMessage());
            }
        }

        return savedBooking;
    }

    // --- LISTAR TODAS ---
    public List<Booking> getAllBookings() {
        return repository.findAll();
    }

    // --- BUSCAR POR ID ---
    public Booking getBookingById(Long id) {
        return repository.findById(id).orElse(null);
    }
}