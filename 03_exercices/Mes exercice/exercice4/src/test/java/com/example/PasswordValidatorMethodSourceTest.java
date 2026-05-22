package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PasswordValidatorMethodSourceTest {

    private PasswordValidator passwordValidator;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
    }

    static Stream<Object[]> passwordCases() {
        return Stream.of(
                new Object[]{"Password1!", true},
                new Object[]{"Admin2024@", true},
                new Object[]{"short1!", false},
                new Object[]{"PASSWORD1!", false},
                new Object[]{"password1!", false},
                new Object[]{"Password!", false},
                new Object[]{"Password1", false}
        );
    }

    @ParameterizedTest(name = "{index} => motDePasse={0}, résultatAttendu={1}")
    @MethodSource("passwordCases")
    @DisplayName("Doit valider les mots de passe depuis une méthode source")
    void devraitRetournerLeResultatAttenduQuandOnValideLeMotDePasseDepuisUneMethodeSource(String password, boolean resultatAttendu) {
        boolean result = passwordValidator.isValid(password);

        assertEquals(resultatAttendu, result);
    }
}
