package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PasswordValidatorCsvSourceTest {

    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
    }

    @ParameterizedTest(name = "{index} => motDePasse={0}, résultatAttendu={1}")
    @CsvSource({
            "Password1!, true",
            "Admin2024@, true",
            "short1!, false",
            "PASSWORD1!, false",
            "password1!, false",
            "Password!, false",
            "Password1, false"
    })
    @DisplayName("Doit retourner le résultat attendu pour chaque mot de passe")
    void devraitRetournerLeResultatAttenduQuandOnValideLeMotDePasse(String password, boolean resultatAttendu) {
        boolean result = passwordValidator.isValid(password);

        assertEquals(resultatAttendu, result);
    }
}
