package pricing;
import products.Product;

public class RegularPricing implements PricingStrategy {
    
    @Override
    public double calculatePrice(Product product) {
        return product.getBasePrice();
    }
}