package com.pilarmichal.orderscasestudy.repository;

import com.pilarmichal.orderscasestudy.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
}
