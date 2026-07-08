package simulation;

import products.Product;
import service.InventoryService;

import java.util.List;

public class StockMonitor implements Runnable {

    private final InventoryService inventoryService;
    private volatile boolean running = true;

    private static final int RESTOCK_AMOUNT = 50;

    public StockMonitor(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void stopMonitor() {
        running = false;
    }

    @Override
    public void run() {

        System.out.println("[Stock Monitor] Background thread active. Monitoring threshold rules...");

        while (running) {

            try {

                Thread.sleep(5000);

                System.out.println("\n[Stock Monitor] Scanning database for low stock items...");

                List<Product> products = inventoryService.getAllProducts();

                boolean found = false;

                for (Product product : products) {

                    if (product.getQuantity() <= product.getReorderThreshold()) {

                        found = true;

                        System.out.println(
                                "[ALERT] Low Stock Detected! SKU: "
                                        + product.getSku()
                                        + " | Current Qty: "
                                        + product.getQuantity()
                                        + " | Threshold: "
                                        + product.getReorderThreshold());

                        inventoryService.restockProduct(
                                product.getSku(),
                                RESTOCK_AMOUNT
                        );

                        System.out.println(
                                "[RESTOCK SUCCESS] Replenished "
                                        + RESTOCK_AMOUNT
                                        + " units for SKU: "
                                        + product.getSku());
                    }
                }

                if (!found) {
                    System.out.println("[Stock Monitor] All product lines healthy. No restocking needed.");
                }

            } catch (Exception e) {

                System.err.println("[Stock Monitor Error] " + e.getMessage());

            }
        }
    }
}