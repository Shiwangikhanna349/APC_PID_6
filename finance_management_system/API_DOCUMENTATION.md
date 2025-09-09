# Finance Management System - REST API Documentation

## Base URL
```
http://localhost:8080
```

## API Endpoints

### User Management

#### 1. Add User
- **URL:** `POST /user/add`
- **Description:** Create a new user
- **Request Body:**
```json
{
    "name": "John Doe"
}
```
- **Response:**
```json
{
    "success": true,
    "message": "User added successfully",
    "data": {
        "id": 1,
        "name": "John Doe"
    }
}
```

#### 2. Get All Users
- **URL:** `GET /user/all`
- **Description:** Retrieve all users
- **Response:**
```json
{
    "success": true,
    "message": "Users retrieved successfully",
    "data": [
        {
            "id": 1,
            "name": "John Doe"
        },
        {
            "id": 2,
            "name": "Jane Smith"
        }
    ]
}
```

#### 3. Get User by ID
- **URL:** `GET /user/{id}`
- **Description:** Retrieve a specific user by ID
- **Path Parameters:**
  - `id`: User ID (Long)
- **Response:**
```json
{
    "success": true,
    "message": "User retrieved successfully",
    "data": {
        "id": 1,
        "name": "John Doe"
    }
}
```

#### 4. Update User
- **URL:** `PUT /user/edit/{id}`
- **Description:** Update an existing user
- **Path Parameters:**
  - `id`: User ID (Long)
- **Request Body:**
```json
{
    "name": "John Updated"
}
```
- **Response:**
```json
{
    "success": true,
    "message": "User updated successfully",
    "data": {
        "id": 1,
        "name": "John Updated"
    }
}
```

#### 5. Delete User
- **URL:** `DELETE /user/delete/{id}`
- **Description:** Delete a user by ID
- **Path Parameters:**
  - `id`: User ID (Long)
- **Response:**
```json
{
    "success": true,
    "message": "User deleted successfully",
    "data": null
}
```

### Transaction Management

#### 1. Add Transaction
- **URL:** `POST /transaction/add`
- **Description:** Create a new transaction
- **Request Body:**
```json
{
    "amount": 1500.00,
    "type": "INCOME",
    "description": "Salary payment",
    "userId": 1
}
```
- **Transaction Types:** `INCOME`, `EXPENSE`
- **Response:**
```json
{
    "success": true,
    "message": "Transaction added successfully",
    "data": {
        "id": 1,
        "amount": 1500.00,
        "type": "INCOME",
        "description": "Salary payment",
        "transactionDate": "2024-01-15T10:30:00",
        "userId": 1,
        "userName": "John Doe"
    }
}
```

#### 2. Get Transactions by User
- **URL:** `GET /transaction/user/{userId}`
- **Description:** Retrieve all transactions for a specific user
- **Path Parameters:**
  - `userId`: User ID (Long)
- **Response:**
```json
{
    "success": true,
    "message": "Transactions retrieved successfully",
    "data": [
        {
            "id": 1,
            "amount": 1500.00,
            "type": "INCOME",
            "description": "Salary payment",
            "transactionDate": "2024-01-15T10:30:00",
            "userId": 1,
            "userName": "John Doe"
        }
    ]
}
```

#### 3. Get All Transactions
- **URL:** `GET /transaction/all`
- **Description:** Retrieve all transactions from all users
- **Response:**
```json
{
    "success": true,
    "message": "All transactions retrieved successfully",
    "data": [
        {
            "id": 1,
            "amount": 1500.00,
            "type": "INCOME",
            "description": "Salary payment",
            "transactionDate": "2024-01-15T10:30:00",
            "userId": 1,
            "userName": "John Doe"
        }
    ]
}
```

#### 4. Update Transaction
- **URL:** `PUT /transaction/edit/{id}`
- **Description:** Update an existing transaction
- **Path Parameters:**
  - `id`: Transaction ID (Long)
- **Request Body:**
```json
{
    "amount": 2000.00,
    "type": "INCOME",
    "description": "Updated salary payment"
}
```
- **Response:**
```json
{
    "success": true,
    "message": "Transaction updated successfully",
    "data": {
        "id": 1,
        "amount": 2000.00,
        "type": "INCOME",
        "description": "Updated salary payment",
        "transactionDate": "2024-01-15T10:30:00",
        "userId": 1,
        "userName": "John Doe"
    }
}
```

