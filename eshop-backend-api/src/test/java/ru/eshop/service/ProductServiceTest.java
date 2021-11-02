package ru.eshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.eshop.database.persist.ProductRepository;
import ru.eshop.database.persist.model.Brand;
import ru.eshop.database.persist.model.Category;
import ru.eshop.database.persist.model.Picture;
import ru.eshop.database.persist.model.Product;
import ru.eshop.dto.ProductDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductServiceTest {

    private ProductService productService;
    private ProductRepository productRepository;

    private static Product getExpectedProduct() {
        Category expectedCategory = new Category(1L, "expectedCategory");
        Brand expectedBrand = new Brand(1L, "expectedBrand");
        Product expectedProduct = new Product();
        expectedProduct.setId(1L);
        expectedProduct.setCategory(expectedCategory);
        expectedProduct.setBrand(expectedBrand);
        expectedProduct.setPrice(BigDecimal.valueOf(1234L));
        expectedProduct.setDescription("desc");
        expectedProduct.setTitle("expectedProduct");
        List<Picture> pictureList = new ArrayList<>();
        pictureList.add(new Picture(1L, "testPic", "noContent", "testFilename", expectedProduct));
        expectedProduct.setPicture(pictureList);
        return expectedProduct;
    }

    @BeforeEach
    public void init() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    public void testFindById() {
        Product expectedProduct = getExpectedProduct();
        when(productRepository.findById(eq(expectedProduct.getId()))).thenReturn(Optional.of(expectedProduct));
        Optional<ProductDto> product = productService.findById(expectedProduct.getId());
        assertTrue(product.isPresent());
        assertEquals(expectedProduct.getId(), product.get().getId());
        assertEquals(expectedProduct.getTitle(), product.get().getTitle());
        assertEquals(expectedProduct.getPrice(), product.get().getPrice());
        assertEquals(expectedProduct.getCategory().getName(), product.get().getCategory().getName());
        assertEquals(expectedProduct.getBrand().getTitle(), product.get().getBrand().getTitle());
        assertEquals(expectedProduct.getDescription(), product.get().getDescription());
        assertNotNull(product.get().getPictures());
        assertEquals(expectedProduct.getPictures().size(), product.get().getPictures().size());
        assertEquals(expectedProduct.getPictures().get(0).getId(), product.get().getPictures().get(0));
    }

    @Test
    public void testFindAll() {
        Product expectedProduct = getExpectedProduct();
        Page<Product> products = new PageImpl<>(Collections.singletonList(expectedProduct));
        when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(products);
        Page<ProductDto> actualProducts = productService.findAll(Optional.empty(), Optional.empty(),
                BigDecimal.valueOf(0), BigDecimal.valueOf(9999), 1, 5, "id");
        assertNotNull(actualProducts);
        assertEquals(1, actualProducts.getSize());
        ProductDto product = actualProducts.toList().get(0);
        assertEquals(expectedProduct.getId(), product.getId());
        assertEquals(expectedProduct.getTitle(), product.getTitle());
        assertEquals(expectedProduct.getPrice(), product.getPrice());
        assertEquals(expectedProduct.getCategory().getName(), product.getCategory().getName());
        assertEquals(expectedProduct.getBrand().getTitle(), product.getBrand().getTitle());
        assertEquals(expectedProduct.getDescription(), product.getDescription());
        assertNotNull(product.getPictures());
        assertEquals(expectedProduct.getPictures().size(), product.getPictures().size());
        assertEquals(expectedProduct.getPictures().get(0).getId(), product.getPictures().get(0));
    }
}
