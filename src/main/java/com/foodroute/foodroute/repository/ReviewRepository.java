package com.foodroute.foodroute.repository;

import com.foodroute.foodroute.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAll(Pageable pageable);
    List<Review> findAllByProduct_Id(Long productId);
    @Query("""
    SELECT r FROM Review r 
    WHERE r.product.id = :productId
    ORDER BY 
        CASE WHEN :priorityUserId IS NULL THEN 1
             WHEN r.user.id = :priorityUserId THEN 0
             ELSE 1 END,
        r.createdAt DESC
    """)
    Page<Review> findByProductWithUserPriority(
            @Param("productId") Long productId,
            @Param("priorityUserId") Long priorityUserId,
            Pageable pageable);
}
