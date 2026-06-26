package com.example.bankapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// request pour un virement entre deux comptes
public record TransferRequest(

        @NotBlank(message = "Le numero du compte source est obligatoire")
        String fromAccount,

        @NotBlank(message = "Le numero du compte destinataire est obligatoire")
        String toAccount,

        @NotNull(message = "Le montant est obligatoire")
        Double amount
) {
}
