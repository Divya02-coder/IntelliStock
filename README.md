# IntelliStock — Inventory Management System

IntelliStock is an inventory management application built with Java and MySQL. It helps manage product records, monitor stock levels, process sales, and provide inventory insights through a Python analytics dashboard.

## Features

- **Product Management:** Add, view, update, delete, and search products.
- **Inventory Tracking:** Maintain product quantities and update stock levels.
- **Low-Stock Alerts:** Identify products with quantities below the configured threshold of 10 units.
- **Sales Processing:** Validate available stock and deduct quantities when recording sales.
- **Database Integration:** Store inventory information in MySQL.
- **Analytics Dashboard:** Use Python to analyze sales data and visualize relevant insights, provided sales records are populated in the database.
- **Service Layer:** Separate business logic from database operations using `InventoryService` and `ProductRepository`.
- **Automated Testing:** Use JUnit 5 and Maven to test service behavior.

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Application logic |
| JDBC | Database connectivity |
| MySQL | Inventory and sales database |
| Maven | Dependency management and build |
| JUnit 5 | Automated unit tests |
| Python | Sales analytics |
| Pandas / Matplotlib | Data analysis and visualization, if used by the dashboard |

## Project Structure

```text
IntelliStock/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── jdbc/
│   │       │   ├── DBConnection.java
│   │       │   └── ProductRepository.java
│   │       ├── products/
│   │       │   └── Product.java
│   │       ├── service/
│   │       │   └── InventoryService.java
│   │       └── Main.java
│   └── test/
│       └── java/
│           └── service/
│               └── InventoryServiceTest.java
├── analytics.py
├── mysql.sql
├── pom.xml
└── README.md
```

*The structure above reflects the expected layout. Adjust filenames if your local project differs.*

## Prerequisites

Install the following before running the project:

- Java Development Kit (JDK) 21
- Apache Maven
- MySQL Server
- Python 3, if using the analytics dashboard

## Setup and Installation

### 1. Clone the repository

```bash
git clone https://github.com/Divya02-coder/IntelliStock.git
cd IntelliStock
```

### 2. Set up the MySQL database

1. Start your MySQL server.
2. Open MySQL Workbench or your preferred MySQL client.
3. Execute the SQL statements in `mysql.sql`.
4. Confirm that the required database and tables have been created.

Check that the table names and columns match those expected by `ProductRepository.java` and `analytics.py`.

### 3. Configure database credentials

Set your database connection details in the application's configuration. The database password should be provided through an environment variable named `DB_PASSWORD`, rather than hardcoded in source files.

**Windows PowerShell:**

```powershell
$env:DB_PASSWORD = Read-Host "Enter MySQL password"
```

**Windows Command Prompt (CMD):**

```cmd
set DB_PASSWORD=your_mysql_password
```

Replace the placeholder with your actual password. Set any other connection variables required by `DBConnection.java`.

Keep credentials private and never commit them to GitHub.

### 4. Build the Java application

From the directory containing `pom.xml`, run:

```bash
mvn clean compile
```

### 5. Run automated tests

```bash
