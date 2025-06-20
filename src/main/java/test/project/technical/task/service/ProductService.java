package test.project.technical.task.service;

import java.util.List;
import org.springframework.data.domain.Page;
import test.project.technical.task.dto.ProductRequestDto;
import test.project.technical.task.model.Product;

public interface ProductService {
    Page<Product> getAllProducts(int page, int size);

    Product findProductById(Long id);

    Product createProduct(ProductRequestDto productRequestDto);

    Product updateProduct(Long id, ProductRequestDto productRequestDto);

    void deleteProduct(Long id);

    List<Product> getProductByCategory(String category);
}
