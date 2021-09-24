package ru.eshop.dto;

public class LineItemDto {
    private Long productId;
    private Integer qty;
    private String color;
    private String model;

    public LineItemDto() {

    }

    public LineItemDto(Long productId, Integer qty, String color, String model) {
        this.productId = productId;
        this.qty = qty;
        this.color = color;
        this.model = model;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
