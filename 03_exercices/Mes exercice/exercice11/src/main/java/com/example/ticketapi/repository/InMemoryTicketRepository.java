package com.example.ticketapi.repository;

import com.example.ticketapi.model.Priority;
import com.example.ticketapi.model.Ticket;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// on stocke les tickets en mémoire avec une HashMap
@Repository
public class InMemoryTicketRepository implements TicketRepository {

    // compteur pour les ids
    private int nextId = 1;

    private Map<Long, Ticket> tickets = new HashMap<>();

    @Override
    public Ticket create(String title, Priority priority) {
        Long id = (long) nextId;
        nextId++;

        Ticket ticket = new Ticket(id, title, priority);
        tickets.put(id, ticket);
        return ticket;
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        Ticket t = tickets.get(id);
        if (t != null) {
            return Optional.of(t);
        }
        return Optional.empty();
    }

    @Override
    public List<Ticket> findAll() {
        // retourner tous les tickets
        List<Ticket> liste = new ArrayList<>(tickets.values());
        return liste;
    }

    @Override
    public void deleteAll() {
        tickets.clear();
        nextId = 1;
    }
}
