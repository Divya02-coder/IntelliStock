package simulation;

import jdbc.DatabaseManager;

import java.sql.*;

public class DatabaseViewer {

    public static void main(String[] args) {

        try (Connection conn = DatabaseManager.getConnection()) {

            System.out.println("========== PRODUCTS ==========");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM products");

            while(rs.next()){

                System.out.printf(
                    "%-12s %-25s Qty:%3d Price:%8.2f Threshold:%2d%n",
                    rs.getString("sku"),
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getDouble("selling_price"),
                    rs.getInt("reorder_threshold")
                );
            }

            System.out.println("\n========== SALES ==========");

            rs = stmt.executeQuery("SELECT * FROM sales");

            while(rs.next()){

                System.out.printf(
                    "%-12s Qty:%2d Date:%s%n",
                    rs.getString("sku"),
                    rs.getInt("quantity"),
                    rs.getString("sale_time")
                );
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}