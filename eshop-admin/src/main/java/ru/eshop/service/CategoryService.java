package ru.eshop.service;

import org.springframework.data.domain.Page;
import ru.eshop.controller.CategoryListParams;
import ru.eshop.dto.CategoryDto;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryDto> findAll();

    Page<CategoryDto> findWithFilter(CategoryListParams params);

    Optional<CategoryDto> findById(Long id);

    void save(CategoryDto category);

    void deleteById(Long id);
}
