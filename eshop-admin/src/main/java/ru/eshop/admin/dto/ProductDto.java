package ru.eshop.admin.dto;

import org.springframework.web.multipart.MultipartFile;
import ru.eshop.database.persist.model.Picture;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

public class ProductDto {

    private Long id;

    @NotBlank
    private String title;

    @Positive
    private BigDecimal price;

    private String description;

    private CategoryDto category;

    private BrandDto brand;

    private List<Long> pictures;

    private MultipartFile[] newPictures;

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

    public MultipartFile[] getNewPictures() {
        return newPictures;
    }

    public void setNewPictures(MultipartFile[] newPictures) {
        this.newPictures = newPictures;
    }
}
