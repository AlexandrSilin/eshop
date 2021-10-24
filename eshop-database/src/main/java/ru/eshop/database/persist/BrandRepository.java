package ru.eshop.database.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.eshop.database.persist.model.Brand;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long>, JpaSpecificationExecutor<Brand> {
    @Query("select distinct b from Brand b where b.title = :name")
    List<Brand> findByName(@Param("name") String name);
}
