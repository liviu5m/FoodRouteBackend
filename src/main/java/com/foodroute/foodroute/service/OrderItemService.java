package com.foodroute.foodroute.service;

import com.foodroute.foodroute.dto.OrderItemDto;
import com.foodroute.foodroute.model.Order;
import com.foodroute.foodroute.model.OrderItem;
import com.foodroute.foodroute.model.Product;
import com.foodroute.foodroute.repository.OrderItemRepository;
import com.foodroute.foodroute.repository.OrderRepository;
import com.foodroute.foodroute.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderItemService(OrderItemRepository orderItemRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    public Optional<OrderItem> getOrderItemById(Long id) {
        return orderItemRepository.findById(id);
    }

    public OrderItem saveOrderItem(OrderItemDto orderItemDto) {
        OrderItem orderItem = new OrderItem();
        Order order = orderRepository.findById(orderItemDto.getOrderId()).orElseThrow(() -> new RuntimeException("Order not found"));
        Product product = productRepository.findById(orderItemDto.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemDto.getQuantity());
        return orderItemRepository.save(orderItem);
    }

    public OrderItem updateOrderItem(OrderItemDto orderItemDto, Long id) {
        OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        Order order = orderRepository.findById(orderItemDto.getOrderId()).orElseThrow(() -> new RuntimeException("Order not found"));
        Product product = productRepository.findById(orderItemDto.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(orderItemDto.getQuantity());
        return orderItemRepository.save(orderItem);
    }

    public void deleteOrderItem(Long id) {
        orderItemRepository.deleteById(id);
    }
}
