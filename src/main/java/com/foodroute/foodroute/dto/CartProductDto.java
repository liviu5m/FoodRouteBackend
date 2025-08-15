package com.foodroute.foodroute.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartProductDto {

    private Long id;
    private Long cartId;
    private Long productId;
    private int quantity;

}
