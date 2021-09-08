package ru.eshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.eshop.dto.ProductDto;
import ru.eshop.service.ProductService;

import java.math.BigDecimal;
import java.util.Optional;

@RequestMapping("/products")
@RestController
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    public Page<ProductDto> findAll(@RequestParam("categoryId") Optional<Long> categoryId,
                                    @RequestParam("namePattern") Optional<String> namePattern,
                                    @RequestParam("minPrice") Optional<BigDecimal> minPrice,
                                    @RequestParam("maxPrice") Optional<BigDecimal> maxPrice,
                                    @RequestParam("page") Optional<Integer> page,
                                    @RequestParam("size") Optional<Integer> size,
                                    @RequestParam("sortField") Optional<String> sortField) {
        return productService.findAll(categoryId, namePattern, minPrice.orElse(new BigDecimal(0L)),
                maxPrice.orElse(new BigDecimal(Long.MAX_VALUE)), page.orElse(1) - 1, size.orElse(5),
                sortField.filter(f -> !f.isBlank()).orElse("id"));
    }
}
