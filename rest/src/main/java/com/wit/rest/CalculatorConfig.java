package com.wit.rest;

import com.wit.calculator.CalculatorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuração do Spring que cria um bean do CalculatorService.
 */
@Configuration
public class CalculatorConfig {

    /**
     * Define um bean para o serviço de cálculo.
     *
     * @return Instância de CalculatorService.
     */
    @Bean
    public CalculatorService calculatorService() {
        return new CalculatorService();
    }
}
