package com.example.reportservice.controller;

import com.example.reportservice.dto.ApiResponse;
import com.example.reportservice.dto.MonthlyReportResponse;
import com.example.reportservice.dto.UserReportResponse;
import com.example.reportservice.service.UserServiceClient;
import com.example.reportservice.service.TransactionServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private TransactionServiceClient transactionServiceClient;

    @GetMapping("/income")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTotalIncome(
            @RequestParam(required = false) Long userId) {
        try {
            Map<String, Object> result = new HashMap<>();

            if (userId != null) {
                Boolean userExists = userServiceClient.userExists(userId);
                if (userExists == null || !userExists) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("User with ID " + userId + " not found"));
                }

                Double totalIncome = transactionServiceClient.getTotalIncome(userId);
                String userName = userServiceClient.getUserName(userId);
                
                result.put("userId", userId);
                result.put("userName", userName);
                result.put("totalIncome", totalIncome);
            } else {
                // For all users, we would need to get all users first and then calculate
                // This is a simplified version
                result.put("totalIncome", 0.0);
                result.put("userBreakdown", new HashMap<>());
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
                Boolean userExists = userServiceClient.userExists(userId);
                if (userExists == null || !userExists) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("User with ID " + userId + " not found"));
                }

                Double totalExpenses = transactionServiceClient.getTotalExpenses(userId);
                String userName = userServiceClient.getUserName(userId);
                
                result.put("userId", userId);
                result.put("userName", userName);
                result.put("totalExpenses", totalExpenses);
            } else {
                result.put("totalExpenses", 0.0);
                result.put("userBreakdown", new HashMap<>());
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
                Boolean userExists = userServiceClient.userExists(userId);
                if (userExists == null || !userExists) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("User with ID " + userId + " not found"));
                }

                Double income = transactionServiceClient.getTotalIncome(userId);
                Double expenses = transactionServiceClient.getTotalExpenses(userId);
                Double balance = income - expenses;
                String userName = userServiceClient.getUserName(userId);

                result.put("userId", userId);
                result.put("userName", userName);
                result.put("income", income);
                result.put("expenses", expenses);
                result.put("balance", balance);
            } else {
                result.put("totalBalance", 0.0);
                result.put("userBreakdown", new HashMap<>());
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
            Boolean userExists = userServiceClient.userExists(userId);
            if (userExists == null || !userExists) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User with ID " + userId + " not found"));
            }

            UserReportResponse userDTO = userServiceClient.getUser(userId);
            List<MonthlyReportResponse> transactionDTOs = transactionServiceClient.getTransactionsByUser(userId);
            
            Double totalIncome = transactionServiceClient.getTotalIncome(userId);
            Double totalExpenses = transactionServiceClient.getTotalExpenses(userId);
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
            Boolean userExists = userServiceClient.userExists(userId);
            if (userExists == null || !userExists) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User with ID " + userId + " not found"));
            }

            String userName = userServiceClient.getUserName(userId);
            List<MonthlyReportResponse> allTransactions = transactionServiceClient.getTransactionsByUser(userId);
            
            // Filter transactions for the specific month and year
            List<MonthlyReportResponse> monthlyTransactions = new ArrayList<>();
            double monthlyIncome = 0.0;
            double monthlyExpenses = 0.0;
            
            for (MonthlyReportResponse transaction : allTransactions) {
                if (transaction.getTransactionDate().getYear() == year && 
                    transaction.getTransactionDate().getMonthValue() == month) {
                    monthlyTransactions.add(transaction);
                    
                    if ("INCOME".equals(transaction.getType())) {
                        monthlyIncome += transaction.getAmount();
                    } else {
                        monthlyExpenses += transaction.getAmount();
                    }
                }
            }
            
            double monthlyBalance = monthlyIncome - monthlyExpenses;

            Map<String, Object> result = new HashMap<>();
            result.put("userId", userId);
            result.put("userName", userName);
            result.put("year", year);
            result.put("month", month);
            result.put("monthlyIncome", monthlyIncome);
            result.put("monthlyExpenses", monthlyExpenses);
            result.put("monthlyBalance", monthlyBalance);
            result.put("transactionCount", monthlyTransactions.size());
            result.put("transactions", monthlyTransactions);

            return ResponseEntity.ok(ApiResponse.success("Monthly summary generated successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error generating monthly summary: " + e.getMessage()));
        }
    }

    @GetMapping("/weekly")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getWeeklySummary(
            @RequestParam Long userId,
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam Integer week) {
        try {
            Boolean userExists = userServiceClient.userExists(userId);
            if (userExists == null || !userExists) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User with ID " + userId + " not found"));
            }

            String userName = userServiceClient.getUserName(userId);
            List<MonthlyReportResponse> allTransactions = transactionServiceClient.getTransactionsByUser(userId);
            
            // Calculate the date range for the specified week
            java.time.LocalDate firstDayOfMonth = java.time.LocalDate.of(year, month, 1);
            java.time.LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());
            
            // Calculate week start and end dates
            int startDay = (week - 1) * 7 + 1;
            int endDay = Math.min(startDay + 6, lastDayOfMonth.getDayOfMonth());
            
            java.time.LocalDate weekStart = java.time.LocalDate.of(year, month, startDay);
            java.time.LocalDate weekEnd = java.time.LocalDate.of(year, month, endDay);
            
            // Filter transactions for the specific week
            List<MonthlyReportResponse> weeklyTransactions = new ArrayList<>();
            double weeklyIncome = 0.0;
            double weeklyExpenses = 0.0;
            
            for (MonthlyReportResponse transaction : allTransactions) {
                java.time.LocalDate transactionDate = transaction.getTransactionDate().toLocalDate();
                if (!transactionDate.isBefore(weekStart) && !transactionDate.isAfter(weekEnd)) {
                    weeklyTransactions.add(transaction);
                    
                    if ("INCOME".equals(transaction.getType())) {
                        weeklyIncome += transaction.getAmount();
                    } else {
                        weeklyExpenses += transaction.getAmount();
                    }
                }
            }
            
            double weeklyBalance = weeklyIncome - weeklyExpenses;

            Map<String, Object> result = new HashMap<>();
            result.put("userId", userId);
            result.put("userName", userName);
            result.put("year", year);
            result.put("month", month);
            result.put("week", week);
            result.put("weekStart", weekStart.toString());
            result.put("weekEnd", weekEnd.toString());
            result.put("weeklyIncome", weeklyIncome);
            result.put("weeklyExpenses", weeklyExpenses);
            result.put("weeklyBalance", weeklyBalance);
            result.put("transactionCount", weeklyTransactions.size());
            result.put("transactions", weeklyTransactions);

            return ResponseEntity.ok(ApiResponse.success("Weekly summary generated successfully", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error generating weekly summary: " + e.getMessage()));
        }
    }
}
