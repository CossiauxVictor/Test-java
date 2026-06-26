package com.example.bankapi.bdd;

import com.example.bankapi.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BankApiStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ResultActions lastResult;

    @Before
    public void setUp() {
        accountRepository.deleteAll();
        lastResult = null;
    }

    @Given("aucun compte n existe")
    public void aucunCompteNExiste() {
        accountRepository.deleteAll();
    }

    @Given("un compte {string} appartenant a {string} avec un solde de {double}")
    public void unCompteAppartenant(String number, String owner, double balance) throws Exception {
        // creer le compte
        String body = String.format("{\"number\":\"%s\",\"owner\":\"%s\"}", number, owner);
        mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // faire un depot si le solde est > 0
        if (balance > 0) {
            String depositBody = String.format("{\"amount\":%s}", balance);
            mockMvc.perform(post("/accounts/" + number + "/deposit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(depositBody));
        }
    }

    @When("je cree un compte avec le numero {string} et le titulaire {string}")
    public void jeCreeUnCompte(String number, String owner) throws Exception {
        String body = String.format("{\"number\":\"%s\",\"owner\":\"%s\"}", number, owner);
        lastResult = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    @When("je depose {double} sur le compte {string}")
    public void jeDepose(double amount, String number) throws Exception {
        String body = String.format("{\"amount\":%s}", amount);
        lastResult = mockMvc.perform(post("/accounts/" + number + "/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    @When("je retire {double} du compte {string}")
    public void jeRetire(double amount, String number) throws Exception {
        String body = String.format("{\"amount\":%s}", amount);
        lastResult = mockMvc.perform(post("/accounts/" + number + "/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    @When("j effectue un virement de {double} du compte {string} vers le compte {string}")
    public void jEffectueUnVirement(double amount, String from, String to) throws Exception {
        String body = String.format("{\"fromAccount\":\"%s\",\"toAccount\":\"%s\",\"amount\":%s}", from, to, amount);
        lastResult = mockMvc.perform(post("/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    @Then("la reponse HTTP doit etre {int}")
    public void laReponseHTTPDoitEtre(int expectedStatus) throws Exception {
        lastResult.andExpect(status().is(expectedStatus));
    }

    @And("le solde du compte est {double}")
    public void leSoldeEstDe(double expectedBalance) throws Exception {
        lastResult.andExpect(jsonPath("$.balance").value(expectedBalance));
    }

    @And("la reponse contient un message d erreur")
    public void laReponseContientUnMessageDErreur() throws Exception {
        lastResult.andExpect(jsonPath("$.message").exists());
    }
}
