package com.foodroute.foodroute.service;

import com.foodroute.foodroute.dto.OrderDto;
import com.foodroute.foodroute.model.Order;
import com.foodroute.foodroute.model.OrderStatus;
import com.foodroute.foodroute.model.User;
import com.foodroute.foodroute.repository.OrderRepository;
import com.foodroute.foodroute.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public Page<Order> findAllByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAllByUserId(userId, pageable);
    }

    public Optional<Order> findOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> findOrderedCanceledForUserId(Long userId) {
        return orderRepository.findAllByUserIdAndStatus(userId, OrderStatus.CANCELED);
    }

    public Order createOrder(OrderDto orderDto) {
        try {
            Order order = new Order();
            order.setShippingAddress(orderDto.getShippingAddress());
            order.setPhoneNumber(orderDto.getPhoneNumber());
            order.setStatus(orderDto.getStatus());
            Optional<User> user = userRepository.findById(orderDto.getUserId());
            if(!user.isPresent()) throw new RuntimeException("User not found");
            order.setUser(user.get());
            return orderRepository.save(order);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Order updateOrder(OrderDto orderDto, Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (!optionalOrder.isPresent()) throw new RuntimeException("Order not found");
        Order order = optionalOrder.get();
        order.setShippingAddress(orderDto.getShippingAddress());
        order.setPhoneNumber(orderDto.getPhoneNumber());
        order.setStatus(orderDto.getStatus());
        Optional<User> user = userRepository.findById(orderDto.getUserId());
        if(!user.isPresent()) throw new RuntimeException("User not found");
        order.setUser(user.get());
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

}
