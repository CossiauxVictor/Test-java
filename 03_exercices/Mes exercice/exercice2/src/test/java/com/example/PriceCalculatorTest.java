package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PriceCalculatorTest {

    // --- calculateTotalPrice ---

    @Test
    void devraitRetournerLePrixTotalQuandLePrixUnitaireEst10EtLaQuantiteEst3() {
        // Arrange
        PriceCalculator priceCalculator = new PriceCalculator();

        // Act
        double result = priceCalculator.calculateTotalPrice(10.0, 3);

        // Assert
        assertEquals(30.0, result);
    }

    @Test
    void devraitLeverUneExceptionQuandLePrixUnitaireEstNegatif() {
        // Arrange
        PriceCalculator priceCalculator = new PriceCalculator();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> priceCalculator.calculateTotalPrice(-10.0, 3)
        );

        assertEquals("Le prix unitaire ne doit pas être négatif", exception.getMessage());
    }

    @Test
    void devraitLeverUneExceptionQuandLaQuantiteEstNegative() {
        // Arrange
        PriceCalculator priceCalculator = new PriceCalculator();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> priceCalculator.calculateTotalPrice(10.0, -3)
        );

        assertEquals("La quantité ne doit pas être négative", exception.getMessage());
    }

    // --- applyDiscount ---

    @Test
    void devraitRetournerLePrixRemiseQuandLeTauxEst20Pourcent() {
        // Arrange
        PriceCalculator priceCalculator = new PriceCalculator();

        // Act
        double result = priceCalculator.applyDiscount(100.0, 0.20);

        // Assert
        assertEquals(80.0, result);
    }

    @Test
    void devraitLeverUneExceptionQuandLeTauxDeRemiseEstNegatif() {
        // Arrange
        PriceCalculator priceCalculator = new PriceCalculator();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> priceCalculator.applyDiscount(100.0, -0.20)
        );

        assertEquals("Le taux de remise ne doit pas être négatif", exception.getMessage());
    }

    // --- calculateVat ---

    @Test
    void devraitRetournerLeMontantTVAQuandLeTauxEst20Pourcent() {
        // Arrange
        PriceCalculator priceCalculator = new PriceCalculator();

        // Act
        double result = priceCalculator.calculateVat(100.0, 0.20);

        // Assert
        assertEquals(20.0, result);
    }

    @Test
    void devraitLeverUneExceptionQuandLeTauxDeTVAEstNegatifDansCalculerTVA() {
        // Arrange
        PriceCalculator priceCalculator = new PriceCalculator();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> priceCalculator.calculateVat(100.0, -0.20)
        );

        assertEquals("Le taux de TVA ne doit pas être négatif", exception.getMessage());
    }

    // --- calculatePriceWithVat ---

    @Test
    void devraitRetournerLePrixAvecTVAQuandLeTauxEst20Pourcent() {
        // Arrange
        PriceCalculator priceCalculator = new PriceCalculator();

        // Act
        double result = priceCalculator.calculatePriceWithVat(100.0, 0.20);

        // Assert
        assertEquals(120.0, result);
    }

    @Test
    void devraitLeverUneExceptionQuandLeTauxDeTVAEstNegatifDansCalculerPrixAvecTVA() {
        // Arrange
        PriceCalculator priceCalculator = new PriceCalculator();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> priceCalculator.calculatePriceWithVat(100.0, -0.20)
        );

        assertEquals("Le taux de TVA ne doit pas être négatif", exception.getMessage());
    }
}
