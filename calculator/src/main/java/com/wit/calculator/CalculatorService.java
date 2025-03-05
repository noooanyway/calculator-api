package com.wit.calculator;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Service // Esta anotação informa ao Spring para gerenciar essa classe como um bean
public class CalculatorService {
    private static final Logger logger = LoggerFactory.getLogger(CalculatorService.class);

    private void validateInputs(BigDecimal a, BigDecimal b) {
        if (Objects.isNull(a) || Objects.isNull(b)) {
            logger.error("Valores nulos recebidos: a={}, b={}", a, b);
            throw new IllegalArgumentException("Os valores não podem ser nulos.");
        }
    }

    public BigDecimal add(BigDecimal a, BigDecimal b) {
        validateInputs(a, b);
        logger.info("Calculando soma: {} + {}", a, b);
        return a.add(b);
    }

    public BigDecimal subtract(BigDecimal a, BigDecimal b) {
        validateInputs(a, b);
        logger.info("Calculando subtração: {} - {}", a, b);
        return a.subtract(b);
    }

    public BigDecimal multiply(BigDecimal a, BigDecimal b) {
        validateInputs(a, b);
        logger.info("Calculando multiplicação: {} * {}", a, b);
        return a.multiply(b);
    }

    public BigDecimal divide(BigDecimal a, BigDecimal b) {
        validateInputs(a, b);
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            logger.error("Tentativa de divisão por zero: {} / {}", a, b);
            throw new ArithmeticException("Divisão por zero não permitida.");
        }
        logger.info("Calculando divisão: {} / {}", a, b);
        return a.divide(b, 10, RoundingMode.HALF_UP);
    }
}
