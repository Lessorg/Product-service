package test.project.technical_task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import test.project.technical_task.dto.ProductRequestDto;
import test.project.technical_task.model.Product;
import test.project.technical_task.service.ProductService;

import java.util.List;

@Tag(name = "Products", description = "Product management API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Get all products")
    @GetMapping
    public Page<Product> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return productService.getAllProducts(page, size);
    }

    @Operation(summary = "Get product by ID")
    @Cacheable(value = "products", key = "#id")
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.findProductById(id);
    }

    @Operation(summary = "Create a new product")
    @CacheEvict(value = "productsByCategory", allEntries = true)
    @PostMapping
    public Product createProduct(
            @RequestBody @Validated ProductRequestDto productRequestDto) {
        return productService.createProduct(productRequestDto);
    }

    @Operation(summary = "Update a product")
    @Caching(evict = {
            @CacheEvict(value = "products", key = "#id"),
            @CacheEvict(value = "productsByCategory", allEntries = true)
    })
    @PutMapping("/{id}")
    public Product updateProduct(
            @PathVariable Long id,
            @RequestBody @Validated ProductRequestDto productRequestDto) {
        return productService.updateProduct(id, productRequestDto);
    }

    @Operation(summary = "Delete a product")
    @Caching(evict = {
            @CacheEvict(value = "products", key = "#id"),
            @CacheEvict(value = "productsByCategory", allEntries = true)
    })
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @Operation(summary = "Get products by category")
    @Cacheable(value = "productsByCategory", key = "#category")
    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productService.getProductByCategory(category);
    }
}
