package com.example.finance_management.controller;

import com.example.finance_management.FinanceService;
import com.example.finance_management.User;
import com.example.finance_management.dto.ApiResponse;
import com.example.finance_management.dto.MonthlyReportResponse;
import com.example.finance_management.dto.UserReportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserReport(@RequestParam Long userId) {
        try {
            Optional<User> userOpt = financeService.getUserById(userId);
            if (!userOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User with ID " + userId + " not found"));
            }

            User user = userOpt.get();
            List<com.example.finance_management.Transaction> transactions = financeService.getTransactionsByUser(userId);
            
         
            UserReportResponse userDTO = new UserReportResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getBankAccountNumber()
            );

            List<MonthlyReportResponse> transactionDTOs = new ArrayList<>();
            for (com.example.finance_management.Transaction transaction : transactions) {
                MonthlyReportResponse dto = new MonthlyReportResponse(
                    transaction.getId(),
                    transaction.getAmount(),
                    transaction.getType().toString(),
                    transaction.getDescription(),
                    transaction.getTransactionDate(),
                    transaction.getUserId(),
                    transaction.getUserName()
                );
                transactionDTOs.add(dto);
            }
            
            Double totalIncome = financeService.getTotalIncome(userId);
            Double totalExpenses = financeService.getTotalExpenses(userId);
            Double netBalance = totalIncome - totalExpenses;

            Map<String, Object> result = new HashMap<>();
            result.put("user", userDTO);
            result.put("totalIncome", totalIncome);
            result.put("totalExpenses", totalExpenses);
            result.put("netBalance", netBalance);
            result.put("transactions", transactionDTOs);

            return ResponseEntity.ok(ApiResponse.success("User report generated successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error generating user report: " + e.getMessage()));
        }
    }

    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMonthlySummary(
            @RequestParam Long userId,
            @RequestParam Integer year,
            @RequestParam Integer month) {
        try {
            Optional<User> userOpt = financeService.getUserById(userId);
            if (!userOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User with ID " + userId + " not found"));
            }

            User user = userOpt.get();
            List<com.example.finance_management.Transaction> allTransactions = financeService.getTransactionsByUser(userId);
            
            // Filter transactions for the specific month and year
            List<com.example.finance_management.Transaction> monthlyTransactions = new ArrayList<>();
            double monthlyIncome = 0.0;
            double monthlyExpenses = 0.0;
            
            for (com.example.finance_management.Transaction transaction : allTransactions) {
                if (transaction.getTransactionDate().getYear() == year && 
                    transaction.getTransactionDate().getMonthValue() == month) {
                    monthlyTransactions.add(transaction);
                    
                    if (transaction.getType().toString().equals("INCOME")) {
                        monthlyIncome += transaction.getAmount();
                    } else {
                        monthlyExpenses += transaction.getAmount();
                    }
                }
            }
            
            double monthlyBalance = monthlyIncome - monthlyExpenses;
            
            // Convert to DTOs to avoid circular reference
            List<MonthlyReportResponse> transactionDTOs = new ArrayList<>();
            for (com.example.finance_management.Transaction transaction : monthlyTransactions) {
                MonthlyReportResponse dto = new MonthlyReportResponse(
                    transaction.getId(),
                    transaction.getAmount(),
                    transaction.getType().toString(),
                    transaction.getDescription(),
                    transaction.getTransactionDate(),
                    transaction.getUserId(),
                    transaction.getUserName()
                );
                transactionDTOs.add(dto);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("userId", userId);
            result.put("userName", user.getName());
            result.put("year", year);
            result.put("month", month);
            result.put("monthlyIncome", monthlyIncome);
            result.put("monthlyExpenses", monthlyExpenses);
            result.put("monthlyBalance", monthlyBalance);
            result.put("transactionCount", monthlyTransactions.size());
            result.put("transactions", transactionDTOs);

            return ResponseEntity.ok(ApiResponse.success("Monthly summary generated successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error generating monthly summary: " + e.getMessage()));
        }
    }
}
