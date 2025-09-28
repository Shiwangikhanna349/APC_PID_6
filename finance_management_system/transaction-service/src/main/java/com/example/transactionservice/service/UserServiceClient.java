package com.example.transactionservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceClient {

    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${user.service.url}")
    private String userServiceUrl;

    public Boolean userExists(Long userId) {
        try {
            String response = restTemplate.getForObject(userServiceUrl + "/" + userId, String.class);
            return response != null && response.contains("\"success\":true");
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserName(Long userId) {
        try {
            String response = restTemplate.getForObject(userServiceUrl + "/" + userId, String.class);
            if (response != null && response.contains("\"success\":true")) {
                int nameStart = response.indexOf("\"name\":\"") + 8;
                int nameEnd = response.indexOf("\"", nameStart);
                if (nameStart > 7 && nameEnd > nameStart) {
                    return response.substring(nameStart, nameEnd);
                }
            }
            return "Unknown User";
        } catch (Exception e) {
            return "Unknown User";
        }
    }
}
