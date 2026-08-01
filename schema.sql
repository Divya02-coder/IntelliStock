-- ===========================================
-- IntelliStock Database
-- ===========================================

CREATE DATABASE IF NOT EXISTS intellistock;

USE intellistock;

-- ===========================
-- Products Table
-- ===========================

CREATE TABLE products (

    id INT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(100) NOT NULL,

    category VARCHAR(50) NOT NULL,

    price DOUBLE NOT NULL,

    quantity INT NOT NULL

);

-- ===========================
-- Sales Table
-- ===========================

CREATE TABLE sales (

    sale_id INT AUTO_INCREMENT PRIMARY KEY,

    product_id INT,

    quantity INT,

    sale_date DATE,

    FOREIGN KEY(product_id)
    REFERENCES products(id)

);