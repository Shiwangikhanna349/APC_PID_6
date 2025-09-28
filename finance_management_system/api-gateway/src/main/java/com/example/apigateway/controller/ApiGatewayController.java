package com.example.apigateway.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiGatewayController {

    private final WebClient userServiceClient;
    private final WebClient transactionServiceClient;
    private final WebClient reportServiceClient;

    public ApiGatewayController(
            @Value("${user.service.url}") String userServiceUrl,
            @Value("${transaction.service.url}") String transactionServiceUrl,
            @Value("${report.service.url}") String reportServiceUrl) {
        
        this.userServiceClient = WebClient.builder()
                .baseUrl(userServiceUrl)
                .build();
        
        this.transactionServiceClient = WebClient.builder()
                .baseUrl(transactionServiceUrl)
                .build();
        
        this.reportServiceClient = WebClient.builder()
                .baseUrl(reportServiceUrl)
                .build();
    }

    // User Service Routes
    @PostMapping("/user/add")
    public Mono<ResponseEntity<String>> addUser(@RequestBody String requestBody) {
        return userServiceClient.post()
                .uri("")
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(500).body("Error calling user service"));
    }

    @GetMapping("/user/all")
    public Mono<ResponseEntity<String>> getAllUsers() {
        return userServiceClient.get()
                .uri("")
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(500).body("Error calling user service"));
    }

    @GetMapping("/user/{id}")
    public Mono<ResponseEntity<String>> getUserById(@PathVariable Long id) {
        return userServiceClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(500).body("Error calling user service"));
    }

    @PutMapping("/user/edit/{id}")
    public Mono<ResponseEntity<String>> updateUser(@PathVariable Long id, @RequestBody String requestBody) {
        return userServiceClient.put()
                .uri("/{id}", id)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(500).body("Error calling user service"));
    }

    @DeleteMapping("/user/delete/{id}")
    public Mono<ResponseEntity<String>> deleteUser(@PathVariable Long id) {
        return userServiceClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(500).body("Error calling user service"));
    }

    // Transaction Service Routes
    @PostMapping("/transaction/add")
    public Mono<ResponseEntity<String>> addTransaction(@RequestBody String requestBody) {
        return transactionServiceClient.post()
                .uri("")
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(500).body("Error calling transaction service"));
    }

    @GetMapping("/transaction/user/{userId}")
    public Mono<ResponseEntity<String>> getTransactionsByUser(@PathVariable Long userId) {
        return transactionServiceClient.get()
                .uri("/user/{userId}", userId)
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(500).body("Error calling transaction service"));
    }

    @GetMapping("/transaction/all")
    public Mono<ResponseEntity<String>> getAllTransactions() {
        return transactionServiceClient.get()
                .uri("")
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(500).body("Error calling transaction service"));
    }

    @PutMapping("/transaction/edit/{id}")
    public Mono<ResponseEntity<String>> updateTransaction(@PathVariable Long id, @RequestBody String requestBody) {
        return transactionServiceClient.put()
                .uri("/{id}", id)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(500).body("Error calling transaction service"));
    }

    @DeleteMapping("/transaction/delete/{id}")
    public Mono<ResponseEntity<String>> deleteTransaction(@PathVariable Long id) {
        return transactionServiceClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(500).body("Error calling transaction service"));
    }

    // Report Service Routes
    @GetMapping("/reports/income")
    public Mono<ResponseEntity<String>> getTotalIncome(@RequestParam(required = false) Long userId) {
        String uri = userId != null ? "/income?userId=" + userId : "/income";
        return reportServiceClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(500).body("Error calling report service"));
    }

    @GetMapping("/reports/expenses")
    public Mono<ResponseEntity<String>> getTotalExpenses(@RequestParam(required = false) Long userId) {
        String uri = userId != null ? "/expenses?userId=" + userId : "/expenses";
        return reportServiceClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(500).body("Error calling report service"));
    }

    @GetMapping("/reports/balance")
    public Mono<ResponseEntity<String>> getBalance(@RequestParam(required = false) Long userId) {
        String uri = userId != null ? "/balance?userId=" + userId : "/balance";
        return reportServiceClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(500).body("Error calling report service"));
    }

    @GetMapping("/reports/user")
    public Mono<ResponseEntity<String>> getUserReport(@RequestParam Long userId) {
        return reportServiceClient.get()
                .uri("/user?userId=" + userId)
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(500).body("Error calling report service"));
    }

    @GetMapping("/reports/monthly")
    public Mono<ResponseEntity<String>> getMonthlySummary(
            @RequestParam Long userId,
            @RequestParam Integer year,
            @RequestParam Integer month) {
        return reportServiceClient.get()
                .uri("/monthly?userId=" + userId + "&year=" + year + "&month=" + month)
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(500).body("Error calling report service"));
    }

    @GetMapping("/reports/weekly")
    public Mono<ResponseEntity<String>> getWeeklySummary(
            @RequestParam Long userId,
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam Integer week) {
        return reportServiceClient.get()
                .uri("/weekly?userId=" + userId + "&year=" + year + "&month=" + month + "&week=" + week)
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.status(500).body("Error calling report service"));
    }
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/index.html")
    public String indexHtml() {
        return "index";
    }
}
