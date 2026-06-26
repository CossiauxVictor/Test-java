package com.example.bankapi.dto;

import jakarta.validation.constraints.NotNull;

// request pour depot et retrait
public record DepositWithdrawRequest(

        @NotNull(message = "Le montant est obligatoire")
        Double amount
) {
}
