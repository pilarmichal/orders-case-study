package com.pilarmichal.orderscasestudy.mapper;

import com.pilarmichal.orderscasestudy.dto.OrderItemDTO;
import com.pilarmichal.orderscasestudy.dto.PurchaseOrderDTO;
import com.pilarmichal.orderscasestudy.model.PurchaseOrder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PurchaseOrderMapper {

    public PurchaseOrderDTO toDTO(PurchaseOrder purchaseOrder) {
        PurchaseOrderDTO dto = new PurchaseOrderDTO();
        dto.setPurchaseOrderId(purchaseOrder.getPurchaseOrderId());
        dto.setOrderStatus(purchaseOrder.getOrderStatus());
        dto.setCreatedAt(purchaseOrder.getCreatedAt());
        dto.setLastUpdated(purchaseOrder.getLastUpdated());

        List<OrderItemDTO> orderItemDTOs = purchaseOrder.getOrderItems().stream()
                .map(OrderItemDTO::new)
                .collect(Collectors.toList());

        dto.setOrderItems(orderItemDTOs);
        return dto;
    }
}
