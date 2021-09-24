package ru.eshop.service;

import org.springframework.data.domain.Page;
import ru.eshop.controller.BrandListParams;
import ru.eshop.dto.BrandDto;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    List<BrandDto> findAll();

    Page<BrandDto> findWithFilter(BrandListParams params);

    Optional<BrandDto> findById(Long id);

    void save(BrandDto brand);

    void deleteById(Long id);
}
