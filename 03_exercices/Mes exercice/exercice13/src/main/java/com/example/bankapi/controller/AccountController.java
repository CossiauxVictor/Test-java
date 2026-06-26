package com.example.bankapi.controller;

import com.example.bankapi.dto.AccountResponse;
import com.example.bankapi.dto.CreateAccountRequest;
import com.example.bankapi.dto.DepositWithdrawRequest;
import com.example.bankapi.dto.TransferRequest;
import com.example.bankapi.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest request) {
        var account = service.create(request.number(), request.holder());
        return ResponseEntity
                .created(URI.create("/accounts/" + account.getNumber()))
                .body(AccountResponse.from(account));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> findAll() {
        var accounts = service.findAll().stream()
                .map(AccountResponse::from)
                .toList();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{number}")
    public ResponseEntity<AccountResponse> getByNumber(@PathVariable String number) {
        return ResponseEntity.ok(AccountResponse.from(service.getByNumber(number)));
    }

    @PostMapping("/{number}/deposit")
    public ResponseEntity<AccountResponse> deposit(@PathVariable String number,
                                                   @RequestBody DepositWithdrawRequest request) {
        return ResponseEntity.ok(AccountResponse.from(service.deposit(number, request.amount())));
    }

    @PostMapping("/{number}/withdraw")
    public ResponseEntity<AccountResponse> withdraw(@PathVariable String number,
                                                    @RequestBody DepositWithdrawRequest request) {
        return ResponseEntity.ok(AccountResponse.from(service.withdraw(number, request.amount())));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody TransferRequest request) {
        service.transfer(request.fromNumber(), request.toNumber(), request.amount());
        return ResponseEntity.noContent().build();
    }
}
