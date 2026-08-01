package service;

import jdbc.ProductRepository;
import products.Product;

import java.util.List;

public class InventoryService {

    private ProductRepository productRepository = new ProductRepository();

    // Add Product
    public void addProduct(Product product) throws Exception {
        productRepository.save(product);
    }

    // Get Product by ID
    public Product getProduct(int id) throws Exception {
        return productRepository.findById(id);
    }

    // View All Products
    public List<Product> getAllProducts() throws Exception {
        return productRepository.findAll();
    }

    // Update Product
    public void updateProduct(Product product) throws Exception {
        productRepository.update(product);
    }

    // Delete Product
    public void deleteProduct(int id) throws Exception {
        productRepository.delete(id);
    }

    // Restock Product
    public void restockProduct(int id, int quantity) throws Exception {

        Product product = productRepository.findById(id);

        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        product.setQuantity(product.getQuantity() + quantity);

        productRepository.update(product);

        System.out.println("Stock Updated Successfully.");
    }

    // Record Sale
    public void recordSale(int id, int quantity) throws Exception {

        Product product = productRepository.findById(id);

        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        if (product.getQuantity() < quantity) {
            System.out.println("Insufficient Stock.");
            return;
        }

        product.setQuantity(product.getQuantity() - quantity);

        productRepository.update(product);

        System.out.println("Sale Recorded Successfully.");
    }

    // Low Stock Alert
    public void checkLowStock(Product product) {

        if (product.getQuantity() < 10) {
            System.out.println("Low Stock Alert for " + product.getName());
        }

    }

    // Search Product by Name
    public List<Product> searchProduct(String name) throws Exception {
        return productRepository.searchByName(name);
    }

}