package com.example.apigateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiGatewayController {

    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${user.service.url}")
    private String userServiceUrl;
    
    @Value("${transaction.service.url}")
    private String transactionServiceUrl;
    
    @Value("${report.service.url}")
    private String reportServiceUrl;

    // User Service Routes
    @PostMapping("/user/add")
    public ResponseEntity<String> addUser(@RequestBody String requestBody) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.postForEntity(userServiceUrl, entity, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error calling user service: " + e.getMessage());
        }
    }

    @GetMapping("/user/all")
    public ResponseEntity<String> getAllUsers() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(userServiceUrl, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error calling user service: " + e.getMessage());
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<String> getUserById(@PathVariable Long id) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(userServiceUrl + "/" + id, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error calling user service: " + e.getMessage());
        }
    }

    @PutMapping("/user/edit/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody String requestBody) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                userServiceUrl + "/" + id, 
                HttpMethod.PUT, 
                entity, 
                String.class
            );
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error calling user service: " + e.getMessage());
        }
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                userServiceUrl + "/" + id, 
                HttpMethod.DELETE, 
                null, 
                String.class
            );
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error calling user service: " + e.getMessage());
        }
    }

    // Transaction Service Routes
    @PostMapping("/transaction/add")
    public ResponseEntity<String> addTransaction(@RequestBody String requestBody) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.postForEntity(transactionServiceUrl, entity, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error calling transaction service: " + e.getMessage());
        }
    }

    @GetMapping("/transaction/user/{userId}")
    public ResponseEntity<String> getTransactionsByUser(@PathVariable Long userId) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                transactionServiceUrl + "/user/" + userId, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error calling transaction service: " + e.getMessage());
        }
    }

    @GetMapping("/transaction/all")
    public ResponseEntity<String> getAllTransactions() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(transactionServiceUrl, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error calling transaction service: " + e.getMessage());
        }
    }

    @PutMapping("/transaction/edit/{id}")
    public ResponseEntity<String> updateTransaction(@PathVariable Long id, @RequestBody String requestBody) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                transactionServiceUrl + "/" + id, 
                HttpMethod.PUT, 
                entity, 
                String.class
            );
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error calling transaction service: " + e.getMessage());
        }
    }

    @DeleteMapping("/transaction/delete/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                transactionServiceUrl + "/" + id, 
                HttpMethod.DELETE, 
                null, 
                String.class
            );
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error calling transaction service: " + e.getMessage());
        }
    }

    // Report Service Routes
    @GetMapping("/reports/income")
    public ResponseEntity<String> getTotalIncome(@RequestParam(required = false) Long userId) {
        try {
            String uri = userId != null ? reportServiceUrl + "/income?userId=" + userId : reportServiceUrl + "/income";
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error calling report service: " + e.getMessage());
        }
    }

    @GetMapping("/reports/expenses")
    public ResponseEntity<String> getTotalExpenses(@RequestParam(required = false) Long userId) {
        try {
            String uri = userId != null ? reportServiceUrl + "/expenses?userId=" + userId : reportServiceUrl + "/expenses";
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error calling report service: " + e.getMessage());
        }
    }

    @GetMapping("/reports/balance")
    public ResponseEntity<String> getBalance(@RequestParam(required = false) Long userId) {
        try {
            String uri = userId != null ? reportServiceUrl + "/balance?userId=" + userId : reportServiceUrl + "/balance";
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error calling report service: " + e.getMessage());
        }
    }

    @GetMapping("/reports/user")
    public ResponseEntity<String> getUserReport(@RequestParam Long userId) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                reportServiceUrl + "/user?userId=" + userId, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error calling report service: " + e.getMessage());
        }
    }

    @GetMapping("/reports/monthly")
    public ResponseEntity<String> getMonthlySummary(
            @RequestParam Long userId,
            @RequestParam Integer year,
            @RequestParam Integer month) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                reportServiceUrl + "/monthly?userId=" + userId + "&year=" + year + "&month=" + month, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error calling report service: " + e.getMessage());
        }
    }

    @GetMapping("/reports/weekly")
    public ResponseEntity<String> getWeeklySummary(
            @RequestParam Long userId,
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam Integer week) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(
                reportServiceUrl + "/weekly?userId=" + userId + "&year=" + year + "&month=" + month + "&week=" + week, String.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error calling report service: " + e.getMessage());
        }
    }
    
    // Static file serving - removed Thymeleaf methods
    // The static files will be served automatically from src/main/resources/static/
}
