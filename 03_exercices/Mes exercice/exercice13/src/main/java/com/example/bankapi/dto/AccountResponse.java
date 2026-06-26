package com.example.bankapi.dto;

import com.example.bankapi.model.Account;

public record AccountResponse(String number, String holder, double balance) {
    public static AccountResponse from(Account account) {
        return new AccountResponse(account.getNumber(), account.getHolder(), account.getBalance());
    }
}
