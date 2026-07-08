package service;

import jdbc.ProductRepository;
// Removed unresolved import: jdbc.InvalidProductException
import products.Product;
import interfaces.StockAlertObserver;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryService {
    private final ProductRepository productRepository = new ProductRepository();
    private final List<StockAlertObserver> observers = new ArrayList<>();

    public synchronized void registerObserver(StockAlertObserver observer) {
        observers.add(observer);
    }
    public void restockProduct(String sku, int quantity) throws Exception {

    Product product = productRepository.findBySku(sku);

    if (product == null) {
        throw new RuntimeException("Product not found.");
    }

    product.setQuantity(product.getQuantity() + quantity);

    productRepository.save(product);
}
    public List<Product> getAllProducts() throws Exception {
    return productRepository.findAll();
}
    

    public synchronized Product getProduct(String sku) throws SQLException {
        try {
            return productRepository.findBySku(sku);
        } catch (Exception e) {
            // Wrap any repository exception as SQLException
            throw new SQLException("Invalid product for SKU: " + sku, e);
        }
    }

    public synchronized void updateProductStock(Product product) throws SQLException {
        productRepository.save(product);
        // Check if item needs restocking based on your plan's logic
        if (product.getQuantity() <= product.getReorderThreshold()) {
            notifyObservers(product.getSku(), product.getQuantity(), product.getReorderThreshold());
        }
    }

    private void notifyObservers(String sku, int currentQty, int threshold) {
        for (StockAlertObserver observer : observers) {
            observer.onLowStockDetected(sku, currentQty, threshold);
        }
    }
}
