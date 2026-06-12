package com.example.ticketapi.controller;

import com.example.ticketapi.exception.InvalidStatusTransitionException;
import com.example.ticketapi.exception.TicketNotFoundException;
import com.example.ticketapi.model.Priority;
import com.example.ticketapi.model.Status;
import com.example.ticketapi.model.Ticket;
import com.example.ticketapi.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// tests du controlleur avec MockMvc
@WebMvcTest(TicketController.class)
class TicketControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService service;

    @Test
    void shouldReturnCreated_whenPostBodyIsValid() throws Exception {
        // Arrange
        Ticket ticket = new Ticket(1L, "Bug reseau", Priority.HIGH);
        when(service.create("Bug reseau", Priority.HIGH)).thenReturn(ticket);

        // Act + Assert
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Bug reseau\",\"priority\":\"HIGH\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/tickets/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Bug reseau"))
                .andExpect(jsonPath("$.status").value("OPEN"));

        verify(service).create("Bug reseau", Priority.HIGH);
    }

    @Test
    void shouldReturnBadRequest_whenTitleIsMissing() throws Exception {
        // Act + Assert
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"priority\":\"HIGH\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists());

        verify(service, never()).create(anyString(), any());
    }

    @Test
    void shouldReturnBadRequest_whenPriorityIsMissing() throws Exception {
        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Bug reseau\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists());

        verify(service, never()).create(anyString(), any());
    }

    @Test
    void shouldReturnOk_whenTicketExists() throws Exception {
        // Arrange
        Ticket ticket = new Ticket(1L, "Bug reseau", Priority.HIGH);
        when(service.getById(1L)).thenReturn(ticket);

        // Act + Assert
        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Bug reseau"))
                .andExpect(jsonPath("$.status").value("OPEN"));

        verify(service).getById(1L);
    }

    @Test
    void shouldReturnNotFound_whenTicketDoesNotExist() throws Exception {
        // Arrange
        when(service.getById(99L)).thenThrow(new TicketNotFoundException(99L));

        // Act + Assert
        mockMvc.perform(get("/api/tickets/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").exists());

        verify(service).getById(99L);
    }

    @Test
    void shouldReturnOk_whenStatusIsUpdated() throws Exception {
        // Arrange
        Ticket ticket = new Ticket(1L, "Bug reseau", Priority.HIGH);
        ticket.setStatus(Status.IN_PROGRESS);
        when(service.updateStatus(1L, Status.IN_PROGRESS)).thenReturn(ticket);

        // Act + Assert
        mockMvc.perform(patch("/api/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        verify(service).updateStatus(1L, Status.IN_PROGRESS);
    }

    @Test
    void shouldReturnConflict_whenStatusTransitionIsInvalid() throws Exception {
        // Arrange
        when(service.updateStatus(1L, Status.IN_PROGRESS))
                .thenThrow(new InvalidStatusTransitionException(Status.RESOLVED, Status.IN_PROGRESS));

        // Act + Assert
        mockMvc.perform(patch("/api/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").exists());

        verify(service).updateStatus(1L, Status.IN_PROGRESS);
    }

    @Test
    void shouldReturnOk_withListOfTickets() throws Exception {
        // Arrange
        when(service.findAll()).thenReturn(List.of(
                new Ticket(1L, "Bug A", Priority.HIGH),
                new Ticket(2L, "Bug B", Priority.LOW)
        ));

        // Act + Assert
        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(service).findAll();
    }
}
