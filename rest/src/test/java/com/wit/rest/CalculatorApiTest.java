package com.wit.rest;

import com.wit.calculator.CalculatorService;
import com.wit.rest.kafka.OperationProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

@WebMvcTest(CalculatorController.class)
public class CalculatorApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalculatorService calculatorService;

    @MockBean
    private OperationProducer operationProducer;

    @Test
    void testSumEndpoint() throws Exception {
        when(calculatorService.add(new BigDecimal("5"), new BigDecimal("3"))).thenReturn(new BigDecimal("8"));

        mockMvc.perform(get("/api/calculator/sum?a=5&b=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("8"));
    }

    @Test
    void testDivisionByZero() throws Exception {
        when(calculatorService.divide(new BigDecimal("10"), BigDecimal.ZERO))
                .thenThrow(new ArithmeticException("Divisão por zero não permitida."));

        mockMvc.perform(get("/api/calculator/division")
                        .param("a", "10")
                        .param("b", "0"))
                .andExpect(status().isBadRequest()) // Alterado para BAD_REQUEST (400)
                .andExpect(jsonPath("$.error").value("Erro na operação: Divisão por zero não permitida."));
    }

}
