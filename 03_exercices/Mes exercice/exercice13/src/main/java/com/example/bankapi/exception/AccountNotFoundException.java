package com.example.bankapi.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String number) {
        super("Compte non trouve : " + number);
    }
}
