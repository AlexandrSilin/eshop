package ru.eshop.service;

import ru.eshop.dto.LineItem;
import ru.eshop.dto.ProductDto;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {
    void addProductQty(ProductDto productDto, String color, String material, Integer qty);

    void removeProductQty(ProductDto productDto, String color, String material, Integer qty);

    void removeProduct(ProductDto productDto, String color, String material);

    List<LineItem> getLineItems();

    BigDecimal getSubTotal();
}
