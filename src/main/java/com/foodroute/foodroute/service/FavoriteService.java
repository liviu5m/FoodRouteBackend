package com.foodroute.foodroute.service;

import com.foodroute.foodroute.dto.FavoriteDto;
import com.foodroute.foodroute.model.Favorite;
import com.foodroute.foodroute.model.Product;
import com.foodroute.foodroute.model.User;
import com.foodroute.foodroute.repository.FavoriteRepository;
import com.foodroute.foodroute.repository.ProductRepository;
import com.foodroute.foodroute.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {

    private FavoriteRepository favoriteRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<Favorite> getAllFavoritesByUserId(Long userId) {
        return favoriteRepository.findAll();
    }

    public Optional<Favorite> getFavoriteById(Long id) {
        return favoriteRepository.findById(id);
    }

    @Transactional
    public Favorite createFavorite(FavoriteDto favoriteDto) {
        User user = userRepository.findById(favoriteDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(favoriteDto.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);
        return favoriteRepository.save(favorite);
    }

    public Favorite updateFavorite(FavoriteDto favoriteDto, Long id) {
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(() -> new RuntimeException("Favorite not found"));
        User user = userRepository.findById(favoriteDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(favoriteDto.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
        favorite.setUser(user);
        favorite.setProduct(product);
        return favoriteRepository.save(favorite);
    }

    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }

    @Transactional
    public void deleteFavoriteByUserIdAndProductId(Long userId, Long productId) {
        favoriteRepository.deleteFavoriteByUserIdAndProductId(userId, productId);
    }

    public void deleteFavoriteByUserId(Long userId) {
        favoriteRepository.deleteFavoriteByUserId(userId);
    }
}
