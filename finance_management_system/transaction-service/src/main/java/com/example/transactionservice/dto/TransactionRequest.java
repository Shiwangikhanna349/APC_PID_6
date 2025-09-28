package com.example.transactionservice.dto;

import com.example.transactionservice.entity.TransactionType;
import jakarta.validation.constraints.*;

public class TransactionRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private Double amount;

    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    public TransactionRequest() {
    }

    public TransactionRequest(Long userId, Double amount, TransactionType type, String description) {
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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
}
