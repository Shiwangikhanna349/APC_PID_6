package com.example.finance_management.controller;

import com.example.finance_management.FinanceService;
import com.example.finance_management.Transaction;
import com.example.finance_management.User;
import com.example.finance_management.dto.ApiResponse;
import com.example.finance_management.dto.TransactionRequest;
import com.example.finance_management.dto.TransactionResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transaction")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private FinanceService financeService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<TransactionResponse>> addTransaction(
            @Valid @RequestBody TransactionRequest transactionRequest) {
        try {
            Optional<User> userOpt = financeService.getUserById(transactionRequest.getUserId());
            if (!userOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User with ID " + transactionRequest.getUserId() + " not found"));
            }

            Transaction transaction = financeService.addTransaction(
                    transactionRequest.getAmount(),
                    transactionRequest.getType(),
                    transactionRequest.getDescription().trim(),
                    transactionRequest.getUserId());

            if (transaction != null) {
                TransactionResponse transactionResponse = new TransactionResponse(
                        transaction.getId(),
                        transaction.getAmount(),
                        transaction.getType(),
                        transaction.getDescription(),
                        transaction.getTransactionDate(),
                        transaction.getUser().getId(),
                        transaction.getUser().getName());
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
 
            Optional<User> userOpt = financeService.getUserById(userId);
            if (!userOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User with ID " + userId + " not found"));
            }

            List<Transaction> transactions = financeService.getTransactionsByUser(userId);
            List<TransactionResponse> transactionResponses = transactions.stream()
                    .map(transaction -> new TransactionResponse(
                            transaction.getId(),
                            transaction.getAmount(),
                            transaction.getType(),
                            transaction.getDescription(),
                            transaction.getTransactionDate(),
                            transaction.getUser().getId(),
                            transaction.getUser().getName()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("Transactions retrieved successfully", transactionResponses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving transactions: " + e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getAllTransactions() {
        try {
            List<User> users = financeService.getAllUsers();
            List<TransactionResponse> allTransactions = users.stream()
                    .flatMap(user -> financeService.getTransactionsByUser(user.getId()).stream())
                    .map(transaction -> new TransactionResponse(
                            transaction.getId(),
                            transaction.getAmount(),
                            transaction.getType(),
                            transaction.getDescription(),
                            transaction.getTransactionDate(),
                            transaction.getUser().getId(),
                            transaction.getUser().getName()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("All transactions retrieved successfully", allTransactions));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving all transactions: " + e.getMessage()));
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ApiResponse<TransactionResponse>> updateTransaction(@PathVariable Long id,
            @Valid @RequestBody TransactionRequest transactionRequest) {
        try {
            Transaction updatedTransaction = financeService.updateTransaction(
                    id,
                    transactionRequest.getAmount(),
                    transactionRequest.getType(),
                    transactionRequest.getDescription().trim());

            if (updatedTransaction != null) {
                TransactionResponse transactionResponse = new TransactionResponse(
                        updatedTransaction.getId(),
                        updatedTransaction.getAmount(),
                        updatedTransaction.getType(),
                        updatedTransaction.getDescription(),
                        updatedTransaction.getTransactionDate(),
                        updatedTransaction.getUser().getId(),
                        updatedTransaction.getUser().getName());
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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTransaction(@PathVariable Long id) {
        try {
            boolean deleted = financeService.deleteTransaction(id);
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
