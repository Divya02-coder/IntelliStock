package service;

import jdbc.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import products.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {

    private FakeProductRepository repository;
    private InventoryService service;

    @BeforeEach
    void setUp() {
        repository = new FakeProductRepository();
        service = new InventoryService(repository);
    }

    @Test
    void lowStockThresholdIsTen() {
        assertEquals(10, InventoryService.LOW_STOCK_THRESHOLD);
    }

    @Test
    void quantityBelowTenIsLowStock() {
        Product product = new Product(
                1, "Keyboard", "Electronics", 500, 9);

        assertTrue(service.isLowStock(product));
    }

    @Test
    void quantityOfTenIsNotLowStock() {
        Product product = new Product(
                1, "Keyboard", "Electronics", 500, 10);

        assertFalse(service.isLowStock(product));
    }

    @Test
    void lowStockQueryUsesSharedThreshold() throws Exception {
        service.getLowStockProducts();

        assertEquals(
                InventoryService.LOW_STOCK_THRESHOLD,
                repository.requestedThreshold);
    }

    @Test
    void saleIsDelegatedToRepository() throws Exception {
        service.recordSale(12, 3);
        assertTrue(repository.saleCalled);

        assertEquals(12, repository.saleProductId);
        assertEquals(3, repository.saleQuantity);
    }

    @Test
    void zeroSaleQuantityIsRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.recordSale(12, 0));

        assertFalse(repository.saleCalled);
    }

    @Test
    void negativeSaleQuantityIsRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.recordSale(12, -1));

        assertFalse(repository.saleCalled);
    }

    @Test
    void negativeRestockQuantityIsRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.restockProduct(12, -2));
    }

    private static class FakeProductRepository
            extends ProductRepository {

        int requestedThreshold = -1;
        int saleProductId = -1;
        int saleQuantity = -1;
        boolean saleCalled = false;

        @Override
        public List<Product> lowStockProducts(int threshold) {
            requestedThreshold = threshold;
            return List.of();
        }
        

        @Override
        public void recordSale(int productId, int quantity) {
            saleCalled = true;
            saleProductId = productId;
            saleQuantity = quantity;
        }
    }
}