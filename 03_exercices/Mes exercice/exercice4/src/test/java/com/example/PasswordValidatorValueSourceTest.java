package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordValidatorValueSourceTest {

    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Password1!",
            "Admin2024@",
            "Secure9#",
            "Hello1@World"
    })
    @DisplayName("Doit retourner true pour des mots de passe valides")
    void devraitRetournerTrueQuandLeMotDePasseRespecteToutesLesRegles(String password) {
        boolean result = passwordValidator.isValid(password);

        assertTrue(result);
    }
}
