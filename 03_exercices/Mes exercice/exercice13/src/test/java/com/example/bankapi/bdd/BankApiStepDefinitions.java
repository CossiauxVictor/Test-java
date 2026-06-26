package com.example.bankapi.bdd;

import com.example.bankapi.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.fr.Alors;
import io.cucumber.java.fr.Et;
import io.cucumber.java.fr.Etantdonné;
import io.cucumber.java.fr.Quand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class BankApiStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ResultActions result;

    @Before
    public void resetRepo() {
        accountRepository.deleteAll();
    }

    @Etantdonné("aucun compte n'existe")
    public void aucun_compte_nexiste() {
        accountRepository.deleteAll();
    }

    @Etantdonné("un compte {string} existe avec {double} euros")
    public void un_compte_existe_avec(String number, double solde) throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("number", number, "holder", "Test"));
        mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        if (solde > 0) {
            String depositBody = objectMapper.writeValueAsString(Map.of("amount", solde));
            mockMvc.perform(post("/accounts/" + number + "/deposit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(depositBody));
        }
    }

    @Quand("je crée un compte avec le numéro {string} et le titulaire {string}")
    public void je_cree_un_compte(String number, String holder) throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("number", number, "holder", holder));
        result = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    @Quand("je dépose {double} euros sur le compte {string}")
    public void je_depose(double amount, String number) throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("amount", amount));
        result = mockMvc.perform(post("/accounts/" + number + "/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    @Quand("je retire {double} euros du compte {string}")
    public void je_retire(double amount, String number) throws Exception {
        String body = objectMapper.writeValueAsString(Map.of("amount", amount));
        result = mockMvc.perform(post("/accounts/" + number + "/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    @Quand("je vire {double} euros du compte {string} vers le compte {string}")
    public void je_vire(double amount, String from, String to) throws Exception {
        String body = objectMapper.writeValueAsString(
                Map.of("fromNumber", from, "toNumber", to, "amount", amount));
        result = mockMvc.perform(post("/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    @Alors("le statut de la réponse est {int}")
    public void le_statut_est(int status) throws Exception {
        assertThat(result.andReturn().getResponse().getStatus()).isEqualTo(status);
    }

    @Et("le compte a un solde de {double}")
    public void le_compte_a_un_solde_de(double expected) throws Exception {
        String content = result.andReturn().getResponse().getContentAsString();
        Map<?, ?> response = objectMapper.readValue(content, Map.class);
        double balance = ((Number) response.get("balance")).doubleValue();
        assertThat(balance).isEqualTo(expected);
    }
}
