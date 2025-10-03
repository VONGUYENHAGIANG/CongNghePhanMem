-- Tạo database
CREATE DATABASE atm_demo;
USE atm_demo;

-- Tạo bảng accounts
CREATE TABLE accounts (
    account_id INT PRIMARY KEY AUTO_INCREMENT,
    balance DECIMAL(15,2)
);

-- Tạo bảng cards
CREATE TABLE cards (
    card_no VARCHAR(20) PRIMARY KEY,
    account_id INT,
    pin_hash VARCHAR(64),
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);

-- Tạo bảng transactions
CREATE TABLE transactions (
    tx_id INT PRIMARY KEY AUTO_INCREMENT,
    account_id INT,
    card_no VARCHAR(20),
    atm_id INT,
    tx_type VARCHAR(20),
    amount DECIMAL(15,2),
    balance_after DECIMAL(15,2),
    tx_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Thêm dữ liệu demo
INSERT INTO accounts(balance) VALUES(1000.00);
INSERT INTO cards(card_no, account_id, pin_hash)
VALUES('1234567890', 1, SHA2('1234', 256));
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '123456';
FLUSH PRIVILEGES;
USE atm_db;

-- nếu có bảng accounts hoặc users, kiểm tra tên trước
SHOW TABLES;

-- ví dụ nếu bảng tên là accounts
DESC accounts;

-- thêm dữ liệu test
INSERT INTO accounts (account_number, pin, balance)
VALUES ('123456', '2512', 1000000);
-- Chọn database atm_db
USE atm_db;

-- Tạo bảng accounts (nếu chưa có)
CREATE TABLE IF NOT EXISTS accounts (
    account_number VARCHAR(20) PRIMARY KEY,
    pin VARCHAR(10) NOT NULL,
    balance DECIMAL(15,2) NOT NULL
);

-- Xoá dữ liệu cũ (nếu có) để tránh trùng
DELETE FROM accounts;

-- Thêm dữ liệu test
INSERT INTO accounts (account_number, pin, balance) VALUES
('123456', '2512', 1000000.00),
('654321', '1234', 500000.00),
('111111', '0000', 750000.00);

-- Kiểm tra dữ liệu
SELECT * FROM accounts;

