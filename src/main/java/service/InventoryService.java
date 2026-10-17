
package service;

import jdbc.ProductRepository;
import products.Product;

import java.util.List;

public class InventoryService {

    public static final int LOW_STOCK_THRESHOLD = 10;

    private final ProductRepository productRepository;

    // Default constructor
    public InventoryService() {
        this.productRepository = new ProductRepository();
    }

    // Constructor for dependency injection and testing
    public InventoryService(ProductRepository productRepository) {
        if (productRepository == null) {
            throw new IllegalArgumentException(
                "ProductRepository cannot be null."
            );
        }
        this.productRepository = productRepository;
    }

    // Add Product
    public void addProduct(Product product) throws Exception {
        if (product == null) {
            throw new IllegalArgumentException(
                "Product cannot be null."
            );
        }

        if (product.getName() == null
                || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Product name cannot be empty."
            );
        }

        if (product.getPrice() < 0) {
            throw new IllegalArgumentException(
                "Product price cannot be negative."
            );
        }

        if (product.getQuantity() < 0) {
            throw new IllegalArgumentException(
                "Product quantity cannot be negative."
            );
        }

        productRepository.save(product);
    }

    // Get Product by ID
    public Product getProduct(int id) throws Exception {
        validateId(id);
        return productRepository.findById(id);
    }

    // View All Products
    public List<Product> getAllProducts() throws Exception {
        return productRepository.findAll();
    }

    // Update Product
    public void updateProduct(Product product) throws Exception {
        if (product == null) {
            throw new IllegalArgumentException(
                "Product cannot be null."
            );
        }

        validateId(product.getId());

        if (product.getName() == null
                || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Product name cannot be empty."
            );
        }

        if (product.getPrice() < 0) {
            throw new IllegalArgumentException(
                "Product price cannot be negative."
            );
        }

        if (product.getQuantity() < 0) {
            throw new IllegalArgumentException(
                "Product quantity cannot be negative."
            );
        }

        productRepository.update(product);
    }

    // Delete Product
    public void deleteProduct(int id) throws Exception {
        validateId(id);
        productRepository.delete(id);
    }

    // Restock Product
    public void restockProduct(int id, int quantity) throws Exception {
        validateId(id);

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                "Restock quantity must be greater than zero."
            );
        }

        Product product = productRepository.findById(id);

        if (product == null) {
            throw new IllegalArgumentException(
                "Product not found."
            );
        }

        product.setQuantity(
            Math.addExact(product.getQuantity(), quantity)
        );

        productRepository.update(product);
    }

    // Record Sale
    public void recordSale(int id, int quantity) throws Exception {
        validateId(id);

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                "Sale quantity must be greater than zero."
            );
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                "Sale quantity must be greater than zero."
            );
        }

        productRepository.recordSale(id, quantity);
    }

    // Get Products with Low Stock
    public List<Product> getLowStockProducts() throws Exception {
        return productRepository.lowStockProducts(
            LOW_STOCK_THRESHOLD
        );
    }

    // Check Low Stock for One Product
    public boolean isLowStock(Product product) {
        return product != null
            && product.getQuantity() < LOW_STOCK_THRESHOLD;
    }

    // Low Stock Alert
    public void checkLowStock(Product product) {
        if (isLowStock(product)) {
            System.out.println(
                "Low Stock Alert for " + product.getName()
                + " (quantity: " + product.getQuantity() + ")"
            );
        }
    }

    // Search Product by Name
    public List<Product> searchProduct(String name) throws Exception {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Search name cannot be empty."
            );
        }

        return productRepository.searchByName(name.trim());
    }

    // Validate Product ID
    private void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                "Product ID must be greater than zero."
            );
        }
    }
}
