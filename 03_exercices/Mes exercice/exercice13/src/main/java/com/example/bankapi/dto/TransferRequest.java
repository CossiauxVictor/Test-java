package com.example.bankapi.dto;

public record TransferRequest(String fromNumber, String toNumber, double amount) {}
