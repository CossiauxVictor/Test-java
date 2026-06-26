package com.example.bankapi.service;

import com.example.bankapi.exception.AccountAlreadyExistsException;
import com.example.bankapi.exception.AccountNotFoundException;
import com.example.bankapi.exception.InsufficientFundsException;
import com.example.bankapi.model.Account;
import com.example.bankapi.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // creer un compte bancaire
    public Account createAccount(String number, String owner) {

        // verifier que le numero n est pas deja pris
        if (accountRepository.findByNumber(number).isPresent()) {
            throw new AccountAlreadyExistsException(number);
        }

        Account account = new Account(number, owner);
        return accountRepository.save(account);
    }

    // recuperer un compte par son numero
    public Account getAccount(String number) {
        Account account = accountRepository.findByNumber(number).orElse(null);
        if (account == null) {
            throw new AccountNotFoundException(number);
        }
        return account;
    }

    // lister tous les comptes
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // deposer de l argent sur un compte
    public Account deposit(String number, double amount) {

        // le montant doit etre positif
        if (amount <= 0) {
            throw new IllegalArgumentException("Le montant doit etre strictement positif");
        }

        Account account = getAccount(number);
        account.setBalance(account.getBalance() + amount);
        return account;
    }

    // retirer de l argent d un compte
    public Account withdraw(String number, double amount) {

        // le montant doit etre positif
        if (amount <= 0) {
            throw new IllegalArgumentException("Le montant doit etre strictement positif");
        }

        Account account = getAccount(number);

        // verifier que le solde est suffisant
        if (account.getBalance() < amount) {
            throw new InsufficientFundsException("Solde insuffisant pour effectuer le retrait");
        }

        account.setBalance(account.getBalance() - amount);
        return account;
    }

    // virement entre deux comptes
    public void transfer(String fromNumber, String toNumber, double amount) {

        // le montant doit etre positif
        if (amount <= 0) {
            throw new IllegalArgumentException("Le montant doit etre strictement positif");
        }

        // verifier que les deux comptes existent
        Account from = getAccount(fromNumber);
        Account to = getAccount(toNumber);

        // verifier le solde du compte emetteur
        if (from.getBalance() < amount) {
            throw new InsufficientFundsException("Solde insuffisant pour effectuer le virement");
        }

        // effectuer le virement
        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);
    }
}
