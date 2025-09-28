package com.example.transactionservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserServiceClient {

    private final WebClient webClient;

    public UserServiceClient(@Value("${user.service.url}") String userServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(userServiceUrl)
                .build();
    }

    public Mono<Boolean> userExists(Long userId) {
        return webClient.get()
                .uri("/{id}", userId)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> response.contains("\"success\":true"))
                .onErrorReturn(false);
    }

    public Mono<String> getUserName(Long userId) {
        return webClient.get()
                .uri("/{id}", userId)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    // Simple JSON parsing to extract name
                    if (response.contains("\"success\":true")) {
                        int nameStart = response.indexOf("\"name\":\"") + 8;
                        int nameEnd = response.indexOf("\"", nameStart);
                        if (nameStart > 7 && nameEnd > nameStart) {
                            return response.substring(nameStart, nameEnd);
                        }
                    }
                    return "Unknown User";
                })
                .onErrorReturn("Unknown User");
    }
}
