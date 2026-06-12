package com.example.ticketapi.service;

import com.example.ticketapi.exception.InvalidStatusTransitionException;
import com.example.ticketapi.exception.TicketNotFoundException;
import com.example.ticketapi.model.Priority;
import com.example.ticketapi.model.Status;
import com.example.ticketapi.model.Ticket;
import com.example.ticketapi.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository repository;

    // TODO: peut etre ajouter une validation plus poussée plus tard

    public Ticket create(String title, Priority priority) {

        // verifier le titre
        if (title == null || title.equals("")) {
            throw new IllegalArgumentException("Le titre est obligatoire");
        }

        if (title.strip().length() < 3) {
            throw new IllegalArgumentException("Le titre doit contenir au moins 3 caracteres");
        }

        // debug - a supprimer
        System.out.println("Creation du ticket avec le titre : " + title);

        return repository.create(title.strip(), priority);
    }

    public Ticket getById(Long id) {
        Ticket ticket = repository.findById(id).orElse(null);

        if (ticket == null) {
            throw new TicketNotFoundException(id);
        }

        return ticket;
    }

    public List<Ticket> findAll() {
        return repository.findAll();
    }

    public Ticket updateStatus(Long id, Status newStatus) {

        Ticket ticket = repository.findById(id).orElse(null);
        if (ticket == null) {
            throw new TicketNotFoundException(id);
        }

        Status statusActuel = ticket.getStatus();

        // un ticket resolu ne peux plus etre modifié
        if (statusActuel == Status.RESOLVED) {
            throw new InvalidStatusTransitionException(statusActuel, newStatus);
        }

        // transitions depuis OPEN
        if (statusActuel == Status.OPEN) {
            if (newStatus == Status.IN_PROGRESS) {
                ticket.setStatus(newStatus);
            } else if (newStatus == Status.RESOLVED) {
                ticket.setStatus(newStatus);
            } else {
                throw new InvalidStatusTransitionException(statusActuel, newStatus);
            }
        }

        // transitions depuis IN_PROGRESS
        if (statusActuel == Status.IN_PROGRESS) {
            if (newStatus == Status.RESOLVED) {
                ticket.setStatus(newStatus);
            } else {
                throw new InvalidStatusTransitionException(statusActuel, newStatus);
            }
        }

        return ticket;
    }
}
