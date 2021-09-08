package ru.eshop.service;

import org.springframework.data.domain.Page;
import ru.eshop.controller.ProductListParams;
import ru.eshop.dto.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductDto> findAll();

    Page<ProductDto> findWithFilter(ProductListParams params);

    Optional<ProductDto> findById(Long id);

    void save(ProductDto product);

    void deleteById(Long id);

    List<ProductDto> findByCategory(Long categoryId);

    List<ProductDto> findByBrand(Long brandId);
}
