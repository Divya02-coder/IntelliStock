package products;
import exception.*;
import pricing.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
/* A product with a shelf life - e.g. groceries, dairy, bakery items.
  Overrides getHandlingNote() to warn about expiry, and automatically
  switches to ClearancePricing when expiry is imminent (polymorphic
  behavior triggered from within the subclass). */

public class PerishableProduct extends Product {
 
    private final LocalDate expiryDate;
    private static final long CLEARANCE_TRIGGER_DAYS = 3;
 
    public PerishableProduct(String sku, String name, int quantity, double costPrice,
                              double basePrice, int reorderThreshold, LocalDate expiryDate)
            throws InvalidProductException {
        super(sku, name, quantity, costPrice, basePrice, reorderThreshold);
        if (expiryDate == null) {
            throw new InvalidProductException("Perishable products require an expiry date.");
        }
        this.expiryDate = expiryDate;
 
        if (isNearExpiry()) {
            setPricingStrategy(new ClearancePricing(30.0));
        }
    }
 
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
 
    public long daysUntilExpiry() {
        return ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }
 
    public boolean isNearExpiry() {
        return daysUntilExpiry() <= CLEARANCE_TRIGGER_DAYS;
    }
 
    public boolean isExpired() {
        return daysUntilExpiry() < 0;
    }
 
    @Override
    public String getCategory() {
        return "Perishable";
    }
 
    @Override
    public String getHandlingNote() {
        if (isExpired()) {
            return "EXPIRED - remove from shelf";
        }
        if (isNearExpiry()) {
            return "Expires in " + daysUntilExpiry() + " day(s) - clearance pricing active";
        }
        return "Expires on " + expiryDate;
    }
    @Override
public int suggestedRestockQuantity() {
    // Perishables restock little-and-often — target just 1.5x the
    // threshold, not double, since overstocking = wasted expiry.
    int target = (int) Math.ceil(getReorderThreshold() * 1.5);
    return Math.max(target - getQuantity(), 0);
}

@Override
public boolean needsRestock() {
    // Flag restock a bit early if it's also nearing expiry, so a
    // fresh batch can arrive before the old one has to be pulled.
    return super.needsRestock() || isNearExpiry();
}
}