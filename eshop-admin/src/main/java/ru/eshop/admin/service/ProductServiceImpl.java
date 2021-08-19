package ru.eshop.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.eshop.admin.controller.ProductListParams;
import ru.eshop.admin.dto.BrandDto;
import ru.eshop.admin.dto.CategoryDto;
import ru.eshop.admin.dto.ProductDto;
import ru.eshop.database.persist.BrandRepository;
import ru.eshop.database.persist.CategoryRepository;
import ru.eshop.database.persist.ProductRepository;
import ru.eshop.database.persist.ProductSpecifications;
import ru.eshop.database.persist.model.Brand;
import ru.eshop.database.persist.model.Category;
import ru.eshop.database.persist.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
    }

    private static CategoryDto getCategory(Product product) {
        Category category = product.getCategory();
        return new CategoryDto(category.getId(), category.getName());
    }

    private static BrandDto getBrand(Product product) {
        Brand brand = product.getBrand();
        return new BrandDto(brand.getId(), brand.getTitle());
    }

    private static ProductDto getProductDto(Product product) {
        return new ProductDto(product.getId(), product.getTitle(), product.getPrice(),
                product.getDescription(), getCategory(product), getBrand(product));
    }

    @Override
    public List<ProductDto> findAll() {
        return productRepository.findAll().stream()
                .map(ProductServiceImpl::getProductDto).collect(Collectors.toList());
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
        )).map(ProductServiceImpl::getProductDto);
    }

    @Override
    public Optional<ProductDto> findById(Long id) {
        return productRepository.findById(id).map(ProductServiceImpl::getProductDto);
    }

    @Override
    public void save(ProductDto product) {
        productRepository.save(new Product(product.getId(),
                product.getTitle(),
                product.getPrice(),
                product.getDescription(),
                categoryRepository.findById(product.getCategory().getId()).orElseThrow(),
                brandRepository.findById(product.getBrand().getId()).orElseThrow()));
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDto> findByCategory(Long categoryId) {
        return productRepository.findAll().stream().filter(product -> product.getCategory().getId().equals(categoryId))
                .map(ProductServiceImpl::getProductDto).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> findByBrand(Long brandId) {
        return productRepository.findAll().stream().filter(product -> product.getBrand().getId().equals(brandId))
                .map(ProductServiceImpl::getProductDto).collect(Collectors.toList());
    }
}
