package com.example;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class FrameSteps {

    private IGenerateur generateur;
    private Frame frame;

    @Before
    public void setUp() {
        generateur = mock(IGenerateur.class);
    }

    // ── Setup des séries ───────────────────────────────────────────────────

    @Given("une série standard est en cours")
    public void uneSerieStandardEstEnCours() {
        frame = new Frame(generateur, false);
    }

    @Given("une dernière série est en cours")
    public void uneDerniereSerieEstEnCours() {
        frame = new Frame(generateur, true);
    }

    @Given("une série standard est en cours avec 2 lancers normaux")
    public void uneSerieStandardAvec2LancersNormaux() {
        frame = new Frame(generateur, false);
        when(generateur.randomPin(10)).thenReturn(3);
        when(generateur.randomPin(7)).thenReturn(4);
        frame.makeRoll();
        frame.makeRoll();
    }

    @Given("une dernière série est en cours avec un strike et un lancer de 5")
    public void uneDerniereSerieAvecUnStrikeEtUnLancerDe5() {
        frame = new Frame(generateur, true);
        when(generateur.randomPin(10)).thenReturn(10, 5);
        frame.makeRoll(); // strike
        frame.makeRoll(); // 5
    }

    @Given("une dernière série est en cours avec un spare")
    public void uneDerniereSerieAvecUnSpare() {
        frame = new Frame(generateur, true);
        when(generateur.randomPin(10)).thenReturn(7);
        when(generateur.randomPin(3)).thenReturn(3);
        frame.makeRoll(); // 7
        frame.makeRoll(); // 3 → spare (7+3=10)
    }

    @Given("une dernière série est en cours avec 2 lancers normaux")
    public void uneDerniereSerieAvec2LancersNormaux() {
        frame = new Frame(generateur, true);
        when(generateur.randomPin(10)).thenReturn(3);
        when(generateur.randomPin(7)).thenReturn(4);
        frame.makeRoll(); // 3
        frame.makeRoll(); // 4 (total 7, pas de spare)
    }

    @Given("une dernière série est en cours avec un strike et deux lancers")
    public void uneDerniereSerieAvecUnStrikeEtDeuxLancers() {
        frame = new Frame(generateur, true);
        when(generateur.randomPin(10)).thenReturn(10, 5);
        when(generateur.randomPin(5)).thenReturn(3);
        frame.makeRoll(); // strike
        frame.makeRoll(); // 5
        frame.makeRoll(); // 3
    }

    // ── Actions ────────────────────────────────────────────────────────────

    @When("le joueur effectue un lancer de {int} quilles")
    public void leJoueurEffectueUnLancerDe(int pins) {
        when(generateur.randomPin(anyInt())).thenReturn(pins);
        frame.makeRoll();
    }

    @When("le joueur fait un strike")
    public void leJoueurFaitUnStrike() {
        when(generateur.randomPin(10)).thenReturn(10);
        frame.makeRoll();
    }

    // ── Assertions ─────────────────────────────────────────────────────────

    @Then("le score de la série est {int}")
    public void leScoreDeLaSerieEst(int expected) {
        assertEquals(expected, frame.getScore());
    }

    @Then("le joueur peut lancer à nouveau dans cette série")
    public void leJoueurPeutLancerANouveau() {
        when(generateur.randomPin(anyInt())).thenReturn(1);
        assertTrue(frame.makeRoll());
    }

    @Then("le joueur ne peut pas lancer à nouveau dans cette série")
    public void leJoueurNePeutPasLancerANouveau() {
        assertFalse(frame.makeRoll());
    }
}
