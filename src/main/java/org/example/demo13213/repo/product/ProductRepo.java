package org.example.demo13213.repo.product;

import org.example.demo13213.model.dao.Products;
import org.example.demo13213.model.dao.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Products, Long> {

        @Query("SELECT p FROM Products p " +
                "WHERE p.isActive = true " +
                "AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
        List<Products> searchProductsByName(@Param("name") String name);



}
