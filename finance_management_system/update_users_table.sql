-- Update Users Table with New Fields
-- Run this script to add new columns to the existing users table

-- Add new columns to the users table
ALTER TABLE users 
ADD COLUMN email VARCHAR(255) NOT NULL DEFAULT '' AFTER name,
ADD COLUMN phone_number VARCHAR(15) NOT NULL DEFAULT '' AFTER email,
ADD COLUMN address VARCHAR(500) NULL AFTER phone_number,
ADD COLUMN bank_account_number VARCHAR(20) NULL AFTER address;

-- Add unique constraint for email
ALTER TABLE users 
ADD CONSTRAINT uk_users_email UNIQUE (email);

-- Update existing users with sample data (optional)
-- You can modify these values as needed
UPDATE users SET 
    email = CASE 
        WHEN id = 1 THEN 'john.doe@example.com'
        WHEN id = 2 THEN 'jane.smith@example.com'
        WHEN id = 3 THEN 'bob.johnson@example.com'
        ELSE CONCAT('user', id, '@example.com')
    END,
    phone_number = CASE 
        WHEN id = 1 THEN '9876543210'
        WHEN id = 2 THEN '9876543211'
        WHEN id = 3 THEN '9876543212'
        ELSE CONCAT('98765432', LPAD(id, 2, '0'))
    END,
    address = CASE 
        WHEN id = 1 THEN '123 Main Street, Mumbai, Maharashtra 400001'
        WHEN id = 2 THEN '456 Park Avenue, Delhi, Delhi 110001'
        WHEN id = 3 THEN '789 Business District, Bangalore, Karnataka 560001'
        ELSE CONCAT('Address for User ', id)
    END,
    bank_account_number = CASE 
        WHEN id = 1 THEN '1234567890123456'
        WHEN id = 2 THEN '2345678901234567'
        WHEN id = 3 THEN '3456789012345678'
        ELSE CONCAT('1234567890', LPAD(id, 6, '0'))
    END
WHERE id IN (1, 2, 3);

-- Verify the changes
SELECT id, name, email, phone_number, address, bank_account_number 
FROM users 
ORDER BY id;
