package simulation;

import jdbc.ProductRepository;
import jdbc.SalesRepository;
import service.InventoryService;
import products.Product;

import java.sql.SQLException;
import java.util.Random;

public class OrderSimulator implements Runnable {
    private final String[] targetSkus;
    private final Random random = new Random();
    private boolean running = true;
    // Accept InventoryService and SalesRepository
    private final InventoryService inventoryService;
    private final SalesRepository salesRepo;

    public OrderSimulator(InventoryService inventoryService, SalesRepository salesRepo, String[] targetSkus) {
        this.inventoryService = inventoryService;
        this.salesRepo = salesRepo;
        this.targetSkus = targetSkus;
    }
    public void stopSimulation() {
        this.running = false;
    }

    @Override
    public void run() {
        System.out.println("[Order Simulator] Thread started and generating customer demands...");

        while (running) {
            try {
                // 1. Wait a random interval between 2 to 4 seconds to simulate real customer behavior
                Thread.sleep(2000 + random.nextInt(2000));

                // 2. Pick a random product from our catalog
                String randomSku = targetSkus[random.nextInt(targetSkus.length)];
                
                // 3. Process the transaction inside a synchronized/safe workflow
                processSimulatedOrder(randomSku);

            } catch (InterruptedException e) {
                System.out.println("[Order Simulator] Interrupted, shutting down.");
                break;
            }
        }
    }

    private synchronized void processSimulatedOrder(String sku) {
        try {
            // Fetch fresh stock status from SQLite
            Product product = inventoryService.getProduct(sku);
            
            if (product == null) return;

            int qtyToBuy = random.nextInt(3) + 1; // Buy 1 to 3 items
            System.out.println("\n[Order] Attempting to buy " + qtyToBuy + " units of " + sku);

            if (product.getQuantity() >= qtyToBuy) {
                // Deduct stock levels locally
                product.setQuantity(product.getQuantity() - qtyToBuy);
                
                // Persist the updated lower stock value back to SQLite
                inventoryService.updateProductStock(product);
                
                // Log the transaction records into the sales table for Python forecasting
                salesRepo.recordSale(sku, qtyToBuy, product.getBasePrice());
                
                System.out.println("[Order SUCCESS] " + qtyToBuy + "x " + sku + " sold. Remaining stock: " + product.getQuantity());
            } else {
                System.out.println("[Order FAILED] Out of stock for SKU: " + sku + " (Requested: " + qtyToBuy + ", Available: " + product.getQuantity() + ")");
            }

        } catch (SQLException e) {
            System.err.println("[Order Error] Database transaction failed for SKU " + sku + ": " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[Order Error] Unexpected issue: " + e.getMessage());
        }
    }
} 
