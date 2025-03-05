package com.wit.rest;

import com.wit.calculator.CalculatorService;
import com.wit.rest.kafka.OperationProducer;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CalculatorControllerTest {

    @Mock
    private CalculatorService calculatorService;

    @Mock
    private OperationProducer operationProducer;

    @InjectMocks
    private CalculatorController calculatorController;

    @BeforeEach
    void setUp() {
     }

    @Test
    void testSum() {
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Configurando o mock para retornar um valor esperado
        when(calculatorService.add(any(), any())).thenReturn(new BigDecimal("8"));

        // Chamando o método da API
        ResponseEntity<Map<String, Object>> result = calculatorController.sum(new BigDecimal("5"), new BigDecimal("3"), response);

        // Validações adicionais para garantir que o corpo da resposta está correto
        assertNotNull(result.getBody(), "O corpo da resposta não deveria ser nulo.");
        assertTrue(result.getBody().containsKey("result"), "O corpo da resposta deve conter a chave 'result'.");
        assertNotNull(result.getBody().get("result"), "O valor de 'result' não pode ser nulo.");
        assertEquals("8", result.getBody().get("result").toString(), "O resultado da soma deve ser 8.");

        // Verificação do cabeçalho
        verify(response).setHeader(eq("X-Request-ID"), anyString());
    }

    @Test
    void testDivisionByZero() {
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Simular erro de divisão por zero
        when(calculatorService.divide(any(), eq(BigDecimal.ZERO)))
                .thenThrow(new ArithmeticException("Divisão por zero não permitida."));

        ResponseEntity<Map<String, Object>> result = calculatorController.division(new BigDecimal("10"), BigDecimal.ZERO, response);

        // Verificação de erro na resposta
        assertNotNull(result.getBody(), "O corpo da resposta não deveria ser nulo.");
        assertTrue(result.getBody().containsKey("error"), "A resposta deve conter a chave 'error'.");
        assertEquals("Erro na operação: Divisão por zero não permitida.", result.getBody().get("error").toString(), "Mensagem de erro incorreta.");
    }

}
