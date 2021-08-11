package ru.eshop.admin.presist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

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
