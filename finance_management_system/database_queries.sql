
SELECT 
    u.id,
    u.name,
    u.email,
    u.phone_number,
    u.address,
    u.bank_account_number,
    COUNT(t.id) as total_transactions,
    CONCAT('₹', FORMAT(COALESCE(SUM(CASE WHEN t.type='INCOME' THEN t.amount ELSE 0 END), 0), 2)) as total_income,
    CONCAT('₹', FORMAT(COALESCE(SUM(CASE WHEN t.type='EXPENSE' THEN t.amount ELSE 0 END), 0), 2)) as total_expenses,
    CONCAT('₹', FORMAT(COALESCE(SUM(CASE WHEN t.type='INCOME' THEN t.amount ELSE 0 END), 0) - 
                   COALESCE(SUM(CASE WHEN t.type='EXPENSE' THEN t.amount ELSE 0 END), 0), 2)) as net_balance
FROM users u 
LEFT JOIN transactions t ON u.id = t.user_id 
GROUP BY u.id, u.name, u.email, u.phone_number, u.address, u.bank_account_number
ORDER BY u.id;


SELECT 
    t.id,
    u.name as user_name,
    u.email,
    u.phone_number,
    CONCAT('₹', FORMAT(t.amount, 2)) as amount,
    t.type,
    t.description,
    DATE(t.transaction_date) as date,
    TIME(t.transaction_date) as time
FROM transactions t 
JOIN users u ON t.user_id = u.id 
ORDER BY t.transaction_date DESC, t.id DESC;


SELECT 
    DATE_FORMAT(transaction_date, '%Y-%m') as month,
    CONCAT('₹', FORMAT(SUM(CASE WHEN type='INCOME' THEN amount ELSE 0 END), 2)) as total_income,
    CONCAT('₹', FORMAT(SUM(CASE WHEN type='EXPENSE' THEN amount ELSE 0 END), 2)) as total_expenses,
    CONCAT('₹', FORMAT(SUM(CASE WHEN type='INCOME' THEN amount ELSE -amount END), 2)) as net_balance,
    COUNT(*) as transaction_count
FROM transactions 
GROUP BY DATE_FORMAT(transaction_date, '%Y-%m')
ORDER BY month;


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


SELECT 
    u.name,
    COUNT(t.id) as transaction_count,
    CONCAT('₹', FORMAT(COALESCE(AVG(t.amount), 0), 2)) as avg_transaction_amount
FROM users u 
LEFT JOIN transactions t ON u.id = t.user_id 
GROUP BY u.id, u.name
ORDER BY transaction_count DESC;

-- 9. USER CONTACT INFORMATION - Shows all user contact details
SELECT 
    u.id,
    u.name,
    u.email,
    u.phone_number,
    u.address,
    u.bank_account_number,
    CASE 
        WHEN u.address IS NOT NULL AND u.bank_account_number IS NOT NULL THEN 'Complete Profile'
        WHEN u.address IS NOT NULL OR u.bank_account_number IS NOT NULL THEN 'Partial Profile'
        ELSE 'Basic Profile'
    END as profile_status
FROM users u 
ORDER BY u.id;
