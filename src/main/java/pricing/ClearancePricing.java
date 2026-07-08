package pricing;
import products.Product;

public class ClearancePricing implements PricingStrategy {
    private double discountPercent;

    public ClearancePricing(double discountPercent) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
        }
        this.discountPercent = discountPercent;
    }

    @Override
    public double calculatePrice(Product product) {
        return product.getBasePrice() * (1 - (discountPercent / 100.0));
    }
} // ONLY close the class at the very bottom!