package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GradingCalculatorTest {

    @Test
    void devraitRetournerLaNoteAQuandLeScoreEst95EtLaPresenceEst90() {
        // Arrange
        GradingCalculator gradingCalculator = new GradingCalculator(95, 90);

        // Act
        char result = gradingCalculator.getGrade();

        // Assert
        assertEquals('A', result);
    }

    @Test
    void devraitRetournerLaNoteBQuandLeScoreEst85EtLaPresenceEst90() {
        // Arrange
        GradingCalculator gradingCalculator = new GradingCalculator(85, 90);

        // Act
        char result = gradingCalculator.getGrade();

        // Assert
        assertEquals('B', result);
    }

    @Test
    void devraitRetournerLaNoteCQuandLeScoreEst65EtLaPresenceEst90() {
        // Arrange
        GradingCalculator gradingCalculator = new GradingCalculator(65, 90);

        // Act
        char result = gradingCalculator.getGrade();

        // Assert
        assertEquals('C', result);
    }

    @Test
    void devraitRetournerLaNoteBQuandLeScoreEst95EtLaPresenceEst65() {
        // Arrange
        GradingCalculator gradingCalculator = new GradingCalculator(95, 65);

        // Act
        char result = gradingCalculator.getGrade();

        // Assert
        assertEquals('B', result);
    }

    @Test
    void devraitRetournerLaNoteFQuandLeScoreEst95EtLaPresenceEst55() {
        // Arrange
        GradingCalculator gradingCalculator = new GradingCalculator(95, 55);

        // Act
        char result = gradingCalculator.getGrade();

        // Assert
        assertEquals('F', result);
    }

    @Test
    void devraitRetournerLaNoteFQuandLeScoreEst65EtLaPresenceEst55() {
        // Arrange
        GradingCalculator gradingCalculator = new GradingCalculator(65, 55);

        // Act
        char result = gradingCalculator.getGrade();

        // Assert
        assertEquals('F', result);
    }

    @Test
    void devraitRetournerLaNoteFQuandLeScoreEst50EtLaPresenceEst90() {
        // Arrange
        GradingCalculator gradingCalculator = new GradingCalculator(50, 90);

        // Act
        char result = gradingCalculator.getGrade();

        // Assert
        assertEquals('F', result);
    }
}
