package products;

import exception.*;
import pricing.*;

/* 
  An electronics item - e.g. phones, laptops, accessories.
  Tracks warranty period and serial-number-style identifiers.
 */
public class ElectronicsProduct extends Product {

    private final int warrantyMonths;
    private final String brand;

    public ElectronicsProduct(String sku, String name, int quantity, double costPrice,
                               double basePrice, int reorderThreshold, int warrantyMonths,
                               String brand) throws InvalidProductException {
        super(sku, name, quantity, costPrice, basePrice, reorderThreshold);
        if (warrantyMonths < 0) {
            throw new InvalidProductException("Warranty months cannot be negative.");
        }
        if (brand == null || brand.isBlank()) {
            throw new InvalidProductException("Electronics products require a brand.");
        }
        this.warrantyMonths = warrantyMonths;
        this.brand = brand;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    public String getBrand() {
        return brand;
    }

    @Override
    public String getCategory() {
        return "Electronics";
    }
    @Override
    public int suggestedRestockQuantity() {
    // Electronics restock in bulk — target 3x the threshold, since
    // lead times (shipping/import) are long and frequent small
    // restocks aren't cost-effective.
    int target = getReorderThreshold() * 3;
    return Math.max(target - getQuantity(), 0);
}

    @Override
    public String getHandlingNote() {
        if (warrantyMonths == 0) {
            return "No warranty (" + brand + ")";
        }
        return brand + " - " + warrantyMonths + " month warranty";
    }
}