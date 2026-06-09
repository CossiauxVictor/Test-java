package com.example;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservationSteps {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private RoomRepository roomRepository;
    private ReservationRepository reservationRepository;
    private NotificationService notificationService;
    private ReservationService reservationService;
    private ReservationReceipt receipt;
    private ReservationException thrownException;

    @Before
    public void setUp() {
        roomRepository = mock(RoomRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        notificationService = mock(NotificationService.class);
        reservationService = new ReservationService(roomRepository, reservationRepository, notificationService);
        receipt = null;
        thrownException = null;
    }

    @Given("la salle {string} nommée {string} avec une capacité de {int}")
    public void laSalle(String code, String name, int capacity) {
        Room room = new Room(code, name, capacity);
        when(roomRepository.findByCode(code)).thenReturn(Optional.of(room));
    }

    @Given("aucune salle n'existe avec le code {string}")
    public void aucuneSalleNExisteAvecLeCode(String code) {
        when(roomRepository.findByCode(code)).thenReturn(Optional.empty());
    }

    @Given("aucune réservation existante pour {string}")
    public void aucuneReservationExistantePour(String roomCode) {
        when(reservationRepository.findByRoomCode(roomCode)).thenReturn(List.of());
    }

    @Given("une réservation existante pour {string} du {string} au {string}")
    public void uneReservationExistantePour(String roomCode, String start, String end) {
        Reservation existing = new Reservation(
                "existing@example.com", roomCode, 1,
                LocalDateTime.parse(start, FORMATTER),
                LocalDateTime.parse(end, FORMATTER));
        when(reservationRepository.findByRoomCode(roomCode)).thenReturn(List.of(existing));
    }

    @When("{string} réserve {string} pour {int} participants du {string} au {string}")
    public void reserve(String email, String roomCode, int participants, String start, String end) {
        Reservation reservation = new Reservation(
                email, roomCode, participants,
                LocalDateTime.parse(start, FORMATTER),
                LocalDateTime.parse(end, FORMATTER));
        try {
            receipt = reservationService.reserve(reservation);
        } catch (ReservationException e) {
            thrownException = e;
        }
    }

    @Then("la réservation est acceptée")
    public void laReservationEstAcceptee() {
        assertNotNull(receipt);
        assertNull(thrownException);
    }

    @And("une confirmation est envoyée à {string}")
    public void uneConfirmationEstEnvoyeeA(String email) {
        verify(notificationService).sendConfirmation(eq(email), any());
    }

    @Then("la réservation est refusée")
    public void laReservationEstRefusee() {
        assertNotNull(thrownException);
        assertNull(receipt);
    }

    @And("le motif du refus est {string}")
    public void leMotifDuRefusEst(String expected) {
        assertEquals(expected, thrownException.getMessage());
    }

    @And("aucune confirmation n'est envoyée")
    public void aucuneConfirmationNEstEnvoyee() {
        verify(notificationService, never()).sendConfirmation(any(), any());
    }
}
