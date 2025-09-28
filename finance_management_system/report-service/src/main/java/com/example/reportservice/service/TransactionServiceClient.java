package com.example.reportservice.service;

import com.example.reportservice.dto.MonthlyReportResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceClient {

    @Autowired
    private RestTemplate restTemplate;
    
    private final ObjectMapper objectMapper;
    
    @Value("${transaction.service.url}")
    private String transactionServiceUrl;

    public TransactionServiceClient() {
        this.objectMapper = new ObjectMapper();
    }

    public List<MonthlyReportResponse> getTransactionsByUser(Long userId) {
        try {
            String response = restTemplate.getForObject(transactionServiceUrl + "/user/" + userId, String.class);
            if (response != null) {
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
                    return transactions;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public Double getTotalIncome(Long userId) {
        try {
            String response = restTemplate.getForObject(transactionServiceUrl + "/user/" + userId, String.class);
            if (response != null) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public Double getTotalExpenses(Long userId) {
        try {
            String response = restTemplate.getForObject(transactionServiceUrl + "/user/" + userId, String.class);
            if (response != null) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
