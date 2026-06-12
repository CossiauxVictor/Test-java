package com.example.ticketapi.service;

import com.example.ticketapi.exception.InvalidStatusTransitionException;
import com.example.ticketapi.exception.TicketNotFoundException;
import com.example.ticketapi.model.Priority;
import com.example.ticketapi.model.Status;
import com.example.ticketapi.model.Ticket;
import com.example.ticketapi.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository repository;

    @InjectMocks
    private TicketService service;

    // je sais pas si on a besoin d un beforeeach ici mais je mets quand meme
    @BeforeEach
    void init() {
    }

    @Test
    void shouldCreateTicket_whenTitleAndPriorityAreValid() {
        // Arrange
        Ticket ticket = new Ticket(1L, "Probleme reseau", Priority.HIGH);
        when(repository.create("Probleme reseau", Priority.HIGH)).thenReturn(ticket);

        // Act
        Ticket result = service.create("Probleme reseau", Priority.HIGH);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Probleme reseau", result.getTitle());
        assertEquals(Priority.HIGH, result.getPriority());
        verify(repository).create("Probleme reseau", Priority.HIGH);
    }

    // test pour verifier que le statut par defaut est bien OPEN
    @Test
    void shouldHaveStatusOpen_whenTicketIsCreated() {
        // Arrange
        Ticket ticket = new Ticket(1L, "Bug login", Priority.MEDIUM);
        when(repository.create("Bug login", Priority.MEDIUM)).thenReturn(ticket);

        // Act
        Ticket result = service.create("Bug login", Priority.MEDIUM);

        // Assert
        assertEquals(Status.OPEN, result.getStatus());
    }

    @Test
    void shouldThrowException_whenTitleIsNull() {
        assertThrows(IllegalArgumentException.class, () -> service.create(null, Priority.LOW));
        verify(repository, never()).create(anyString(), any());
    }

    @Test
    void shouldThrowException_whenTitleIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> service.create("", Priority.LOW));
        verify(repository, never()).create(anyString(), any());
    }

    @Test
    void shouldThrowException_whenTitleIsTooShort() {
        // le titre AB fait 2 caracteres donc ca doit throw
        assertThrows(IllegalArgumentException.class, () -> service.create("AB", Priority.LOW));
        verify(repository, never()).create(anyString(), any());
    }

    @Test
    void shouldTrimTitle_whenThereAreSpaces() {
        // Arrange
        Ticket ticket = new Ticket(1L, "Bug login", Priority.MEDIUM);
        when(repository.create("Bug login", Priority.MEDIUM)).thenReturn(ticket);

        // Act
        service.create("  Bug login  ", Priority.MEDIUM);

        // Assert - le repository doit etre appelé sans les espaces
        verify(repository).create("Bug login", Priority.MEDIUM);
    }

    @Test
    void shouldReturnTicket_whenIdExists() {
        // Arrange
        Ticket ticket = new Ticket(1L, "Test", Priority.LOW);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));

        // Act
        Ticket result = service.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowTicketNotFoundException_whenIdDoesNotExist() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(TicketNotFoundException.class, () -> service.getById(99L));
    }

    // test transitions autorisées
    @Test
    void shouldUpdateStatus_WhenOpenToInProgressIsAllowed() {
        // Arrange
        Ticket ticket = new Ticket(1L, "Test", Priority.HIGH);
        // ticket est OPEN par defaut
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));

        // Act
        Ticket result = service.updateStatus(1L, Status.IN_PROGRESS);

        // Assert
        assertEquals(Status.IN_PROGRESS, result.getStatus());
    }

    @Test
    void shouldUpdateStatus_WhenOpenToResolvedIsAllowed() {
        // Arrange
        Ticket ticket = new Ticket(1L, "Test", Priority.HIGH);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));

        // Act
        Ticket result = service.updateStatus(1L, Status.RESOLVED);

        // Assert
        assertEquals(Status.RESOLVED, result.getStatus());
    }

    @Test
    void shouldUpdateStatus_WhenInProgressToResolvedIsAllowed() {
        // Arrange
        Ticket ticket = new Ticket(1L, "Test", Priority.HIGH);
        ticket.setStatus(Status.IN_PROGRESS);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));

        // Act
        Ticket result = service.updateStatus(1L, Status.RESOLVED);

        // Assert
        assertEquals(Status.RESOLVED, result.getStatus());
    }

    // test transitions interdites
    @Test
    void shouldThrowException_whenTicketIsAlreadyResolved() {
        // Arrange
        Ticket ticket = new Ticket(1L, "Test", Priority.HIGH);
        ticket.setStatus(Status.RESOLVED);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));

        // Act + Assert
        // un ticket resolu ne peut plus changer de statut
        assertThrows(InvalidStatusTransitionException.class,
                () -> service.updateStatus(1L, Status.IN_PROGRESS));
    }

    @Test
    void shouldThrowException_whenInProgressToOpenIsAttempted() {
        // Arrange
        Ticket ticket = new Ticket(1L, "Test", Priority.HIGH);
        ticket.setStatus(Status.IN_PROGRESS);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));

        // Act + Assert
        assertThrows(InvalidStatusTransitionException.class,
                () -> service.updateStatus(1L, Status.OPEN));
    }

    @Test
    void shouldThrowNotFoundException_whenTicketDoesNotExistForStatusUpdate() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(TicketNotFoundException.class,
                () -> service.updateStatus(99L, Status.IN_PROGRESS));
    }
}
