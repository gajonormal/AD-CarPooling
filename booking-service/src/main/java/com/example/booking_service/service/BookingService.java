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

    // --- 1. CRIAR RESERVA (Só cria o pedido, NÃO desconta lugar) ---
    public Booking createBooking(Booking booking) {
        UserDTO user = authClient.getUserById(booking.getPassengerId());
        if (user == null) throw new RuntimeException("Passageiro não encontrado.");

        TripDTO trip = tripClient.getTripById(booking.getTripId());
        if (trip == null) throw new RuntimeException("Viagem não encontrada.");

        if (repository.existsByTripIdAndPassengerId(booking.getTripId(), booking.getPassengerId())) {
            throw new RuntimeException("Já tens um pedido ou reserva nesta viagem!");
        }

        // Alteração: NÃO descontamos o lugar aqui. Fica Pendente.
        booking.setPrice(trip.getPrice());
        booking.setStatus("PENDING");
        booking.setBookingDate(LocalDateTime.now());

        return repository.save(booking);
    }

    // --- 2. ACEITAR RESERVA (Aqui é que descontamos o lugar) ---
    public void acceptBooking(Long bookingId) {
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        if (!"PENDING".equals(booking.getStatus())) {
            throw new RuntimeException("Esta reserva não está pendente.");
        }

        // Tenta tirar o lugar agora
        boolean lugarReservado = tripClient.reduceSeat(booking.getTripId());
        if (!lugarReservado) {
            throw new RuntimeException("Não foi possível aceitar: Viagem cheia!");
        }

        booking.setStatus("CONFIRMED");
        repository.save(booking);
    }

    // --- 3. REJEITAR RESERVA ---
    public void rejectBooking(Long bookingId) {
        Booking booking = repository.findById(bookingId).orElseThrow();
        booking.setStatus("REJECTED");
        repository.save(booking);
    }

    // --- 4. PAGAMENTOS ---
    public void processPaymentsForTrip(Long tripId, Double totalTripPrice) {
        // Buscar TODAS as reservas (Pending, Confirmed, Rejected...)
        List<Booking> allBookings = repository.findByTripId(tripId);

        // Filtrar APENAS as Confirmadas (Quem vai pagar)
        List<Booking> confirmedBookings = allBookings.stream()
                .filter(b -> "CONFIRMED".equals(b.getStatus()) || "COMPLETED".equals(b.getStatus()))
                .toList();

        TripDTO trip = tripClient.getTripById(tripId);
        if (trip == null) return;
        Long driverId = trip.getDriverId();

        // Conta: Confirmados + Condutor
        int totalOccupants = confirmedBookings.size() + 1;
        double pricePerPerson = totalTripPrice / totalOccupants;

        System.out.println("Total a dividir: " + totalTripPrice + " por " + totalOccupants + " pessoas.");

        // Cobrar Passageiros
        for (Booking booking : confirmedBookings) {
            try {
                booking.setPrice(pricePerPerson);
                booking.setStatus("COMPLETED");
                repository.save(booking);

                PaymentDTO paymentReq = new PaymentDTO(
                        booking.getId(),
                        pricePerPerson,
                        "MBWAY",
                        booking.getPassengerId()
                );
                paymentClient.processPayment(paymentReq);
            } catch (Exception e) {
                System.err.println("Erro ao cobrar passageiro " + booking.getPassengerId());
            }
        }

        // Cobrar Condutor (Registo)
        try {
            PaymentDTO driverPayment = new PaymentDTO(null, pricePerPerson, "DRIVER_SHARE", driverId);
            paymentClient.processPayment(driverPayment);
        } catch (Exception e) {
            System.err.println("Erro driver: " + e.getMessage());
        }
    }

    public List<Booking> getAllBookings() { return repository.findAll(); }
    public Booking getBookingById(Long id) { return repository.findById(id).orElse(null); }


    public List<Booking> getBookingsByTrip(Long tripId) {
        return repository.findByTripId(tripId);
    }

    // ==========================================================
    //       NOVOS MÉTODOS PARA O HISTÓRICO 📜
    // ==========================================================

    /**
     * Retorna os IDs de todos os passageiros que EFETIVAMENTE viajaram (status COMPLETED).
     * Chamado pelo Frontend para mostrar ao Condutor quem ele levou.
     */
    public List<Long> getPassengerIdsByTrip(Long tripId) {
        return repository.findByTripId(tripId).stream()
                .filter(b -> "COMPLETED".equalsIgnoreCase(b.getStatus()))
                .map(Booking::getPassengerId)
                .toList();
    }

    /**
     * Retorna os IDs das viagens onde o utilizador foi como passageiro e a viagem terminou.
     * Chamado pelo Frontend para montar a lista do Histórico do Passageiro.
     */
    public List<Long> getTripIdsByPassenger(Long passengerId) {
        return repository.findByPassengerId(passengerId).stream()
                .filter(b -> "COMPLETED".equalsIgnoreCase(b.getStatus()))
                .map(Booking::getTripId)
                .toList();
    }
}