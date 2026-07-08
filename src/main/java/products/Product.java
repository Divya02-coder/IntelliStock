package products;
import exception.*;
import pricing.*;
import interfaces.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
  Abstract base for every item IntelliStock tracks. Holds the fields
  and behavior common to all products (identity, stock level, pricing,
  audit trail) and leaves category-specific behavior — expiry, warranty,
  size/season, etc. — to the subclasses via getCategory()/getHandlingNote().

  Implements Sellable/Restockable/Auditable so any Product can be sold,
  restocked, and audited polymorphically regardless of concrete type.
 */
public abstract class Product implements Sellable, Restockable, Auditable {

    private final String sku;
    private String name;
    private int quantity;
    private double costPrice;
    private double basePrice;
    private int reorderThreshold;

    private PricingStrategy pricingStrategy;

    private final List<String> auditLog = new ArrayList<>();
    private LocalDateTime lastModified;

    protected Product(String sku, String name, int quantity, double costPrice,
                       double basePrice, int reorderThreshold) throws InvalidProductException {
        if (sku == null || sku.isBlank()) {
            throw new InvalidProductException("Product SKU cannot be blank.");
        }
        if (name == null || name.isBlank()) {
            throw new InvalidProductException("Product name cannot be blank.");
        }
        if (quantity < 0) {
            throw new InvalidProductException("Initial quantity cannot be negative.");
        }
        if (costPrice < 0) {
            throw new InvalidProductException("Cost price cannot be negative.");
        }
        if (basePrice < 0) {
            throw new InvalidProductException("Base price cannot be negative.");
        }
        if (reorderThreshold < 0) {
            throw new InvalidProductException("Reorder threshold cannot be negative.");
        }

        this.sku = sku;
        this.name = name;
        this.quantity = quantity;
        this.costPrice = costPrice;
        this.basePrice = basePrice;
        this.reorderThreshold = reorderThreshold;
        this.pricingStrategy = new RegularPricing();
        this.lastModified = LocalDateTime.now();
        logChange("Product created with initial quantity " + quantity);
    }

    // ---- Category-specific hooks (subclasses must define) ----

    public abstract String getCategory();

    public abstract String getHandlingNote();

    // ---- Sellable ----

    @Override
    public void sell(int quantityToSell) throws InsufficientStockException {
        if (quantityToSell <= 0) {
            // Treated as a no-op-with-warning rather than an exception,
            // since selling zero isn't a stock problem, just a bad call.
            logChange("Ignored sell() call for non-positive quantity: " + quantityToSell);
            return;
        }
        if (quantityToSell > quantity) {
            throw new InsufficientStockException(sku, quantityToSell, quantity);
        }
        setQuantity(quantity - quantityToSell);
        logChange("Sold " + quantityToSell + " unit(s); remaining stock: " + quantity);
    }

    @Override
    public double getSellingPrice() {
        return pricingStrategy.calculatePrice(this);
    }

    // ---- Restockable ----

    @Override
    public void restock(int quantityToAdd) {
        if (quantityToAdd <= 0) {
            logChange("Ignored restock() call for non-positive quantity: " + quantityToAdd);
            return;
        }
        setQuantity(quantity + quantityToAdd);
        logChange("Restocked " + quantityToAdd + " unit(s); new stock: " + quantity);
    }

    @Override
    public boolean needsRestock() {
        return quantity <= reorderThreshold;
    }

    @Override
    public int suggestedRestockQuantity() {
        // Bring stock back up to double the reorder threshold, but
        // never suggest less than a single unit's worth of restocking.
        int target = Math.max(reorderThreshold * 2, reorderThreshold + 1);
        return Math.max(target - quantity, 0);
    }

    // ---- Auditable ----

    @Override
    public LocalDateTime getLastModified() {
        return lastModified;
    }

    @Override
    public List<String> getAuditLog() {
        return Collections.unmodifiableList(auditLog);
    }

    @Override
    public void logChange(String description) {
        lastModified = LocalDateTime.now();
        auditLog.add(lastModified + " [" + sku + "] " + description);
    }

    // ---- Encapsulated field access ----

    public void setQuantity(int newQuantity) {
        if (newQuantity < 0) {
            // Defensive guard; sell()/restock() already prevent this,
            // but the setter itself must never allow a negative value.
            throw new IllegalStateException("Quantity cannot go negative for product " + sku);
        }
        this.quantity = newQuantity;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws InvalidProductException {
        if (name == null || name.isBlank()) {
            throw new InvalidProductException("Product name cannot be blank.");
        }
        this.name = name;
        logChange("Name updated to \"" + name + "\"");
    }

    public int getQuantity() {
        return quantity;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) throws InvalidProductException {
        if (costPrice < 0) {
            throw new InvalidProductException("Cost price cannot be negative.");
        }
        this.costPrice = costPrice;
        logChange("Cost price updated to " + costPrice);
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) throws InvalidProductException {
        if (basePrice < 0) {
            throw new InvalidProductException("Base price cannot be negative.");
        }
        this.basePrice = basePrice;
        logChange("Base price updated to " + basePrice);
    }

    public int getReorderThreshold() {
        return reorderThreshold;
    }

    public void setReorderThreshold(int reorderThreshold) throws InvalidProductException {
        if (reorderThreshold < 0) {
            throw new InvalidProductException("Reorder threshold cannot be negative.");
        }
        this.reorderThreshold = reorderThreshold;
        logChange("Reorder threshold updated to " + reorderThreshold);
    }

    public PricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }

    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
        logChange("Pricing strategy switched to " + pricingStrategy.getClass().getSimpleName());
    }

    @Override
    public String toString() {
        return String.format("%s [%s] \"%s\" qty=%d price=%.2f (%s)",
                getCategory(), sku, name, quantity, getSellingPrice(), getHandlingNote());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        return sku.equals(((Product) o).sku);
    }

    @Override
    public int hashCode() {
        return sku.hashCode();
    }
}