package com.pilarmichal.orderscasestudy.controller;

import com.pilarmichal.orderscasestudy.dto.ErrorDTO;
import com.pilarmichal.orderscasestudy.dto.OrderItemDTO;
import com.pilarmichal.orderscasestudy.dto.PurchaseOrderDTO;
import com.pilarmichal.orderscasestudy.mapper.PurchaseOrderMapper;
import com.pilarmichal.orderscasestudy.model.PurchaseOrder;
import com.pilarmichal.orderscasestudy.service.PurchaseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Order Controller", description = "APIs for managing orders in the system")
@RequestMapping("/orders")
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;
    private final PurchaseOrderMapper purchaseOrderMapper;

    @Autowired
    public PurchaseOrderController(PurchaseOrderService purchaseOrderService, PurchaseOrderMapper purchaseOrderMapper) {
        this.purchaseOrderService = purchaseOrderService;
        this.purchaseOrderMapper = purchaseOrderMapper;
    }

    @Operation(summary = "Get all orders", description = "Retrieves a list of all orders in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of orders")
    })
    @GetMapping
    public List<PurchaseOrderDTO> getPurchaseOrders() {
        List<PurchaseOrder> orders = purchaseOrderService.getPurchaseOrders();
        return orders.stream()
                .map(purchaseOrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get a specific order", description = "Retrieves an order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the order",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PurchaseOrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderDTO> getPurchaseOrder(@PathVariable Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderService.getPurchaseOrder(id);
        return ResponseEntity.ok(this.purchaseOrderMapper.toDTO(purchaseOrder));
    }

    @Operation(summary = "Create a new order",
            description = "Creates a new purchase order with the specified items.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PurchaseOrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request or insufficient item stock",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Item or order not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping
    public ResponseEntity<PurchaseOrderDTO> createOrder(@Valid @RequestBody List<OrderItemDTO> orderItemDTOS) {
        PurchaseOrder purchaseOrder = this.purchaseOrderService.createOrder(orderItemDTOS);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.purchaseOrderMapper.toDTO(purchaseOrder));
    }

    @Operation(summary = "Cancel an order",
            description = "Marks the purchase order as CANCELLED and returns items to stock if applicable.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully cancelled", content = @Content(schema = @Schema(implementation = PurchaseOrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Order cannot be cancelled",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PutMapping("/{id}/cancel")
    public ResponseEntity<PurchaseOrder> cancelPurchaseOrder(@PathVariable Long id) {
        PurchaseOrder purchaseOrder = this.purchaseOrderService.cancelPurchaseOrder(id);
        return ResponseEntity.status(HttpStatus.OK).body(purchaseOrder);
    }

    @Operation(summary = "Pay for an order",
            description = "Marks the purchase order as PAID. Order must be in PENDING status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully paid", content = @Content(schema = @Schema(implementation = PurchaseOrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Order cannot be paid",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PutMapping("/{id}/pay")
    public ResponseEntity<PurchaseOrder> payPurchaseOrder(@PathVariable Long id) {
        PurchaseOrder purchaseOrder = this.purchaseOrderService.payPurchaseOrder(id);
        return ResponseEntity.status(HttpStatus.OK).body(purchaseOrder);
    }
}
