package jdbc;

import products.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    public void save(Product product) throws SQLException {
        String sql = """
                INSERT INTO products(name, category, price, quantity)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getCategory());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getQuantity());
            ps.executeUpdate();
        }
    }

    public List<Product> lowStockProducts(int threshold)
            throws SQLException {

        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE quantity < ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, threshold);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProduct(rs));
                }
            }
        }

        return products;
    }

    public Product findById(int id) throws SQLException {
        String sql = "SELECT * FROM products WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapProduct(rs) : null;
            }
        }
    }

    public List<Product> findAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(mapProduct(rs));
            }
        }

        return products;
    }

    public void update(Product product) throws SQLException {
        String sql = """
                UPDATE products
                SET name = ?, category = ?, price = ?, quantity = ?
                WHERE id = ?
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getCategory());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getQuantity());
            ps.setInt(5, product.getId());

            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("Product not found.");
            }
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("Product not found.");
            }
        }
    }

    public List<Product> searchByName(String name)
            throws SQLException {

        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + name + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProduct(rs));
                }
            }
        }

        return products;
    }

    public void recordSale(int productId, int quantity)
            throws SQLException {

        if (quantity <= 0 || productId <= 0) {
            throw new IllegalArgumentException(
                    "Sale quantity and product ID must be positive.");
        }

        String updateStock = """
                UPDATE products
                SET quantity = quantity - ?
                WHERE id = ? AND quantity >= ?
                """;

        String insertSale = """
                INSERT INTO sales(product_id, quantity, sale_date)
                VALUES (?, ?, CURRENT_DATE)
                """;

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try {
                try (PreparedStatement ps =
                             conn.prepareStatement(updateStock)) {

                    ps.setInt(1, quantity);
                    ps.setInt(2, productId);
                    ps.setInt(3, quantity);

                    if (ps.executeUpdate() == 0) {
                        throw new IllegalStateException(
                                "Product not found or insufficient stock.");
                    }
                }

                try (PreparedStatement ps =
                             conn.prepareStatement(insertSale)) {

                    ps.setInt(1, productId);
                    ps.setInt(2, quantity);
                    ps.executeUpdate();
                }

                conn.commit();

            } catch (SQLException | RuntimeException e) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;
            }
        }
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getDouble("price"),
                rs.getInt("quantity")
        );
    }
}