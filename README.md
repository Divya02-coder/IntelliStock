# IntelliStock – Inventory Management System

## Overview
IntelliStock is a console-based Inventory Management System built using Java, JDBC, and MySQL. It allows users to manage product inventory through a menu-driven interface.

## Features

- Add Product
- View All Products
- Search Product
- Update Product
- Delete Product
- Low Stock Alert
- Inventory Summary

## Technologies Used

- Java
- JDBC
- MySQL
- Maven
- Object-Oriented Programming

## Project Structure

```
src
└── main
    └── java
        ├── jdbc
        │   ├── DBConnection.java
        │   └── ProductRepository.java
        ├── products
        │   └── Product.java
        ├── service
        │   └── InventoryService.java
        └── Main.java
```

## Database

Create the database:

```sql
CREATE DATABASE intellistock;
```

Use:

```sql
USE intellistock;
```

Create the products table:

```sql
CREATE TABLE products(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    category VARCHAR(50),
    price DOUBLE,
    quantity INT
);
```

## How to Run

1. Clone the repository
2. Configure MySQL credentials in `DBConnection.java`
3. Run the SQL script
4. Execute `Main.java`

## Sample Menu

```
1. Add Product
2. View All Products
3. Search Product
4. Update Product
5. Delete Product
6. Low Stock Products
7. Inventory Summary
8. Exit
```