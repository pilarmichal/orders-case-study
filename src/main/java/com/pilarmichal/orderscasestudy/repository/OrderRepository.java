package com.pilarmichal.orderscasestudy.repository;

import com.pilarmichal.orderscasestudy.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
