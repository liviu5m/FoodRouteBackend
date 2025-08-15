package com.foodroute.foodroute.service;

import com.foodroute.foodroute.dto.ReviewDto;
import com.foodroute.foodroute.model.Product;
import com.foodroute.foodroute.model.Review;
import com.foodroute.foodroute.model.User;
import com.foodroute.foodroute.repository.ProductRepository;
import com.foodroute.foodroute.repository.ReviewRepository;
import com.foodroute.foodroute.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public Page<Review> getReviewsPaging(int page, int size, Long productId, Long userId) {
        Pageable pageable = PageRequest.of(page, size);
        return reviewRepository.findByProductWithUserPriority(productId, userId, pageable);
    }

    public List<Review> getAllReviews(Long productId) {
        return reviewRepository.findAllByProduct_Id(productId);
    }

    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    public Review createReview(ReviewDto reviewDto) {
        try {
            User user = userRepository.findById(reviewDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
            Product product = productRepository.findById(reviewDto.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
            if(reviewDto.getReview().equals("")) throw new RuntimeException("Review must not be empty");
            Review review = new Review(reviewDto.getRating(), reviewDto.getReview(), product, user);
            return reviewRepository.save(review);
        }catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Review updateReview(Long id, ReviewDto updateReviewDto) {
        try {
            Optional<Review> optionalReview = reviewRepository.findById(id);
            if(!optionalReview.isPresent()) throw new RuntimeException("Review not found");
            Review review = optionalReview.get();
            review.setReview(updateReviewDto.getReview());
            review.setRating(updateReviewDto.getRating());
            return reviewRepository.save(review);
        }catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
