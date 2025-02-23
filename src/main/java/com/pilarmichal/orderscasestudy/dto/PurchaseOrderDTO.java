package com.pilarmichal.orderscasestudy.dto;

import com.pilarmichal.orderscasestudy.model.PurchaseOrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class PurchaseOrderDTO {
    private Long purchaseOrderId;
    private PurchaseOrderStatus orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;
    private List<OrderItemDTO> orderItems;
}
