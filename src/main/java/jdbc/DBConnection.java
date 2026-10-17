package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = System.getenv()
            .getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/intellistock");

    private static final String USER = System.getenv()
            .getOrDefault("DB_USER", "root");

    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException("Please configure the DB_PASSWORD environment variable.");
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}