#### 5. Delete Transaction
- **URL:** `DELETE /transaction/delete/{id}`
- **Description:** Delete a transaction by ID
- **Path Parameters:**
  - `id`: Transaction ID (Long)
- **Response:**
```json
{
    "success": true,
    "message": "Transaction deleted successfully",
    "data": null
}
```

### Reports

#### 1. Income Report
- **URL:** `GET /reports/income`
- **Description:** Get total income report
- **Query Parameters:**
  - `userId` (optional): User ID to get income for specific user
- **Examples:**
  - All users: `GET /reports/income`
  - Specific user: `GET /reports/income?userId=1`
- **Response (All users):**
```json
{
    "success": true,
    "message": "Income report generated successfully",
    "data": {
        "totalIncome": 5000.00,
        "userBreakdown": {
            "John Doe": 3000.00,
            "Jane Smith": 2000.00
        }
    }
}
```
- **Response (Specific user):**
```json
{
    "success": true,
    "message": "Income report generated successfully",
    "data": {
        "userId": 1,
        "userName": "John Doe",
        "totalIncome": 3000.00
    }
}
```

#### 2. Expenses Report
- **URL:** `GET /reports/expenses`
- **Description:** Get total expenses report
- **Query Parameters:**
  - `userId` (optional): User ID to get expenses for specific user
- **Examples:**
  - All users: `GET /reports/expenses`
  - Specific user: `GET /reports/expenses?userId=1`
- **Response (All users):**
```json
{
    "success": true,
    "message": "Expenses report generated successfully",
    "data": {
        "totalExpenses": 2000.00,
        "userBreakdown": {
            "John Doe": 1200.00,
            "Jane Smith": 800.00
        }
    }
}
```

#### 3. Balance Report
- **URL:** `GET /reports/balance`
- **Description:** Get balance report (Income - Expenses)
- **Query Parameters:**
  - `userId` (optional): User ID to get balance for specific user
- **Examples:**
  - All users: `GET /reports/balance`
  - Specific user: `GET /reports/balance?userId=1`
- **Response (All users):**
```json
{
    "success": true,
    "message": "Balance report generated successfully",
    "data": {
        "totalBalance": 3000.00,
        "userBreakdown": {
            "John Doe": {
                "income": 3000.00,
                "expenses": 1200.00,
                "balance": 1800.00
            },
            "Jane Smith": {
                "income": 2000.00,
                "expenses": 800.00,
                "balance": 1200.00
            }
        }
    }
}
```

#### 4. Monthly Summary
- **URL:** `GET /reports/monthly`
- **Description:** Get monthly transaction summary
- **Query Parameters:**
  - `userId` (required): User ID
  - `year` (optional): Year (defaults to current year)
  - `month` (optional): Month 1-12 (defaults to current month)
- **Examples:**
  - Current month: `GET /reports/monthly?userId=1`
  - Specific month: `GET /reports/monthly?userId=1&year=2024&month=1`
- **Response:**
```json
{
    "success": true,
    "message": "Monthly summary generated successfully",
    "data": {
        "userId": 1,
        "userName": "John Doe",
        "period": "2024-01",
        "transactions": [
            {
                "id": 1,
                "amount": 1500.00,
                "type": "INCOME",
                "description": "Salary payment",
                "transactionDate": "2024-01-15T10:30:00"
            }
        ],
        "summary": {
            "totalIncome": 1500.00,
            "totalExpenses": 500.00,
            "netBalance": 1000.00,
            "transactionCount": 2
        }
    }
}
```

## Error Responses

All endpoints return consistent error responses:

```json
{
    "success": false,
    "message": "Error description",
    "data": null
}
```

Common HTTP status codes:
- `200 OK`: Success
- `400 Bad Request`: Invalid request data
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

## Postman Collection Setup

1. **Base URL:** Set `{{baseUrl}}` variable to `http://localhost:8080`
2. **Headers:** 
   - `Content-Type: application/json` (for POST/PUT requests)
3. **Environment Variables:**
   - `baseUrl`: `http://localhost:8080`

## Testing Workflow

1. **Start the application** (it will run on port 8080)
2. **Add users** using `POST /user/add`
3. **Add transactions** using `POST /transaction/add`
4. **View reports** using the various `/reports/*` endpoints
5. **Update/Delete** as needed using the respective endpoints

## Notes

- All endpoints support CORS for cross-origin requests
- The application uses MySQL database (configured in `application.properties`)
- Sample data is loaded automatically when the application starts
- All monetary values are in Double format
- Transaction dates are automatically set to current timestamp when created
