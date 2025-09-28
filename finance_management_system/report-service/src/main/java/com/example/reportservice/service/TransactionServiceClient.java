package com.example.reportservice.service;

import com.example.reportservice.dto.MonthlyReportResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public TransactionServiceClient(@Value("${transaction.service.url}") String transactionServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(transactionServiceUrl)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public Mono<List<MonthlyReportResponse>> getTransactionsByUser(Long userId) {
        return webClient.get()
                .uri("/user/{userId}", userId)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(response);
                        if (jsonNode.get("success").asBoolean()) {
                            JsonNode dataArray = jsonNode.get("data");
                            List<MonthlyReportResponse> transactions = new ArrayList<>();
                            
                            for (JsonNode transactionNode : dataArray) {
                                MonthlyReportResponse transaction = new MonthlyReportResponse(
                                        transactionNode.get("id").asLong(),
                                        transactionNode.get("amount").asDouble(),
                                        transactionNode.get("type").asText(),
                                        transactionNode.get("description").asText(),
                                        LocalDateTime.parse(transactionNode.get("transactionDate").asText()),
                                        transactionNode.get("userId").asLong(),
                                        transactionNode.get("userName").asText()
                                );
                                transactions.add(transaction);
                            }
                            return (List<MonthlyReportResponse>) transactions;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return (List<MonthlyReportResponse>) new ArrayList<MonthlyReportResponse>();
                })
                .onErrorReturn(new ArrayList<>());
    }

    public Mono<Double> getTotalIncome(Long userId) {
        return webClient.get()
                .uri("/user/{userId}", userId)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(response);
                        if (jsonNode.get("success").asBoolean()) {
                            JsonNode dataArray = jsonNode.get("data");
                            double totalIncome = 0.0;
                            
                            for (JsonNode transactionNode : dataArray) {
                                if ("INCOME".equals(transactionNode.get("type").asText())) {
                                    totalIncome += transactionNode.get("amount").asDouble();
                                }
                            }
                            return totalIncome;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return 0.0;
                })
                .onErrorReturn(0.0);
    }

    public Mono<Double> getTotalExpenses(Long userId) {
        return webClient.get()
                .uri("/user/{userId}", userId)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(response);
                        if (jsonNode.get("success").asBoolean()) {
                            JsonNode dataArray = jsonNode.get("data");
                            double totalExpenses = 0.0;
                            
                            for (JsonNode transactionNode : dataArray) {
                                if ("EXPENSE".equals(transactionNode.get("type").asText())) {
                                    totalExpenses += transactionNode.get("amount").asDouble();
                                }
                            }
                            return totalExpenses;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return 0.0;
                })
                .onErrorReturn(0.0);
    }
}
