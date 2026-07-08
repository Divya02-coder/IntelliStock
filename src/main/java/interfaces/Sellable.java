package interfaces;
/*
  Contract for anything that can be sold out of inventory.
 */
import exception.InsufficientStockException;

public interface Sellable {
    double getSellingPrice();
    void sell(int quantity) throws InsufficientStockException;
}

 