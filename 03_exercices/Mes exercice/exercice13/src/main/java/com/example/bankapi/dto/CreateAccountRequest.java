package com.example.bankapi.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequest(@NotBlank String number, @NotBlank String holder) {}
