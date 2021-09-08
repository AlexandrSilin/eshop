package ru.eshop.service;

import org.springframework.data.domain.Page;
import ru.eshop.dto.ProductDto;

import java.math.BigDecimal;
import java.util.Optional;

public interface ProductService {
    Page<ProductDto> findAll(Optional<Long> categoryId, Optional<String> namePattern, BigDecimal minPrice,
                             BigDecimal maxPrice, Integer page, Integer size, String sortField);

    Optional<ProductDto> findById(Long id);
}
