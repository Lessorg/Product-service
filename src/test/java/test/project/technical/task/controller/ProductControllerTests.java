package test.project.technical_task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import test.project.technical_task.dto.ProductRequestDto;


import java.math.BigDecimal;


import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = "/db/add-test-products-with-id.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/db/clean-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ProductControllerTests {
    public static final long NONEXISTENT_PRODUCT_ID = 999L;
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get all products successfully")
    void getAllProducts_Success() throws Exception {
        mockMvc.perform(get("/api/v1/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get product by ID when product exists")
    void getProductById_ProductExists_ReturnsProduct() throws Exception {
        Long id = 1L;
        mockMvc.perform(get("/api/v1/products/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create product with valid request")
    void createProduct_ValidRequest_ReturnsCreatedProduct() throws Exception {
        ProductRequestDto requestDto = new ProductRequestDto(
                "New Product", "Description", BigDecimal.valueOf(19.99), "Electronics", 10);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Update existing product")
    void updateProduct_ValidRequest_ReturnsUpdatedProduct() throws Exception {
        Long id = 1L;
        ProductRequestDto requestDto = new ProductRequestDto(
                "Updated Product", "Updated Description", BigDecimal.valueOf(29.99), "Gadgets", 15);

        mockMvc.perform(put("/api/v1/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Delete product by ID")
    void deleteProduct_ProductExists_DeletesSuccessfully() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/api/v1/products/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get products by category")
    void getProductsByCategory_CategoryExists_ReturnsProducts() throws Exception {
        mockMvc.perform(get("/api/v1/products/category/{category}", "Electronics"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Unauthenticated user forbidden")
    void getAllProducts_Unauthenticated_Forbidden() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get non-existing product returns 404")
    void getProductById_ProductNotFound_Returns404() throws Exception {
        mockMvc.perform(get("/api/v1/products/{id}", NONEXISTENT_PRODUCT_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create product with invalid data returns 400")
    void createProduct_InvalidRequest_ReturnsBadRequest() throws Exception {
        ProductRequestDto invalidDto = new ProductRequestDto("", "", null, "", -1);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }
}
