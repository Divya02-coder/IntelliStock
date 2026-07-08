package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:intellistock.db";
    private static Connection connection;

    // Open and cache a single shared connection// Open and cache a single shared connection
public static synchronized Connection getConnection() throws SQLException {
    if (connection == null || connection.isClosed()) {
        try {
            // Force load the SQLite JDBC driver class to register it with DriverManager
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC Driver not found on classpath: " + e.getMessage());
            throw new SQLException("Driver class loading failed", e);
        }
        connection = DriverManager.getConnection(DB_URL);
    }
    return connection;
}
    

    // Automatically trigger table creation when the class is first loaded
    static {
        setupDatabase();
    }

    public static void setupDatabase() {
        // 1. Products table MUST be created first because 'sales' references it
        String createProductsTableSQL = "CREATE TABLE IF NOT EXISTS products ("
                + "sku TEXT PRIMARY KEY, category TEXT NOT NULL, name TEXT NOT NULL, "
                + "quantity INTEGER NOT NULL, cost_price REAL NOT NULL, base_price REAL NOT NULL, "
                + "reorder_threshold INTEGER NOT NULL, size TEXT, season TEXT, "
                + "warranty_months INTEGER, brand TEXT, expiry_date TEXT);";

        // 2. Sales table created second
        String createSalesTableSQL = "CREATE TABLE IF NOT EXISTS sales ("
                + "sale_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "sku TEXT NOT NULL, "
                + "quantity_sold INTEGER NOT NULL, "
                + "sale_price REAL NOT NULL, "
                + "sale_date TEXT NOT NULL, "
                + "FOREIGN KEY(sku) REFERENCES products(sku));";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Execute both statements sequentially
            stmt.execute(createProductsTableSQL);
            stmt.execute(createSalesTableSQL);
            
            System.out.println("Database tables initialized successfully!");
        } catch (SQLException e) {
            System.err.println("Database setup failed: " + e.getMessage());
        }
    }
}