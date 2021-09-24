package ru.eshop.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProductDto {

    private Long id;
    private String title;
    private BigDecimal price;
    private String description;
    private CategoryDto category;
    private BrandDto brand;
    private List<Long> pictures;

    public ProductDto() {

    }

    public ProductDto(Long id, String title, BigDecimal price, String description, CategoryDto category, BrandDto brand, List<Long> pictures) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
        this.brand = brand;
        this.pictures = pictures;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public BrandDto getBrand() {
        return brand;
    }

    public void setBrand(BrandDto brand) {
        this.brand = brand;
    }

    public List<Long> getPictures() {
        return pictures;
    }

    public void setPictures(List<Long> pictures) {
        this.pictures = pictures;
    }

}
