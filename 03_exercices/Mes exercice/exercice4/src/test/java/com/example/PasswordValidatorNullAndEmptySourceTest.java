package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class PasswordValidatorNullAndEmptySourceTest {

    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Doit retourner false pour null et une chaîne vide")
    void devraitRetournerFalseQuandLeMotDePasseEstNullOuVide(String password) {
        boolean result = passwordValidator.isValid(password);

        assertFalse(result);
    }
}
