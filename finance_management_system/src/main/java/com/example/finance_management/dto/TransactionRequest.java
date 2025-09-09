package com.example.finance_management.dto;

import com.example.finance_management.TransactionType;

public class TransactionRequest {
    private Double amount;
    private TransactionType type;
    private String description;
    private Long userId;

    public TransactionRequest() {
    }

    public TransactionRequest(Double amount, TransactionType type, String description, Long userId) {
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
