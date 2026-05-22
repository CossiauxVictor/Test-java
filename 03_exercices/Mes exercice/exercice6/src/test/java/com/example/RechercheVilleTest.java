package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RechercheVilleTest {

    private RechercheVille rechercheVille;

    @BeforeEach
    void setUp() {
        rechercheVille = new RechercheVille();
    }

    @Test
    @DisplayName("Doit lever une NotFoundException quand le texte est vide")
    void devraitLeverUneNotFoundExceptionQuandLeTexteEstVide() {
        assertThrows(NotFoundException.class, () -> rechercheVille.Rechercher(""));
    }

    @Test
    @DisplayName("Doit lever une NotFoundException quand le texte contient moins de 2 caractères")
    void devraitLeverUneNotFoundExceptionQuandLeTexteContientMoinsDe2Caracteres() {
        assertThrows(NotFoundException.class, () -> rechercheVille.Rechercher("a"));
    }

    @Test
    @DisplayName("Doit retourner Valence et Vancouver pour la recherche 'Va'")
    void devraitRetournerValenceEtVancouverPourLaRechercheVa() {
        List<String> result = rechercheVille.Rechercher("Va");

        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of("Valence", "Vancouver")));
    }

    @Test
    @DisplayName("Doit être insensible à la casse")
    void devraitEtreInsensibleALaCasse() {
        List<String> result = rechercheVille.Rechercher("va");

        assertTrue(result.containsAll(List.of("Valence", "Vancouver")));
    }

    @Test
    @DisplayName("Doit retourner Budapest pour la recherche 'ape'")
    void devraitRetournerBudapestPourLaRechercheApe() {
        List<String> result = rechercheVille.Rechercher("ape");

        assertTrue(result.contains("Budapest"));
    }

    @Test
    @DisplayName("Doit retourner toutes les villes quand le texte est '*'")
    void devraitRetournerToutesLesVillesQuandLeTexteEstUnAsterisque() {
        List<String> result = rechercheVille.Rechercher("*");

        assertEquals(16, result.size());
    }
}
