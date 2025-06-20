package test.project.technical.task.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import test.project.technical.task.model.Product;

@DataJpaTest
@Sql(scripts = "/db/add-test-products-with-id.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/db/clean-data.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Find products by category (case insensitive) - existing category")
    void findProductByCategoryEqualsIgnoreCase_CategoryExists_ReturnsProducts() {
        String category = "Electronics";

        List<Product> products = productRepository.findProductByCategoryEqualsIgnoreCase(category);

        assertFalse(products.isEmpty(), "Products should be found for category 'Electronics'");
        assertTrue(products.stream().allMatch(p -> p.getCategory().equalsIgnoreCase(category)));
    }

    @Test
    @DisplayName("Find products by category (case insensitive) - different case")
    void findProductByCategoryEqualsIgnoreCase_DifferentCase_ReturnsProducts() {
        String category = "electronics";

        List<Product> products = productRepository.findProductByCategoryEqualsIgnoreCase(category);

        assertFalse(products.isEmpty(), "Products should be found ignoring case");
        assertTrue(products.stream().allMatch(p -> p.getCategory().equalsIgnoreCase(category)));
    }

    @Test
    @DisplayName("Find products by category when category does not exist")
    void findProductByCategoryEqualsIgnoreCase_CategoryNotExists_ReturnsEmpty() {
        String category = "NonExistingCategory";

        List<Product> products = productRepository.findProductByCategoryEqualsIgnoreCase(category);

        assertTrue(products.isEmpty(), "No products should be found for non-existing category");
    }

    @Test
    @DisplayName("Find product by ID when product exists")
    void findById_ProductExists_ReturnsProduct() {
        Long productId = 1L;

        Optional<Product> product = productRepository.findById(productId);

        assertTrue(product.isPresent(), "Product should be found with id=1");
        assertEquals(productId, product.get().getId());
    }

    @Test
    @DisplayName("Find product by ID when product does not exist")
    void findById_ProductNotExists_ReturnsEmpty() {
        Long productId = 999L;

        Optional<Product> product = productRepository.findById(productId);

        assertTrue(product.isEmpty(), "Product should not be found with non-existing id");
    }
}
