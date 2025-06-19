package test.project.technical_task.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import test.project.technical_task.dto.ProductRequestDto;
import test.project.technical_task.mapper.ProductMapper;
import test.project.technical_task.model.Product;
import test.project.technical_task.repository.ProductRepository;
import test.project.technical_task.service.impl.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTests {
    private static final Long EXISTING_ID = 1L;
    private static final Long NON_EXISTENT_ID = 999L;
    private static final int PAGE = 0;
    private static final int SIZE = 10;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductRequestDto createSampleProductRequestDto() {
        return new ProductRequestDto(
                "Sample Product",
                "This is a description",
                new BigDecimal("19.99"),
                "Electronics",
                10
        );
    }

    @Test
    @DisplayName("Get all products returns paged result")
    void getAllProducts_ReturnsPagedProducts() {
        Pageable pageable = PageRequest.of(PAGE, SIZE);
        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(2L);
        Page<Product> pagedProducts = new PageImpl<>(List.of(product1, product2));

        when(productRepository.findAll(pageable)).thenReturn(pagedProducts);

        Page<Product> result = productService.getAllProducts(PAGE, SIZE);

        assertEquals(2, result.getContent().size());
        verify(productRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Find product by ID when exists returns product")
    void findProductById_ProductExists_ReturnsProduct() {
        Product product = new Product();
        product.setId(EXISTING_ID);

        when(productRepository.findById(EXISTING_ID)).thenReturn(Optional.of(product));

        Product result = productService.findProductById(EXISTING_ID);

        assertEquals(EXISTING_ID, result.getId());
        verify(productRepository).findById(EXISTING_ID);
    }

    @Test
    @DisplayName("Find product by ID when does not exist throws exception")
    void findProductById_ProductNotExists_ThrowsEntityNotFoundException() {
        when(productRepository.findById(NON_EXISTENT_ID)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                productService.findProductById(NON_EXISTENT_ID));

        assertEquals("Product not found with id: " + NON_EXISTENT_ID, ex.getMessage());
        verify(productRepository).findById(NON_EXISTENT_ID);
    }

    @Test
    @DisplayName("Create product saves and returns saved product")
    void createProduct_SavesAndReturnsProduct() {
        ProductRequestDto productRequestDto = createSampleProductRequestDto();
        Product productToSave = new Product();
        Product savedProduct = new Product();
        savedProduct.setId(EXISTING_ID);

        when(productMapper.toEntity(productRequestDto)).thenReturn(productToSave);
        when(productRepository.save(productToSave)).thenReturn(savedProduct);

        Product result = productService.createProduct(productRequestDto);

        assertEquals(EXISTING_ID, result.getId());
        verify(productMapper).toEntity(productRequestDto);
        verify(productRepository).save(productToSave);
    }

    @Test
    @DisplayName("Update product when exists updates and returns updated product")
    void updateProduct_ProductExists_UpdatesAndReturnsProduct() {
        ProductRequestDto productRequestDto = createSampleProductRequestDto();
        Product existingProduct = new Product();
        existingProduct.setId(EXISTING_ID);
        Product updatedProduct = new Product();
        updatedProduct.setId(EXISTING_ID);

        when(productRepository.findById(EXISTING_ID)).thenReturn(Optional.of(existingProduct));
        doAnswer(invocation -> {
            return null;
        }).when(productMapper).updateProductFromDto(productRequestDto, existingProduct);
        when(productRepository.save(existingProduct)).thenReturn(updatedProduct);

        Product result = productService.updateProduct(EXISTING_ID, productRequestDto);

        assertEquals(EXISTING_ID, result.getId());
        verify(productRepository).findById(EXISTING_ID);
        verify(productMapper).updateProductFromDto(productRequestDto, existingProduct);
        verify(productRepository).save(existingProduct);
    }

    @Test
    @DisplayName("Update product when does not exist throws exception")
    void updateProduct_ProductNotExists_ThrowsEntityNotFoundException() {
        ProductRequestDto productRequestDto = createSampleProductRequestDto();

        when(productRepository.findById(NON_EXISTENT_ID)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                productService.updateProduct(NON_EXISTENT_ID, productRequestDto));

        assertEquals("Product not found with id: " + NON_EXISTENT_ID, ex.getMessage());
        verify(productRepository).findById(NON_EXISTENT_ID);
        verify(productRepository, never()).save(any());
        verify(productMapper, never()).updateProductFromDto(any(), any());
    }

    @Test
    @DisplayName("Delete product when exists deletes successfully")
    void deleteProduct_ProductExists_DeletesSuccessfully() {
        when(productRepository.existsById(EXISTING_ID)).thenReturn(true);

        productService.deleteProduct(EXISTING_ID);

        verify(productRepository).existsById(EXISTING_ID);
        verify(productRepository).deleteById(EXISTING_ID);
    }

    @Test
    @DisplayName("Delete product when does not exist throws exception")
    void deleteProduct_ProductNotExists_ThrowsEntityNotFoundException() {
        when(productRepository.existsById(NON_EXISTENT_ID)).thenReturn(false);

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                productService.deleteProduct(NON_EXISTENT_ID));

        assertEquals("Product not found with id: " + NON_EXISTENT_ID, ex.getMessage());
        verify(productRepository).existsById(NON_EXISTENT_ID);
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Get products by category returns list")
    void getProductByCategory_ReturnsProducts() {
        String category = "Electronics";
        Product product1 = new Product();
        product1.setCategory(category);
        Product product2 = new Product();
        product2.setCategory(category);

        when(productRepository.findProductByCategoryEqualsIgnoreCase(category))
                .thenReturn(List.of(product1, product2));

        List<Product> result = productService.getProductByCategory(category);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.getCategory().equalsIgnoreCase(category)));
        verify(productRepository).findProductByCategoryEqualsIgnoreCase(category);
    }
}

