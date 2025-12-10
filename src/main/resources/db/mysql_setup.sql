-- ============================================================================
-- MySQL Database Setup for Music Hub
-- ============================================================================
-- Run this script in MySQL to create the database and user

-- Create database
CREATE DATABASE IF NOT EXISTS bai_2 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Create user (optional - if you want to use a specific user instead of root)
-- CREATE USER 'musicuser'@'localhost' IDENTIFIED BY 'your_password_here';
-- GRANT ALL PRIVILEGES ON bai_2.* TO 'musicuser'@'localhost';
-- FLUSH PRIVILEGES;

-- Use the database
USE bai_2;

-- Display confirmation
SELECT 'Database setup completed successfully!' AS Status;
SELECT DATABASE() AS CurrentDatabase;
