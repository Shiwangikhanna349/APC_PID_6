package com.example.finance_management.controller;

import com.example.finance_management.FinanceService;
import com.example.finance_management.Transaction;
import com.example.finance_management.User;
import com.example.finance_management.dto.TransactionRequest;
import com.example.finance_management.dto.UserRequest;
import com.example.finance_management.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class WebController {

    @Autowired
    private FinanceService financeService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // User Management Pages
    @GetMapping("/user/add-form")
    public String addUserForm() {
        return "user-add";
    }

    @GetMapping("/web/user/list")
    public String getAllUsers(Model model) {
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
        model.addAttribute("users", userResponses);
        return "user-list";
    }

    @GetMapping("/user/search-form")
    public String searchUserForm() {
        return "user-search";
    }

    @GetMapping("/user/edit-form")
    public String editUserForm() {
        return "user-edit-form";
    }

    @GetMapping("/web/user/view/{id}")
    public String getUserById(@PathVariable Long id, Model model) {
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
            model.addAttribute("user", userResponse);
            return "user-detail";
        }
        return "user-not-found";
    }

    @GetMapping("/web/user/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
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
            model.addAttribute("user", userResponse);
            return "user-edit";
        }
        return "user-not-found";
    }

    @GetMapping("/user/delete-form")
    public String deleteUserForm() {
        return "user-delete";
    }

    // Form handling methods
    @PostMapping("/user/add-web")
    public String addUser(@ModelAttribute UserRequest userRequest, RedirectAttributes redirectAttributes) {
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
                redirectAttributes.addFlashAttribute("success", "User added successfully!");
                return "redirect:/web/user/list";
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to add user");
                return "redirect:/user/add-form";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding user: " + e.getMessage());
            return "redirect:/user/add-form";
        }
    }

    @PostMapping("/user/edit-web")
    public String updateUser(@ModelAttribute UserRequest userRequest, RedirectAttributes redirectAttributes) {
        try {
            User updatedUser;
            if (userRequest.getEmail() != null && userRequest.getPhoneNumber() != null) {
                updatedUser = financeService.updateUser(
                    userRequest.getId(),
                    userRequest.getName().trim(),
                    userRequest.getEmail().trim(),
                    userRequest.getPhoneNumber().trim(),
                    userRequest.getAddress() != null ? userRequest.getAddress().trim() : null,
                    userRequest.getBankAccountNumber() != null ? userRequest.getBankAccountNumber().trim() : null
                );
            } else {
                updatedUser = financeService.updateUser(userRequest.getId(), userRequest.getName().trim());
            }

            if (updatedUser != null) {
                redirectAttributes.addFlashAttribute("success", "User updated successfully!");
                return "redirect:/web/user/list";
            } else {
                redirectAttributes.addFlashAttribute("error", "User not found");
                return "redirect:/web/user/list";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating user: " + e.getMessage());
            return "redirect:/web/user/list";
        }
    }

    @PostMapping("/user/delete-web")
    public String deleteUser(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean deleted = financeService.deleteUser(id);
            if (deleted) {
                redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "User not found");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting user: " + e.getMessage());
        }
        return "redirect:/web/user/list";
    }

    @GetMapping("/user/search-web")
    public String searchUser(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<User> userOpt = financeService.getUserById(id);
            if (userOpt.isPresent()) {
                return "redirect:/web/user/view/" + id;
            } else {
                redirectAttributes.addFlashAttribute("error", "User not found");
                return "redirect:/user/search-form";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error searching user: " + e.getMessage());
            return "redirect:/user/search-form";
        }
    }

    @GetMapping("/transaction/search-web")
    public String searchTransaction(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Transaction> transactionOpt = financeService.getTransactionById(id);
            if (transactionOpt.isPresent()) {
                return "redirect:/web/transaction/edit/" + id;
            } else {
                redirectAttributes.addFlashAttribute("error", "Transaction not found");
                return "redirect:/transaction/edit-form";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error searching transaction: " + e.getMessage());
            return "redirect:/transaction/edit-form";
        }
    }

    // Transaction Management Pages
    @GetMapping("/transaction/add-form")
    public String addTransactionForm() {
        return "transaction-add";
    }
    @PostMapping("/transaction/add-web")
    public String addTransaction(@ModelAttribute TransactionRequest transactionRequest, RedirectAttributes redirectAttributes) {
        try {
            Optional<User> userOpt = financeService.getUserById(transactionRequest.getUserId());
            if (!userOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "User not found");
                return "redirect:/transaction/add-form";
            }
    
            Transaction transaction = financeService.addTransaction(
                transactionRequest.getAmount(),
                transactionRequest.getType(),
                transactionRequest.getDescription().trim(),
                transactionRequest.getUserId()
            );
    
            if (transaction != null) {
                redirectAttributes.addFlashAttribute("success", "Transaction added successfully!");
                return "redirect:/web/transaction/list";
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to add transaction");
                return "redirect:/transaction/add-form";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding transaction: " + e.getMessage());
            return "redirect:/transaction/add-form";
        }
    }
    @GetMapping("/transaction/list")
    public String getAllTransactions(Model model) {
        List<Transaction> transactions = financeService.getAllTransactions();
        model.addAttribute("transactions", transactions);
        return "transaction-list";
    }

    @GetMapping("/transaction/user-form")
    public String getUserTransactionsForm() {
        return "transaction-user";
    }

    @GetMapping("/transaction/edit/{id}")
    public String editTransactionForm(@PathVariable Long id, Model model) {
        Optional<Transaction> transactionOpt = financeService.getTransactionById(id);
        if (transactionOpt.isPresent()) {
            model.addAttribute("transaction", transactionOpt.get());
            return "transaction-edit";
        }
        return "transaction-not-found";
    }

    @GetMapping("/transaction/delete-form")
    public String deleteTransactionForm() {
        return "transaction-delete";
    }

    @GetMapping("/transaction/edit-form")
    public String editTransactionForm() {
        return "transaction-edit-form";
    }

    @GetMapping("/web/transaction/list")
    public String getAllTransactionsWeb(Model model) {
        List<Transaction> transactions = financeService.getAllTransactions();
        model.addAttribute("transactions", transactions);
        return "transaction-list";
    }

    @GetMapping("/web/transaction/user")
    public String getUserTransactionsWeb(@RequestParam Long userId, Model model) {
        List<Transaction> transactions = financeService.getTransactionsByUser(userId);
        model.addAttribute("transactions", transactions);
        model.addAttribute("userId", userId);
        return "transaction-user-list";
    }

    @GetMapping("/web/transaction/edit/{id}")
    public String editTransactionWeb(@PathVariable Long id, Model model) {
        Optional<Transaction> transactionOpt = financeService.getTransactionById(id);
        if (transactionOpt.isPresent()) {
            model.addAttribute("transaction", transactionOpt.get());
            return "transaction-edit";
        }
        return "transaction-not-found";
    }

    @PostMapping("/transaction/delete-web")
    public String deleteTransactionWeb(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean deleted = financeService.deleteTransaction(id);
            if (deleted) {
                redirectAttributes.addFlashAttribute("success", "Transaction deleted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Transaction not found");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting transaction: " + e.getMessage());
        }
        return "redirect:/web/transaction/list";
    }

    @PostMapping("/transaction/edit-web")
    public String updateTransaction(@ModelAttribute TransactionRequest transactionRequest, RedirectAttributes redirectAttributes) {
        try {
            Optional<User> userOpt = financeService.getUserById(transactionRequest.getUserId());
            if (!userOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "User not found");
                return "redirect:/web/transaction/list";
            }

            Transaction updatedTransaction = financeService.updateTransaction(
                transactionRequest.getId(),
                transactionRequest.getAmount(),
                transactionRequest.getType(),
                transactionRequest.getDescription().trim()
            );

            if (updatedTransaction != null) {
                redirectAttributes.addFlashAttribute("success", "Transaction updated successfully!");
                return "redirect:/web/transaction/list";
            } else {
                redirectAttributes.addFlashAttribute("error", "Transaction not found");
                return "redirect:/web/transaction/list";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating transaction: " + e.getMessage());
            return "redirect:/web/transaction/list";
        }
    }

    // Report Pages
    @GetMapping("/report/user-form")
    public String userReportForm() {
        return "report-user";
    }

    @GetMapping("/report/monthly-form")
    public String monthlyReportForm(Model model) {
        return "report-monthly";
    }

    @GetMapping("/web/report/user")
    public String userReportWeb(@RequestParam Long userId, Model model) {
        try {
            // Get user details
            Optional<User> userOpt = financeService.getUserById(userId);
            if (!userOpt.isPresent()) {
                model.addAttribute("error", "User not found");
                return "user-not-found";
            }
            
            // Get user transactions
            List<Transaction> transactions = financeService.getTransactionsByUser(userId);
            model.addAttribute("user", userOpt.get());
            model.addAttribute("transactions", transactions);
            
            // Calculate totals
            double totalIncome = transactions.stream()
                .filter(t -> t.getType() == com.example.finance_management.TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
            double totalExpenses = transactions.stream()
                .filter(t -> t.getType() == com.example.finance_management.TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
            double balance = totalIncome - totalExpenses;
            
            model.addAttribute("totalIncome", totalIncome);
            model.addAttribute("totalExpenses", totalExpenses);
            model.addAttribute("balance", balance);
            
            return "report-user-detail";
        } catch (Exception e) {
            model.addAttribute("error", "Error generating report: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/web/report/monthly")
    public String monthlyReportWeb(@RequestParam Long userId, 
                                 @RequestParam Integer year, 
                                 @RequestParam Integer month, 
                                 Model model) {
        try {
            // Get user details
            Optional<User> userOpt = financeService.getUserById(userId);
            if (!userOpt.isPresent()) {
                model.addAttribute("error", "User not found");
                return "user-not-found";
            }
            
            // Get user transactions for the month
            List<Transaction> allTransactions = financeService.getTransactionsByUser(userId);
            List<Transaction> monthlyTransactions = new ArrayList<>();
            
            for (Transaction t : allTransactions) {
                try {
                    LocalDate transactionDate = t.getTransactionDate().toLocalDate();
                    if (transactionDate.getYear() == year && transactionDate.getMonthValue() == month) {
                        monthlyTransactions.add(t);
                    }
                } catch (Exception e) {
                    // Skip this transaction if there's an error
                    continue;
                }
            }
            
            model.addAttribute("user", userOpt.get());
            model.addAttribute("transactions", monthlyTransactions);
            model.addAttribute("year", year);
            model.addAttribute("month", month);
            
            // Calculate monthly totals
            double monthlyIncome = 0.0;
            double monthlyExpenses = 0.0;
            
            for (Transaction t : monthlyTransactions) {
                if (t.getType() == com.example.finance_management.TransactionType.INCOME) {
                    monthlyIncome += t.getAmount();
                } else if (t.getType() == com.example.finance_management.TransactionType.EXPENSE) {
                    monthlyExpenses += t.getAmount();
                }
            }
            
            double monthlyBalance = monthlyIncome - monthlyExpenses;
            
            model.addAttribute("monthlyIncome", monthlyIncome);
            model.addAttribute("monthlyExpenses", monthlyExpenses);
            model.addAttribute("monthlyBalance", monthlyBalance);
            
            return "report-monthly-detail";
        } catch (Exception e) {
            model.addAttribute("error", "Error generating monthly report: " + e.getMessage());
            return "error";
        }
    }

}
