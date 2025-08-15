package com.foodroute.foodroute.dto;

import com.foodroute.foodroute.model.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {

    private OrderStatus status;
    private String shippingAddress;
    private String phoneNumber;
    private Long userId;
}
