package com.example.reportservice.service;

import com.example.reportservice.dto.UserReportResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceClient {

    @Autowired
    private RestTemplate restTemplate;
    
    private final ObjectMapper objectMapper;
    
    @Value("${user.service.url}")
    private String userServiceUrl;

    public UserServiceClient() {
        this.objectMapper = new ObjectMapper();
    }

    public Boolean userExists(Long userId) {
        try {
            String response = restTemplate.getForObject(userServiceUrl + "/" + userId, String.class);
            return response != null && response.contains("\"success\":true");
        } catch (Exception e) {
            return false;
        }
    }

    public UserReportResponse getUser(Long userId) {
        try {
            String response = restTemplate.getForObject(userServiceUrl + "/" + userId, String.class);
            if (response != null) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUserName(Long userId) {
        try {
            String response = restTemplate.getForObject(userServiceUrl + "/" + userId, String.class);
            if (response != null) {
                JsonNode jsonNode = objectMapper.readTree(response);
                if (jsonNode.get("success").asBoolean()) {
                    JsonNode data = jsonNode.get("data");
                    return data.get("name").asText();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unknown User";
    }
}
