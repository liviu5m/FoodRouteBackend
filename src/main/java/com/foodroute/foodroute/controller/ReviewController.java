package com.foodroute.foodroute.controller;

import com.foodroute.foodroute.dto.ReviewDto;
import com.foodroute.foodroute.model.Review;
import com.foodroute.foodroute.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public Page<Review> getReviewsPaging(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size , @RequestParam Long productId, @RequestParam(defaultValue = "-1") Long userId) {
        return reviewService.getReviewsPaging(page, size, productId, userId);
    }

    @GetMapping("/all")
    public List<Review> getAllReview(@RequestParam Long productId) {
        return reviewService.getAllReviews(productId);
    }

    @GetMapping("/{id}")
    public Optional<Review> getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody ReviewDto reviewDto) {
        try {
            Review review = reviewService.createReview(reviewDto);
            return ResponseEntity.ok(review);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Long id, @RequestBody ReviewDto reviewDto
    ) {
        try {
            Review review = reviewService.updateReview(id, reviewDto);
            return ResponseEntity.ok(review);
        }catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }

}
