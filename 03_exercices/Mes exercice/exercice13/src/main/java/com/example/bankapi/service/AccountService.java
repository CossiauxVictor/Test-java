package com.example.bankapi.service;

import com.example.bankapi.exception.AccountAlreadyExistsException;
import com.example.bankapi.exception.AccountNotFoundException;
import com.example.bankapi.exception.InsufficientFundsException;
import com.example.bankapi.model.Account;
import com.example.bankapi.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public Account create(String number, String holder) {
        if (repository.existsByNumber(number)) {
            throw new AccountAlreadyExistsException(number);
        }
        return repository.save(new Account(number, holder));
    }

    public Account getByNumber(String number) {
        return repository.findByNumber(number)
                .orElseThrow(() -> new AccountNotFoundException(number));
    }

    public List<Account> findAll() {
        return repository.findAll();
    }

    public Account deposit(String number, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        Account account = getByNumber(number);
        account.deposit(amount);
        return repository.save(account);
    }

    public Account withdraw(String number, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        Account account = getByNumber(number);
        if (account.getBalance() < amount) {
            throw new InsufficientFundsException(number);
        }
        account.withdraw(amount);
        return repository.save(account);
    }

    public void transfer(String fromNumber, String toNumber, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        Account from = getByNumber(fromNumber);
        Account to = getByNumber(toNumber);
        if (from.getBalance() < amount) {
            throw new InsufficientFundsException(fromNumber);
        }
        from.withdraw(amount);
        to.deposit(amount);
        repository.save(from);
        repository.save(to);
    }
}
