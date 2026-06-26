package com.example.bankapi.repository;

import com.example.bankapi.model.Account;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// stockage des comptes en memoire avec une hashmap
@Repository
public class InMemoryAccountRepository implements AccountRepository {

    // cle = numero de compte, valeur = compte
    private Map<String, Account> accounts = new HashMap<>();

    @Override
    public Account save(Account account) {
        accounts.put(account.getNumber(), account);
        return account;
    }

    @Override
    public Optional<Account> findByNumber(String number) {
        Account account = accounts.get(number);
        if (account == null) {
            return Optional.empty();
        }
        return Optional.of(account);
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public void deleteAll() {
        accounts.clear();
    }
}
