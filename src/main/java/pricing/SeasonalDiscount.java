package pricing;
import products.Product;
import java.time.LocalDate;

/*
  Applies a discount for a fixed calendar window - e.g. a Diwali
  sale or end-of-summer clearance on apparel. Falls back to base
  price outside the window, so the strategy can be attached ahead
  of time and it "just works" once the window opens.
 */
public class SeasonalDiscount implements PricingStrategy {

    private final LocalDate windowStart;
    private final LocalDate windowEnd;
    private final double discountPercent;

    public SeasonalDiscount(LocalDate windowStart, LocalDate windowEnd, double discountPercent) {
        if (windowStart == null || windowEnd == null) {
            throw new IllegalArgumentException("Discount window dates cannot be null.");
        }
        if (windowEnd.isBefore(windowStart)) {
            throw new IllegalArgumentException("windowEnd cannot be before windowStart.");
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
        }
        this.windowStart = windowStart;
        this.windowEnd = windowEnd;
        this.discountPercent = discountPercent;
    }

    @Override
    public double calculatePrice(Product product) {
        double base = product.getBasePrice();
        LocalDate today = LocalDate.now();
        boolean inWindow = !today.isBefore(windowStart) && !today.isAfter(windowEnd);
        if (inWindow) {
            return round2(base * (1 - discountPercent / 100.0));
        }
        return base;
    }

    public LocalDate getWindowStart() {
        return windowStart;
    }

    public LocalDate getWindowEnd() {
        return windowEnd;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}