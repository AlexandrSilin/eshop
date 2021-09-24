package ru.eshop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.eshop.dto.AllCartDto;
import ru.eshop.dto.LineItem;
import ru.eshop.dto.LineItemDto;
import ru.eshop.dto.ProductDto;
import ru.eshop.service.CartService;
import ru.eshop.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);
    private final CartService cartService;
    private final ProductService productService;

    @Autowired
    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public List<LineItem> addToCart(@RequestBody LineItemDto lineItemDto) {
        logger.info("New LineItem. ProductId = {}, qty = {}", lineItemDto.getProductId(), lineItemDto.getQty());
        ProductDto productDto = productService.findById(lineItemDto.getProductId())
                .orElseThrow(RuntimeException::new);
        cartService.addProductQty(productDto, lineItemDto.getColor(), lineItemDto.getModel(), lineItemDto.getQty());
        return cartService.getLineItems();
    }

    @GetMapping("/all")
    public AllCartDto findAll() {
        return new AllCartDto(cartService.getLineItems(), cartService.getSubTotal());
    }
}

