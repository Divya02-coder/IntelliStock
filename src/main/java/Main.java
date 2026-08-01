import jdbc.ProductRepository;
import products.Product;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ProductRepository repo = new ProductRepository();

        while (true) {

            System.out.println("\n====================================");
            System.out.println("     IntelliStock Inventory");
            System.out.println("====================================");
            System.out.println("1. Add Product");
            System.out.println("2. View All Products");
            System.out.println("3. Search Product");
            System.out.println("4. Update Product");
            System.out.println("5. Delete Product");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            try {

                switch (choice) {

                    case 1:

                        System.out.print("Product Name: ");
                        sc.nextLine();
                        String name = sc.nextLine();

                        System.out.print("Category: ");
                        String category = sc.nextLine();

                        System.out.print("Price: ");
                        double price = sc.nextDouble();

                        System.out.print("Quantity: ");
                        int quantity = sc.nextInt();

                        Product p = new Product(0, name, category, price, quantity);

                        repo.save(p);

                        break;

                    case 2:

                        List<Product> products = repo.findAll();

                        if (products.isEmpty()) {
                            System.out.println("No Products Found.");
                        } else {

                            System.out.println("\n------ Product List ------");

                            for (Product product : products) {
                                System.out.println(product);
                            }

                        }

                        break;

                    case 3:

                        System.out.print("Enter Product Name: ");
                        sc.nextLine();
                        String search = sc.nextLine();

                        List<Product> result = repo.searchByName(search);

                        if (result.isEmpty()) {

                            System.out.println("No Product Found.");

                        } else {

                            for (Product product : result) {
                                System.out.println(product);
                            }

                        }

                        break;

                    case 4:

                        System.out.print("Enter Product ID: ");
                        int id = sc.nextInt();

                        Product product = repo.findById(id);

                        if (product == null) {

                            System.out.println("Product Not Found.");

                            break;

                        }

                        System.out.print("New Name: ");
                        sc.nextLine();
                        product.setName(sc.nextLine());

                        System.out.print("New Category: ");
                        product.setCategory(sc.nextLine());

                        System.out.print("New Price: ");
                        product.setPrice(sc.nextDouble());

                        System.out.print("New Quantity: ");
                        product.setQuantity(sc.nextInt());

                        repo.update(product);

                        break;

                    case 5:

                        System.out.print("Enter Product ID: ");
                        int deleteId = sc.nextInt();

                        repo.delete(deleteId);

                        break;

                    case 6:

                        System.out.println("Thank you for using IntelliStock.");
                        sc.close();
                        System.exit(0);

                    default:

                        System.out.println("Invalid Choice.");

                }

            } catch (Exception e) {

                System.out.println("\nError: " + e.getMessage());

            }

        }

    }
}