package ru.eshop.database.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.eshop.database.persist.model.Role;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("select distinct r from Role r " +
            "where r.name = :name")
    List<Role> findByName(@Param("name") String name);
}
