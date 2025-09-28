package com.example.finance_management.dto;

import com.example.finance_management.TransactionType;
import jakarta.validation.constraints.*;

public class TransactionUpdateRequest {
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private Double amount;

    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    public TransactionUpdateRequest() {
    }

    public TransactionUpdateRequest(Double amount, TransactionType type, String description) {
        this.amount = amount;
        this.type = type;
        this.description = description;
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
