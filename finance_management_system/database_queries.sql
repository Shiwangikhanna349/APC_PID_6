-- Finance Management Database 
-- Run these queries in MySQL Workbench or command line to understand your data
-- All amounts are in Indian Rupees (₹)

-- ===================================================================================
-- UNDERSTANDING INCOME vs EXPENSES
-- ===================================================================================
-- 
-- INCOME (Money Coming IN):
-- - Money you EARN or RECEIVE
-- - Examples: Salary, Freelance work, Investment returns, Part-time job
-- - These INCREASE your total money
-- - In our database: type = 'INCOME'
--
-- EXPENSES (Money Going OUT):
-- - Money you SPEND or PAY
-- - Examples: Rent, Groceries, Utilities, Car payment, Internet bill
-- - These DECREASE your total money
-- - In our database: type = 'EXPENSE'
--
-- NET BALANCE = Total Income - Total Expenses
-- - Positive balance = You have money left (Savings)
-- - Negative balance = You spent more than you earned (Debt)
--
-- ===================================================================================

-- 1. USER SUMMARY - Shows each user's financial overview
SELECT 
    u.id,
    u.name,
    COUNT(t.id) as total_transactions,
    CONCAT('₹', FORMAT(COALESCE(SUM(CASE WHEN t.type='INCOME' THEN t.amount ELSE 0 END), 0), 2)) as total_income,
    CONCAT('₹', FORMAT(COALESCE(SUM(CASE WHEN t.type='EXPENSE' THEN t.amount ELSE 0 END), 0), 2)) as total_expenses,
    CONCAT('₹', FORMAT(COALESCE(SUM(CASE WHEN t.type='INCOME' THEN t.amount ELSE 0 END), 0) - 
                   COALESCE(SUM(CASE WHEN t.type='EXPENSE' THEN t.amount ELSE 0 END), 0), 2)) as net_balance
FROM users u 
LEFT JOIN transactions t ON u.id = t.user_id 
GROUP BY u.id, u.name 
ORDER BY u.id;

-- 2. DETAILED TRANSACTIONS - Shows all transactions with user names
SELECT 
    t.id,
    u.name as user_name,
    CONCAT('₹', FORMAT(t.amount, 2)) as amount,
    t.type,
    t.description,
    DATE(t.transaction_date) as date,
    TIME(t.transaction_date) as time
FROM transactions t 
JOIN users u ON t.user_id = u.id 
ORDER BY t.transaction_date DESC, t.id DESC;

-- 3. TRANSACTIONS BY USER - Shows transactions for a specific user (replace USER_ID)
-- SELECT 
--     t.id,
--     CONCAT('₹', FORMAT(t.amount, 2)) as amount,
--     t.type,
--     t.description,
--     t.transaction_date
-- FROM transactions t 
-- WHERE t.user_id = 11  -- Replace 11 with actual user ID
-- ORDER BY t.transaction_date DESC;

-- 4. MONTHLY SUMMARY - Shows income vs expenses by month
SELECT 
    DATE_FORMAT(transaction_date, '%Y-%m') as month,
    CONCAT('₹', FORMAT(SUM(CASE WHEN type='INCOME' THEN amount ELSE 0 END), 2)) as total_income,
    CONCAT('₹', FORMAT(SUM(CASE WHEN type='EXPENSE' THEN amount ELSE 0 END), 2)) as total_expenses,
    CONCAT('₹', FORMAT(SUM(CASE WHEN type='INCOME' THEN amount ELSE -amount END), 2)) as net_balance,
    COUNT(*) as transaction_count
FROM transactions 
GROUP BY DATE_FORMAT(transaction_date, '%Y-%m')
ORDER BY month;

-- 5. TOP EXPENSES - Shows largest expense transactions
SELECT 
    u.name as user_name,
    CONCAT('₹', FORMAT(t.amount, 2)) as amount,
    t.description,
    t.transaction_date
FROM transactions t 
JOIN users u ON t.user_id = u.id 
WHERE t.type = 'EXPENSE'
ORDER BY t.amount DESC
LIMIT 10;

-- 6. TOP INCOME - Shows largest income transactions
SELECT 
    u.name as user_name,
    CONCAT('₹', FORMAT(t.amount, 2)) as amount,
    t.description,
    t.transaction_date
FROM transactions t 
JOIN users u ON t.user_id = u.id 
WHERE t.type = 'INCOME'
ORDER BY t.amount DESC
LIMIT 10;

-- 7. OVERALL FINANCIAL SUMMARY
SELECT 
    'Total Users' as metric,
    COUNT(*) as value
FROM users
UNION ALL
SELECT 
    'Total Transactions' as metric,
    COUNT(*) as value
FROM transactions
UNION ALL
SELECT 
    'Total Income' as metric,
    CONCAT('₹', FORMAT(SUM(CASE WHEN type='INCOME' THEN amount ELSE 0 END), 2)) as value
FROM transactions
UNION ALL
SELECT 
    'Total Expenses' as metric,
    CONCAT('₹', FORMAT(SUM(CASE WHEN type='EXPENSE' THEN amount ELSE 0 END), 2)) as value
FROM transactions
UNION ALL
SELECT 
    'Net Balance' as metric,
    CONCAT('₹', FORMAT(SUM(CASE WHEN type='INCOME' THEN amount ELSE -amount END), 2)) as value
FROM transactions;

-- 8. USER TRANSACTION COUNTS - Shows how many transactions each user has
SELECT 
    u.name,
    COUNT(t.id) as transaction_count,
    CONCAT('₹', FORMAT(COALESCE(AVG(t.amount), 0), 2)) as avg_transaction_amount
FROM users u 
LEFT JOIN transactions t ON u.id = t.user_id 
GROUP BY u.id, u.name
ORDER BY transaction_count DESC;
