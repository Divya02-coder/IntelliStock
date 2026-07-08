package jdbc;
import java.sql.*;

import products.ApparelProduct;
import products.ElectronicsProduct;
import products.PerishableProduct;
import products.Product;
import exception.InvalidProductException;

public class ProductRepository {

    // 1. SAVE / INSERT a product into the database
    public void save(Product product) throws SQLException {
        String sql = "INSERT OR REPLACE INTO products (sku, category, name, quantity, cost_price, base_price, "
                   + "reorder_threshold, size, season, warranty_months, brand, expiry_date) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // 1. Set common variables
stmt.setString(1, product.getSku());
stmt.setString(2, product.getCategory());
stmt.setString(3, product.getName());
stmt.setInt(4, product.getQuantity());
stmt.setDouble(5, product.getCostPrice());
stmt.setDouble(6, product.getBasePrice());
stmt.setInt(7, product.getReorderThreshold());

// 2. Clear all category fields by default using safe JDBC setNull types
stmt.setNull(8, Types.VARCHAR);  // size
stmt.setNull(9, Types.VARCHAR);  // season
stmt.setNull(10, Types.INTEGER); // warranty_months
stmt.setNull(11, Types.VARCHAR); // brand
stmt.setNull(12, Types.VARCHAR); // expiry_date

// 3. Overwrite only what's active
if (product instanceof ApparelProduct) {
    ApparelProduct apparel = (ApparelProduct) product;
    stmt.setString(8, apparel.getSize());
    stmt.setString(9, apparel.getSeason().name());
} else if (product instanceof ElectronicsProduct) {
    ElectronicsProduct electronics = (ElectronicsProduct) product;
    stmt.setInt(10, electronics.getWarrantyMonths());
    stmt.setString(11, electronics.getBrand());
} else if (product instanceof PerishableProduct) {
    PerishableProduct perishable = (PerishableProduct) product;
    stmt.setString(12, perishable.getExpiryDate().toString());
}

            stmt.executeUpdate();
        }
    }

    // 2. FIND a product out of the database by its SKU key
    public Product findBySku(String sku) throws SQLException, InvalidProductException {
        String sql = "SELECT * FROM products WHERE sku = ?;";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, sku);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Turn database row back into a real Java object using our factory
                    return ProductFactory.createProductFromRow(rs);
                }
            }
        }
        return null; // Return null if product isn't found
    }
    public java.util.List<Product> findAll() throws SQLException {

    java.util.List<Product> products = new java.util.ArrayList<>();

    String sql = "SELECT * FROM products";

    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {

            try {
                products.add(ProductFactory.createProductFromRow(rs));
            } catch (Exception e) {
                System.out.println("Skipping invalid row: " + e.getMessage());
            }

        }

    }

    return products;
}
}