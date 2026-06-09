package com.example;

import java.util.List;
import java.util.Optional;

public class ReservationService {
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final NotificationService notificationService;

    public ReservationService(RoomRepository roomRepository,
                               ReservationRepository reservationRepository,
                               NotificationService notificationService) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.notificationService = notificationService;
    }

    public ReservationReceipt reserve(Reservation reservation) {
        Optional<Room> roomOpt = roomRepository.findByCode(reservation.getRoomCode());
        if (roomOpt.isEmpty()) {
            throw new ReservationException("Salle inconnue");
        }

        if (!reservation.getEndTime().isAfter(reservation.getStartTime())) {
            throw new ReservationException("Période invalide");
        }

        Room room = roomOpt.get();
        if (reservation.getParticipantCount() > room.getCapacity()) {
            throw new ReservationException("Capacité insuffisante");
        }

        List<Reservation> existing = reservationRepository.findByRoomCode(reservation.getRoomCode());
        for (Reservation ex : existing) {
            if (ex.getStartTime().isBefore(reservation.getEndTime())
                    && ex.getEndTime().isAfter(reservation.getStartTime())) {
                throw new ReservationException("Créneau non disponible");
            }
        }

        ReservationReceipt receipt = new ReservationReceipt(
                reservation.getRoomCode(),
                reservation.getUserEmail(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                "Réservation confirmée"
        );
        notificationService.sendConfirmation(reservation.getUserEmail(), receipt);
        return receipt;
    }
}
