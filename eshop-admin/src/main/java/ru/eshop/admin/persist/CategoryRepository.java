package ru.eshop.admin.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.eshop.admin.persist.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
