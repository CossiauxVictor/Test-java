package com.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FibTest {

    // --- Range 1 ---

    @Test
    void avecRange1LeResultatNEstPasVide() {
        // Arrange
        Fib fib = new Fib(1);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertFalse(result.isEmpty());
    }

    @Test
    void avecRange1LeResultatCorrespondALaListeContenant0() {
        // Arrange
        Fib fib = new Fib(1);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertEquals(List.of(0), result);
    }

    // --- Range 6 ---

    @Test
    void avecRange6LeResultatContientLeChiffre3() {
        // Arrange
        Fib fib = new Fib(6);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertTrue(result.contains(3));
    }

    @Test
    void avecRange6LeResultatContient6Elements() {
        // Arrange
        Fib fib = new Fib(6);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertEquals(6, result.size());
    }

    @Test
    void avecRange6LeResultatNePasContenir4() {
        // Arrange
        Fib fib = new Fib(6);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertFalse(result.contains(4));
    }

    @Test
    void avecRange6LeResultatCorrespondALaListeContenant0_1_1_2_3_5() {
        // Arrange
        Fib fib = new Fib(6);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertEquals(List.of(0, 1, 1, 2, 3, 5), result);
    }

    @Test
    void avecRange6LeResultatEstTrieDemanierAscendante() {
        // Arrange
        Fib fib = new Fib(6);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        List<Integer> sorted = new ArrayList<>(result);
        Collections.sort(sorted);
        assertEquals(sorted, result);
    }
}
