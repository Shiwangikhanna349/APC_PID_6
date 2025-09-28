package com.example.transactionservice.service;

import com.example.transactionservice.entity.Transaction;
import com.example.transactionservice.entity.TransactionType;
import com.example.transactionservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    public Transaction addTransaction(Double amount, TransactionType type, String description, Long userId) {
        Transaction transaction = new Transaction(amount, type, description, userId);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByUser(Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        // Populate userName for JSON response
        for (Transaction transaction : transactions) {
            String userName = userServiceClient.getUserName(userId).block();
            transaction.setUserName(userName);
        }
        return transactions;
    }

    public Transaction updateTransaction(Long transactionId, Double amount, TransactionType type, String description) {
        Optional<Transaction> transactionOpt = transactionRepository.findById(transactionId);
        if (transactionOpt.isPresent()) {
            Transaction transaction = transactionOpt.get();
            transaction.setAmount(amount);
            transaction.setType(type);
            transaction.setDescription(description);
            return transactionRepository.save(transaction);
        }
        return null;
    }

    public boolean deleteTransaction(Long transactionId) {
        if (transactionRepository.existsById(transactionId)) {
            transactionRepository.deleteById(transactionId);
            return true;
        }
        return false;
    }

    public Double getTotalIncome(Long userId) {
        if (userId != null) {
            return transactionRepository.getTotalIncomeByUser(userId);
        }
        return transactionRepository.getTotalIncome();
    }

    public Double getTotalExpenses(Long userId) {
        if (userId != null) {
            return transactionRepository.getTotalExpensesByUser(userId);
        }
        return transactionRepository.getTotalExpenses();
    }

    public Double getBalance(Long userId) {
        Double income = getTotalIncome(userId);
        Double expenses = getTotalExpenses(userId);
        return income - expenses;
    }

    public List<Transaction> getMonthlySummary(Long userId, int year, int month) {
        List<Transaction> transactions = transactionRepository.getMonthlyTransactions(userId, year, month);
        // Populate userName for JSON response
        for (Transaction transaction : transactions) {
            String userName = userServiceClient.getUserName(userId).block();
            transaction.setUserName(userName);
        }
        return transactions;
    }

    public List<Transaction> getCurrentMonthSummary(Long userId) {
        YearMonth currentMonth = YearMonth.now();
        return getMonthlySummary(userId, currentMonth.getYear(), currentMonth.getMonthValue());
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        // Populate userName for JSON response
        for (Transaction transaction : transactions) {
            String userName = userServiceClient.getUserName(transaction.getUserId()).block();
            transaction.setUserName(userName);
        }
        return transactions;
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }
}
