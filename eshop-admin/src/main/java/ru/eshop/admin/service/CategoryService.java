package ru.eshop.admin.service;

import org.springframework.data.domain.Page;
import ru.eshop.admin.controller.CategoryListParams;
import ru.eshop.admin.dto.CategoryDto;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<CategoryDto> findAll();

    Page<CategoryDto> findWithFilter(CategoryListParams params);

    Optional<CategoryDto> findById(Long id);

    void save(CategoryDto category);

    void deleteById(Long id);
}
