package com.foodroute.foodroute.repository;

import com.foodroute.foodroute.model.Order;
import com.foodroute.foodroute.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserIdAndStatus(Long userId, OrderStatus status);
    List<Order> findAllByUserId(Long userId);
    Page<Order> findAllByUserId(Long userId, Pageable pageable);
}
