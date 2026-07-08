package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class SalesRepository {

    public void recordSale(String sku, int quantity, double price) throws SQLException {
        String sql = "INSERT INTO sales (sku, quantity_sold, sale_price, sale_date) VALUES (?, ?, ?, ?);";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, sku);
            stmt.setInt(2, quantity);
            stmt.setDouble(3, price);
            stmt.setString(4, LocalDateTime.now().toString()); // ISO-8601 string format

            stmt.executeUpdate();
        }
    }
}