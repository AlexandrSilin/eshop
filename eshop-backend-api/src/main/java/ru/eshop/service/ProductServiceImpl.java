package ru.eshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.eshop.database.persist.ProductRepository;
import ru.eshop.database.persist.ProductSpecifications;
import ru.eshop.database.persist.model.Picture;
import ru.eshop.database.persist.model.Product;
import ru.eshop.dto.BrandDto;
import ru.eshop.dto.CategoryDto;
import ru.eshop.dto.ProductDto;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private static ProductDto getDto(Product product) {
        return new ProductDto(product.getId(),
                product.getTitle(),
                product.getPrice(),
                product.getDescription(),
                new CategoryDto(product.getCategory().getId(), product.getCategory().getName()),
                new BrandDto(product.getBrand().getId(), product.getBrand().getTitle()),
                product.getPictures().stream().map(Picture::getId).collect(Collectors.toList()));
    }

    @Override
    public Page<ProductDto> findAll(Optional<Long> categoryId, Optional<String> namePattern, BigDecimal minPrice,
                                    BigDecimal maxPrice, Integer page, Integer size, String sortField) {
        Specification<Product> specification = Specification.where(null);
        if (categoryId.isPresent() && categoryId.get() != -1) {
            specification = specification.and(ProductSpecifications.filterByCategory(categoryId.get()));
        }
        if (namePattern.isPresent()) {
            specification = specification.and(ProductSpecifications.filterByName(namePattern.get()));
        }
        if (minPrice != null) {
            specification.and(ProductSpecifications.minPrice(minPrice));
        }
        if (maxPrice != null) {
            specification.and(ProductSpecifications.maxPrice(maxPrice));
        }
        return productRepository.findAll(specification, PageRequest.of(page, size, Sort.by(sortField)))
                .map(ProductServiceImpl::getDto);
    }

    @Override
    public Optional<ProductDto> findById(Long id) {
        return productRepository.findById(id).map(ProductServiceImpl::getDto);
    }
}
