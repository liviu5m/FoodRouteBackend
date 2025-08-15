package com.foodroute.foodroute.controller;

import com.foodroute.foodroute.dto.OrderDto;
import com.foodroute.foodroute.dto.OrderItemDto;
import com.foodroute.foodroute.model.Order;
import com.foodroute.foodroute.model.OrderItem;
import com.foodroute.foodroute.service.OrderItemService;
import com.foodroute.foodroute.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order-item")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping
    public List<OrderItem> findAll(@RequestParam Long orderId) {
        return orderItemService.getOrderItems(orderId);
    }

    @GetMapping("/{id}")
    public Optional<OrderItem> findById(@PathVariable Long id) {
        return orderItemService.getOrderItemById(id);
    }

    @PostMapping
    public OrderItem save(@RequestBody OrderItemDto orderItemDto) {
        return orderItemService.saveOrderItem(orderItemDto);
    }

    @PutMapping("/{id}")
    public OrderItem update(@RequestBody OrderItemDto orderItemDto, @PathVariable Long id) {
        return orderItemService.updateOrderItem(orderItemDto, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        orderItemService.deleteOrderItem(id);
    }
}
