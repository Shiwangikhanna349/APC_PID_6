package com.example.transactionservice.controller;

import com.example.transactionservice.entity.Transaction;
import com.example.transactionservice.service.TransactionService;
import com.example.transactionservice.service.UserServiceClient;
import com.example.transactionservice.dto.ApiResponse;
import com.example.transactionservice.dto.TransactionRequest;
import com.example.transactionservice.dto.TransactionUpdateRequest;
import com.example.transactionservice.dto.TransactionResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserServiceClient userServiceClient;

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionResponse>> addTransaction(
            @Valid @RequestBody TransactionRequest transactionRequest) {
        try {
            // Validate user exists
            Boolean userExists = userServiceClient.userExists(transactionRequest.getUserId()).block();
            if (userExists == null || !userExists) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User with ID " + transactionRequest.getUserId() + " not found"));
            }

            Transaction transaction = transactionService.addTransaction(
                    transactionRequest.getAmount(),
                    transactionRequest.getType(),
                    transactionRequest.getDescription().trim(),
                    transactionRequest.getUserId());

            if (transaction != null) {
                String userName = userServiceClient.getUserName(transaction.getUserId()).block();
                TransactionResponse transactionResponse = new TransactionResponse(
                        transaction.getId(),
                        transaction.getAmount(),
                        transaction.getType(),
                        transaction.getDescription(),
                        transaction.getTransactionDate(),
                        transaction.getUserId(),
                        userName);
                return ResponseEntity.ok(ApiResponse.success("Transaction added successfully", transactionResponse));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error("Failed to add transaction"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error adding transaction: " + e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactionsByUser(@PathVariable Long userId) {
        try {
            // Validate user exists
            Boolean userExists = userServiceClient.userExists(userId).block();
            if (userExists == null || !userExists) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User with ID " + userId + " not found"));
            }

            List<Transaction> transactions = transactionService.getTransactionsByUser(userId);
            List<TransactionResponse> transactionResponses = transactions.stream()
                    .map(transaction -> new TransactionResponse(
                            transaction.getId(),
                            transaction.getAmount(),
                            transaction.getType(),
                            transaction.getDescription(),
                            transaction.getTransactionDate(),
                            transaction.getUserId(),
                            transaction.getUserName()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("Transactions retrieved successfully", transactionResponses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving transactions: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getAllTransactions() {
        try {
            List<Transaction> transactions = transactionService.getAllTransactions();
            List<TransactionResponse> transactionResponses = transactions.stream()
                    .map(transaction -> new TransactionResponse(
                            transaction.getId(),
                            transaction.getAmount(),
                            transaction.getType(),
                            transaction.getDescription(),
                            transaction.getTransactionDate(),
                            transaction.getUserId(),
                            transaction.getUserName()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("All transactions retrieved successfully", transactionResponses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving all transactions: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TransactionResponse>> updateTransaction(@PathVariable Long id,
            @Valid @RequestBody TransactionUpdateRequest transactionUpdateRequest) {
        try {
            Transaction updatedTransaction = transactionService.updateTransaction(
                    id,
                    transactionUpdateRequest.getAmount(),
                    transactionUpdateRequest.getType(),
                    transactionUpdateRequest.getDescription().trim());

            if (updatedTransaction != null) {
                String userName = userServiceClient.getUserName(updatedTransaction.getUserId()).block();
                TransactionResponse transactionResponse = new TransactionResponse(
                        updatedTransaction.getId(),
                        updatedTransaction.getAmount(),
                        updatedTransaction.getType(),
                        updatedTransaction.getDescription(),
                        updatedTransaction.getTransactionDate(),
                        updatedTransaction.getUserId(),
                        userName);
                return ResponseEntity.ok(ApiResponse.success("Transaction updated successfully", transactionResponse));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Transaction with ID " + id + " not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error updating transaction: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTransaction(@PathVariable Long id) {
        try {
            boolean deleted = transactionService.deleteTransaction(id);
            if (deleted) {
                return ResponseEntity.ok(ApiResponse.success("Transaction deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Transaction with ID " + id + " not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error deleting transaction: " + e.getMessage()));
        }
    }
}
