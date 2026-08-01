import jdbc.ProductRepository;
import products.Product;
import java.util.List;

public class MainTest {

    public static void main(String[] args) {

        try {

            ProductRepository repo = new ProductRepository();

            List<Product> products = repo.findAll();

            for (Product p : products) {
                System.out.println(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}