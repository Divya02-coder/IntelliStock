package simulation;

import interfaces.StockAlertObserver;

public class ConsoleAlertLogger implements StockAlertObserver {
    @Override
    public void onLowStockDetected(String sku, int currentQty, int threshold) {
        System.out.println("🚨 [OBSERVER NOTIFICATION] Low Stock Alert! SKU: " + sku 
                + " is at " + currentQty + " units (Threshold: " + threshold + ")");
    }
}