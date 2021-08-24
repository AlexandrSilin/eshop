package ru.eshop.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.eshop.admin.controller.ProductListParams;
import ru.eshop.admin.dto.BrandDto;
import ru.eshop.admin.dto.CategoryDto;
import ru.eshop.admin.dto.ProductDto;
import ru.eshop.admin.exceptions.NotFoundException;
import ru.eshop.database.persist.BrandRepository;
import ru.eshop.database.persist.CategoryRepository;
import ru.eshop.database.persist.ProductRepository;
import ru.eshop.database.persist.ProductSpecifications;
import ru.eshop.database.persist.model.Brand;
import ru.eshop.database.persist.model.Category;
import ru.eshop.database.persist.model.Picture;
import ru.eshop.database.persist.model.Product;
import ru.eshop.pictures.service.PictureService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final PictureService pictureService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              BrandRepository brandRepository,
                              PictureService pictureService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.pictureService = pictureService;
    }

    @Override
    public List<ProductDto> findAll() {
        return productRepository.findAll().stream()
                .map(product -> new ProductDto(product.getId(),
                        product.getTitle(),
                        product.getPrice(),
                        product.getDescription(),
                        new CategoryDto(product.getCategory().getId(), product.getCategory().getName()),
                        new BrandDto(product.getBrand().getId(), product.getBrand().getTitle()),
                        product.getPictures().stream().map(Picture::getId).collect(Collectors.toList())))
                .collect(Collectors.toList());
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
                new CategoryDto(product.getCategory().getId(), product.getCategory().getName()),
                new BrandDto(product.getBrand().getId(), product.getBrand().getTitle()),
                product.getPictures().stream().map(Picture::getId).collect(Collectors.toList())));
    }

    @Override
    public Optional<ProductDto> findById(Long id) {
        return productRepository.findById(id).map(product -> new ProductDto(product.getId(),
                product.getTitle(),
                product.getPrice(),
                product.getDescription(),
                new CategoryDto(product.getCategory().getId(), product.getCategory().getName()),
                new BrandDto(product.getBrand().getId(), product.getBrand().getTitle()),
                product.getPictures().stream().map(Picture::getId).collect(Collectors.toList())));
    }

    @Override
    @Transactional
    public void save(ProductDto productDto) {
        Product product = (productDto.getId() != null) ? productRepository.findById(productDto.getId())
                .orElseThrow(() -> new NotFoundException("")) : new Product();
        Category category = categoryRepository.findById(productDto.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Brand brand = brandRepository.findById(productDto.getBrand().getId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));
        product.setTitle(productDto.getTitle());
        product.setCategory(category);
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setBrand(brand);
        if (productDto.getNewPictures() != null) {
            for (MultipartFile newPicture : productDto.getNewPictures()) {
                try {
                    product.getPictures().add(new Picture(null,
                            newPicture.getOriginalFilename(),
                            newPicture.getContentType(),
                            pictureService.createPicture(newPicture.getBytes()),
                            product
                    ));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDto> findByCategory(Long categoryId) {
        return productRepository.findAll().stream().filter(product -> product.getCategory().getId().equals(categoryId))
                .map(product -> new ProductDto(product.getId(),
                        product.getTitle(),
                        product.getPrice(),
                        product.getDescription(),
                        new CategoryDto(product.getCategory().getId(), product.getCategory().getName()),
                        new BrandDto(product.getBrand().getId(), product.getBrand().getTitle()),
                        product.getPictures().stream().map(Picture::getId).collect(Collectors.toList()))).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> findByBrand(Long brandId) {
        return productRepository.findAll().stream().filter(product -> product.getBrand().getId().equals(brandId))
                .map(product -> new ProductDto(product.getId(),
                        product.getTitle(),
                        product.getPrice(),
                        product.getDescription(),
                        new CategoryDto(product.getCategory().getId(), product.getCategory().getName()),
                        new BrandDto(product.getBrand().getId(), product.getBrand().getTitle()),
                        product.getPictures().stream().map(Picture::getId).collect(Collectors.toList()))).collect(Collectors.toList());
    }
}
