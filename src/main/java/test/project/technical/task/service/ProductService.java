package test.project.technical_task.service;

import org.springframework.data.domain.Page;
import test.project.technical_task.dto.ProductRequestDto;
import test.project.technical_task.model.Product;

import java.util.List;

public interface ProductService {
    Page<Product> getAllProducts(int page, int size);

    Product findProductById(Long id);

    Product createProduct(ProductRequestDto productRequestDto);

    Product updateProduct(Long id, ProductRequestDto productRequestDto);

    void deleteProduct(Long id);

    List<Product> getProductByCategory(String category);
}
