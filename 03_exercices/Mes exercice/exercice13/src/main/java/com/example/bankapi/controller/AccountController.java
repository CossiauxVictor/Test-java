package com.example.bankapi.controller;

import com.example.bankapi.dto.AccountResponse;
import com.example.bankapi.dto.CreateAccountRequest;
import com.example.bankapi.dto.DepositWithdrawRequest;
import com.example.bankapi.dto.TransferRequest;
import com.example.bankapi.model.Account;
import com.example.bankapi.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // creer un compte
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {

        Account account = accountService.createAccount(request.number(), request.owner());

        AccountResponse response = toResponse(account);

        return ResponseEntity.created(URI.create("/accounts/" + account.getNumber())).body(response);
    }

    // lister tous les comptes
    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {

        List<Account> accounts = accountService.getAllAccounts();
        List<AccountResponse> responses = new ArrayList<>();

        for (Account a : accounts) {
            responses.add(toResponse(a));
        }

        return ResponseEntity.ok(responses);
    }

    // consulter un compte
    @GetMapping("/{number}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String number) {

        Account account = accountService.getAccount(number);

        return ResponseEntity.ok(toResponse(account));
    }

    // depot d argent
    @PostMapping("/{number}/deposit")
    public ResponseEntity<AccountResponse> deposit(
            @PathVariable String number,
            @Valid @RequestBody DepositWithdrawRequest request) {

        Account account = accountService.deposit(number, request.amount());

        return ResponseEntity.ok(toResponse(account));
    }

    // retrait d argent
    @PostMapping("/{number}/withdraw")
    public ResponseEntity<AccountResponse> withdraw(
            @PathVariable String number,
            @Valid @RequestBody DepositWithdrawRequest request) {

        Account account = accountService.withdraw(number, request.amount());

        return ResponseEntity.ok(toResponse(account));
    }

    // virement entre deux comptes
    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequest request) {

        accountService.transfer(request.fromAccount(), request.toAccount(), request.amount());

        return ResponseEntity.ok().build();
    }

    // methode utilitaire pour convertir Account en AccountResponse
    private AccountResponse toResponse(Account account) {
        return new AccountResponse(account.getNumber(), account.getOwner(), account.getBalance());
    }
}
