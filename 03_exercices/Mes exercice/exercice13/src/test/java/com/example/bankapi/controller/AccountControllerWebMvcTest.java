package com.example.bankapi.controller;

import com.example.bankapi.dto.AccountResponse;
import com.example.bankapi.exception.AccountAlreadyExistsException;
import com.example.bankapi.exception.AccountNotFoundException;
import com.example.bankapi.exception.InsufficientFundsException;
import com.example.bankapi.model.Account;
import com.example.bankapi.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_retourne_201_avec_compte() throws Exception {
        Account account = new Account("FR001", "Alice");
        when(service.create("FR001", "Alice")).thenReturn(account);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("number", "FR001", "holder", "Alice"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number").value("FR001"))
                .andExpect(jsonPath("$.balance").value(0.0));
    }

    @Test
    void create_doublon_retourne_409() throws Exception {
        when(service.create(anyString(), anyString())).thenThrow(new AccountAlreadyExistsException("FR001"));

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("number", "FR001", "holder", "Alice"))))
                .andExpect(status().isConflict());
    }

    @Test
    void getAll_retourne_liste_comptes() throws Exception {
        Account alice = new Account("FR001", "Alice");
        Account bob = new Account("FR002", "Bob");
        when(service.findAll()).thenReturn(List.of(alice, bob));

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getByNumber_existant_retourne_200() throws Exception {
        Account account = new Account("FR001", "Alice");
        when(service.getByNumber("FR001")).thenReturn(account);

        mockMvc.perform(get("/accounts/FR001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.holder").value("Alice"));
    }

    @Test
    void getByNumber_inexistant_retourne_404() throws Exception {
        when(service.getByNumber("FR999")).thenThrow(new AccountNotFoundException("FR999"));

        mockMvc.perform(get("/accounts/FR999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deposit_valide_retourne_200() throws Exception {
        Account account = new Account("FR001", "Alice");
        account.deposit(100.0);
        when(service.deposit("FR001", 100.0)).thenReturn(account);

        mockMvc.perform(post("/accounts/FR001/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("amount", 100.0))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(100.0));
    }

    @Test
    void withdraw_fonds_insuffisants_retourne_422() throws Exception {
        when(service.withdraw(anyString(), anyDouble())).thenThrow(new InsufficientFundsException("FR001"));

        mockMvc.perform(post("/accounts/FR001/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("amount", 100.0))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void transfer_valide_retourne_204() throws Exception {
        mockMvc.perform(post("/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("fromNumber", "FR001", "toNumber", "FR002", "amount", 100.0))))
                .andExpect(status().isNoContent());
    }
}
