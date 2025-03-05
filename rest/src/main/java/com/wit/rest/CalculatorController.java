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

/**
 * REST Controller responsible for handling calculator operations.
 * Provides endpoints for addition, subtraction, multiplication, and division.
 *
 * Each operation logs the request details and sends an event to Kafka.
 */
@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);
    private static final String REQUEST_ID = "requestId";

    private final CalculatorService calculatorService;
    private final OperationProducer operationProducer;

    /**
     * Constructor to inject dependencies.
     *
     * @param calculatorService Service responsible for performing calculations.
     * @param operationProducer Kafka producer for sending operation messages.
     */
    public CalculatorController(CalculatorService calculatorService, OperationProducer operationProducer) {
        this.calculatorService = calculatorService;
        this.operationProducer = operationProducer;
    }

    /**
     * Endpoint for performing addition.
     *
     * @param a First operand.
     * @param b Second operand.
     * @param response HTTP response to set request ID header.
     * @return Response containing the operation details and result.
     */
    @GetMapping("/sum")
    public ResponseEntity<Map<String, Object>> sum(
            @RequestParam("a") BigDecimal a,
            @RequestParam("b") BigDecimal b,
            HttpServletResponse response) {
        return processOperation("sum", a, b, () -> calculatorService.add(a, b), response);
    }

    /**
     * Endpoint for performing subtraction.
     *
     * @param a First operand.
     * @param b Second operand.
     * @param response HTTP response to set request ID header.
     * @return Response containing the operation details and result.
     */
    @GetMapping("/subtraction")
    public ResponseEntity<Map<String, Object>> subtraction(
            @RequestParam("a") BigDecimal a,
            @RequestParam("b") BigDecimal b,
            HttpServletResponse response) {
        return processOperation("subtraction", a, b, () -> calculatorService.subtract(a, b), response);
    }

    /**
     * Endpoint for performing multiplication.
     *
     * @param a First operand.
     * @param b Second operand.
     * @param response HTTP response to set request ID header.
     * @return Response containing the operation details and result.
     */
    @GetMapping("/multiplication")
    public ResponseEntity<Map<String, Object>> multiplication(
            @RequestParam("a") BigDecimal a,
            @RequestParam("b") BigDecimal b,
            HttpServletResponse response) {
        return processOperation("multiplication", a, b, () -> calculatorService.multiply(a, b), response);
    }

    /**
     * Endpoint for performing division.
     *
     * @param a First operand.
     * @param b Second operand.
     * @param response HTTP response to set request ID header.
     * @return Response containing the operation details and result, or an error if division by zero occurs.
     */
    @GetMapping("/division")
    public ResponseEntity<Map<String, Object>> division(
            @RequestParam("a") BigDecimal a,
            @RequestParam("b") BigDecimal b,
            HttpServletResponse response) {
        return processOperation("division", a, b, () -> calculatorService.divide(a, b), response);
    }

    /**
     * Processes a mathematical operation, handling request ID, logging, and error management.
     *
     * @param operation The name of the operation (e.g., "sum", "division").
     * @param a First operand.
     * @param b Second operand.
     * @param executor Functional interface to execute the operation.
     * @param response HTTP response to set request ID header.
     * @return Response containing operation details and result, or an error message if an exception occurs.
     */
    private ResponseEntity<Map<String, Object>> processOperation(
            String operation, BigDecimal a, BigDecimal b,
            OperationExecutor executor, HttpServletResponse response) {

        // Generate a unique request ID for tracking
        String requestId = UUID.randomUUID().toString();
        response.setHeader("X-Request-ID", requestId);
        MDC.put(REQUEST_ID, requestId);

        // Create response body with operation details
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("requestId", requestId);
        responseBody.put("operation", operation);
        responseBody.put("a", a);
        responseBody.put("b", b);

        try {
            // Execute the operation
            BigDecimal result = executor.execute();
            if (result == null) {
                throw new IllegalStateException("Unexpected null result.");
            }

            // Log and store result
            logger.info("UUID={} | Operation={} | a={} | b={} | Result={}", requestId, operation, a, b, result);
            responseBody.put("result", result);

            // Send operation message to Kafka
            OperationMessage message = new OperationMessage(operation, a, b, result);
            operationProducer.sendOperation(message, requestId);

            return ResponseEntity.ok(responseBody);

        } catch (ArithmeticException e) {
            // Handle division by zero and other arithmetic errors
            String errorMessage = "Erro na operação: " + e.getMessage();
            logger.error("Error UUID={} | Operation={} | a={} | b={} | Error={}", requestId, operation, a, b, e.getMessage());
            responseBody.put("error", errorMessage);
            response.setStatus(400); // CLIENT_ERROR
            return ResponseEntity.badRequest().body(responseBody);

        } catch (Exception e) {
            // Handle unexpected errors
            String errorMessage = "Unexpected error: " + e.getMessage();
            logger.error("Error UUID={} | Operation={} | a={} | b={} | Unexpected error={}", requestId, operation, a, b, e.getMessage(), e);
            responseBody.put("error", errorMessage);
            response.setStatus(500); // SERVER_ERROR
            return ResponseEntity.internalServerError().body(responseBody);
        } finally {
            // Clear MDC (Mapped Diagnostic Context) after request is processed
            MDC.clear();
        }
    }

    /**
     * Functional interface for executing a mathematical operation.
     */
    @FunctionalInterface
    private interface OperationExecutor {
        BigDecimal execute();
    }
}
