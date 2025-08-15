package com.foodroute.foodroute.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    private Long productId;
    private Long orderId;
    private int quantity;

}
