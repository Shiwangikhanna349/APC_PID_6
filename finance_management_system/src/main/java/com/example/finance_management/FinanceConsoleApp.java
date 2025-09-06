package com.example.finance_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class FinanceConsoleApp implements CommandLineRunner {

    @Autowired
    private FinanceService financeService;

    @Autowired
    private DataLoader dataLoader;

    private Scanner scanner = new Scanner(System.in);

    @Override
    public void run(String... args) throws Exception {

        dataLoader.loadSampleData();

        Thread.sleep(1000);

        System.out.println("\n" + "=".repeat(50));
        System.out.println("=== Finance Management System ===");
        System.out.println("Welcome to the Finance Management Console!");
        System.out.println("=".repeat(50));

        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    manageUsers();
                    break;
                case 2:
                    manageTransactions();
                    break;
                case 3:
                    generateReports();
                    break;
                case 4:
                    System.out.println("Thank you for using Finance Management System. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("=== MAIN MENU ===");
        System.out.println("=".repeat(30));
        System.out.println("1. Manage Users");
        System.out.println("2. Manage Transactions");
        System.out.println("3. Reports");
        System.out.println("4. Exit");
        System.out.println("=".repeat(30));
    }

    private void manageUsers() {
        boolean backToMain = false;
        while (!backToMain) {
            System.out.println("\n" + "=".repeat(30));
            System.out.println("=== USER MANAGEMENT ===");
            System.out.println("=".repeat(30));
            System.out.println("1. Add User");
            System.out.println("2. View All Users");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
            System.out.println("5. Back to Main Menu");
            System.out.println("=".repeat(30));

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addUser();
                    break;
                case 2:
                    viewAllUsers();
                    break;
                case 3:
                    updateUser();
                    break;
                case 4:
                    deleteUser();
                    break;
                case 5:
                    backToMain = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void manageTransactions() {
        boolean backToMain = false;
        while (!backToMain) {
            System.out.println("\n" + "=".repeat(35));
            System.out.println("=== TRANSACTION MANAGEMENT ===");
            System.out.println("=".repeat(35));
            System.out.println("1. Add Transaction");
            System.out.println("2. View Transactions by User");
            System.out.println("3. Update Transaction");
            System.out.println("4. Delete Transaction");
            System.out.println("5. Back to Main Menu");
            System.out.println("=".repeat(35));

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addTransaction();
                    break;
                case 2:
                    viewTransactionsByUser();
                    break;
                case 3:
                    updateTransaction();
                    break;
                case 4:
                    deleteTransaction();
                    break;
                case 5:
                    backToMain = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void generateReports() {
        boolean backToMain = false;
        while (!backToMain) {
            System.out.println("\n" + "=".repeat(25));
            System.out.println("=== REPORTS ===");
            System.out.println("=".repeat(25));
            System.out.println("1. Show Total Income");
            System.out.println("2. Show Total Expenses");
            System.out.println("3. Show Balance");
            System.out.println("4. Monthly Summary");
            System.out.println("5. Back to Main Menu");
            System.out.println("=".repeat(25));

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    showTotalIncome();
                    break;
                case 2:
                    showTotalExpenses();
                    break;
                case 3:
                    showBalance();
                    break;
                case 4:
                    showMonthlySummary();
                    break;
                case 5:
                    backToMain = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addUser() {
        System.out.println("\n" + "-".repeat(20));
        System.out.println("--- Add User ---");
        System.out.println("-".repeat(20));
        String name = getStringInput("Enter user name: ");
        User user = financeService.addUser(name);
        if (user != null) {
            System.out.println("✅ User added successfully! ID: " + user.getId());
        } else {
            System.out.println("❌ Failed to add user.");
        }
    }

    private void viewAllUsers() {
        System.out.println("\n" + "-".repeat(25));
        System.out.println("--- All Users ---");
        System.out.println("-".repeat(25));
        List<User> users = financeService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("📝 No users found.");
        } else {
            System.out.printf("%-5s %-20s%n", "ID", "Name");
            System.out.println("-".repeat(25));
            for (User user : users) {
                System.out.printf("%-5d %-20s%n", user.getId(), user.getName());
            }
            System.out.println("-".repeat(25));
            System.out.println("Total users: " + users.size());
        }
    }

    private void updateUser() {
        System.out.println("\n" + "-".repeat(25));
        System.out.println("--- Update User ---");
        System.out.println("-".repeat(25));

        List<User> users = financeService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("📝 No users found.");
            return;
        }

        System.out.println("Available users:");
        for (User user : users) {
            System.out.println(user.getId() + ". " + user.getName());
        }

        Long id = getLongInput("Enter user ID to update: ");
        User existingUser = financeService.getUserById(id).orElse(null);
        if (existingUser == null) {
            System.out.println("❌ User with ID " + id + " not found.");
            return;
        }

        System.out.println("Current user: " + existingUser.getName());
        String newName = getStringInput("Enter new name: ");
        User updatedUser = financeService.updateUser(id, newName);
        if (updatedUser != null) {
            System.out.println("✅ User updated successfully!");
        } else {
            System.out.println("❌ Failed to update user.");
        }
    }

    private void deleteUser() {
        System.out.println("\n" + "-".repeat(25));
        System.out.println("--- Delete User ---");
        System.out.println("-".repeat(25));

        List<User> users = financeService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("📝 No users found.");
            return;
        }

        System.out.println("Available users:");
        for (User user : users) {
            System.out.println(user.getId() + ". " + user.getName());
        }

        Long id = getLongInput("Enter user ID to delete: ");
        User existingUser = financeService.getUserById(id).orElse(null);
        if (existingUser == null) {
            System.out.println("❌ User with ID " + id + " not found.");
            return;
        }

        System.out.println("Are you sure you want to delete user: " + existingUser.getName() + "? (y/n)");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (confirm.equals("y") || confirm.equals("yes")) {
            boolean deleted = financeService.deleteUser(id);
            if (deleted) {
                System.out.println("✅ User deleted successfully!");
            } else {
                System.out.println("❌ Failed to delete user.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void addTransaction() {
        System.out.println("\n--- Add Transaction ---");

        List<User> users = financeService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found. Please add a user first.");
            return;
        }

        System.out.println("Available users:");
        for (User user : users) {
            System.out.println(user.getId() + ". " + user.getName());
        }

        Long userId = getLongInput("Enter user ID: ");
        User user = financeService.getUserById(userId).orElse(null);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        Double amount = getDoubleInput("Enter amount: ");

        System.out.println("Transaction type:");
        System.out.println("1. Income");
        System.out.println("2. Expense");
        int typeChoice = getIntInput("Enter choice (1 or 2): ");
        TransactionType type = (typeChoice == 1) ? TransactionType.INCOME : TransactionType.EXPENSE;

        String description = getStringInput("Enter description: ");

        Transaction transaction = financeService.addTransaction(amount, type, description, userId);
        if (transaction != null) {
            System.out.println("Transaction added successfully! ID: " + transaction.getId());
        } else {
            System.out.println("Failed to add transaction.");
        }
    }

    private void viewTransactionsByUser() {
        System.out.println("\n--- View Transactions by User ---");

        List<User> users = financeService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("Available users:");
        for (User user : users) {
            System.out.println(user.getId() + ". " + user.getName());
        }

        Long userId = getLongInput("Enter user ID: ");
        User user = financeService.getUserById(userId).orElse(null);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        List<Transaction> transactions = financeService.getTransactionsByUser(userId);
        if (transactions.isEmpty()) {
            System.out.println("No transactions found for user: " + user.getName());
        } else {
            System.out.println("\nTransactions for user: " + user.getName());
            System.out.printf("%-5s %-10s %-15s %-20s %-30s%n", "ID", "Amount", "Type", "Date", "Description");
            System.out.println("--------------------------------------------------------------------------------");
            for (Transaction transaction : transactions) {
                System.out.printf("%-5d %-10.2f %-15s %-20s %-30s%n",
                        transaction.getId(),
                        transaction.getAmount(),
                        transaction.getType(),
                        transaction.getTransactionDate().toLocalDate(),
                        transaction.getDescription());
            }
        }
    }

    private void updateTransaction() {
        System.out.println("\n" + "-".repeat(30));
        System.out.println("--- Update Transaction ---");
        System.out.println("-".repeat(30));

        List<User> users = financeService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("📝 No users found.");
            return;
        }

        System.out.println("All transactions:");
        System.out.printf("%-5s %-10s %-15s %-20s %-30s%n", "ID", "Amount", "Type", "Date", "Description");
        System.out.println("-".repeat(80));

        boolean foundAny = false;
        for (User user : users) {
            List<Transaction> transactions = financeService.getTransactionsByUser(user.getId());
            for (Transaction transaction : transactions) {
                System.out.printf("%-5d %-10.2f %-15s %-20s %-30s%n",
                        transaction.getId(),
                        transaction.getAmount(),
                        transaction.getType(),
                        transaction.getTransactionDate().toLocalDate(),
                        transaction.getDescription());
                foundAny = true;
            }
        }

        if (!foundAny) {
            System.out.println("📝 No transactions found.");
            return;
        }

        Long transactionId = getLongInput("\nEnter transaction ID to update: ");

        Transaction transactionToUpdate = null;
        for (User user : users) {
            List<Transaction> transactions = financeService.getTransactionsByUser(user.getId());
            for (Transaction transaction : transactions) {
                if (transaction.getId().equals(transactionId)) {
                    transactionToUpdate = transaction;
                    break;
                }
            }
            if (transactionToUpdate != null)
                break;
        }

        if (transactionToUpdate == null) {
            System.out.println("❌ Transaction with ID " + transactionId + " not found.");
            return;
        }

        System.out.println("\nCurrent transaction details:");
        System.out.println("Amount: " + transactionToUpdate.getAmount());
        System.out.println("Type: " + transactionToUpdate.getType());
        System.out.println("Description: " + transactionToUpdate.getDescription());

        Double newAmount = getDoubleInput("\nEnter new amount: ");

        System.out.println("New transaction type:");
        System.out.println("1. Income");
        System.out.println("2. Expense");
        int typeChoice = getIntInput("Enter choice (1 or 2): ");
        TransactionType newType = (typeChoice == 1) ? TransactionType.INCOME : TransactionType.EXPENSE;

        String newDescription = getStringInput("Enter new description: ");

        Transaction updatedTransaction = financeService.updateTransaction(transactionId, newAmount, newType,
                newDescription);
        if (updatedTransaction != null) {
            System.out.println("✅ Transaction updated successfully!");
        } else {
            System.out.println("❌ Failed to update transaction.");
        }
    }

    private void deleteTransaction() {
        System.out.println("\n" + "-".repeat(30));
        System.out.println("--- Delete Transaction ---");
        System.out.println("-".repeat(30));

        List<User> users = financeService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("📝 No users found.");
            return;
        }

        System.out.println("All transactions:");
        System.out.printf("%-5s %-10s %-15s %-20s %-30s%n", "ID", "Amount", "Type", "Date", "Description");
        System.out.println("-".repeat(80));

        boolean foundAny = false;
        for (User user : users) {
            List<Transaction> transactions = financeService.getTransactionsByUser(user.getId());
            for (Transaction transaction : transactions) {
                System.out.printf("%-5d %-10.2f %-15s %-20s %-30s%n",
                        transaction.getId(),
                        transaction.getAmount(),
                        transaction.getType(),
                        transaction.getTransactionDate().toLocalDate(),
                        transaction.getDescription());
                foundAny = true;
            }
        }

        if (!foundAny) {
            System.out.println("📝 No transactions found.");
            return;
        }

        Long transactionId = getLongInput("\nEnter transaction ID to delete: ");

        Transaction transactionToDelete = null;
        for (User user : users) {
            List<Transaction> transactions = financeService.getTransactionsByUser(user.getId());
            for (Transaction transaction : transactions) {
                if (transaction.getId().equals(transactionId)) {
                    transactionToDelete = transaction;
                    break;
                }
            }
            if (transactionToDelete != null)
                break;
        }

        if (transactionToDelete == null) {
            System.out.println("❌ Transaction with ID " + transactionId + " not found.");
            return;
        }

        System.out.println("\nTransaction details:");
        System.out.println("Amount: " + transactionToDelete.getAmount());
        System.out.println("Type: " + transactionToDelete.getType());
        System.out.println("Description: " + transactionToDelete.getDescription());
        System.out.println("Are you sure you want to delete this transaction? (y/n)");

        String confirm = scanner.nextLine().trim().toLowerCase();
        if (confirm.equals("y") || confirm.equals("yes")) {
            boolean deleted = financeService.deleteTransaction(transactionId);
            if (deleted) {
                System.out.println("✅ Transaction deleted successfully!");
            } else {
                System.out.println("❌ Failed to delete transaction.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private void showTotalIncome() {
        System.out.println("\n" + "-".repeat(30));
        System.out.println("--- Total Income Report ---");
        System.out.println("-".repeat(30));
        System.out.println("1. Show income for all users");
        System.out.println("2. Show income for specific user");
        int choice = getIntInput("Enter choice: ");

        if (choice == 1) {
            Double totalIncome = financeService.getTotalIncome(null);
            System.out.println("\n💰 Total Income (All Users): $" + String.format("%.2f", totalIncome));

            List<User> users = financeService.getAllUsers();
            if (!users.isEmpty()) {
                System.out.println("\n📊 Income breakdown by user:");
                System.out.println("-".repeat(40));
                for (User user : users) {
                    Double userIncome = financeService.getTotalIncome(user.getId());
                    System.out.println(user.getName() + ": $" + String.format("%.2f", userIncome));
                }
            }
        } else if (choice == 2) {
            List<User> users = financeService.getAllUsers();
            if (users.isEmpty()) {
                System.out.println("📝 No users found.");
                return;
            }

            System.out.println("Available users:");
            for (User user : users) {
                System.out.println(user.getId() + ". " + user.getName());
            }

            Long userId = getLongInput("Enter user ID: ");
            User user = financeService.getUserById(userId).orElse(null);
            if (user == null) {
                System.out.println("❌ User with ID " + userId + " not found.");
                return;
            }

            Double totalIncome = financeService.getTotalIncome(userId);
            System.out.println("\n💰 Total Income for " + user.getName() + ": $" + String.format("%.2f", totalIncome));
        }
    }

    private void showTotalExpenses() {
        System.out.println("\n" + "-".repeat(30));
        System.out.println("--- Total Expenses Report ---");
        System.out.println("-".repeat(30));
        System.out.println("1. Show expenses for all users");
        System.out.println("2. Show expenses for specific user");
        int choice = getIntInput("Enter choice: ");

        if (choice == 1) {
            Double totalExpenses = financeService.getTotalExpenses(null);
            System.out.println("\n💸 Total Expenses (All Users): $" + String.format("%.2f", totalExpenses));

            List<User> users = financeService.getAllUsers();
            if (!users.isEmpty()) {
                System.out.println("\n📊 Expenses breakdown by user:");
                System.out.println("-".repeat(40));
                for (User user : users) {
                    Double userExpenses = financeService.getTotalExpenses(user.getId());
                    System.out.println(user.getName() + ": $" + String.format("%.2f", userExpenses));
                }
            }
        } else if (choice == 2) {
            List<User> users = financeService.getAllUsers();
            if (users.isEmpty()) {
                System.out.println("📝 No users found.");
                return;
            }

            System.out.println("Available users:");
            for (User user : users) {
                System.out.println(user.getId() + ". " + user.getName());
            }

            Long userId = getLongInput("Enter user ID: ");
            User user = financeService.getUserById(userId).orElse(null);
            if (user == null) {
                System.out.println("❌ User with ID " + userId + " not found.");
                return;
            }

            Double totalExpenses = financeService.getTotalExpenses(userId);
            System.out.println(
                    "\n💸 Total Expenses for " + user.getName() + ": $" + String.format("%.2f", totalExpenses));
        }
    }

    private void showBalance() {
        System.out.println("\n" + "-".repeat(25));
        System.out.println("--- Balance Report ---");
        System.out.println("-".repeat(25));
        System.out.println("1. Show balance for all users");
        System.out.println("2. Show balance for specific user");
        int choice = getIntInput("Enter choice: ");

        if (choice == 1) {
            Double balance = financeService.getBalance(null);
            System.out.println("\n💳 Total Balance (All Users): $" + String.format("%.2f", balance));

            List<User> users = financeService.getAllUsers();
            if (!users.isEmpty()) {
                System.out.println("\n📊 Balance breakdown by user:");
                System.out.println("-".repeat(50));
                System.out.printf("%-20s %-15s %-15s %-15s%n", "User", "Income", "Expenses", "Balance");
                System.out.println("-".repeat(50));
                for (User user : users) {
                    Double userIncome = financeService.getTotalIncome(user.getId());
                    Double userExpenses = financeService.getTotalExpenses(user.getId());
                    Double userBalance = userIncome - userExpenses;
                    System.out.printf("%-20s $%-14.2f $%-14.2f $%-14.2f%n",
                            user.getName(), userIncome, userExpenses, userBalance);
                }
            }
        } else if (choice == 2) {
            List<User> users = financeService.getAllUsers();
            if (users.isEmpty()) {
                System.out.println("📝 No users found.");
                return;
            }

            System.out.println("Available users:");
            for (User user : users) {
                System.out.println(user.getId() + ". " + user.getName());
            }

            Long userId = getLongInput("Enter user ID: ");
            User user = financeService.getUserById(userId).orElse(null);
            if (user == null) {
                System.out.println("❌ User with ID " + userId + " not found.");
                return;
            }

            Double userIncome = financeService.getTotalIncome(userId);
            Double userExpenses = financeService.getTotalExpenses(userId);
            Double balance = financeService.getBalance(userId);

            System.out.println("\n💳 Balance for " + user.getName() + ":");
            System.out.println("Income: $" + String.format("%.2f", userIncome));
            System.out.println("Expenses: $" + String.format("%.2f", userExpenses));
            System.out.println("Balance: $" + String.format("%.2f", balance));
        }
    }

    private void showMonthlySummary() {
        System.out.println("\n--- Monthly Summary Report ---");

        List<User> users = financeService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        System.out.println("Available users:");
        for (User user : users) {
            System.out.println(user.getId() + ". " + user.getName());
        }

        Long userId = getLongInput("Enter user ID: ");
        User user = financeService.getUserById(userId).orElse(null);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }

        System.out.println("1. Current month summary");
        System.out.println("2. Specific month summary");
        int choice = getIntInput("Enter choice: ");

        List<Transaction> transactions;
        if (choice == 1) {
            transactions = financeService.getCurrentMonthSummary(userId);
            System.out.println("\nCurrent Month Summary for " + user.getName() + ":");
        } else {
            int year = getIntInput("Enter year: ");
            int month = getIntInput("Enter month (1-12): ");
            transactions = financeService.getMonthlySummary(userId, year, month);
            System.out.println("\nMonthly Summary for " + user.getName() + " (" + year + "-"
                    + String.format("%02d", month) + "):");
        }

        if (transactions.isEmpty()) {
            System.out.println("No transactions found for the selected period.");
        } else {
            Double totalIncome = 0.0;
            Double totalExpenses = 0.0;

            System.out.printf("%-5s %-10s %-15s %-20s %-30s%n", "ID", "Amount", "Type", "Date", "Description");
            System.out.println("--------------------------------------------------------------------------------");

            for (Transaction transaction : transactions) {
                System.out.printf("%-5d %-10.2f %-15s %-20s %-30s%n",
                        transaction.getId(),
                        transaction.getAmount(),
                        transaction.getType(),
                        transaction.getTransactionDate().toLocalDate(),
                        transaction.getDescription());

                if (transaction.getType() == TransactionType.INCOME) {
                    totalIncome += transaction.getAmount();
                } else {
                    totalExpenses += transaction.getAmount();
                }
            }

            System.out.println("\n--- Summary ---");
            System.out.println("Total Income: $" + String.format("%.2f", totalIncome));
            System.out.println("Total Expenses: $" + String.format("%.2f", totalExpenses));
            System.out.println("Net Balance: $" + String.format("%.2f", totalIncome - totalExpenses));
        }
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private Long getLongInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private Double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
