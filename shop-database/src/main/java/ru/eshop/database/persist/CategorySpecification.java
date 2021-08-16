package ru.eshop.database.persist;

import org.springframework.data.jpa.domain.Specification;
import ru.eshop.database.persist.model.Category;

public final class CategorySpecification {
    public static Specification<Category> categoryPrefix(String prefix) {
        return (root, query, builder) -> builder.like(root.get("name"), prefix + "%");
    }
}
