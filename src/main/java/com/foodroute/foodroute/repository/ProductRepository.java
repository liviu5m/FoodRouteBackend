package com.foodroute.foodroute.repository;

import com.foodroute.foodroute.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("SELECT p FROM Product p WHERE " +
            "p.price >= :min AND p.price <= :max AND" +
            "(:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:categoryId = -1 OR p.category.id = :categoryId)")
    Page<Product> findAllByFilters(
            @Param("min") Double minPrice,
            @Param("max") Double maxPrice,
            @Param("search") String search,
            @Param("categoryId") Long categoryId,
            Pageable pageable);

    default Page<Product> findAllByDefaultFilter(Double minPrice, Double maxPrice,
                                                 String search, Long categoryId,
                                                 Pageable pageable) {
        return findAllByFilters(minPrice, maxPrice, search, categoryId, pageable);
    }

    default Page<Product> findAllByLatestFilter(Double minPrice, Double maxPrice,
                                                String search, Long categoryId,
                                                Pageable pageable) {
        return findAllByFilters(minPrice, maxPrice, search, categoryId,
                PageRequest.of(pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by("createdAt").descending()));
    }

    default Page<Product> findAllByPriceLow(Double minPrice, Double maxPrice,
                                            String search, Long categoryId,
                                            Pageable pageable) {
        return findAllByFilters(minPrice, maxPrice, search, categoryId,
                PageRequest.of(pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by("price").ascending()));
    }

    default Page<Product> findAllByPriceHigh(Double minPrice, Double maxPrice,
                                             String search, Long categoryId,
                                             Pageable pageable) {
        return findAllByFilters(minPrice, maxPrice, search, categoryId,
                PageRequest.of(pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by("price").descending()));
    }
    @Query("""
    SELECT p
    FROM Product p
    LEFT JOIN p.reviews r
    WHERE p.price BETWEEN :min AND :max
      AND (:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')))
      AND (:categoryId = -1 OR p.category.id = :categoryId)
    GROUP BY p.id
    ORDER BY COALESCE(AVG(r.rating), 0) DESC
""")
    Page<Product> findAllByFiltersOrderByAvgRatingDesc(
            @Param("min") Double minPrice,
            @Param("max") Double maxPrice,
            @Param("search") String search,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );
}