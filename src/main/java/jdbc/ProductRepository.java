package jdbc;

import products.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    // Add Product
    public void save(Product product) throws Exception {

        Connection conn = DBConnection.getConnection();

        String sql = "INSERT INTO products(name, category, price, quantity) VALUES (?, ?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, product.getName());
        ps.setString(2, product.getCategory());
        ps.setDouble(3, product.getPrice());
        ps.setInt(4, product.getQuantity());

        ps.executeUpdate();

        System.out.println("Product Added Successfully.");
    }
    public List<Product> lowStockProducts() throws Exception {

    Connection conn = DBConnection.getConnection();

    List<Product> products = new ArrayList<>();

    String sql = "SELECT * FROM products WHERE quantity < 5";

    Statement stmt = conn.createStatement();

    ResultSet rs = stmt.executeQuery(sql);

    while(rs.next()){

        products.add(new Product(

                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getDouble("price"),
                rs.getInt("quantity")

        ));

    }

    return products;

}

    // View Product by ID
    public Product findById(int id) throws Exception {

        Connection conn = DBConnection.getConnection();

        String sql = "SELECT * FROM products WHERE id=?";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            return new Product(

                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getInt("quantity")

            );

        }

        return null;
    }

    // View All Products
    public List<Product> findAll() throws Exception {

        Connection conn = DBConnection.getConnection();

        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products";

        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {

            products.add(

                    new Product(

                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getInt("quantity")

                    )

            );

        }

        return products;

    }

    // Update Product
    public void update(Product product) throws Exception {

        Connection conn = DBConnection.getConnection();

        String sql = "UPDATE products SET name=?, category=?, price=?, quantity=? WHERE id=?";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, product.getName());
        ps.setString(2, product.getCategory());
        ps.setDouble(3, product.getPrice());
        ps.setInt(4, product.getQuantity());
        ps.setInt(5, product.getId());

        ps.executeUpdate();

        System.out.println("Product Updated Successfully.");

    }

    // Delete Product
    public void delete(int id) throws Exception {

        Connection conn = DBConnection.getConnection();

        String sql = "DELETE FROM products WHERE id=?";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, id);

        ps.executeUpdate();

        System.out.println("Product Deleted Successfully.");

    }

    // Search Product
    public List<Product> searchByName(String name) throws Exception {

        Connection conn = DBConnection.getConnection();

        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products WHERE name LIKE ?";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, "%" + name + "%");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            products.add(

                    new Product(

                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getInt("quantity")

                    )

            );

        }

        return products;

    }

}