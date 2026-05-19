package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {

    @Test
    void shouldAddTwoNumbers(){
        // Arange
        Calculator calculator = new Calculator();

        // Act
        int result = calculator.add(2, 3);

        // Assert
        assertEquals(5,result);

    }

    @Test
    void shouldSubtractTwoNumbers() {
        Calculator calculator = new Calculator();

        int result = calculator.subtract(10, 4);

        assertEquals(6, result);
    }

    @Test
    void shouldMultiplyTwoNumbers() {
        Calculator calculator = new Calculator();

        int result = calculator.multiply(5, 4);

        assertEquals(20, result);
    }

    @Test
    void shouldDivideTwoNumbers() {
        Calculator calculator = new Calculator();

        int result = calculator.divide(10, 2);

        assertEquals(5, result);
    }

    @Test
    void shouldThrowExceptionWhenDividingByZero() {
        Calculator calculator = new Calculator();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.divide(10, 0)
        );

        assertEquals("Division by zero is not allowed", exception.getMessage());
    }

    @Test
    void shouldReturnTrueWhenNumberIsEven() {
        Calculator calculator = new Calculator();

        boolean result = calculator.isEven(8);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenNumberIsOdd() {
        Calculator calculator = new Calculator();

        boolean result = calculator.isEven(7);

        assertFalse(result);
    }
}
