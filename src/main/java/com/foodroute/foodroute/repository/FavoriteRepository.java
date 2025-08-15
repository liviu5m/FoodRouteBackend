package com.foodroute.foodroute.repository;

import com.foodroute.foodroute.model.Favorite;
import com.foodroute.foodroute.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long>  {
    void deleteFavoriteByUserIdAndProductId(Long userId, Long productId);

    Long product(Product product);
    void deleteFavoriteByUserId(Long userId);
}
