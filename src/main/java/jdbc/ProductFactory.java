package jdbc;

import products.ApparelProduct;
import products.ElectronicsProduct;
import products.PerishableProduct;
import products.Product;
import exception.InvalidProductException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ProductFactory {

    public static Product createProductFromRow(ResultSet rs) throws SQLException, InvalidProductException {
        // Extract common core data fields
        String sku = rs.getString("sku");
        String name = rs.getString("name");
        int quantity = rs.getInt("quantity");
        double costPrice = rs.getDouble("cost_price");
        double basePrice = rs.getDouble("base_price");
        int reorderThreshold = rs.getInt("reorder_threshold");
        String category = rs.getString("category");

        // Build and return the correct subclass object depending on the category column
        switch (category) {
            case "Apparel":
                String size = rs.getString("size");
                String seasonStr = rs.getString("season");
                ApparelProduct.Season season = ApparelProduct.Season.valueOf(seasonStr);
                return new ApparelProduct(sku, name, quantity, costPrice, basePrice, reorderThreshold, size, season);

            case "Electronics":
                int warranty = rs.getInt("warranty_months");
                String brand = rs.getString("brand");
                return new ElectronicsProduct(sku, name, quantity, costPrice, basePrice, reorderThreshold, warranty, brand);

            case "Perishable":
                String dateStr = rs.getString("expiry_date");
                LocalDate expiryDate = LocalDate.parse(dateStr);
                return new PerishableProduct(sku, name, quantity, costPrice, basePrice, reorderThreshold, expiryDate);

            default:
                throw new InvalidProductException("Unknown product category in database: " + category);
        }
    }
} 
