package com.example.bankapi.exception;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException(String number) {
        super("Compte déjà existant : " + number);
    }
}
