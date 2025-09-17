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

           
            User user1 = financeService.addUser(
                "John Doe",
                "john.doe@example.com",
                "9876543210",
                "123 Main Street, Mumbai, Maharashtra 400001",
                "1234567890123456"
            );
            
            User user2 = financeService.addUser(
                "Jane Smith",
                "jane.smith@example.com",
                "9876543211",
                "456 Park Avenue, Delhi, Delhi 110001",
                "2345678901234567"
            );
            
            User user3 = financeService.addUser(
                "Bob Johnson",
                "bob.johnson@example.com",
                "9876543212",
                "789 Business District, Bangalore, Karnataka 560001",
                "3456789012345678"
            );

           
            User user4 = financeService.addUser(
                "Alice Brown",
                "alice.brown@example.com",
                "9876543213"
            );
            
            User user5 = financeService.addUser("Mike Wilson");

            System.out.println("Created users: " + user1.getName() + " (" + user1.getEmail() + "), " + 
                             user2.getName() + " (" + user2.getEmail() + "), " + 
                             user3.getName() + " (" + user3.getEmail() + "), " +
                             user4.getName() + " (" + user4.getEmail() + "), " +
                             user5.getName());

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

            // Add transactions for new users
            financeService.addTransaction(4000.0, TransactionType.INCOME, "Monthly Salary", user4.getId());
            financeService.addTransaction(800.0, TransactionType.EXPENSE, "Rent Payment", user4.getId());
            financeService.addTransaction(200.0, TransactionType.EXPENSE, "Groceries", user4.getId());

            financeService.addTransaction(3500.0, TransactionType.INCOME, "Monthly Salary", user5.getId());
            financeService.addTransaction(600.0, TransactionType.EXPENSE, "Rent Payment", user5.getId());

            System.out.println("Sample data loaded successfully!");
            System.out.println("Total users: " + financeService.getAllUsers().size());
            System.out.println("User profiles created:");
            System.out.println("- " + user1.getName() + ": Complete profile with address and bank account");
            System.out.println("- " + user2.getName() + ": Complete profile with address and bank account");
            System.out.println("- " + user3.getName() + ": Complete profile with address and bank account");
            System.out.println("- " + user4.getName() + ": Partial profile with email and phone");
            System.out.println("- " + user5.getName() + ": Basic profile with name only");
            System.out.println("Total transactions: " + financeService.getTransactionsByUser(user1.getId()).size() +
                    " + " + financeService.getTransactionsByUser(user2.getId()).size() +
                    " + " + financeService.getTransactionsByUser(user3.getId()).size() +
                    " + " + financeService.getTransactionsByUser(user4.getId()).size() +
                    " + " + financeService.getTransactionsByUser(user5.getId()).size());
        }
    }
}
