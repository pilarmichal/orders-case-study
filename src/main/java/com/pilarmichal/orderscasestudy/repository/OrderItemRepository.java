package com.pilarmichal.orderscasestudy.repository;

import com.pilarmichal.orderscasestudy.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
