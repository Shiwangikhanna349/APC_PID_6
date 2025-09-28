package com.example.transactionservice.dto;

import com.example.transactionservice.entity.TransactionType;
import java.time.LocalDateTime;

public class TransactionResponse {

    private Long id;
    private Double amount;
    private TransactionType type;
    private String description;
    private LocalDateTime transactionDate;
    private Long userId;
    private String userName;

    public TransactionResponse() {
    }

    public TransactionResponse(Long id, Double amount, TransactionType type, String description, 
                             LocalDateTime transactionDate, Long userId, String userName) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.transactionDate = transactionDate;
        this.userId = userId;
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
