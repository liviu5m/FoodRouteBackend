package com.foodroute.foodroute.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {

    private int rating;
    private String review;
    private Long userId;
    private Long productId;

}
