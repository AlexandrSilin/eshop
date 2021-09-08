package ru.eshop.database.persist;

import org.springframework.data.jpa.domain.Specification;
import ru.eshop.database.persist.model.Product;

import java.math.BigDecimal;

public final class ProductSpecifications {
    public static Specification<Product> maxPrice(BigDecimal maxPrice) {
        return (root, query, builder) -> builder.le(root.get("price"), maxPrice);
    }

    public static Specification<Product> minPrice(BigDecimal minPrice) {
        return (root, query, builder) -> builder.ge(root.get("price"), minPrice);
    }

    public static Specification<Product> filterByCategory(long id) {
        return (root, query, builder) -> builder.equal(root.get("category"), id);
    }

    public static Specification<Product> filterByName(String pattern) {
        return (root, query, builder) -> builder.like(root.get("name"), "%" + pattern + "%");
    }
}
