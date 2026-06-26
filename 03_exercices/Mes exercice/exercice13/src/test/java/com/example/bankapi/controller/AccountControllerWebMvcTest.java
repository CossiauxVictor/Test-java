package com.example.bankapi.controller;

import com.example.bankapi.exception.AccountAlreadyExistsException;
import com.example.bankapi.exception.AccountNotFoundException;
import com.example.bankapi.exception.InsufficientFundsException;
import com.example.bankapi.model.Account;
import com.example.bankapi.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// tests du controller avec MockMvc
@WebMvcTest(AccountController.class)
class AccountControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @Test
    void shouldReturn201_whenAccountIsCreated() throws Exception {
        // Arrange
        Account account = new Account("FR001", "Alice");
        when(accountService.createAccount("FR001", "Alice")).thenReturn(account);

        // Act + Assert
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number\":\"FR001\",\"owner\":\"Alice\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number").value("FR001"))
                .andExpect(jsonPath("$.owner").value("Alice"))
                .andExpect(jsonPath("$.balance").value(0.0));

        verify(accountService).createAccount("FR001", "Alice");
    }

    @Test
    void shouldReturn400_whenNumberIsMissing() throws Exception {
        // numero manquant
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"owner\":\"Alice\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void shouldReturn409_whenAccountAlreadyExists() throws Exception {
        // Arrange
        when(accountService.createAccount("FR001", "Alice"))
                .thenThrow(new AccountAlreadyExistsException("FR001"));

        // Act + Assert
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number\":\"FR001\",\"owner\":\"Alice\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void shouldReturn404_whenAccountNotFound() throws Exception {
        // Arrange
        when(accountService.getAccount("INCONNU")).thenThrow(new AccountNotFoundException("INCONNU"));

        // Act + Assert
        mockMvc.perform(get("/accounts/INCONNU"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturn409_whenInsufficientFundsForWithdraw() throws Exception {
        // Arrange
        when(accountService.withdraw(eq("FR001"), anyDouble()))
                .thenThrow(new InsufficientFundsException("Solde insuffisant"));

        // Act + Assert
        mockMvc.perform(post("/accounts/FR001/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":1000}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void shouldReturn200_withListOfAccounts() throws Exception {
        // Arrange
        when(accountService.getAllAccounts()).thenReturn(List.of(
                new Account("FR001", "Alice"),
                new Account("FR002", "Bob")
        ));

        // Act + Assert
        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(accountService).getAllAccounts();
    }
}
