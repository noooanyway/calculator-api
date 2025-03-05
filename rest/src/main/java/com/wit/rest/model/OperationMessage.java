package com.wit.rest.model;

import java.math.BigDecimal;

public class OperationMessage {

    private String operation;
    private BigDecimal a;
    private BigDecimal b;
    private BigDecimal result;

    public OperationMessage() {
    }

    public OperationMessage(String operation, BigDecimal a, BigDecimal b, BigDecimal result) {
        this.operation = operation;
        this.a = a;
        this.b = b;
        this.result = result;
    }

    // Getters e setters
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public BigDecimal getA() {
        return a;
    }

    public void setA(BigDecimal a) {
        this.a = a;
    }

    public BigDecimal getB() {
        return b;
    }

    public void setB(BigDecimal b) {
        this.b = b;
    }

    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }
}
