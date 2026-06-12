package com.example.ticketapi.dto;

import com.example.ticketapi.model.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTicketRequest(

        @NotBlank(message = "Le titre est obligatoire")
        @Size(min = 3, message = "Le titre doit contenir au moins 3 caracteres")
        String title,

        @NotNull(message = "La priorite est obligatoire")
        Priority priority
) {
}
