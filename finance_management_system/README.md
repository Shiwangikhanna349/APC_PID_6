# Finance Management Console Application

A Spring Boot console-based application for managing personal finances with MySQL database integration.

## Features

### 1. User Management
- **Add User**: Create new users in the system
- **View All Users**: Display all registered users with proper formatting
- **Update User**: Modify existing user information
- **Delete User**: Remove users from the system

### 2. Transaction Management
- **Add Transaction**: Record income and expense transactions
- **View Transactions by User**: Display all transactions for a specific user
- **Update Transaction**: Modify existing transaction details
- **Delete Transaction**: Remove transactions from the system

### 3. Reports
- **Show Total Income**: View total income for all users or specific user
- **Show Total Expenses**: View total expenses for all users or specific user
- **Show Balance**: Calculate and display net balance (income - expenses)
- **Monthly Summary**: Generate detailed monthly transaction reports

### 4. Data Persistence
- **MySQL Integration**: All data is stored in MySQL database
- **Automatic Table Creation**: Database tables are created automatically
- **Sample Data Loading**: Pre-loaded with sample data for testing
- **Console Interface**: Interactive menu-driven interface

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher

## Database Setup

1. **Install and start MySQL server**

2. **Create a MySQL database** (optional - will be created automatically):
   ```sql
   CREATE DATABASE finance_management;
   ```

3. **Update database credentials** in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

## Running the Application

### Step 1: Start MySQL
```bash
# On macOS with Homebrew
brew services start mysql

# On Linux
sudo systemctl start mysql

# On Windows
net start mysql
```

### Step 2: Run the Application
```bash
# Navigate to project directory
cd finance_management

# Build the project
./mvnw clean compile

# Run the application
./mvnw spring-boot:run
```

## Usage

### Console Interface
Once the application starts, you'll see:

```
==================================================
=== Finance Management System ===
Welcome to the Finance Management Console!
==================================================

==============================
=== MAIN MENU ===
==============================
1. Manage Users
2. Manage Transactions
3. Reports
4. Exit
==============================
Enter your choice: 
```

### Menu Navigation

#### Main Menu Options:
1. **Manage Users** - User management operations
2. **Manage Transactions** - Transaction management operations  
3. **Reports** - Financial reports and summaries
4. **Exit** - Exit the application

#### User Management Submenu:
1. **Add User** - Create a new user
2. **View All Users** - Display all users
3. **Update User** - Modify user information
4. **Delete User** - Remove a user
5. **Back to Main Menu** - Return to main menu

#### Transaction Management Submenu:
1. **Add Transaction** - Create a new transaction
2. **View Transactions by User** - Display user's transactions
3. **Update Transaction** - Modify transaction details
4. **Delete Transaction** - Remove a transaction
5. **Back to Main Menu** - Return to main menu

#### Reports Submenu:
1. **Show Total Income** - Display income reports
2. **Show Total Expenses** - Display expense reports
3. **Show Balance** - Display balance information
4. **Monthly Summary** - Generate monthly reports
5. **Back to Main Menu** - Return to main menu

## Database Schema

### Users Table (`users`)
- `id`: Primary key (auto-generated)
- `name`: User's name (VARCHAR 100, NOT NULL)

### Transactions Table (`transactions`)
- `id`: Primary key (auto-generated)
- `amount`: Transaction amount (DOUBLE, NOT NULL)
- `type`: Transaction type (ENUM: INCOME, EXPENSE)
- `description`: Transaction description (VARCHAR 255)
- `transaction_date`: Date and time of transaction (DATETIME, NOT NULL)
- `user_id`: Foreign key reference to user (BIGINT, NOT NULL)

## Sample Data

The application automatically loads sample data on first run:
- **3 Sample Users**: John Doe, Jane Smith, Bob Johnson
- **Sample Transactions**: Various income and expense transactions
- **Realistic Data**: Salary, rent, groceries, utilities, etc.

## Example Workflow

1. **Start the application** using `./mvnw spring-boot:run`
2. **View sample data** by selecting option 2 (Manage Users) → option 2 (View All Users)
3. **Add a new user** by selecting option 1 (Manage Users) → option 1 (Add User)
4. **Add transactions** by selecting option 2 (Manage Transactions) → option 1 (Add Transaction)
5. **View reports** by selecting option 3 (Reports) → option 1 (Show Total Income)
6. **Exit** by selecting option 4 (Exit)

## Technical Details

- **Framework**: Spring Boot 3.5.5
- **Database**: MySQL with JPA/Hibernate
- **Java Version**: 17
- **Build Tool**: Maven
- **Architecture**: 
  - Console-based interface for interactive use
  - Service layer for business logic
  - Repository layer for data access
  - Automatic data loading on startup

## Troubleshooting

1. **Database Connection Issues**: 
   - Ensure MySQL is running: `brew services list | grep mysql`
   - Check credentials in `application.properties`
   - Verify database exists or let the app create it

2. **Java Version**: 
   - Ensure you're using Java 17 or higher
   - Check with `java -version`

3. **Application Not Starting**: 
   - Make sure MySQL is running first
   - Check the console output for error messages
   - Verify all dependencies are installed

4. **Menu Not Displaying**: 
   - Wait for the application to fully start
   - Look for the "=== Finance Management System ===" message
   - The application loads sample data first, then shows the menu

## Data Persistence

- **Automatic Table Creation**: Tables are created automatically on first run
- **Data Persistence**: All data is stored in MySQL and persists between runs
- **Sample Data**: Pre-loaded with realistic sample data for testing
- **Relationships**: Proper foreign key relationships between users and transactions

## Features Verification

✅ **User Management**: Add, view, update, delete users  
✅ **Transaction Management**: Add, view, update, delete transactions  
✅ **Reports**: Income, expenses, balance, monthly summaries  
✅ **Data Persistence**: All data saved to MySQL database  
✅ **Console Interface**: Interactive menu system  
✅ **Sample Data**: Pre-loaded test data  
✅ **Error Handling**: Input validation and error messages  
✅ **Menu Navigation**: Proper numbering and navigation  

## License

This project is for educational purposes.
