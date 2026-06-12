package com.example.ticketapi.dto;

import com.example.ticketapi.model.Priority;
import com.example.ticketapi.model.Status;

// DTO pour renvoyer un ticket au client
public record TicketResponse(Long id, String title, Priority priority, Status status) {
}
