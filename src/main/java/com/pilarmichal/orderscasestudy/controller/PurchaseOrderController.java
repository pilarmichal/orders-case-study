package com.pilarmichal.orderscasestudy.controller;

import com.pilarmichal.orderscasestudy.dto.OrderRequestDTO;
import com.pilarmichal.orderscasestudy.model.PurchaseOrder;
import com.pilarmichal.orderscasestudy.service.PurchaseOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Order Controller", description = "APIs for managing orders in the system")
@RequestMapping("/orders")
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping
    public ResponseEntity<PurchaseOrder> createOrder(@Valid @RequestBody List<OrderRequestDTO> orderRequestDTOs) {
        PurchaseOrder purchaseOrder = purchaseOrderService.createOrder(orderRequestDTOs);
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseOrder);
    }
}
