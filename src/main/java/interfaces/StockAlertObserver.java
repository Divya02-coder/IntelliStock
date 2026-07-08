package interfaces;

public interface StockAlertObserver {
    void onLowStockDetected(String sku, int currentQty, int threshold);
}