package simulation;

import jdbc.DatabaseManager;
import jdbc.ProductFactory;
import products.Product;
// Use fully-qualified name for InvalidProductException to avoid import resolution issues
import service.InventoryService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockMonitor implements Runnable {
    private final InventoryService inventoryService;
    private boolean running = true;
    private static final int RESTOCK_AMOUNT = 50; // Standard batch replenishment size

    // Updated constructor to accept the business InventoryService layer
    public StockMonitor(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void stopMonitor() {
        this.running = false;
    }

    @Override
    public void run() {
        System.out.println("[Stock Monitor] Background thread active. Monitoring threshold rules via Service Layer...");

        while (running) {
            try {
                // Scan the inventory database every 5 seconds
                Thread.sleep(5000);
                
                System.out.println("\n[Stock Monitor] Scanning database for low stock items...");
                auditAndRestockLowInventory();

            } catch (InterruptedException e) {
                System.out.println("[Stock Monitor] Thread interrupted, shutting down cleanly.");
                break;
            }
        }
    }

    private synchronized void auditAndRestockLowInventory() {
        String sql = "SELECT * FROM products WHERE quantity <= reorder_threshold;";
        List<Product> lowStockItems = new ArrayList<>();

        // 1. Read everything into memory first to avoid cursor statement collisions
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                try {
                    lowStockItems.add(ProductFactory.createProductFromRow(rs));
                } catch (Exception e) {
                    // Guard against product creation failures (including InvalidProductException)
                    System.err.println("[Stock Monitor Warning] Skipping invalid product row: " + e.getMessage());
                    // continue scanning remaining rows
                }
            }
        } catch (SQLException e) {
            System.err.println("[Stock Monitor Error] Failed to scan low stock items: " + e.getMessage());
            return;
        }

        // 2. Process restocking sequentially via the InventoryService
        if (lowStockItems.isEmpty()) {
            System.out.println("[Stock Monitor] All product lines healthy. No restocking needed.");
            return;
        }

        for (Product lowStockProduct : lowStockItems) {
            // Apply restock calculation
            int updatedQty = lowStockProduct.getQuantity() + RESTOCK_AMOUNT;
            lowStockProduct.setQuantity(updatedQty);

            try {
                // updateProductStock updates the database AND fires observer alert notifications automatically!
                inventoryService.updateProductStock(lowStockProduct);
                System.out.println("[RESTOCK SUCCESS] Replenished " + RESTOCK_AMOUNT + " units for SKU: " 
                        + lowStockProduct.getSku() + ". New Qty: " + updatedQty);
            } catch (SQLException e) {
                System.err.println("[Stock Monitor Error] Failed to process restock transaction for SKU " 
                        + lowStockProduct.getSku() + ": " + e.getMessage());
            }
        }
    }
}