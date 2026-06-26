package com.example.bankapi.integration;

import com.example.bankapi.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// test d integration complet - on charge le contexte Spring en entier
@SpringBootTest
@AutoConfigureMockMvc
class BankApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
    }

    @Test
    void shouldCreateAccountThenDepositThenWithdrawThenTransfer() throws Exception {

        // 1 - creer le compte Alice
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number\":\"FR001\",\"owner\":\"Alice\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number").value("FR001"))
                .andExpect(jsonPath("$.balance").value(0.0));

        // 2 - creer le compte Bob
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number\":\"FR002\",\"owner\":\"Bob\"}"))
                .andExpect(status().isCreated());

        // 3 - deposer 500 sur le compte Alice
        mockMvc.perform(post("/accounts/FR001/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":500}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(500.0));

        // 4 - retirer 100 du compte Alice
        mockMvc.perform(post("/accounts/FR001/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":100}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(400.0));

        // 5 - virement de 200 de Alice vers Bob
        mockMvc.perform(post("/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fromAccount\":\"FR001\",\"toAccount\":\"FR002\",\"amount\":200}"))
                .andExpect(status().isOk());

        // 6 - verifier le solde final d Alice
        mockMvc.perform(get("/accounts/FR001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(200.0));

        // 7 - verifier le solde de Bob
        mockMvc.perform(get("/accounts/FR002"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(200.0));
    }
}
