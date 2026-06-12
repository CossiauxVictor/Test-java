package com.example.ticketapi.repository;

import com.example.ticketapi.model.Priority;
import com.example.ticketapi.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {

    Ticket create(String title, Priority priority);

    Optional<Ticket> findById(Long id);

    List<Ticket> findAll();

    void deleteAll();
}
