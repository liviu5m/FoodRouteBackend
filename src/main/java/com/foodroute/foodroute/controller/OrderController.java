package com.foodroute.foodroute.controller;

import com.foodroute.foodroute.dto.OrderDto;
import com.foodroute.foodroute.model.Order;
import com.foodroute.foodroute.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> findAll() {
        return orderService.findAllOrders();
    }

    @GetMapping("/user")
    public Page<Order> findAllByUserId(@RequestParam Long userId, @RequestParam int page, @RequestParam int size) {
        return orderService.findAllByUserId(userId, page, size);
    }

    @GetMapping("/{id}")
    public Optional<Order> findById(@PathVariable Long id) {
        return orderService.findOrderById(id);
    }

    @GetMapping("/cancel")
    public ResponseEntity<List<Order>> findOrderedCanceledForUserId(@RequestParam Long userId) {
        List<Order> order = orderService.findOrderedCanceledForUserId(userId);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody OrderDto orderDto) {
        try {
            Order order = orderService.createOrder(orderDto);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Order update(@RequestBody OrderDto orderDto, @PathVariable Long id) {
        return orderService.updateOrder(orderDto, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}
