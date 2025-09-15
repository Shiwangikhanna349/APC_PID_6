package com.example.finance_management.controller;

import com.example.finance_management.FinanceService;
import com.example.finance_management.User;
import com.example.finance_management.dto.ApiResponse;
import com.example.finance_management.dto.UserRequest;
import com.example.finance_management.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private FinanceService financeService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<UserResponse>> addUser(@Valid @RequestBody UserRequest userRequest) {
        try {
            User user;
            if (userRequest.getEmail() != null && userRequest.getPhoneNumber() != null) {
                user = financeService.addUser(
                    userRequest.getName().trim(),
                    userRequest.getEmail().trim(),
                    userRequest.getPhoneNumber().trim(),
                    userRequest.getAddress() != null ? userRequest.getAddress().trim() : null,
                    userRequest.getBankAccountNumber() != null ? userRequest.getBankAccountNumber().trim() : null
                );
            } else {
                user = financeService.addUser(userRequest.getName().trim());
            }

            if (user != null) {
                UserResponse userResponse = new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getAddress(),
                    user.getBankAccountNumber()
                );
                return ResponseEntity.ok(ApiResponse.success("User added successfully", userResponse));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(ApiResponse.error("Failed to add user"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error adding user: " + e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        try {
            List<User> users = financeService.getAllUsers();
            List<UserResponse> userResponses = users.stream()
                    .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getAddress(),
                        user.getBankAccountNumber()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", userResponses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving users: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        try {
            Optional<User> userOpt = financeService.getUserById(id);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                UserResponse userResponse = new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    user.getAddress(),
                    user.getBankAccountNumber()
                );
                return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", userResponse));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User with ID " + id + " not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error retrieving user: " + e.getMessage()));
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable Long id,
            @Valid @RequestBody UserRequest userRequest) {
        try {
            User updatedUser;
            if (userRequest.getEmail() != null && userRequest.getPhoneNumber() != null) {
                updatedUser = financeService.updateUser(
                    id,
                    userRequest.getName().trim(),
                    userRequest.getEmail().trim(),
                    userRequest.getPhoneNumber().trim(),
                    userRequest.getAddress() != null ? userRequest.getAddress().trim() : null,
                    userRequest.getBankAccountNumber() != null ? userRequest.getBankAccountNumber().trim() : null
                );
            } else {
                updatedUser = financeService.updateUser(id, userRequest.getName().trim());
            }

            if (updatedUser != null) {
                UserResponse userResponse = new UserResponse(
                    updatedUser.getId(),
                    updatedUser.getName(),
                    updatedUser.getEmail(),
                    updatedUser.getPhoneNumber(),
                    updatedUser.getAddress(),
                    updatedUser.getBankAccountNumber()
                );
                return ResponseEntity.ok(ApiResponse.success("User updated successfully", userResponse));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User with ID " + id + " not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error updating user: " + e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        try {
            boolean deleted = financeService.deleteUser(id);
            if (deleted) {
                return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("User with ID " + id + " not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error deleting user: " + e.getMessage()));
        }
    }
}
