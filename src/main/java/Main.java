import products.Product;
import service.InventoryService;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        InventoryService service = new InventoryService();

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n=== IntelliStock Inventory ===");
                System.out.println("1. Add Product");
                System.out.println("2. View All Products");
                System.out.println("3. Search Product");
                System.out.println("4. Update Product");
                System.out.println("5. Delete Product");
                System.out.println("6. Record Sale");
                System.out.println("7. Low Stock Products");
                System.out.println("8. Exit");
                System.out.print("Enter choice: ");

                try {
                    int choice = Integer.parseInt(sc.nextLine().trim());

                    switch (choice) {
                        case 1 -> addProduct(sc, service);
                        case 2 -> showProducts(service.getAllProducts());
                        case 3 -> {
                            System.out.print("Product name: ");
                            showProducts(service.searchProduct(sc.nextLine()));
                        }
                        case 4 -> updateProduct(sc, service);
                        case 5 -> {
                            System.out.print("Product ID: ");
                            service.deleteProduct(readInt(sc));
                            System.out.println("Product deleted.");
                        }
                        case 6 -> recordSale(sc, service);
                        case 7 -> showProducts(service.getLowStockProducts());
                        case 8 -> {
                            System.out.println("Goodbye.");
                            return;
                        }
                        default -> System.out.println("Invalid choice.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }
    }

    private static void addProduct(
            Scanner sc, InventoryService service) throws Exception {

        System.out.print("Product name: ");
        String name = sc.nextLine();

        System.out.print("Category: ");
        String category = sc.nextLine();

        System.out.print("Price: ");
        double price = Double.parseDouble(sc.nextLine());

        System.out.print("Quantity: ");
        int quantity = readInt(sc);

        service.addProduct(
                new Product(0, name, category, price, quantity));

        System.out.println("Product added.");
    }

    private static void updateProduct(
            Scanner sc, InventoryService service) throws Exception {

        System.out.print("Product ID: ");
        int id = readInt(sc);

        Product product = service.getProduct(id);

        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.print("New name: ");
        product.setName(sc.nextLine());

        System.out.print("New category: ");
        product.setCategory(sc.nextLine());

        System.out.print("New price: ");
        product.setPrice(Double.parseDouble(sc.nextLine()));

        System.out.print("New quantity: ");
        product.setQuantity(readInt(sc));

        service.updateProduct(product);
        System.out.println("Product updated.");
    }

    private static void recordSale(
            Scanner sc, InventoryService service) throws Exception {

        System.out.print("Product ID: ");
        int id = readInt(sc);

        System.out.print("Quantity sold: ");
        int quantity = readInt(sc);

        service.recordSale(id, quantity);
        System.out.println("Sale recorded and stock updated.");
    }

    private static void showProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }

        products.forEach(System.out::println);
    }

    private static int readInt(Scanner sc) {
        return Integer.parseInt(sc.nextLine().trim());
    }
}