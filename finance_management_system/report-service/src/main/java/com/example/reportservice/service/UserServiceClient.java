package com.example.reportservice.service;

import com.example.reportservice.dto.UserReportResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserServiceClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public UserServiceClient(@Value("${user.service.url}") String userServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(userServiceUrl)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public Mono<Boolean> userExists(Long userId) {
        return webClient.get()
                .uri("/{id}", userId)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> response.contains("\"success\":true"))
                .onErrorReturn(false);
    }

    public Mono<UserReportResponse> getUser(Long userId) {
        return webClient.get()
                .uri("/{id}", userId)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(response);
                        if (jsonNode.get("success").asBoolean()) {
                            JsonNode data = jsonNode.get("data");
                            return new UserReportResponse(
                                    data.get("id").asLong(),
                                    data.get("name").asText(),
                                    data.get("email").asText(),
                                    data.get("phoneNumber").asText(),
                                    data.get("address").asText(),
                                    data.get("bankAccountNumber").asText()
                            );
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .onErrorReturn(null);
    }

    public Mono<String> getUserName(Long userId) {
        return webClient.get()
                .uri("/{id}", userId)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(response);
                        if (jsonNode.get("success").asBoolean()) {
                            JsonNode data = jsonNode.get("data");
                            return data.get("name").asText();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return "Unknown User";
                })
                .onErrorReturn("Unknown User");
    }
}
