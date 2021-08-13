package ru.eshop.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.eshop.admin.controller.ProductListParams;
import ru.eshop.admin.dto.CategoryDto;
import ru.eshop.admin.dto.ProductDto;
import ru.eshop.admin.persist.CategoryRepository;
import ru.eshop.admin.persist.ProductRepository;
import ru.eshop.admin.persist.ProductSpecifications;
import ru.eshop.admin.persist.model.Category;
import ru.eshop.admin.persist.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    private static CategoryDto getCategory(Product product) {
        Category category = product.getCategory();
        return new CategoryDto(category.getId(), category.getName());
    }

    @Override
    public List<ProductDto> findAll() {
        return productRepository.findAll().stream()
                .map(product -> new ProductDto(product.getId(),
                        product.getTitle(),
                        product.getPrice(),
                        product.getDescription(),
                        getCategory(product))).collect(Collectors.toList());
    }

    @Override
    public Page<ProductDto> findWithFilter(ProductListParams params) {
        Specification<Product> specification = Specification.where(null);
        if (params.getMinPrice() != null) {
            specification.and(ProductSpecifications.minPrice(params.getMinPrice()));
        }
        if (params.getMaxPrice() != null) {
            specification.and(ProductSpecifications.maxPrice(params.getMaxPrice()));
        }
        return productRepository.findAll(specification, PageRequest.of(
                Optional.ofNullable(params.getPage()).orElse(1) - 1,
                Optional.ofNullable(params.getSize()).orElse(5),
                Sort.by(Optional.ofNullable(params.getSortField()).filter(f -> !f.isBlank()).orElse("id"))
        )).map(product -> new ProductDto(product.getId(),
                product.getTitle(),
                product.getPrice(),
                product.getDescription(),
                getCategory(product)));
    }

    @Override
    public Optional<ProductDto> findById(Long id) {
        return productRepository.findById(id).map(product -> new ProductDto(product.getId(),
                product.getTitle(),
                product.getPrice(),
                product.getDescription(),
                getCategory(product)));
    }

    @Override
    public void save(ProductDto product) {
        productRepository.save(new Product(product.getId(),
                product.getTitle(),
                product.getPrice(),
                product.getDescription(),
                categoryRepository.findById(product.getCategory().getId()).orElseThrow()));
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
