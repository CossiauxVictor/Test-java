package com.example.bankapi.integration;

import com.example.bankapi.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BankApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void scenario_complet_depot_retrait_virement() throws Exception {
        // Créer deux comptes
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("number", "FR001", "holder", "Alice"))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("number", "FR002", "holder", "Bob"))))
                .andExpect(status().isCreated());

        // Dépôt sur FR001
        mockMvc.perform(post("/accounts/FR001/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("amount", 500.0))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(500.0));

        // Virement FR001 -> FR002
        mockMvc.perform(post("/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("fromNumber", "FR001", "toNumber", "FR002", "amount", 200.0))))
                .andExpect(status().isNoContent());

        // Vérifier solde FR001
        mockMvc.perform(get("/accounts/FR001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(300.0));

        // Vérifier solde FR002
        mockMvc.perform(get("/accounts/FR002"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(200.0));
    }

    @Test
    void creer_compte_doublon_retourne_409() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("number", "FR001", "holder", "Alice"));

        mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict());
    }

    @Test
    void retrait_fonds_insuffisants_retourne_422() throws Exception {
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("number", "FR001", "holder", "Alice"))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/accounts/FR001/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("amount", 100.0))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void consulter_compte_inexistant_retourne_404() throws Exception {
        mockMvc.perform(get("/accounts/INCONNU"))
                .andExpect(status().isNotFound());
    }
}
