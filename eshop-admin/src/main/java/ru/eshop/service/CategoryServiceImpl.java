package ru.eshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.eshop.controller.CategoryListParams;
import ru.eshop.database.persist.CategoryRepository;
import ru.eshop.database.persist.CategorySpecification;
import ru.eshop.database.persist.model.Category;
import ru.eshop.dto.CategoryDto;
import ru.eshop.dto.ProductDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductService productService;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductService productService) {
        this.categoryRepository = categoryRepository;
        this.productService = productService;
    }

    private static CategoryDto getCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream().map(CategoryServiceImpl::getCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CategoryDto> findWithFilter(CategoryListParams params) {
        Specification<Category> specification = Specification.where(null);
        if (params.getPrefix() != null) {
            specification.and(CategorySpecification.categoryPrefix(params.getPrefix()));
        }
        return categoryRepository.findAll(specification, PageRequest.of(
                        Optional.ofNullable(params.getPage()).orElse(1) - 1,
                        Optional.ofNullable(params.getSize()).orElse(5),
                        Sort.by(Optional.ofNullable(params.getSortField()).filter(f -> !f.isBlank()).orElse("id"))))
                .map(CategoryServiceImpl::getCategoryDto);
    }

    @Override
    public Optional<CategoryDto> findById(Long id) {
        return categoryRepository.findById(id).map(CategoryServiceImpl::getCategoryDto);
    }

    @Override
    public void save(CategoryDto category) {
        categoryRepository.save(new Category(category.getId(), category.getName()));
    }

    @Override
    public void deleteById(Long id) {
        List<ProductDto> productsToDelete = productService.findByCategory(id);
        for (ProductDto product : productsToDelete) {
            productService.deleteById(product.getId());
        }
        categoryRepository.deleteById(id);
    }
}
