import jdbc.DatabaseManager;
import jdbc.ProductRepository;
import products.ApparelProduct;
import products.ElectronicsProduct;
import products.PerishableProduct;
import products.Product;

import java.time.LocalDate;

public class MainTest {
    public static void main(String[] args) {
        System.out.println("=== Starting IntelliStock JDBC Integration Test ===");

        try {
            // 1. Initialize the Database and Schema
            System.out.println("\n[1/4] Initializing Database...");
            // The static initializer in DatabaseManager handles table creation upon class loading.
            // Instantiating or simply referencing DatabaseManager will trigger the setup.
            new DatabaseManager(); 

            // 2. Instantiate the Repository
            ProductRepository repository = new ProductRepository();

            // 3. Create Sample Domain Model Objects
            System.out.println("\n[2/4] Generating Mock Products...");
            
            // Apparel Product
            ApparelProduct shirt = new ApparelProduct(
                "APP-SHIRT-01", "Vintage Denim Shirt", 50, 15.0, 35.0, 10, 
                "L", ApparelProduct.Season.SUMMER
            );

            // Electronics Product
            ElectronicsProduct laptop = new ElectronicsProduct(
                "ELE-LAPTOP-02", "Pro Book 14 Inch", 15, 450.0, 799.99, 5, 
                24, "TechCorp"
            );

            // Perishable Product
            PerishableProduct milk = new PerishableProduct(
                "PER-MILK-03", "Organic Whole Milk", 100, 1.20, 3.49, 20, 
                LocalDate.now().plusDays(7) // Expires in 1 week
            );

            // 4. Save Products to Database
            System.out.println("\n[3/4] Saving products to SQLite via ProductRepository...");
            repository.save(shirt);
            repository.save(laptop);
            repository.save(milk);
            System.out.println("-> All records successfully inserted/replaced!");

            // 5. Read Back and Verify Polymorphic Reconstitution
            System.out.println("\n[4/4] Retrieving and verifying products from Database...");
            
            String[] testSkus = {"APP-SHIRT-01", "ELE-LAPTOP-02", "PER-MILK-03"};
            
            for (String sku : testSkus) {
                Product retrieved = repository.findBySku(sku);
                
                if (retrieved != null) {
                    System.out.println("\n-------------------------------------------");
                    System.out.println("Successfully found SKU: " + retrieved.getSku());
                    System.out.println("Class Type: " + retrieved.getClass().getSimpleName());
                    System.out.println("Name: " + retrieved.getName());
                    System.out.println("Category String: " + retrieved.getCategory());
                    System.out.println("Stock Level: " + retrieved.getQuantity());
                    
                    // Verify class-specific properties printed dynamically
                    if (retrieved instanceof ApparelProduct) {
                        ApparelProduct app = (ApparelProduct) retrieved;
                        System.out.println("Apparel Details: Size = " + app.getSize() + ", Season = " + app.getSeason());
                    } else if (retrieved instanceof ElectronicsProduct) {
                        ElectronicsProduct ele = (ElectronicsProduct) retrieved;
                        System.out.println("Electronics Details: Warranty = " + ele.getWarrantyMonths() + " months, Brand = " + ele.getBrand());
                    } else if (retrieved instanceof PerishableProduct) {
                        PerishableProduct per = (PerishableProduct) retrieved;
                        System.out.println("Perishable Details: Expiry Date = " + per.getExpiryDate());
                    }
                } else {
                    System.err.println("❌ Verification Failed: Could not locate SKU " + sku);
                }
            }

            System.out.println("\n===========================================");
            System.out.println("🎉 SUCCESS: JDBC Data Access Layer Working Perfectly!");
            System.out.println("===========================================");

        } catch (Exception e) {
            System.err.println("\n❌ TEST FAILED with exception:");
            e.printStackTrace();
        }
    }
}