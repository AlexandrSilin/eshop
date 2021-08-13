package ru.eshop.admin.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class ProductDto {

    private Long id;

    @NotBlank
    private String title;

    @Positive
    private BigDecimal price;

    private String description;

    private CategoryDto category;

    public ProductDto() {

    }

    public ProductDto(Long id, String title, BigDecimal price, String description, CategoryDto category) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
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
}
