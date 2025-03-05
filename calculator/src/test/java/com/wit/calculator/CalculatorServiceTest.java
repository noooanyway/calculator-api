package com.wit.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorServiceTest {

    private CalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        calculatorService = new CalculatorService();
    }

    @Test
    void testAdd() {
        BigDecimal result = calculatorService.add(new BigDecimal("5"), new BigDecimal("3"));
        assertEquals(new BigDecimal("8"), result);
    }

    @Test
    void testSubtract() {
        BigDecimal result = calculatorService.subtract(new BigDecimal("10"), new BigDecimal("4"));
        assertEquals(new BigDecimal("6"), result);
    }

    @Test
    void testMultiply() {
        BigDecimal result = calculatorService.multiply(new BigDecimal("2"), new BigDecimal("3"));
        assertEquals(new BigDecimal("6"), result);
    }

    @Test
    void testDivide() {
        BigDecimal result = calculatorService.divide(new BigDecimal("10"), new BigDecimal("2"));
        assertEquals(new BigDecimal("5"), result.stripTrailingZeros(), "O resultado esperado é 5.");
    }




    @Test
    void testDivideByZero() {
        Executable executable = () -> calculatorService.divide(new BigDecimal("10"), BigDecimal.ZERO);
        assertThrows(ArithmeticException.class, executable, "Divisão por zero não permitida.");
    }
}
