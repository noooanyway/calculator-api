package com.wit.rest;

import com.wit.calculator.CalculatorService;
import com.wit.rest.kafka.OperationProducer;
import com.wit.rest.model.OperationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);
    private static final String REQUEST_ID = "requestId";

    private final CalculatorService calculatorService;
    private final OperationProducer operationProducer;

    public CalculatorController(CalculatorService calculatorService, OperationProducer operationProducer) {
        this.calculatorService = calculatorService;
        this.operationProducer = operationProducer;
    }

    @GetMapping("/sum")
    public ResponseEntity<Map<String, Object>> sum(
            @RequestParam("a") BigDecimal a,
            @RequestParam("b") BigDecimal b,
            HttpServletResponse response) {
        return processOperation("sum", a, b, () -> calculatorService.add(a, b), response);
    }

    @GetMapping("/subtraction")
    public ResponseEntity<Map<String, Object>> subtraction(
            @RequestParam("a") BigDecimal a,
            @RequestParam("b") BigDecimal b,
            HttpServletResponse response) {
        return processOperation("subtraction", a, b, () -> calculatorService.subtract(a, b), response);
    }

    @GetMapping("/multiplication")
    public ResponseEntity<Map<String, Object>> multiplication(
            @RequestParam("a") BigDecimal a,
            @RequestParam("b") BigDecimal b,
            HttpServletResponse response) {
        return processOperation("multiplication", a, b, () -> calculatorService.multiply(a, b), response);
    }

    @GetMapping("/division")
    public ResponseEntity<Map<String, Object>> division(
            @RequestParam("a") BigDecimal a,
            @RequestParam("b") BigDecimal b,
            HttpServletResponse response) {
        return processOperation("division", a, b, () -> calculatorService.divide(a, b), response);
    }

    private ResponseEntity<Map<String, Object>> processOperation(
            String operation, BigDecimal a, BigDecimal b,
            OperationExecutor executor, HttpServletResponse response) {

        String requestId = UUID.randomUUID().toString();
        response.setHeader("X-Request-ID", requestId);
        MDC.put(REQUEST_ID, requestId);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("requestId", requestId);
        responseBody.put("operation", operation);
        responseBody.put("a", a);
        responseBody.put("b", b);

        try {
            BigDecimal result = executor.execute();
            if (result == null) {
                throw new IllegalStateException("Resultado inesperadamente nulo.");
            }

            logger.info("UUID={} | Operação={} | a={} | b={} | Resultado={}", requestId, operation, a, b, result);

            responseBody.put("result", result);

            OperationMessage message = new OperationMessage(operation, a, b, result);
            operationProducer.sendOperation(message, requestId);

            return ResponseEntity.ok(responseBody);

        } catch (ArithmeticException e) {
            String errorMessage = "Erro na operação: " + e.getMessage();
            logger.error("Erro UUID={} | Operação={} | a={} | b={} | Erro={}", requestId, operation, a, b, e.getMessage());
            responseBody.put("error", errorMessage);
            response.setStatus(400); // CLIENT_ERROR
            return ResponseEntity.badRequest().body(responseBody);

        } catch (Exception e) {
            String errorMessage = "Erro inesperado: " + e.getMessage();
            logger.error("Erro UUID={} | Operação={} | a={} | b={} | Erro inesperado={}", requestId, operation, a, b, e.getMessage(), e);
            responseBody.put("error", errorMessage);
            response.setStatus(500); // SERVER_ERROR
            return ResponseEntity.internalServerError().body(responseBody);
        } finally {
            MDC.clear();
        }
    }

    @FunctionalInterface
    private interface OperationExecutor {
        BigDecimal execute();
    }
}
