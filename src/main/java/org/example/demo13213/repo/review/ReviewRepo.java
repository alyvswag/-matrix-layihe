package org.example.demo13213.repo.review;

import org.example.demo13213.model.dao.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Reviews, Long> {

    @Query("SELECT r FROM Reviews r WHERE r.product.id = :productId")
    List<Reviews> findByProductIdForReview(@Param("productId") Long productId);

    @Query("SELECT AVG(r.rating) FROM Reviews r WHERE r.product.id = :productId")
    Double findAverageRatingByProductId(@Param("productId") Long productId);
}