package com.example.bankapi.repository;

import com.example.bankapi.model.Account;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryAccountRepository implements AccountRepository {
    private final Map<String, Account> store = new LinkedHashMap<>();

    @Override
    public Account save(Account account) {
        store.put(account.getNumber(), account);
        return account;
    }

    @Override
    public Optional<Account> findByNumber(String number) {
        return Optional.ofNullable(store.get(number));
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public boolean existsByNumber(String number) {
        return store.containsKey(number);
    }

    @Override
    public void deleteAll() {
        store.clear();
    }
}
