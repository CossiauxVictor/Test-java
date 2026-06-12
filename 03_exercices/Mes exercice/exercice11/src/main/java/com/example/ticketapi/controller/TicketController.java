package com.example.ticketapi.controller;

import com.example.ticketapi.dto.CreateTicketRequest;
import com.example.ticketapi.dto.TicketResponse;
import com.example.ticketapi.dto.UpdateStatusRequest;
import com.example.ticketapi.model.Ticket;
import com.example.ticketapi.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

// controller pour gérer les tickets
@RestController
@RequestMapping("/api/tickets")
@CrossOrigin // j'ai mis ca pour eviter les problemes de CORS
public class TicketController {

    @Autowired
    private TicketService service;

    // creer un ticket
    @PostMapping
    public ResponseEntity<TicketResponse> create(@Valid @RequestBody CreateTicketRequest request) {

        Ticket ticket = service.create(request.title(), request.priority());

        // construire la reponse
        TicketResponse response = new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getPriority(),
                ticket.getStatus()
        );

        return ResponseEntity.created(URI.create("/api/tickets/" + ticket.getId())).body(response);
    }

    // recuperer un ticket par son id
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getById(@PathVariable Long id) {

        Ticket ticket = service.getById(id);

        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }

        TicketResponse response = new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getPriority(),
                ticket.getStatus()
        );

        return ResponseEntity.ok(response);
    }

    // lister tous les tickets
    @GetMapping
    public ResponseEntity<List<TicketResponse>> findAll() {

        List<Ticket> tickets = service.findAll();
        List<TicketResponse> responses = new ArrayList<>();

        for (Ticket t : tickets) {
            TicketResponse r = new TicketResponse(t.getId(), t.getTitle(), t.getPriority(), t.getStatus());
            responses.add(r);
        }

        return ResponseEntity.ok(responses);
    }

    // modifier le statut d'un ticket
    @PatchMapping("/{id}/status")
    public ResponseEntity<TicketResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request) {

        Ticket ticket = service.updateStatus(id, request.status());

        TicketResponse response = new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getPriority(),
                ticket.getStatus()
        );

        return ResponseEntity.ok(response);
    }
}
