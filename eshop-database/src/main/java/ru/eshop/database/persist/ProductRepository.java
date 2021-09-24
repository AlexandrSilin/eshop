package ru.eshop.database.persist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.eshop.database.persist.model.Category;
import ru.eshop.database.persist.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query(value = "select p from Product p left join fetch p.category left join fetch p.brand",
            countQuery = "select count(p) from Product p")
    Page<Product> findAll(Pageable var2);

    @Query("select p from Product p left join fetch p.category where (p.price >= :minPrice or :minPrice is null) and " +
            "(p.price <= :maxPrice or :maxPrice is null) and " +
            "(p.category = :category or :category is null)")
    List<Product> filterProducts(@Param("minPrice") BigDecimal minPrice,
                                 @Param("maxPrice") BigDecimal maxPrice,
                                 @Param("category") Category category);

    @Query("select distinct p from Product p left join fetch p.category " +
            "where p.title = :title")
    List<Product> findByTitle(@Param("title") String title);
}
