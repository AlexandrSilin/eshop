package ru.eshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.eshop.controller.BrandListParams;
import ru.eshop.database.persist.BrandRepository;
import ru.eshop.database.persist.BrandSpecification;
import ru.eshop.database.persist.model.Brand;
import ru.eshop.dto.BrandDto;
import ru.eshop.dto.ProductDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final ProductService productService;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository, ProductService productService) {
        this.brandRepository = brandRepository;
        this.productService = productService;
    }

    private static BrandDto getBrandDto(Brand brand) {
        return new BrandDto(brand.getId(), brand.getTitle());
    }

    @Override
    public List<BrandDto> findAll() {
        return brandRepository.findAll().stream()
                .map(BrandServiceImpl::getBrandDto).collect(Collectors.toList());
    }

    @Override
    public Page<BrandDto> findWithFilter(BrandListParams params) {
        Specification<Brand> specification = Specification.where(null);
        if (params.getPrefix() != null) {
            specification.and(BrandSpecification.brandPrefix(params.getPrefix()));
        }
        return brandRepository.findAll(specification, PageRequest.of(
                        Optional.ofNullable(params.getPage()).orElse(1) - 1,
                        Optional.ofNullable(params.getSize()).orElse(5),
                        Sort.by(Optional.ofNullable(params.getSortField()).filter(f -> !f.isBlank()).orElse("id"))))
                .map(BrandServiceImpl::getBrandDto);
    }

    @Override
    public Optional<BrandDto> findById(Long id) {
        return brandRepository.findById(id).map(BrandServiceImpl::getBrandDto);
    }

    @Override
    public void save(BrandDto brand) {
        brandRepository.save(new Brand(brand.getId(), brand.getTitle()));
    }

    @Override
    public void deleteById(Long id) {
        List<ProductDto> productsToDelete = productService.findByCategory(id);
        for (ProductDto product : productsToDelete) {
            productService.deleteById(product.getId());
        }
        brandRepository.deleteById(id);
    }
}
