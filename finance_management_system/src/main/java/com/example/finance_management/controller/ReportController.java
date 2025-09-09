package com.example.finance_management.controller;

import com.example.finance_management.FinanceService;
import com.example.finance_management.User;
import com.example.finance_management.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private FinanceService financeService;

    @GetMapping("/income")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTotalIncome(
            @RequestParam(required = false) Long userId) {
        try {
            Map<String, Object> result = new HashMap<>();

            if (userId != null) {
           
                Optional<User> userOpt = financeService.getUserById(userId);
                if (!userOpt.isPresent()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("User with ID " + userId + " not found"));
                }

                Double totalIncome = financeService.getTotalIncome(userId);
                result.put("userId", userId);
                result.put("userName", userOpt.get().getName());
                result.put("totalIncome", totalIncome);
            } else {
                Double totalIncome = financeService.getTotalIncome(null);
                result.put("totalIncome", totalIncome);

               
                List<User> users = financeService.getAllUsers();
                Map<String, Double> userBreakdown = new HashMap<>();
                for (User user : users) {
                    Double userIncome = financeService.getTotalIncome(user.getId());
                    userBreakdown.put(user.getName(), userIncome);
                }
                result.put("userBreakdown", userBreakdown);
            }

            return ResponseEntity.ok(ApiResponse.success("Income report generated successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error generating income report: " + e.getMessage()));
        }
    }

    @GetMapping("/expenses")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTotalExpenses(
            @RequestParam(required = false) Long userId) {
        try {
            Map<String, Object> result = new HashMap<>();

            if (userId != null) {
      
                Optional<User> userOpt = financeService.getUserById(userId);
                if (!userOpt.isPresent()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("User with ID " + userId + " not found"));
                }

                Double totalExpenses = financeService.getTotalExpenses(userId);
                result.put("userId", userId);
                result.put("userName", userOpt.get().getName());
                result.put("totalExpenses", totalExpenses);
            } else {
                Double totalExpenses = financeService.getTotalExpenses(null);
                result.put("totalExpenses", totalExpenses);

              
                List<User> users = financeService.getAllUsers();
                Map<String, Double> userBreakdown = new HashMap<>();
                for (User user : users) {
                    Double userExpenses = financeService.getTotalExpenses(user.getId());
                    userBreakdown.put(user.getName(), userExpenses);
                }
                result.put("userBreakdown", userBreakdown);
            }

            return ResponseEntity.ok(ApiResponse.success("Expenses report generated successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error generating expenses report: " + e.getMessage()));
        }
    }

    @GetMapping("/balance")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getBalance(@RequestParam(required = false) Long userId) {
        try {
            Map<String, Object> result = new HashMap<>();

            if (userId != null) {
           
                Optional<User> userOpt = financeService.getUserById(userId);
                if (!userOpt.isPresent()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("User with ID " + userId + " not found"));
                }

                Double income = financeService.getTotalIncome(userId);
                Double expenses = financeService.getTotalExpenses(userId);
                Double balance = financeService.getBalance(userId);

                result.put("userId", userId);
                result.put("userName", userOpt.get().getName());
                result.put("income", income);
                result.put("expenses", expenses);
                result.put("balance", balance);
            } else {
                Double totalBalance = financeService.getBalance(null);
                result.put("totalBalance", totalBalance);

            
                List<User> users = financeService.getAllUsers();
                Map<String, Map<String, Double>> userBreakdown = new HashMap<>();
                for (User user : users) {
                    Double userIncome = financeService.getTotalIncome(user.getId());
                    Double userExpenses = financeService.getTotalExpenses(user.getId());
                    Double userBalance = userIncome - userExpenses;

                    Map<String, Double> userData = new HashMap<>();
                    userData.put("income", userIncome);
                    userData.put("expenses", userExpenses);
                    userData.put("balance", userBalance);
                    userBreakdown.put(user.getName(), userData);
                }
                result.put("userBreakdown", userBreakdown);
            }

            return ResponseEntity.ok(ApiResponse.success("Balance report generated successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error generating balance report: " + e.getMessage()));
        }
    }

    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMonthlySummary(
            @RequestParam Long userId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        try {
           
            Optional<User> userOpt = financeService.getUserById(userId);
            if (!userOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User with ID " + userId + " not found"));
            }

            List<com.example.finance_management.Transaction> transactions;
            String periodDescription;

            if (year != null && month != null) {
                transactions = financeService.getMonthlySummary(userId, year, month);
                periodDescription = year + "-" + String.format("%02d", month);
            } else {
                transactions = financeService.getCurrentMonthSummary(userId);
                periodDescription = "Current Month";
            }

            Map<String, Object> result = new HashMap<>();
            result.put("userId", userId);
            result.put("userName", userOpt.get().getName());
            result.put("period", periodDescription);
            result.put("transactions", transactions);

            
            Double totalIncome = 0.0;
            Double totalExpenses = 0.0;
            for (com.example.finance_management.Transaction transaction : transactions) {
                if (transaction.getType() == com.example.finance_management.TransactionType.INCOME) {
                    totalIncome += transaction.getAmount();
                } else {
                    totalExpenses += transaction.getAmount();
                }
            }

            Map<String, Object> summary = new HashMap<>();
            summary.put("totalIncome", totalIncome);
            summary.put("totalExpenses", totalExpenses);
            summary.put("netBalance", totalIncome - totalExpenses);
            summary.put("transactionCount", transactions.size());

            result.put("summary", summary);

            return ResponseEntity.ok(ApiResponse.success("Monthly summary generated successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error generating monthly summary: " + e.getMessage()));
        }
    }
}
