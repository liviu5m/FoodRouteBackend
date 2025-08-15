package com.foodroute.foodroute.controller;

import com.foodroute.foodroute.dto.FavoriteDto;
import com.foodroute.foodroute.model.Favorite;
import com.foodroute.foodroute.repository.FavoriteRepository;
import com.foodroute.foodroute.service.FavoriteService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {

    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public List<Favorite> getFavorites(@RequestParam Long id) {
        return favoriteService.getAllFavoritesByUserId(id);
    }

    @GetMapping("/{id}")
    public Optional<Favorite> getFavorite(@PathVariable Long id) {
        return favoriteService.getFavoriteById(id);
    }

    @PostMapping
    public Favorite addFavorite(@RequestBody FavoriteDto favoriteDto) {
        return favoriteService.createFavorite(favoriteDto);
    }

    @PutMapping("/{id}")
    public Favorite updateFavorite(@PathVariable Long id, @RequestBody FavoriteDto favoriteDto) {
        return favoriteService.updateFavorite(favoriteDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteFavorite(@PathVariable Long id) {
        favoriteService.deleteFavorite(id);
    }

    @Transactional
    @DeleteMapping
    public void deleteFavorite(@RequestParam Long userId, @RequestParam Long productId) {
        favoriteService.deleteFavoriteByUserIdAndProductId(userId, productId);
    }

    @Transactional
    @DeleteMapping("/all")
    public void deleteFavoriteByUserId(@RequestParam Long userId) {
        favoriteService.deleteFavoriteByUserId(userId);
    }
}
