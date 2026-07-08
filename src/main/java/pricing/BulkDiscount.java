package pricing;
import products.Product;
/*
  Applies a per-unit discount when the product's on-hand quantity
  is high, since carrying excess stock ties up capital - it's
  cheaper to sell it a bit under price than keep warehousing it.
 */
public class BulkDiscount implements PricingStrategy {

    private final int bulkThreshold;
    private final double discountPercent;

    public BulkDiscount(int bulkThreshold, double discountPercent) {
        if (bulkThreshold < 0) {
            throw new IllegalArgumentException("Bulk threshold cannot be negative.");
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
        }
        this.bulkThreshold = bulkThreshold;
        this.discountPercent = discountPercent;
    }

    @Override
    public double calculatePrice(Product product) {
        double base = product.getBasePrice();
        if (product.getQuantity() >= bulkThreshold) {
            return round2(base * (1 - discountPercent / 100.0));
        }
        return base;
    }

    public int getBulkThreshold() {
        return bulkThreshold;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}