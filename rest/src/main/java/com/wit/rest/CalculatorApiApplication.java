package com.wit.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.wit.rest", "com.wit.calculator"})
public class CalculatorApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CalculatorApiApplication.class, args);
    }
}
