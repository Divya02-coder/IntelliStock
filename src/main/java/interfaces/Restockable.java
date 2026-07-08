package interfaces;
/*
  Contract for anything that can be replenished, and can report
  whether it currently needs to be.
 */
public interface Restockable {
 
    void restock(int quantity);
 
    /*
      Whether this item has fallen at/below its reorder threshold.
      Subclasses of Product override the threshold logic itself
     (e.g. perishables may want to reorder earlier).
     */
    boolean needsRestock();
 
    /*
      Suggested quantity to bring stock back to a healthy level.
      Category-specific (e.g. perishables restock in smaller, more
      frequent batches than electronics).
     */
    int suggestedRestockQuantity();
}
 