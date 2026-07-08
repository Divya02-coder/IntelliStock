package products;
import exception.*;
import java.time.Month;

public class ApparelProduct extends Product {
    public enum Season { SUMMER, WINTER, MONSOON, ALL_SEASON }

    private final String size;
    private final Season season;

    public ApparelProduct(String sku, String name, int quantity, double costPrice,
                           double basePrice, int reorderThreshold, String size, Season season)
            throws InvalidProductException {
        super(sku, name, quantity, costPrice, basePrice, reorderThreshold);
        if (size == null || size.isBlank()) {
            throw new InvalidProductException("Apparel products require a size.");
        }
        if (season == null) {
            throw new InvalidProductException("Apparel products require a season.");
        }
        this.size = size;
        this.season = season;
    }

    public String getSize() {
        return size;
    }

    public Season getSeason() {
        return season;
    }

    @Override
    public String getCategory() {
        return "Apparel";
    }

    @Override
    public int suggestedRestockQuantity() {
        int baseTarget = getReorderThreshold() * 2;
        if (!isInSeason() && getQuantity() > getReorderThreshold()) { // Only reduce target if not in season AND not already below reorder threshold
            baseTarget = (int) Math.ceil(getReorderThreshold() * 1.2);
        }
        return Math.max(baseTarget - getQuantity(), 0);
    }

    private boolean isInSeason() {
        if (season == Season.ALL_SEASON) {
            return true;
        }
        Month current = java.time.LocalDate.now().getMonth();
        switch (season) {
            case SUMMER:
                return current.getValue() >= 3 && current.getValue() <= 6; // Mar-Jun
            case MONSOON:
                return current.getValue() >= 6 && current.getValue() <= 9; // Jun-Sep
            case WINTER:
                return current.getValue() == 12 || current.getValue() <= 2; // Dec-Feb
            default:
                return true;
        }
    }

    @Override
    public String getHandlingNote() {
        return "Size " + size + " - " + season + " wear";
    }
}