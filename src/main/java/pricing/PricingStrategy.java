package pricing;
import products.Product;

/*
  Strategy pattern: decouples "how a product is priced" from the
  Product class itself. Product holds a reference to one of these
  and can swap it at runtime (see PerishableProduct auto-switching
  to ClearancePricing when near expiry).
 */
public interface PricingStrategy {

    /*
      Computes the current selling price for the given product.
      Implementations read basePrice/quantity/etc. off the product
      but must never mutate it - pricing is a pure calculation.
     */
    double calculatePrice(Product product);
}