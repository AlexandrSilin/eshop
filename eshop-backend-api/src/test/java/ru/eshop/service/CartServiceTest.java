package ru.eshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.eshop.dto.BrandDto;
import ru.eshop.dto.CategoryDto;
import ru.eshop.dto.LineItem;
import ru.eshop.dto.ProductDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CartServiceTest {

    private CartService cartService;

    @BeforeEach
    public void init() {
        cartService = new CartServiceImpl();
    }

    @Test
    public void testIfNewCartIsEmpty() {
        assertNotNull(cartService.getLineItems());
        assertEquals(0, cartService.getLineItems().size());
        assertEquals(BigDecimal.ZERO, cartService.getSubTotal());
    }

    @Test
    public void testAddProduct() {
        ProductDto expectedProduct = new ProductDto(1L, "testProduct", BigDecimal.valueOf(4444L), "desc",
                new CategoryDto(1L, "testCategory"), new BrandDto(1L, "testBrand"), new ArrayList<>());
        cartService.addProductQty(expectedProduct, "color", "material", 1);
        List<LineItem> items = cartService.getLineItems();
        assertNotNull(items);
        assertEquals(1, items.size());
        LineItem item = items.get(0);
        assertEquals("color", item.getColor());
        assertEquals("material", item.getMaterial());
        assertEquals(expectedProduct.getTitle(), item.getProductDto().getTitle());
    }

    @Test
    public void testDeleteItem() {
        ProductDto expectedProduct = new ProductDto(1L, "testProduct", BigDecimal.valueOf(4444L), "desc",
                new CategoryDto(1L, "testCategory"), new BrandDto(1L, "testBrand"), new ArrayList<>());
        cartService.addProductQty(expectedProduct, "color", "material", 1);
        List<LineItem> items = cartService.getLineItems();
        assertNotNull(items);
        assertEquals(1, items.size());
        cartService.deleteItem(expectedProduct.getId());
        assertEquals(0, cartService.getLineItems().size());
    }

    @Test
    public void testRecalculateOfCost() {
        ProductDto expectedProduct = new ProductDto(1L, "testProduct", BigDecimal.valueOf(4444L), "desc",
                new CategoryDto(1L, "testCategory"), new BrandDto(1L, "testBrand"), new ArrayList<>());
        cartService.addProductQty(expectedProduct, "color", "material", 1);
        List<LineItem> items = cartService.getLineItems();
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(expectedProduct.getPrice(), cartService.getSubTotal());
        cartService.recalculateOfCost(expectedProduct.getId(), 3);
        assertEquals(expectedProduct.getPrice().multiply(BigDecimal.valueOf(3)), cartService.getSubTotal());
    }

    @Test
    public void testClearCart() {
        ProductDto expectedProduct = new ProductDto(1L, "testProduct", BigDecimal.valueOf(4444L), "desc",
                new CategoryDto(1L, "testCategory"), new BrandDto(1L, "testBrand"), new ArrayList<>());
        cartService.addProductQty(expectedProduct, "color", "material", 3);
        assertNotNull(cartService.getLineItems());
        assertEquals(expectedProduct.getPrice().multiply(BigDecimal.valueOf(3)), cartService.getSubTotal());
        ProductDto expectedProduct2 = new ProductDto(2L, "testProduct2", BigDecimal.valueOf(41444L), "desc",
                new CategoryDto(1L, "testCategory"), new BrandDto(1L, "testBrand"), new ArrayList<>());
        cartService.addProductQty(expectedProduct2, "color", "material", 5);
        assertEquals(2, cartService.getLineItems().size());
        cartService.clearCart();
        assertEquals(0, cartService.getLineItems().size());
    }
}
