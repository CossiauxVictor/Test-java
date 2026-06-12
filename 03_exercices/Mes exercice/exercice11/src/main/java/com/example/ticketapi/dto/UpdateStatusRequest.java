package com.example.ticketapi.dto;

import com.example.ticketapi.model.Status;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(

        @NotNull(message = "Le statut est obligatoire")
        Status status
) {
}
