package com.example.ticketapi.bdd;

import com.example.ticketapi.repository.TicketRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TicketApiStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private ResultActions lastResult;
    private Long createdTicketId;

    @Before
    public void setUp() {
        repository.deleteAll();
        lastResult = null;
        createdTicketId = null;
    }

    @Given("aucun ticket n existe")
    public void aucunTicketNExiste() {
        repository.deleteAll();
    }

    @When("je cree un ticket avec le titre {string} et la priorite {string}")
    public void jeCreeUnTicket(String title, String priority) throws Exception {
        String body = String.format("{\"title\":\"%s\",\"priority\":\"%s\"}", title, priority);

        lastResult = mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // recuperer l id du ticket cree
        String responseBody = lastResult.andReturn().getResponse().getContentAsString();
        if (!responseBody.isBlank() && lastResult.andReturn().getResponse().getStatus() == 201) {
            createdTicketId = objectMapper.readTree(responseBody).get("id").asLong();
        }
    }

    @When("je modifie le statut du ticket cree vers {string}")
    public void jeModifieLeStatutDuTicketCreeVers(String newStatus) throws Exception {
        String body = String.format("{\"status\":\"%s\"}", newStatus);
        lastResult = mockMvc.perform(patch("/api/tickets/" + createdTicketId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    @When("je consulte le ticket avec l identifiant {long}")
    public void jeConsulteLeTicketAvecLIdentifiant(Long id) throws Exception {
        lastResult = mockMvc.perform(get("/api/tickets/" + id));
    }

    @Then("la reponse HTTP doit etre {int}")
    public void laReponseHTTPDoitEtre(int expectedStatus) throws Exception {
        lastResult.andExpect(status().is(expectedStatus));
    }

    @Then("la reponse contient le titre {string}")
    public void laReponseContientLeTitre(String expectedTitle) throws Exception {
        lastResult.andExpect(jsonPath("$.title").value(expectedTitle));
    }

    @Then("le statut du ticket est {string}")
    public void leStatutDuTicketEst(String expectedStatus) throws Exception {
        lastResult.andExpect(jsonPath("$.status").value(expectedStatus));
    }

    @Then("la reponse contient un message d erreur")
    public void laReponseContientUnMessageDErreur() throws Exception {
        lastResult.andExpect(jsonPath("$.message").exists());
    }
}
