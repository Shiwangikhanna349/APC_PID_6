package com.example.finance_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class FinanceService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public User addUser(String name) {
        User user = new User(name);
        return userRepository.save(user);
    }

    public User addUser(String name, String email, String phoneNumber) {
        User user = new User(name, email, phoneNumber);
        return userRepository.save(user);
    }

    public User addUser(String name, String email, String phoneNumber, String address, String bankAccountNumber) {
        User user = new User(name, email, phoneNumber, address, bankAccountNumber);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, String name) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setName(name);
            return userRepository.save(user);
        }
        return null;
    }

    public User updateUser(Long id, String name, String email, String phoneNumber, String address, String bankAccountNumber) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setName(name);
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            user.setAddress(address);
            user.setBankAccountNumber(bankAccountNumber);
            return userRepository.save(user);
        }
        return null;
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Transaction addTransaction(Double amount, TransactionType type, String description, Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            Transaction transaction = new Transaction(amount, type, description, userOpt.get());
            return transactionRepository.save(transaction);
        }
        return null;
    }

    public List<Transaction> getTransactionsByUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return transactionRepository.findByUser(userOpt.get());
        }
        return List.of();
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
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                return transactionRepository.getTotalIncomeByUser(userOpt.get());
            }
            return 0.0;
        }
        return transactionRepository.getTotalIncome();
    }

    public Double getTotalExpenses(Long userId) {
        if (userId != null) {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                return transactionRepository.getTotalExpensesByUser(userOpt.get());
            }
            return 0.0;
        }
        return transactionRepository.getTotalExpenses();
    }

    public Double getBalance(Long userId) {
        Double income = getTotalIncome(userId);
        Double expenses = getTotalExpenses(userId);
        return income - expenses;
    }

    public List<Transaction> getMonthlySummary(Long userId, int year, int month) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return transactionRepository.getMonthlyTransactions(userOpt.get(), year, month);
        }
        return List.of();
    }

    public List<Transaction> getCurrentMonthSummary(Long userId) {
        YearMonth currentMonth = YearMonth.now();
        return getMonthlySummary(userId, currentMonth.getYear(), currentMonth.getMonthValue());
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }
}
