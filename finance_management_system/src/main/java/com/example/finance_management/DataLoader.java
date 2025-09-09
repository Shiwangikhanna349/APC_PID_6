package com.example.finance_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    @Autowired
    private FinanceService financeService;

    public void loadSampleData() {

        if (financeService.getAllUsers().isEmpty()) {
            System.out.println("Loading sample data...");

            User user1 = financeService.addUser("John Doe");
            User user2 = financeService.addUser("Jane Smith");
            User user3 = financeService.addUser("Bob Johnson");

            System.out.println("Created users: " + user1.getName() + ", " + user2.getName() + ", " + user3.getName());

            financeService.addTransaction(5000.0, TransactionType.INCOME, "Monthly Salary", user1.getId());
            financeService.addTransaction(1200.0, TransactionType.EXPENSE, "Rent Payment", user1.getId());
            financeService.addTransaction(300.0, TransactionType.EXPENSE, "Groceries", user1.getId());
            financeService.addTransaction(150.0, TransactionType.EXPENSE, "Utilities", user1.getId());
            financeService.addTransaction(200.0, TransactionType.INCOME, "Freelance Work", user1.getId());

            financeService.addTransaction(4500.0, TransactionType.INCOME, "Monthly Salary", user2.getId());
            financeService.addTransaction(1000.0, TransactionType.EXPENSE, "Rent Payment", user2.getId());
            financeService.addTransaction(250.0, TransactionType.EXPENSE, "Groceries", user2.getId());
            financeService.addTransaction(100.0, TransactionType.EXPENSE, "Utilities", user2.getId());
            financeService.addTransaction(500.0, TransactionType.INCOME, "Investment Returns", user2.getId());

            financeService.addTransaction(6000.0, TransactionType.INCOME, "Monthly Salary", user3.getId());
            financeService.addTransaction(1500.0, TransactionType.EXPENSE, "Rent Payment", user3.getId());
            financeService.addTransaction(400.0, TransactionType.EXPENSE, "Groceries", user3.getId());
            financeService.addTransaction(200.0, TransactionType.EXPENSE, "Utilities", user3.getId());
            financeService.addTransaction(1000.0, TransactionType.EXPENSE, "Car Payment", user3.getId());
            financeService.addTransaction(300.0, TransactionType.INCOME, "Side Business", user3.getId());

            System.out.println("Sample data loaded successfully!");
            System.out.println("Total users: " + financeService.getAllUsers().size());
            System.out.println("Total transactions: " + financeService.getTransactionsByUser(user1.getId()).size() +
                    " + " + financeService.getTransactionsByUser(user2.getId()).size() +
                    " + " + financeService.getTransactionsByUser(user3.getId()).size());
        }
    }
}
