package com.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderSteps {
    private ProductRepository productRepository;
    private OrderService orderService;
    private OrderReceipt receipt;
    private OrderException thrownException;

    @Given("un produit {string} nommé {string} au prix de {double} avec un stock de {int}")
    public void unProduit(String ref, String name, double price, int stock) {
        productRepository = mock(ProductRepository.class);
        orderService = new OrderService(productRepository);
        Product product = new Product(ref, name, price, stock);
        when(productRepository.findByReference(ref)).thenReturn(Optional.of(product));
    }

    @Given("aucun produit n'existe avec la référence {string}")
    public void aucunProduitNExisteAvecLaReference(String ref) {
        productRepository = mock(ProductRepository.class);
        orderService = new OrderService(productRepository);
        when(productRepository.findByReference(ref)).thenReturn(Optional.empty());
    }

    @When("le client {string} de profil {word} commande {int} unité\\(s) de {string}")
    public void leClientCommande(String email, String profile, int quantity, String ref) {
        receipt = null;
        thrownException = null;
        try {
            receipt = orderService.placeOrder(email, CustomerProfile.valueOf(profile), ref, quantity);
        } catch (OrderException e) {
            thrownException = e;
        }
    }

    @Then("la commande est acceptée")
    public void laCommandeEstAcceptee() {
        assertNotNull(receipt);
        assertNull(thrownException);
    }

    @And("le montant total est {double}")
    public void leMontantTotalEst(double expected) {
        assertEquals(expected, receipt.getTotalAmount(), 0.01);
    }

    @And("le message de confirmation contient {string}")
    public void leMessageDeConfirmationContient(String expected) {
        assertTrue(receipt.getConfirmationMessage().contains(expected));
    }

    @Then("la commande est refusée")
    public void laCommandeEstRefusee() {
        assertNotNull(thrownException);
        assertNull(receipt);
    }

    @And("le motif du refus est {string}")
    public void leMotifDuRefusEst(String expected) {
        assertEquals(expected, thrownException.getMessage());
    }
}
