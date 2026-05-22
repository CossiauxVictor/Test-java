package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DiceScoreTest {

    @Mock
    private Ide de;

    private DiceScore diceScore;

    @BeforeEach
    void setUp() {
        diceScore = new DiceScore(de);
    }

    @Test
    @DisplayName("Doit retourner valeur * 2 + 10 quand les deux dés sont identiques")
    void devraitRetournerValeurMultiplieeParDeuxPlusDixQuandLesDeuDesSontIdentiques() {
        when(de.getRoll()).thenReturn(3);

        int result = diceScore.getScore();

        assertEquals(16, result);
    }

    @Test
    @DisplayName("Doit retourner 30 quand les deux dés sont égaux à 6")
    void devraitRetourner30QuandLesDeuDesSontEgauxA6() {
        when(de.getRoll()).thenReturn(6);

        int result = diceScore.getScore();

        assertEquals(30, result);
    }

    @Test
    @DisplayName("Doit retourner la valeur la plus haute quand les dés sont différents")
    void devraitRetournerLaPlusHauteValeurQuandLesDeSontDifferents() {
        when(de.getRoll()).thenReturn(3, 5);

        int result = diceScore.getScore();

        assertEquals(5, result);
    }
}
