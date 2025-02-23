package com.pilarmichal.orderscasestudy.repository;

import com.pilarmichal.orderscasestudy.model.PurchaseOrder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    @EntityGraph(attributePaths = {"orderItems", "orderItems.item"})
    Optional<PurchaseOrder> findById(Long id);

    @Query("SELECT p FROM PurchaseOrder p WHERE p.orderStatus = 'PENDING' AND p.lastUpdated < :expiryTime")
    List<PurchaseOrder> findPendingOrdersOlderThan(LocalDateTime expiryTime);
}
